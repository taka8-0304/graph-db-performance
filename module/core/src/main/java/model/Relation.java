package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Relation {

	private final RelationKey key;

	private final Map<String, String> properties;

	public Relation(RelationKey key, Map<String, String> properties) {
		super();
		assert key != null;
		this.key = key;
		if (properties != null) {
			this.properties = Collections.unmodifiableMap(new HashMap<>(properties));
		} else {
			this.properties = Collections.emptyMap();
		}
	}

	public RelationKey getKey() {
		return key;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return "Relation [key=" + key + ", properties=" + properties + "]";
	}

}
