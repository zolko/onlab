package orientdb;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class Main {

	public static void main(String[] args) throws IOException {
		String dbPath = "/home/zolko/Databases/railway-test-1";
		String graphmlPath = "/home/zolko/Documents/railway-test-1.graphml";
		// String dbPath = "E:/Projects/OrientDB/railway_test-1";
		// String graphmlPath = "C:/Users/Zoltán/Google Drive/BME/Aktuális/Önlab/railway-test-1.graphml";
		File db = new File(dbPath);
		
		// Delete previous database, if it exists
		if (db.exists()) {
			FileUtils.deleteDirectory(db);
		}
		
		// Open new orient database
		OrientGraph g = new OrientGraph("plocal:" + dbPath);
		System.out.println("OrientGraph opened!");
		OGremlinHelper.global().create();
		try {
			// Load the graphml file
			new OCommandGremlin("g.loadGraphML('" + graphmlPath + "')").execute();
			System.out.println("The railway-test-1.graphml has been loaded.");
			
			OCommandGremlin gremcomm = new OCommandGremlin();
			
			// (PosLength)
			gremcomm.setText("g.V.has('Segment_length', T.lte, 0).id");
			System.out.println("The PosLength pattern result:");
			System.out.println(gremcomm.execute());
			
			// (SwitchSensor)
			gremcomm.setText("g.V('labels',':Switch:TrackElement').except(g.V('labels',':Sensor').in('TrackElement_sensor').dedup()" + 
							 ".filter{it.labels == ':Switch:TrackElement'}.toList()).id");
			System.out.println("The SwitchSensor pattern result:");
			System.out.println(gremcomm.execute());
			
			// (RouteSensor)
			gremcomm.setText("sensors = []").execute();
			gremcomm.setText("g.V('labels',':Route').out('Route_routeDefinition').fill(sensors).id").execute();
			gremcomm.setText("g.V('labels',':Route').out('Route_switchPosition')." + 
							 "out('SwitchPosition_switch').out('TrackElement_sensor')." + 
							 "has('id', T.in, sensors).id");
			System.out.println("The RouteSensor pattern result:");
			System.out.println(gremcomm.execute());
			
			// (SwitchSet)
			gremcomm.setText("switchpositions = []").execute();
			gremcomm.setText("g.V('labels',':Signal').has('Signal_currentState', T.eq, 'GO').in('Route_exit')" +
							 ".out('Route_switchPosition').fill(switchpositions).id").execute();
			gremcomm.setText("g.V('labels',':SwitchPosition').or(" +
							 "_().has('SwitchPosition_switchState', T.eq, 'RIGHT').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'RIGHT')," +
							 "_().has('SwitchPosition_switchState', T.eq, 'LEFT').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'LEFT')," +
							 "_().has('SwitchPosition_switchState', T.eq, 'STRAIGHT').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'STRAIGHT')," +
							 "_().has('SwitchPosition_switchState', T.eq, 'FAILURE').out('SwitchPosition_switch').has('Switch_currentState', T.neq, 'FAILURE')" +
							 ").has('id', T.in, switchpositions).id");
			System.out.println("The SwitchSet pattern result:");
			System.out.println(gremcomm.execute());
			
			// (SignalNeighbor)
			gremcomm.setText("g.V('labels',':Route').and(" +
							 "_().out('Route_exit').in('Route_entry').out('Route_routeDefinition').in('TrackElement_sensor')" +
							 ".in('TrackElement_connectsTo').out('TrackElement_sensor').in('Route_routeDefinition')," +
							 "_().as('route1').out('Route_exit').in('Route_entry').as('route2').out('Route_routeDefinition')" +
							 ".in('Route_routeDefinition').except('route1').except('route2'))");
			System.out.println("The SignalNeighbor pattern result:");
			System.out.println(gremcomm.execute());
			
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
