package model;

import io.micrometer.core.instrument.MeterRegistry;

public interface Scenario {

	void accept(Repository repository, MeterRegistry meterRegistry);

}
