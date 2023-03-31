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
import model.EntityKey;
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

	private static Query createEntityQuery(String alias, Entity entity) {
		var properties = createEntityProperties(entity);
		var builder = new StringBuilder();
		builder.append("CREATE (");
		builder.append(alias);
		builder.append(":");
		builder.append(entity.getKey().getType());
		builder.append(" ");
		appendProperiesString(properties, s -> builder.append(s));
		builder.append(")");
		return new Query(builder.toString(), Values.value(properties));
	}

	private static Query createRelationQuery(String fromAlias, String toAlias, String relationAlias,
			Relation relation) {
		var builder = new StringBuilder();
		appendMatchString(fromAlias, relation.getKey().getFrom(), s -> builder.append(s));
		builder.append(" ");
		appendMatchString(toAlias, relation.getKey().getTo(), s -> builder.append(s));
		builder.append(" ");
		appendRelationCreateString(fromAlias, toAlias, relationAlias, relation, s -> builder.append(s));
		var properties = new HashMap<>(relation.getProperties());
		properties.put(fromAlias, relation.getKey().getFrom().getName());
		properties.put(toAlias, relation.getKey().getTo().getName());
		return new Query(builder.toString(), Values.value(properties));
	}

	private static Map<String, String> createEntityProperties(Entity entity) {
		var ret = new HashMap<String, String>(entity.getProperties());
		ret.put("key_", entity.getKey().getName());
		return ret;
	}

	private static void appendMatchString(String alias, EntityKey key, Consumer<String> out) {
		out.accept("MATCH (");
		out.accept(alias);
		out.accept(":");
		out.accept(key.getType());
		out.accept(" ");
		appendProperiesString(Map.of("key_", "$" + alias), out);
		out.accept(")");
	}

	private static void appendRelationCreateString(String fromAlias, String toAlias, String relationAlias,
			Relation relation, Consumer<String> out) {
		out.accept("CREATE (");
		out.accept(fromAlias);
		out.accept(") - [");
		out.accept(relationAlias);
		out.accept(":");
		out.accept(relation.getKey().getType());
		if (!relation.getProperties().isEmpty()) {
			out.accept(" ");
			appendProperiesString(relation.getProperties(), out);
		}
		out.accept("] -> (");
		out.accept(toAlias);
		out.accept(")");
	}

	private static void appendProperiesString(Map<String, String> properties, Consumer<String> out) {
		out.accept("{");
		boolean first = true;
		for (var e : properties.entrySet()) {
			if (!first) {
				out.accept(",");
			} else {
				first = false;
			}
			out.accept(e.getKey());
			out.accept(":");
			if (e.getValue().startsWith("$")) {
				out.accept(e.getValue());
			} else {
				out.accept("$");
				out.accept(e.getKey());
			}
		}
		out.accept("}");
	}

	private class SessionImpl implements Session {

		private final org.neo4j.driver.Session delegate;

		public SessionImpl(org.neo4j.driver.Session delegate) {
			super();
			assert delegate != null;
			this.delegate = delegate;
		}

		@Override
		public void addEntity(Entity entity) {
			this.delegate.executeWriteWithoutResult(tx -> {
				var query = createEntityQuery("e", entity);
				logger.debug("addEntity query=<{}>", query);
				tx.run(query);
			});
		}

		@Override
		public void addRelation(Relation relation) {
			this.delegate.executeWriteWithoutResult(tx -> {
				var query = createRelationQuery("f", "t", "r", relation);
				logger.debug("addRelation query=<{}>", query);
				tx.run(query);
			});
		}

		@Override
		public void close() {
			this.delegate.close();
		}
	}

}
