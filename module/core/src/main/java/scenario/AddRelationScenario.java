package scenario;

import java.util.HashMap;

import io.micrometer.core.instrument.MeterRegistry;
import model.Entity;
import model.EntityKey;
import model.Relation;
import model.RelationKey;
import model.Repository;
import model.Scenario;

public class AddRelationScenario implements Scenario {

	private final int tryCount;

	private final int propertySize;

	public AddRelationScenario(int tryCount, int propertySize) {
		super();
		this.tryCount = tryCount;
		this.propertySize = propertySize;
	}

	@Override
	public void accept(Repository repository, MeterRegistry meterRegistry) {
		try (var session = repository.newSession()) {
			for (int i = 0; i < this.tryCount; i++) {
				var id = i;
				var fromKey = new EntityKey("Person", "key_f_" + id);
				var toKey = new EntityKey("Person", "key_t_" + id);
				session.addEntity(new Entity(fromKey, null));
				session.addEntity(new Entity(toKey, null));
				var properties = new HashMap<String, String>();
				for (int j = 0; j < this.propertySize; j++) {
					properties.put("p_" + j, "p_" + j + "_" + id);
				}
				meterRegistry.timer("execution_time").record(() -> {
					session.addRelation(new Relation(new RelationKey(fromKey, toKey, "IS_FRIENDS_WITH"), properties));
				});
			}
		}
	}

}
