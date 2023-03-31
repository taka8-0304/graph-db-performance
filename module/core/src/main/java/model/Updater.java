package model;

public interface Updater {

	void addEntity(Entity entity);

	default void updateEntity(Entity entity) {

	}

	default void removeEntity(EntityKey key) {

	}

	void addRelation(Relation relation);

	default void updateRelation(Relation relation) {

	}

	default void removeRelation(RelationKey relation) {

	}
}