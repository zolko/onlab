package orientdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
import com.tinkerpop.pipes.util.structures.Row;

public class Main {

	public static void main(String[] args) throws IOException {
		String dbPath = "/home/zolko/Databases/railway-test-1";
		String graphmlPath = "/home/zolko/Documents/railway-test-1-new.graphml";
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
			Transformations t = new Transformations(g);
			List<String> lines;
			String dir = "/home/zolko/git/onlab/orientdb/src/main/resources/queries/";
			
			// Turn on the repair session
			boolean isRepair = true;
			boolean isUser = false;
			
			// (PosLength)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "NewPosLength.gremlin"));
			List<Row> posLengthResult;
			System.out.println("The PosLength pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					posLengthResult = gremcomm.setText(line).execute();
					System.out.println(posLengthResult);
					if (isRepair)
						t.posLengthRepair(posLengthResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (SwitchSensor)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "NewSwitchSensor.gremlin"));
			List<Row> switchSensorResult;
			System.out.println("The SwitchSensor pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					switchSensorResult = gremcomm.setText(line).execute();
					System.out.println(switchSensorResult);
					if (isRepair)
						t.switchSensorRepair(switchSensorResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (RouteSensor)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "NewRouteSensor.gremlin"));
			List<Row> routeSensorResult;
			System.out.println("The RouteSensor pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					routeSensorResult = gremcomm.setText(line).execute();
					System.out.println(routeSensorResult);
					if (isRepair)
						t.routeSensorRepair(routeSensorResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (SwitchSet)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "NewSwitchSet.gremlin"));
			List<Row> switchSetResult;
			System.out.println("The SwitchSet pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					switchSetResult = gremcomm.setText(line).execute();
					System.out.println(switchSetResult);
					if (isRepair)
						t.switchSetRepair(switchSetResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			// (SemaphoreNeighbor)
			lines = FileUtils.readLines(
					FileUtils.getFile(dir + "NewSemaphoreNeighbor.gremlin"));
			List<Row> semaphoreNeighborResult;
			System.out.println("The SemaphoreNeighbor pattern result:");
			for (String line : lines) {
				if (lines.indexOf(line) == lines.size() - 1) {
					semaphoreNeighborResult = gremcomm.setText(line).execute();
					System.out.println(semaphoreNeighborResult);
					if (isRepair)
						t.semaphoreNeighborRepair(semaphoreNeighborResult);
				} else {
					gremcomm.setText(line).execute();
				}
			}
			
			if (isRepair || isUser) {
				// (PosLengthTest)
				lines = FileUtils.readLines(
						FileUtils.getFile(dir + "NewPosLength.gremlin"));
				System.out.println("The PosLength pattern after transformation:");
				for (String line : lines) {
					if (lines.indexOf(line) == lines.size() - 1) {
						System.out.println(gremcomm.setText(line).execute());
					} else {
						gremcomm.setText(line).execute();
					}
				}
				
				// (SwitchSensorTest)
				lines = FileUtils.readLines(
						FileUtils.getFile(dir + "NewSwitchSensor.gremlin"));
				System.out.println("The SwitchSensor pattern after transformation:");
				for (String line : lines) {
					if (lines.indexOf(line) == lines.size() - 1) {
						System.out.println(gremcomm.setText(line).execute());
					} else {
						gremcomm.setText(line).execute();
					}
				}
				
				// (RouteSensorTest)
				lines = FileUtils.readLines(
						FileUtils.getFile(dir + "NewRouteSensor.gremlin"));
				System.out.println("The RouteSensor pattern after transformation:");
				for (String line : lines) {
					if (lines.indexOf(line) == lines.size() - 1) {
						System.out.println(gremcomm.setText(line).execute());
					} else {
						gremcomm.setText(line).execute();
					}
				}
				
				// (SwitchSetRepairTest)
				lines = FileUtils.readLines(
						FileUtils.getFile(dir + "NewSwitchSet.gremlin"));
				System.out.println("The SwitchSet pattern after transformation:");
				for (String line : lines) {
					if (lines.indexOf(line) == lines.size() - 1) {
						System.out.println(gremcomm.setText(line).execute());
					} else {
						gremcomm.setText(line).execute();
					}
				}
				
				// (SemaphoreNeighborRepairTest)
				lines = FileUtils.readLines(
						FileUtils.getFile(dir + "NewSemaphoreNeighbor.gremlin"));
				System.out.println("The SemaphoreNeighbor pattern after transformation:");
				for (String line : lines) {
					if (lines.indexOf(line) == lines.size() - 1) {
						System.out.println(gremcomm.setText(line).execute());
					} else {
						gremcomm.setText(line).execute();
					}
				}
			}
			
		} finally {
			if (g != null) {
				g.shutdown();
				System.out.println("OrientGraph closed!");
			}
		}
		
	}

}
