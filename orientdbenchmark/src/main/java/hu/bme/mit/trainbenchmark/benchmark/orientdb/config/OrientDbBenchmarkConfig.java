package hu.bme.mit.trainbenchmark.benchmark.orientdb.config;

import hu.bme.mit.trainbenchmark.benchmark.config.BenchmarkConfig;
import hu.bme.mit.trainbenchmark.benchmark.config.ModificationMethod;
import hu.bme.mit.trainbenchmark.constants.Query;
import hu.bme.mit.trainbenchmark.constants.Scenario;

import org.apache.commons.cli.ParseException;

public class OrientDbBenchmarkConfig extends BenchmarkConfig {

	protected boolean javaApi;

	public OrientDbBenchmarkConfig(final String[] args, final String tool) throws ParseException {
		super(args, tool);
	}

	public OrientDbBenchmarkConfig(final Scenario scenario, final int size, final String tool, final int runIndex, final Query query,
			final int iterationCount, final ModificationMethod modificationMethod, final long modificationConstant, final boolean javaApi) {
		super(scenario, size, tool, runIndex, query, iterationCount, modificationMethod, modificationConstant);
		this.javaApi = javaApi;
	}

	@Override
	protected void initOptions() {
		super.initOptions();

		options.addOption("javaapi", false, "use the faster, low-level Java API for querying");
	}

	@Override
	public void processArguments(final String[] args) throws ParseException {
		super.processArguments(args);

		javaApi = cmd.hasOption("javaapi");
	}

	public boolean isJavaApi() {
		return javaApi;
	}

}
