package neo4j;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Entity;
import model.EntityKey;

public class Neo4jRepositoryTest {

	private static final Logger logger = LoggerFactory.getLogger(Neo4jRepositoryTest.class);

	@Test
	public void testUpdate() {
		try (var sut = new Neo4jRepository("bolt://localhost:7687", "neo4j", "password")) {
			try (var session = sut.newSession()) {
				session.addEntity(new Entity(new EntityKey("Person", "B1"), Map.of("name", "good")));
			}
		}
	}

}
