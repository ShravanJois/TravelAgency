package travelagencyproject.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Passenger {

	private String name;
    private String passengerId;
    private double balance;
    private PassengerGrade passengerGrade;
    private List<Activity> activities;
    
    Passenger(String passengerId, String name, PassengerGrade grade) {
    	setPassengerId(passengerId);
    	setName(name);
    	setBalance(0.0);
    	setPassengerGrade(grade);
    	activities = new ArrayList();
    }

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getPassengerId() {
		return passengerId;
	}

	protected void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	protected double getBalance() {
		return balance;
	}

	protected void setBalance(double balance) {
		this.balance = balance;
	}

	protected PassengerGrade getPassengerGrade() {
		return passengerGrade;
	}

	protected void setPassengerGrade(PassengerGrade passengerGrade) {
		this.passengerGrade = passengerGrade;
	}

	protected List<Activity> getActivities() {
		return activities;
	}

	protected void addActivity(Activity activity) {
		activities.add(activity);
	}

	@Override
	public String toString() {
		return "Passenger [name=" + name + ",\n passengerId=" + passengerId + 
				",\n balance=" + balance + ",\n passengerGrade="
				+ passengerGrade.getName() + ",\n activities=" + 
				activities.stream().map(activity -> activity.getName() + 
						" for rs " + activity.calculateAmount(this))
                .collect(Collectors.joining(",\n")) + "]";
	}

}
