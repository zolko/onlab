package orientdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
import com.tinkerpop.gremlin.Tokens.T;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;

public class Main {

	public static void main(String[] args) throws IOException {
		String dbPath = "/home/zolko/Databases/railway-test-1";
		String graphmlPath = "/home/zolko/Documents/railway-test-1.graphml";
		// String dbPath = "E:/Projects/OrientDB/railway-test-1";
		// String graphmlPath = "E:/Drive/BME/Aktu�lis/�nlab/railway-test-1.graphml";
		File db = new File(dbPath);
		
		// Delete previous database, if it exists
		if (db.exists()) {
			FileUtils.deleteDirectory(db);
		}
		
		// Open new orient database
		long start_time = System.currentTimeMillis();
		OrientGraph g = new OrientGraph("plocal:" + dbPath);
		System.out.println("OrientGraph opened! (time: " + (System.currentTimeMillis() - start_time) + " ms)");
		
		OGremlinHelper.global().create();
		try {
			// Load the graphml file
			start_time = System.currentTimeMillis();
			GraphMLReader reader = new GraphMLReader(g);
			reader.inputGraph(new FileInputStream(graphmlPath), 100);
			
			System.out.println("The railway-user-1.graphml has been loaded. (time: " + (System.currentTimeMillis() - start_time) + " ms)");

			// Create a gremlin command
			OCommandGremlin gremcomm = new OCommandGremlin();
			
			// (PosLength)
			String dir = "/home/zolko/git/onlab/orientdb/src/main/resources/queries/";
			List<String> lines = FileUtils.readLines(
					FileUtils.getFile(dir + "PosLength.gremlin"));
			List<Vertex> posLengthResult;
			System.out.println("The PosLength pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					posLengthResult = gremcomm.setText(line).execute();
					System.out.println(posLengthResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (SwitchSensor)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "SwitchSensor.gremlin"));
			List<Vertex> switchSensorResult;
			System.out.println("The SwitchSensor pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					switchSensorResult = gremcomm.setText(line).execute();
					System.out.println(switchSensorResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (RouteSensor)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "RouteSensor.gremlin"));
			List<Vertex> routeSensorResult;
			System.out.println("The RouteSensor pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					routeSensorResult = gremcomm.setText(line).execute();
					System.out.println(routeSensorResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (SwitchSet)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "SwitchSet.gremlin"));
			List<Vertex> switchSetResult;
			System.out.println("The SwitchSet pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					switchSetResult = gremcomm.setText(line).execute();
					System.out.println(switchSetResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (SignalNeighbor)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "SignalNeighbor.gremlin"));
			List<Vertex> signalNeighborResult;
			System.out.println("The SignalNeighbor pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					signalNeighborResult = gremcomm.setText(line).execute();
					System.out.println(signalNeighborResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			/*
			// (PosLength)
			start_time = System.currentTimeMillis();
			gremcomm.setText("PosLength = new Table()").execute();
			gremcomm.setText("g.V.has('Segment_length', T.lte, 0).as('segment').table(PosLength)").execute();
			gremcomm.setText("PosLength");
			System.out.println("The PosLength pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (SwitchSensor)
			start_time = System.currentTimeMillis();
			gremcomm.setText("SwitchSensor = new Table()").execute();
			gremcomm.setText("g.V('labels',':Switch:TrackElement').filter{!it.outE('TrackElement_sensor').hasNext()}"
					+ ".as('switch').table(SwitchSensor)").execute();
			gremcomm.setText("SwitchSensor");
			System.out.println("The SwitchSensor pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (RouteSensor)
			start_time = System.currentTimeMillis();
			gremcomm.setText("RouteSensor = new Table()").execute();
			gremcomm.setText("sensors = g.V('labels',':Route').out('Route_routeDefinition').toList()").execute();
			gremcomm.setText("g.V('labels',':Route').as('route').out('Route_switchPosition').as('switchposition')"
					+ ".out('SwitchPosition_switch').as('switch').out('TrackElement_sensor').as('sensor')"
					+ ".except(sensors).table(RouteSensor)").execute();
			gremcomm.setText("RouteSensor");
			System.out.println("The RouteSensor pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");

			// (SwitchSet)
			start_time = System.currentTimeMillis();
			gremcomm.setText("SwitchSet = new Table()").execute();
			gremcomm.setText("position = ''; g.V('labels',':SwitchPosition').as('switchposition').in('Route_switchPosition').as('route')"
					+ ".out('Route_exit').has('Signal_currentState', T.eq, 'GO').as('signal').back('switchposition')"
					+ ".sideEffect{position = it.SwitchPosition_switchState}.out('SwitchPosition_switch')"
					+ ".filter{it.Switch_currentState != position}.as('switch').table(SwitchSet)").execute();
			gremcomm.setText("SwitchSet");
			System.out.println("The SwitchSet pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (SignalNeighbor)
			start_time = System.currentTimeMillis();
			gremcomm.setText("SignalNeighbor = new Table()").execute();
			gremcomm.setText("sig = []; g.V('labels',':Route').as('route1').out('Route_exit').as('signal').store(sig)"
					+ ".back('route1').out('Route_routeDefinition').as('sensor1').in('TrackElement_sensor').as('te1')"
					+ ".out('TrackElement_connectsTo').as('te2').out('TrackElement_sensor').as('sensor2')"
					+ ".in('Route_routeDefinition').except('route1').as('route2').or("
					+ "_().filter{!it.outE('Route_entry').hasNext()},"
					+ "_().out('Route_entry').except(sig)).table(SignalNeighbor)").execute();
			gremcomm.setText("SignalNeighbor");
			System.out.println("The SignalNeighbor pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			/*
			 * old patterns
			// (PosLength)
			start_time = System.currentTimeMillis();
			gremcomm.setText("g.V.has('Segment_length', T.lte, 0)");
			System.out.println("The PosLength pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (SwitchSensor)
			start_time = System.currentTimeMillis();
			gremcomm.setText("g.V('labels',':Switch:TrackElement').except(" +
							 "g.V('labels',':Switch:TrackElement').filter{it.bothE('TrackElement_sensor').hasNext()}.toList()).id");
			System.out.println("The SwitchSensor pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (RouteSensor)
			start_time = System.currentTimeMillis();
			gremcomm.setText("sensors = []; g.V('labels',':Route').out('Route_routeDefinition').fill(sensors).id").execute();
			gremcomm.setText("g.V('labels',':Route').out('Route_switchPosition')." + 
							 "out('SwitchPosition_switch').out('TrackElement_sensor')." + 
							 "has('id', T.notin, sensors).id");
			System.out.println("The RouteSensor pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (SwitchSet)
			start_time = System.currentTimeMillis();
			gremcomm.setText("switchpositions = []; g.V('labels',':Signal').has('Signal_currentState', T.eq, 'GO').in('Route_exit')" +
							 ".out('Route_switchPosition').fill(switchpositions).id").execute();			
			gremcomm.setText("g.V('labels',':SwitchPosition').or(" +
							 "_().has('SwitchPosition_switchState', T.eq, 'RIGHT').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'RIGHT')," +
							 "_().has('SwitchPosition_switchState', T.eq, 'LEFT').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'LEFT')," +
							 "_().has('SwitchPosition_switchState', T.eq, 'STRAIGHT').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'STRAIGHT')," +
							 "_().has('SwitchPosition_switchState', T.eq, 'FAILURE').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'FAILURE')" +
							 ").has('id', T.in, switchpositions).id");
			System.out.println("The SwitchSet pattern result1:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			start_time = System.currentTimeMillis();
			gremcomm.setText("position = ''; g.V('labels',':SwitchPosition').as('switchposition').in('Route_switchPosition')"
					+ ".out('Route_exit').has('Signal_currentState', T.eq, 'GO').back('switchposition')"
					+ ".sideEffect{position = it.SwitchPosition_switchState}.out('SwitchPosition_switch')"
					+ ".filter{it.Switch_currentState != position}.back('switchposition').id");
			System.out.println("The SwitchSet pattern result2:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (SignalNeighbor)
			start_time = System.currentTimeMillis();
			gremcomm.setText("signal = []; route1 = []; sensor = []; g.V('labels',':Route').store(route1).and("
					 + "_().out('Route_exit').store(signal),"
					 + "_().out('Route_routeDefinition').in('TrackElement_sensor')"
					 + ".out('TrackElement_connectsTo').out('TrackElement_sensor').store(sensor).or("
					 + "_().filter{!it.bothE('Route_routeDefinition').hasNext()},"
					 + "_().in('Route_routeDefinition').except(route1).out('Route_entry').except(signal)),"
					 + "_().out('Route_exit').or("
					 + "_().filter{!it.bothE('Route_entry').hasNext()},"
					 + "_().in('Route_entry').out('Route_routeDefinition').except(sensor))).id");
			System.out.println("The SignalNeighbor pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			*/
			
		} finally {
			if (g != null) {
				g.shutdown();
				System.out.println("OrientGraph closed!");
			}
		}
		
	}

}
