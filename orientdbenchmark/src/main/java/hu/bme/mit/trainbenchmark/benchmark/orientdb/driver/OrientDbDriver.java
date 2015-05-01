package hu.bme.mit.trainbenchmark.benchmark.orientdb.driver;

import static hu.bme.mit.trainbenchmark.constants.ModelConstants.DEFINED_BY;
import static hu.bme.mit.trainbenchmark.constants.ModelConstants.ENTRY;
import static hu.bme.mit.trainbenchmark.constants.ModelConstants.SENSOR;
import static hu.bme.mit.trainbenchmark.constants.ModelConstants.SENSOR_EDGE;
import hu.bme.mit.trainbenchmark.benchmark.driver.Driver;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbMatch;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbPosLengthMatch;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbRouteSensorMatch;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbSemaphoreNeighborMatch;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbSwitchSensorMatch;
import hu.bme.mit.trainbenchmark.benchmark.orientdb.matches.OrientDbSwitchSetMatch;
import hu.bme.mit.trainbenchmark.constants.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
import com.tinkerpop.pipes.util.structures.Row;



public class OrientDbDriver extends Driver<Vertex>{

	protected final String definedByEdge = "definedBy";
	protected final String entryEdge = "entry";
	protected final String sensorEdge = "sensor";
	
	protected final String sensorLabel = ":Sensor";
	
	protected OrientGraph graphDb;
	protected String dbPath;
	protected Comparator<Vertex> vertexComparator = new VertexComparator();
	
	public OrientDbDriver(final String dbPath) throws IOException {
		// delete old directory
		if (new File(dbPath).exists()) {
			FileUtils.deleteDirectory(new File(dbPath));
		}
		
		// start the database
		this.dbPath = dbPath;
	}
	
	@Override
	public void beginTransaction() {
		graphDb.begin();
	}

	@Override
	public void finishTransaction() {
		graphDb.commit();
	}

	@Override
	public void read(final String filePath) throws IOException {
		graphDb = new OrientGraph("plocal:" + dbPath);
		GraphMLReader graphMLReader = new GraphMLReader(graphDb);
		graphMLReader.inputGraph(filePath, 100);
	}

	public List<OrientDbMatch> runQuery(final Query query, final String queryDefinition) throws IOException {
		final List<OrientDbMatch> results = new ArrayList<>();

		OGremlinHelper.global().create();
		
		OCommandGremlin gremcomm = new OCommandGremlin(query);
		List<Row> result = gremcomm.execute();
		
		for (final Row row : result) {
			results.add(createMatch(query, row));
		}

		return results;
	}

	protected OrientDbMatch createMatch(final Query query, final Row row) {
		switch (query) {
		case POSLENGTH:
			return new OrientDbPosLengthMatch(row);
		case ROUTESENSOR:
			return new OrientDbRouteSensorMatch(row);
		case SEMAPHORENEIGHBOR:
			return new OrientDbSemaphoreNeighborMatch(row);
		case SWITCHSENSOR:
			return new OrientDbSwitchSensorMatch(row);
		case SWITCHSET:
			return new OrientDbSwitchSetMatch(row);
		default:
			throw new UnsupportedOperationException("Query not supported: " + query);
		}
	}

	@Override
	public void destroy() {
		graphDb.drop();
	}

	public String typeTranslator(String type) {
		String result;
		switch (type) {
			case "Segment":
				result = ":TrackElement:Segment";
				break;
			case "Signal":
				result = ":Signal";
				break;
			case "Sensor":
				result = ":Sensor";
				break;
			case "Switch":
				result = ":Switch:TrackElement";
				break;
			case "Route":
				result = ":Route";
				break;
			case "SwitchPosition":
				result = ":SwitchPosition";
				break;
			default:
				result = type;
				break;
		}
		return result;
	}
	
	// read

	@Override
	public List<Vertex> collectVertices(final String type) {
		final Iterable<Vertex> vertices = graphDb.getVertices("labels", typeTranslator(type));
		List<Vertex> list = Lists.newArrayList(vertices);
		return list;
	}

	// utility

	public OrientGraph getGraphDb() {
		return graphDb;
	}

	@Override
	public Comparator<Vertex> getElementComparator() {
		return vertexComparator;
	}

	@Override
	public String getExtension() {
		return ".graphml";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
