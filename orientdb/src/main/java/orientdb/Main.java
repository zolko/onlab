package orientdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

public class Main {

	public static void main(String[] args) throws IOException {
		String dbPath = "/home/zolko/Databases/railway-repair-1";
		String graphmlPath = "/home/zolko/Documents/railway-repair-1.graphml";
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
			reader.setVertexIdKey("id");
			reader.inputGraph(new FileInputStream(graphmlPath));
			System.out.println("The railway-user-1.graphml has been loaded. (time: " + (System.currentTimeMillis() - start_time) + " ms)");

			// Create a gremlin command
			OCommandGremlin gremcomm = new OCommandGremlin();

			// First command. Some reason it takes more time then other commands.
			gremcomm.setText("g.V('labels',':Route')").execute();
			
			// (PosLength)
			start_time = System.currentTimeMillis();
			gremcomm.setText("g.V.has('Segment_length', T.lte, 0).id");
			System.out.println("The PosLength pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (SwitchSensor)
			start_time = System.currentTimeMillis();
			gremcomm.setText("g.V('labels',':Switch:TrackElement').except(" +
							 "g.V('labels',':Switch:TrackElement').filter{it.bothE('TrackElement_sensor').hasNext()}.toList()).id");
			System.out.println("The SwitchSensor pattern result3:");
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
			gremcomm.setText("switchpositions2 = []; g.V('labels',':Signal').has('Signal_currentState', T.eq, 'GO').in('Route_exit')" +
							 ".out('Route_switchPosition').fill(switchpositions2).id").execute();
			gremcomm.setText("switchpos = ''; g.V('labels',':Switch:TrackElement').sideEffect{switchpos = it.Switch_currentState}" +
							 ".in('SwitchPosition_switch').has('id', T.in, switchpositions2)" +
							 ".filter{it.SwitchPosition_switchState != switchpos}.id");
			System.out.println("The SwitchSet pattern result2:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			// (SignalNeighbor)
			start_time = System.currentTimeMillis();
			/*
			gremcomm.setText("signal = []; route1 = []; sensor = []; g.V('labels',':Route').store(route1).and(" +
							 "_().out('Route_exit').store(signal)," +
							 "_().out('Route_routeDefinition').in('TrackElement_sensor')" +
							 ".out('TrackElement_connectsTo').out('TrackElement_sensor').store(sensor).in('Route_routeDefinition').and(" +
							 "_().except(route1).out('Route_entry').except(signal)," +
							 // nem biztos, hogy ez a sor kell (Route1?=Route2)
							 "_().out('Route_entry').except(signal))," +
							 "_().out('Route_exit').in('Route_entry').out('Route_routeDefinition').except(sensor)).id");
			*/
			gremcomm.setText("signal = []; route1 = []; sensor = []; g.V('labels',':Route').store(route1).and(" +
					 "_().out('Route_exit').store(signal)," +
					 "_().out('Route_routeDefinition').in('TrackElement_sensor')" +
					 ".out('TrackElement_connectsTo').out('TrackElement_sensor').store(sensor).in('Route_routeDefinition')" +
					 ".except(route1).out('Route_entry').except(signal)," +
					 "_().out('Route_exit').in('Route_entry').out('Route_routeDefinition').except(sensor)).id");
			System.out.println("The SignalNeighbor pattern result:");
			System.out.println(gremcomm.execute());
			System.out.println("Running time: " + (System.currentTimeMillis() - start_time) + " ms");
			
			/*
			OCommandGremlin h = new OCommandGremlin("hercules = g.V('name', 'hercules').next()");
			h.execute();
			System.out.println("Hercules variable created.");
			System.out.println(h.getText());
			h.setText("hercules.out('mother', 'father').name");
			System.out.println(h.getText());
			Object execute = h.execute();
			System.out.println(execute.getClass());
			System.out.println("Hercules parents name: " + execute);
			*/
		} finally {
			if (g != null) {
				g.shutdown();
				System.out.println("OrientGraph closed!");
			}
		}
		
	}

}
