package hu.bme.mit.trainbenchmark.benchmark.orientdb.benchmarkcases;

import static hu.bme.mit.trainbenchmark.benchmark.orientdb.constants.OrientDbConstants.labelSegment;
import static hu.bme.mit.trainbenchmark.constants.ModelConstants.LENGTH;
import static hu.bme.mit.trainbenchmark.constants.QueryConstants.VAR_SEGMENT;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.driver.OrientDbDriver;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbPosLengthMatch;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.Vertex;

public class OrientDbPosLengthChecker extends OrientDbChecker<OrientDbPosLengthMatch> {

	public OrientDbPosLengthChecker(final OrientDbDriver orientDriver) {
		super(orientDriver);
	}
	
	@Override
	public Collection<OrientDbPosLengthMatch> check() {
		final Collection<OrientDbPosLengthMatch> matches = new HashSet<>();
		
		final OrientGraph graphDb = driver.getGraphDb();
		
		final Iterable<Vertex> segments = graphDb.getVertices("labels", labelSegment);
		for (final Vertex segment : segments) {
			final Integer length = (Integer) segment.getProperty(LENGTH);
			// Segment.length <= 0
			if (length <= 0) {
				final Map<String, Object> match = new HashMap<>();
				match.put(VAR_SEGMENT, segment);
				matches.add(new OrientDbPosLengthMatch(match));
			}
		}
		
		return matches;
	}
}
