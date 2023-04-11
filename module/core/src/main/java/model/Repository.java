package model;

public interface Repository {

	interface Session extends AutoCloseable {


		void addEntity(Entity entity);

		void addRelation(Relation relation);

		@Override
		void close();

	}

	Session newSession();

}
