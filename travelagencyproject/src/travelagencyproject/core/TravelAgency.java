package travelagencyproject.core;

import java.util.HashMap;
import java.util.List;

import org.junit.platform.engine.discovery.PackageNameFilter;

import travelagencyproject.utils.GenericStringConstants;

/**
 * Assumptions,
 * 1. No date constraint of the travel
 * 2. Users can select destination, activity from multiple packages
 * 3. Passenger grades are blindly assigned based on selection, STANDARD is default
 * 4. Each time program run is one session - hence data is valid until program is run
 * 5. User's id is their email id
 * 6. Activity names are big hence everywhere the index is used for selection
 * 7. No cancellation after purchase
 * 8. No priority based on package grade
 */
public class TravelAgency {
	
	private static HashMap<String, Passenger> passengerIdMap;
	private static HashMap<String, Package> packages = new HashMap<>();
	private static HashMap<String, Destination> destinations = new HashMap<>();
	private static HashMap<String, Activity> activities = new HashMap<>();
	private static HashMap<String, PassengerGrade> passengerGrades = new HashMap<>();
	
	public enum PurchaseErrors {
		LOW_BALANCE("Low balance"),
		INVALID_ACTIVITY_INDEX("Invalid activity number"),
		ALREADY_REGISTERED("Activity already registered"),
		ACTIVITY_CAPACITY_FULL("Activity capacity is full");
		
	    private final String message;

		PurchaseErrors(final String message)
	    {
	        this.message = message;
	    }

	    @Override
	    public String toString()
	    {
	        return message;
	    }
	}

	// reading data from json files
	public TravelAgency() {
		passengerIdMap = new HashMap<>();
		AutowireDbToClass.autoWirePassengerGrades(passengerGrades);
		AutowireDbToClass.autoWireActivities(activities);
		AutowireDbToClass.autoWireDestinations(destinations, activities);
		AutowireDbToClass.autoWirePackages(packages, destinations);
	}
	
	public HashMap<String, PassengerGrade> getPassengerGrades() {
		return passengerGrades;
	}

	public boolean userExists(String email) {
		return passengerIdMap.getOrDefault(email, null) != null;
	}

	public void addUser(String email, String name) {
		passengerIdMap.put(email, new Passenger(email, name, 
				getPassengerGrades().get(GenericStringConstants.STANDARD)));	
	}

	public PassengerGrade getPassengerGrade(String email) {
		return passengerIdMap.get(email).getPassengerGrade();
	}
	
	public double getBalance(String email) {
		return passengerIdMap.get(email).getBalance();
	}

	// this will top up the current amount
	public void topUpUserBalance(String email, double amount) {
		Passenger passenger = passengerIdMap.get(email);
		passenger.setBalance(amount + passenger.getBalance());
	}

	public void setGradeOfUser(String email, PassengerGrade grade) {
		passengerIdMap.get(email).setPassengerGrade(grade);
	}
	
	public String getPassengerName(String email) {
		return passengerIdMap.get(email).getName();
	}

	public void printDestinations(String packageName) {
		for (Destination destination : packages.get(packageName).getDestinations()) {
			System.out.println(destination.toString());
			System.out.println();
		}
	}
	
	public void printPackages() {
		for (String eachPackage : packages.keySet()) {
			System.out.println(packages.get(eachPackage).toString());
			System.out.println();
		}
	}
	
	// printing all types of grades
	public void printPassengerGrades() {
		 for (String grade : passengerGrades.keySet()) {
			System.out.println( 
				passengerGrades.get(grade).getDescription());
		}
	}
	
	public void printActivitiesInThisDestination(String destinationName) {
		List<Activity> activitiesHere = destinations.
				get(destinationName).getActivities();
		for (int i = 0; i < activitiesHere.size(); i++) {
			System.out.println(i+1 + ".\n" + 
					activitiesHere.get(i).toString());
		}
	}
	
	// printing passenger details & available activities 
	public void printSummary() {
		for (Passenger passenger: passengerIdMap.values()) {
			System.out.println(passenger.toString());
			System.out.println();
		}
		System.out.println("Activities available");
		for (Activity activity: activities.values()) {
			if (!activity.isCapacityFull()) {				
				System.out.println(activity.toString());
				System.out.println();
			}
		}
	}

	public boolean validDestination(String packageName, String destinationName) {
		return packages.get(packageName) != null && 
				packages.get(packageName).getDestinations().
				contains(destinations.get(destinationName));
	}
	
	public boolean validActivityIndex(String packageName, String destinationName, 
			int activityIndex) {
		return activityIndex >= 0 && packages.get(packageName) != null && 
		packages.get(packageName).getDestinations().
		contains(destinations.get(destinationName)) && 
		activityIndex < destinations.get(destinationName).getActivities().size();
	}
	
	public boolean validPackageName(String packageName) {
		return packages.getOrDefault(packageName, null) != null;
	}	

	public boolean isActivityCapacityFull(String destinationName, int activityIndex) {
		return destinations.get(destinationName).getActivities().
				get(activityIndex).isCapacityFull();
	}

	public boolean activityAlreadyRegistered(String email, 
			String destinationName, int activityIndex) {
		return passengerIdMap.get(email).getActivities().
				contains(destinations.get(destinationName).getActivities().
						get(activityIndex));
	}

	// discount is applied if any
	private double toPay(String email, String destinationName, int activityIndex) {
		return destinations.get(destinationName).
				getActivities().get(activityIndex).
				calculateAmount(passengerIdMap.get(email));
	}

	public boolean affordableForThisPassenger(String email, String destinationName, int activityIndex) {
		return toPay(email, destinationName, activityIndex) <= 
				passengerIdMap.get(email).getBalance(); 
	}

	private void purchaseActivity(String email, String destinationName, int activityIndex, String packageName) {
		Passenger passenger = passengerIdMap.get(email);
		Activity selectedActivity = destinations.get(destinationName).
				getActivities().get(activityIndex);
		passenger.setBalance(passenger.getBalance()-toPay(email, destinationName, activityIndex));
		passenger.addActivity(selectedActivity);
		selectedActivity.addPassenger(email);
		packages.get(packageName).addUser(email);	
	}

	public boolean isPackageCapacityFull(String packageName) {
		return packages.get(packageName).isFullCapacity();
	}
	
	public int getCapacityInPackage(String PackageName) {
		return packages.get(PackageName).getCapacity();
	}
	
	public int getEnrolledCountInPackage(String PackageName) {
		return packages.get(PackageName).enrolledCount();
	}
	
	public int getEnrolledCountInActivity(String destinationName, int activityIndex) {
		return destinations.get(destinationName).
				getActivities().get(activityIndex).totalEnrolled();
	}

	public PurchaseErrors purchaseActivity(String email, String packageName, 
			String destinationName, int activityIndex) {
		if (!validActivityIndex(packageName, 
				destinationName, activityIndex)) {
			return PurchaseErrors.INVALID_ACTIVITY_INDEX;
		} if (activityAlreadyRegistered(email, 
				destinationName, activityIndex)) {
			// user has already bought
			return PurchaseErrors.ALREADY_REGISTERED;
		} else if (isActivityCapacityFull(
				destinationName, activityIndex)) {
			// capacity is full
			return PurchaseErrors.ACTIVITY_CAPACITY_FULL;
		} else if (!affordableForThisPassenger(
					email, destinationName, activityIndex)) {
			return PurchaseErrors.LOW_BALANCE;
		} else {
			//assuming payment is success
			purchaseActivity(email, 
					destinationName, 
					activityIndex, packageName);
			return null;
		}
	}	
}
