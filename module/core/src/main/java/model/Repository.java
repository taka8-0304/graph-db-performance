package model;

import java.util.function.Consumer;

public interface Repository {

	interface Session extends Updater, AutoCloseable {

		default void execute(Consumer<Updater> operation) {
		}

		@Override
		void close();

	}

	Session newSession();

}
