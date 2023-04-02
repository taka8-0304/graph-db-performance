package neo4j;

import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import model.Entity;
import model.EntityKey;
import model.Relation;
import model.RelationKey;
import model.Repository;

public class Neo4jRepositoryTest {

	@Test
	public void testAddEntity() {
		this.test(session -> {
			session.addEntity(new Entity(new EntityKey("Person", "B1"), Map.of("name", "good")));
		});
	}

	@Test
	public void testAddRelation() {
		this.test(session -> {
			var from = new EntityKey("Person", "C1");
			var to = new EntityKey("Person", "C2");
			session.addEntity(new Entity(from, Map.of("name", "c1")));
			session.addEntity(new Entity(to, Map.of("name", "c2")));
			session.addRelation(new Relation(new RelationKey(from, to, "IS"), Map.of("name", "good")));
		});
	}

	private void test(Consumer<Repository.Session> scenario) {
		try (var sut = new Neo4jRepository("bolt://localhost:7687", "neo4j", "password")) {
			try (var session = sut.newSession()) {
				scenario.accept(session);
			}
		}
	}

}
