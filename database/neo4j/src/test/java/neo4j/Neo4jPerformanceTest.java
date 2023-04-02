package neo4j;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import model.Scenario;
import scenario.AddEntityScenario;

public class Neo4jPerformanceTest {

	private static final Logger logger = LoggerFactory.getLogger(Neo4jPerformanceTest.class);

	@Test
	public void addEntity() {
		this.test(new AddEntityScenario(10, 1));
	}

	private void test(Scenario scenario) {
		try (var sut = new Neo4jRepository("bolt://localhost:7687", "neo4j", "password")) {
			var meterRegistry = new SimpleMeterRegistry();
			scenario.accept(sut, meterRegistry);
			logger.info(meterRegistry.getMetersAsString());
		}
	}

}
