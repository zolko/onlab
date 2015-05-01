package hu.bme.mit.trainbenchmark.benchmark.orientdb.matches;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.structures.Row;

public abstract class OrientDbMatch {
	
	protected Row match;
	
	public OrientDbMatch(final Row match) {
		this.match = match;
	}
	
	public abstract Vertex[] toArray();
	
}
