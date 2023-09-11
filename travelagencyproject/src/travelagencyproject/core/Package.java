package travelagencyproject.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Package {

	private String name;
	private int capacity;
	private List<Destination> destinations;
	private List<String> enrolled;
	
	Package(String name, int capacity) {
		setName(name);
		setCapacity(capacity);
		destinations = new ArrayList<>();
		enrolled = new ArrayList<>();
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected List<Destination> getDestinations() {
		return destinations;
	}

	protected void setDestinations(List<Destination> destinations) {
		this.destinations = destinations;
	}

	protected void addDestination(Destination destination) {
		destinations.add(destination);
	}
	
	protected int getCapacity() {
		return capacity;
	}

	protected void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	protected void addUser(String email) {
		if (!enrolled.contains(email)) {
			enrolled.add(email);
		}
	}
	
	protected boolean isFullCapacity() {
		return enrolled.size() == capacity;
	}
	
	protected int enrolledCount() {
		return enrolled.size();
	}
	
	@Override
	public String toString() {
		return "Package [name=" + name + ", destinations=" + 
				destinations.stream().map(Destination::getName).
				collect(Collectors.joining(",\n")) + ",capacity=" + 
				capacity + ", enrolled=" + enrolled.size() + "]";
	}
}
