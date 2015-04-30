package orientdb;

import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.pipes.util.structures.Row;

public class Transformations {
	
	OrientGraph db;
	
	public Transformations(OrientGraph odb) {
		db = odb;
	}
	
	/*
	// repair
	
	public void posLengthRepair(List<Row> result) {
		Vertex selectedSegment = (Vertex)result.get(0).getColumn("segment");
		Integer length = (Integer)selectedSegment.getProperty("Segment_length");
		selectedSegment.setProperty("Segment_length", -length + 1);
	}
	
	public void routeSensorRepair(List<Row> result) {
		Vertex selectedRoute = (Vertex)result.get(0).getColumn("route");
		Vertex selectedSensor = (Vertex)result.get(0).getColumn("sensor");
		selectedRoute.addEdge("Route_routeDefinition", selectedSensor);
	}
	
	public void signalNeighborRepair(List<Row> result) {
		Vertex selectedRoute2 = (Vertex)result.get(0).getColumn("route2");
		Vertex selectedSignal = (Vertex)result.get(0).getColumn("signal");
		selectedRoute2.addEdge("Route_entry", selectedSignal);
	}
	
	public void switchSensorRepair(List<Row> result) {
		Vertex selectedSwitch = (Vertex)result.get(0).getColumn("switch");
		Vertex sensor = db.addVertex(null);
		sensor.setProperty("labels", ":Sensor");
		selectedSwitch.addEdge("TrackElement_sensor", sensor);
	}
	
	public void switchSetRepair(List<Row> result) {
		Vertex selectedSwitch = (Vertex)result.get(0).getColumn("switch");
		Vertex selectedSwitchPosition = (Vertex)result.get(0).getColumn("switchposition");
		String position = selectedSwitchPosition.getProperty("SwitchPosition_switchState");
		selectedSwitch.setProperty("Switch_currentState", position);
	}
	
	// user
	
	public void posLengthUser(Vertex segment) {
		segment.setProperty("Segment_length", 0);
	}
	
	public void routeSensorUser(Vertex route) {
		Iterable<Edge> definedBys = route.getEdges(Direction.OUT, "Route_routeDefinition");
		for (Edge definedBy : definedBys) {
			definedBy.remove();
			break;
		}
	}
	
	public void signalNeighborUser(Vertex route) {
		Iterable<Edge> entries = route.getEdges(Direction.OUT, "Route_entry");
		for (Edge entry : entries) {
			entry.remove();
		}
	}
	
	public void switchSensorUser(Vertex sw) {
		Iterable<Vertex> sensors = sw.getVertices(Direction.OUT, "TrackElement_sensor");
		for (Vertex sensor : sensors) {
			sensor.remove();
		}
	}
	
	public void switchSetUser() {
		
	}
	
	*/
	
	// Transformation in the new framework
	
	public void posLengthRepair(List<Row> result) {
		Vertex selectedSegment = (Vertex)result.get(0).getColumn("segment");
		Integer length = (Integer)selectedSegment.getProperty("length");
		selectedSegment.setProperty("length", -length + 1);
	}
	
	public void routeSensorRepair(List<Row> result) {
		Vertex selectedRoute = (Vertex)result.get(0).getColumn("route");
		Vertex selectedSensor = (Vertex)result.get(0).getColumn("sensor");
		selectedRoute.addEdge("definedBy", selectedSensor);
	}
	
	public void semaphoreNeighborRepair(List<Row> result) {
		Vertex selectedRoute2 = (Vertex)result.get(0).getColumn("route2");
		Vertex selectedSignal = (Vertex)result.get(0).getColumn("signal");
		selectedRoute2.addEdge("entry", selectedSignal);
	}
	
	public void switchSensorRepair(List<Row> result) {
		Vertex selectedSwitch = (Vertex)result.get(0).getColumn("switch");
		Vertex sensor = db.addVertex(null);
		sensor.setProperty("labels", ":Sensor");
		selectedSwitch.addEdge("sensor", sensor);
	}
	
	public void switchSetRepair(List<Row> result) {
		Vertex selectedSwitch = (Vertex)result.get(0).getColumn("switch");
		Vertex selectedSwitchPosition = (Vertex)result.get(0).getColumn("switchposition");
		String position = selectedSwitchPosition.getProperty("position");
		selectedSwitch.setProperty("currentPosition", position);
	}
	
	// user
	
	public void posLengthUser(Vertex segment) {
		segment.setProperty("length", 0);
	}
	
	public void routeSensorUser(Vertex route) {
		Iterable<Edge> definedBys = route.getEdges(Direction.OUT, "definedBy");
		for (Edge definedBy : definedBys) {
			definedBy.remove();
			break;
		}
	}
	
	public void semaphoreNeighborUser(Vertex route) {
		Iterable<Edge> entries = route.getEdges(Direction.OUT, "entry");
		for (Edge entry : entries) {
			entry.remove();
		}
	}
	
	public void switchSensorUser(Vertex sw) {
		Iterable<Vertex> sensors = sw.getVertices(Direction.OUT, "sensor");
		for (Vertex sensor : sensors) {
			sensor.remove();
		}
	}
	
	public void switchSetUser() {
		
	}
	
}
