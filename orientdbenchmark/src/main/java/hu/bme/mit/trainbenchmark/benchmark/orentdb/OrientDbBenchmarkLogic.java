package hu.bme.mit.trainbenchmark.benchmark.orentdb;

import hu.bme.mit.trainbenchmark.benchmark.orentdb.config.OrientDbBenchmarkConfig;
import hu.bme.mit.trainbenchmark.benchmark.scenarios.AbstractBenchmarkLogic;

import org.apache.commons.cli.ParseException;

public class OrientDbBenchmarkLogic extends AbstractBenchmarkLogic {
	OrientDbBenchmarkConfig nbc;
	
	public OrientDbBenchmarkLogic(final String[] args) throws ParseException {
		bc = nbc = new OrientDbBenchmarkConfig(args, getTool());
	}

	public OrientDbBenchmarkLogic(final OrientDbBenchmarkConfig nbc) {
		super(nbc);
		this.nbc = nbc;
	}

	@Override
	protected String getTool() {
		return "OrientDb";
	}
	
}
