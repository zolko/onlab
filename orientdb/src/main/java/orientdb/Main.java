package orientdb;

import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class Main {

	public static void main(String[] args) {
		OrientGraph g = new OrientGraph("plocal:/home/OrientDB/railway-test-1");
		System.out.println("OrientGraph created!");
		OGremlinHelper.global().create();
		try {
			new OCommandGremlin("g.loadGraphML('/home/Documents/railway-test-1.graphml')").execute();
			System.out.println("The railway-test-1.graphml has been loaded.");
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
			}
		}
		
	}

}
