package neo4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Entity;
import model.Relation;
import model.Repository;

public class Neo4jRepository implements AutoCloseable, Repository {

	private static final Logger logger = LoggerFactory.getLogger(Neo4jRepository.class);

	private final Driver driver;

	public Neo4jRepository(String uri, String user, String password) {
		this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	@Override
	public Session newSession() {
		return new SessionImpl(this.driver.session());
	}

	@Override
	public void close() {
		this.driver.close();
	}

	private static Query createEntityQuery(Entity entity) {
		var properties = createEntityProperties(entity);
		var builder = new StringBuilder();
		builder.append("CREATE (");
		builder.append(entity.getKey().getName());
		builder.append(":");
		builder.append(entity.getKey().getType());
		builder.append(" ");
		appendProperiesString(properties, s -> builder.append(s));
		builder.append(")");
		return new Query(builder.toString(), Values.value(properties));
	}

	private static Map<String, String> createEntityProperties(Entity entity) {
		var ret = new HashMap<String, String>(entity.getProperties());
		ret.put("key_", entity.getKey().getName());
		return ret;
	}

	private static void appendProperiesString(Map<String, String> properties, Consumer<String> out) {
		out.accept("{");
		boolean first = true;
		for (var k : properties.keySet()) {
			if (!first) {
				out.accept(",");
			} else {
				first = false;
			}
			out.accept(k);
			out.accept(":");
			out.accept("$");
			out.accept(k);
		}
		out.accept("}");
	}

	private class SessionImpl implements Session {

//		public String addGreeting(String message) {
//			try (var session = this.driver.session()) {
//				return session.executeWrite(tx -> {
//					var query = new Query("CREATE (a:Greeting) SET a.message = $message RETURN a.message",
//							Values.parameters("message", message));
//					var result = tx.run(query);
//					return result.single().get(0).asString();
//				});
//			}
//		}

		private final org.neo4j.driver.Session delegate;

		public SessionImpl(org.neo4j.driver.Session delegate) {
			super();
			assert delegate != null;
			this.delegate = delegate;
		}

		@Override
		public void addEntity(Entity entity) {
			this.delegate.executeWriteWithoutResult(tx -> {
				var query = createEntityQuery(entity);
				logger.debug("addEntity query=<{}>", query);
				tx.run(query);
			});
		}

		@Override
		public void addRelation(Relation relation) {
		}

		@Override
		public void close() {
			this.delegate.close();
		}
	}

}
