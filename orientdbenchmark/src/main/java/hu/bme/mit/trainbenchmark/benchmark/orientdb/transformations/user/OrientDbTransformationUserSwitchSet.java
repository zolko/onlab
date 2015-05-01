package hu.bme.mit.trainbenchmark.benchmark.orientdb.transformations.user;

import hu.bme.mit.trainbenchmark.benchmark.orientdb.driver.OrientDbDriver;

import java.util.Collection;

import com.tinkerpop.blueprints.Vertex;

public class OrientDbTransformationUserSwitchSet extends OrientDbTransformationUser {

	protected OrientDbTransformationUserSwitchSet(final OrientDbDriver orientDriver) {
		super(orientDriver);
	}

	@Override
	public void rhs(final Collection<Vertex> switches) {
		for (final Vertex sw : switches) {
			
		}
	}
	
}
