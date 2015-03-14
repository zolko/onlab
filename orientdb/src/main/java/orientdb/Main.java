package orientdb;

import java.io.File;

import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class Main {

	public static void main(String[] args) {
		String dbPath = "/home/zolko/OrientDB/railway-test-1";
		String graphmlPath = "/home/zolko/Documents/railway-test-1.graphml";
		boolean dbExist = new File(dbPath).exists();
		// Open database
		OrientGraph g = new OrientGraph("plocal:" + dbPath);
		System.out.println("OrientGraph opened!");
		OGremlinHelper.global().create();
		try {
			// Load the graphml file. If it exists, don't load again!
			if (dbExist == false) {
				new OCommandGremlin("g.loadGraphML('" + graphmlPath + "')").execute();
				System.out.println("The railway-test-1.graphml has been loaded.");
			}
			
			OCommandGremlin gremcomm = new OCommandGremlin();
			
			// Check track elements segment length is negative or zero. (PosLength)
			gremcomm.setText("g.V.has('Segment_length', T.lte, 0).id");
			System.out.println("The PosLength pattern result:");
			System.out.println(gremcomm.execute());
			
			// (SwitchSensor)
			gremcomm.setText("switches = []").execute();
			gremcomm.setText("g.V('labels',':Sensor').in('TrackElement_sensor').dedup()" + 
							 ".filter{it.labels == ':Switch:TrackElement'}.fill(switches).id").execute();
			gremcomm.setText("g.V('labels',':Switch:TrackElement').has('id', T.notin, switches).id");
			System.out.println("The SwitchSensor pattern result:");
			System.out.println(gremcomm.execute());
			
			// (RouteSensor)
			gremcomm.setText("sensors = []").execute();
			gremcomm.setText("g.V('labels',':Route').out('Route_routeDefinition').fill(sensors).id").execute();
			gremcomm.setText("g.V('labels',':Route').out('Route_switchPosition')." + 
							 "out('SwitchPosition_switch').out('TrackElement_sensor')." + 
							 "has('id', T.notin, sensors).id");
			System.out.println("The RouteSensor pattern result:");
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
