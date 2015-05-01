package hu.bme.mit.trainbenchmark.benchmark.orientdb.benchmarkcases;

import hu.bme.mit.trainbenchmark.benchmark.checker.Checker;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.driver.OrientDbDriver;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbMatch;
import hu.bme.mit.trainbenchmark.constants.Query;

public abstract class OrientDbChecker<M extends OrientDbMatch> extends Checker<M> {

	protected final OrientDbDriver driver;

	public OrientDbChecker(final OrientDbDriver driver) {
		super();
		this.driver = driver;
	}

	public static OrientDbChecker newInstance(final OrientDbDriver driver, final Query query) {
		switch (query) {
		case POSLENGTH:
			return new OrientDbPosLengthChecker(driver);
		case ROUTESENSOR:
			return new OrientDbRouteSensorChecker(driver);
		case SEMAPHORENEIGHBOR:
			return new OrientDbSemaphoreNeighborChecker(driver);
		case SWITCHSENSOR:
			return new OrientDbSwitchSensorChecker(driver);
		case SWITCHSET:
			return new OrientDbSwitchSetChecker(driver);
		default:
			throw new UnsupportedOperationException("Query " + query + " not supported");
		}
	}
	
}
