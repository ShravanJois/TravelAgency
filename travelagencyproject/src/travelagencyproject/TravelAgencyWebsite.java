/**
 * 
 */
package travelagencyproject;

import java.util.HashMap;
import java.util.Scanner;

import travelagencyproject.core.PassengerGrade;
import travelagencyproject.core.TravelAgency;
import travelagencyproject.core.TravelAgency.PurchaseErrors;

/**
 * @author Shravan
 *
 */
public class TravelAgencyWebsite {
	
	private static Scanner scanner = new Scanner(System.in);
	
	TravelAgencyWebsite() {
	}

	private String signInPage(TravelAgency agency) {
		System.out.println("Enter email id");
		String email = scanner.next();
//		assuming password sign in
		if (!agency.userExists(email)) {
			System.out.println("Enter name");
			agency.addUser(email, scanner.next());
		}
		return email;
	}

	private void topUp(TravelAgency agency, String email) {
		System.out.println("Do you want to recharge? y / n");
		if ("y".equals(scanner.next())) {
			System.out.println("Enter amount");
			// assuming financial transaction
			// amount entered will be added to current balance
			agency.topUpUserBalance(email, scanner.nextDouble());
		}
	}

	// grade criteria is blind here
	// because no specifications when to assign
	private void selectGrade(TravelAgency agency, String email) {
		System.out.println("Select grade");
		HashMap<String, PassengerGrade> passengerGrades = 
				agency.getPassengerGrades();
		agency.printPassengerGrades();
		PassengerGrade grade = passengerGrades.
				getOrDefault(scanner.next(), null);
		// STANDARD is default in case of invalid input
		if (grade != null) {
			agency.setGradeOfUser(email, grade);
		}
	}

	/**
	 * Selecting package, destination & account for current passenger,
	 * user can select multiple package, destination & activity
	 */
	private void selectPackage(TravelAgency agency, String email) {
		do {
			agency.printPackages();
			System.out.println("Enter package name");
			String packageName = scanner.next();
			if (agency.validPackageName(packageName)) {
				if (agency.isPackageCapacityFull(packageName)) {
					System.out.println("Package capacity is full");
				} else {
					do {
						agency.printDestinations(packageName);
						System.out.println("Enter package destination name");
						String destinationName = scanner.next();

						if (agency.validDestination(packageName, destinationName)) {
							do {
								agency.printActivitiesInThisDestination(destinationName);
								System.out.println("Select the activity number");
								
								// need to enter number from list because 
								// activity names will be big
								int activityIndex = scanner.nextInt()-1;
								
								PurchaseErrors transactionError = agency.purchaseActivity(email,
										packageName, destinationName, activityIndex);
								if (transactionError == null) {
									System.out.println("Purchase succss");
								} else if (transactionError == PurchaseErrors.LOW_BALANCE) {
									System.out.println("Low balance : " +
											agency.getBalance(email)
									+ ", top up? y/n");
									if ("y".equals(scanner.next())) {
										topUp(agency, email);
									}
								} else {
									System.out.println(transactionError.toString());
								}
								System.out.println("continue activity? y/n");
							} while ("y".equals(scanner.next()));
						} else {
							System.out.println("Select valid destination name");
						}
						System.out.println("continue destination? y/n");
					} while ("y".equals(scanner.next()));
				}
			} else {
				System.out.println("Select valid package name");
			}
			System.out.println("continue packages? y/n");
		} while ("y".equals(scanner.next()));
	}

	/**
	 * Enter options when prompted,
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
	public static void main(String[] args) {
		TravelAgencyWebsite web = new TravelAgencyWebsite();
		TravelAgency agency = new TravelAgency();
		do {			
			String userId = web.signInPage(agency);
			web.topUp(agency, userId);
			web.selectGrade(agency, userId);
			web.selectPackage(agency, userId);
			
			agency.printSummary();

			System.out.println("continue? y/n");
		} while ("y".equals(scanner.next()));
		
		System.out.println("Thank you!");
	}

}
