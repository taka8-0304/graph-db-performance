package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Entity {

	private final EntityKey key;

	private final Map<String, String> properties;

	public Entity(EntityKey key, Map<String, String> properties) {
		super();
		assert key != null;
		this.key = key;
		if (properties != null) {
			this.properties = Collections.unmodifiableMap(new HashMap<>(properties));
		} else {
			this.properties = Collections.emptyMap();
		}
	}

	public EntityKey getKey() {
		return key;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

}
