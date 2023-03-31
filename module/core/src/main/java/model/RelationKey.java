package model;

import java.util.Objects;

public class RelationKey {

	private final EntityKey from;

	private final EntityKey to;

	private final String type;

	public RelationKey(EntityKey from, EntityKey to, String type) {
		super();
		assert from != null;
		assert to != null;
		assert type != null;
		this.from = from;
		this.to = to;
		this.type = type;
	}

	public EntityKey getFrom() {
		return from;
	}

	public EntityKey getTo() {
		return to;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(from, to, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelationKey other = (RelationKey) obj;
		return Objects.equals(from, other.from) && Objects.equals(to, other.to) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "Relation [from=" + from + ", to=" + to + ", type=" + type + "]";
	}

}
