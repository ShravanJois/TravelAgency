/**
 * 
 */
package travelagencyproject.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shravan
 *
 */
class Activity {

	private String name;
    private String description;
    private double cost;
    private int capacity;
    private List<String> enrolled;

	Activity(String name, String description, double cost, int capacity) {
        this.setName(name);
        this.setDescription(description);
        this.setCost(cost);
        this.setCapacity(capacity);
        enrolled = new ArrayList();
    }

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	protected double getCost() {
		return cost;
	}

	protected void setCost(double cost) {
		this.cost = cost;
	}

	protected void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	protected List<String> getEnrolled() {
		return enrolled;
	}

	protected void addPassenger(String email) {
		enrolled.add(email);
	}

	protected boolean isCapacityFull() {
		return capacity == enrolled.size();
	}
	
	protected int totalEnrolled() {
		return enrolled.size();
	}
	
	// amount after discounts based on grade
	protected double calculateAmount(Passenger passenger) {
		return getCost() - 
				((passenger.getPassengerGrade().getDiscount() / 100.0) * getCost());
	}
	
	@Override
	public String toString() {
		return "Activity [name=" + name + ", description=" + 
				description + ", cost=" + cost + ", capacity=" + capacity +
				", empty seats=" + (capacity - enrolled.size())
				+ "]";
	}

}
