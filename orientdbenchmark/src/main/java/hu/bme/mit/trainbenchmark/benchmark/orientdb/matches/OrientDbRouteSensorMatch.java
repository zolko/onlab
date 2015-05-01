package hu.bme.mit.trainbenchmark.benchmark.orientdb.matches;

import hu.bme.mit.trainbenchmark.benchmark.matches.RouteSensorMatch;
import hu.bme.mit.trainbenchmark.constants.QueryConstants;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.structures.Row;

public class OrientDbRouteSensorMatch extends OrientDbMatch implements RouteSensorMatch {

	public OrientDbRouteSensorMatch(final Row match) {
		super(match);
	}

	@Override
	public Vertex getRoute() {
		return (Vertex) match.getColumn(QueryConstants.VAR_ROUTE);
	}

	@Override
	public Vertex getSensor() {
		return (Vertex) match.getColumn(QueryConstants.VAR_SENSOR);
	}

	@Override
	public Vertex getSwP() {
		return (Vertex) match.getColumn(QueryConstants.VAR_SWP);
	}

	@Override
	public Vertex getSw() {
		return (Vertex) match.getColumn(QueryConstants.VAR_SW);
	}

	@Override
	public Vertex[] toArray() {
		return new Vertex[] { getRoute(), getSensor(), getSwP(), getSw() };
	}
	
}
