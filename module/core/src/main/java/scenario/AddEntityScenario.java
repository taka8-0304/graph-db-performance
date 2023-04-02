package scenario;

import java.util.HashMap;

import io.micrometer.core.instrument.MeterRegistry;
import model.Entity;
import model.EntityKey;
import model.Repository;
import model.Scenario;

public class AddEntityScenario implements Scenario {

	private final int tryCount;

	private final int propertySize;

	public AddEntityScenario(int tryCount, int propertySize) {
		super();
		this.tryCount = tryCount;
		this.propertySize = propertySize;
	}

	@Override
	public void accept(Repository repository, MeterRegistry meterRegistry) {
		try (var session = repository.newSession()) {
			for (int i = 0; i < this.tryCount; i++) {
				var id = i;
				var properties = new HashMap<String, String>();
				for (int j = 0; j < this.propertySize; j++) {
					properties.put("p_" + j, "p_" + j + "_" + id);
				}
				meterRegistry.timer("execution_time").record(() -> {
					session.addEntity(new Entity(new EntityKey("Person", "key_" + id), properties));
				});
			}
		}
	}

}
