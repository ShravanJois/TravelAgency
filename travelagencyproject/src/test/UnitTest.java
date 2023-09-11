package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import travelagencyproject.core.PassengerGrade;
import travelagencyproject.core.TravelAgency;
import travelagencyproject.core.TravelAgency.PurchaseErrors;
import travelagencyproject.utils.GenericStringConstants;

public class UnitTest {
	
	TravelAgency agency = new TravelAgency();

	public UnitTest() {
		// TODO Auto-generated constructor stub
	}

	@Test
	// testing user name, balance & topup
	public void testUserDetails() {
		String email = "abc1@gmail.com";
		agency.addUser(email, "ABC");
		PassengerGrade grade = agency.getPassengerGrades().get(
				GenericStringConstants.STANDARD);
		agency.setGradeOfUser(email, grade);
		assertEquals(agency.getBalance(email), 0.0);
		assertEquals(agency.getPassengerGrade(email), grade);
		agency.topUpUserBalance(email, 100.0);
		assertEquals(agency.getBalance(email), 100.0);
		assertEquals(agency.getPassengerName(email), "ABC");
		assertEquals(agency.userExists(email), true);
		assertEquals(agency.userExists("false"), false);
	}
	
	@Test
	public void testPackagesNames() {
		assertEquals(agency.validPackageName("Gujarat"), true);
		assertEquals(agency.validPackageName("Gujrath"), false);
	}
	
	@Test
	public void testDesinationNames() {
		assertEquals(agency.validDestination("Gujarat", "Ahmedabad"), true);
		assertEquals(agency.validDestination("Tamil_Nadu", "Ooty"), true);
		assertEquals(agency.validDestination("Guj", "Ahmedabad"), false);
		assertEquals(agency.validDestination("Gujarat", "Ooty"), false);
		assertEquals(agency.validDestination("Gujarat", "ahmedabad"), false);
	}
	
	@Test
	public void testActivitySelection() {
		assertEquals(agency.validActivityIndex("Gujarat", "Ahmedabad", 1), true);
		assertEquals(agency.validActivityIndex("Gujarat", "Ahmedabad", 0), true);
		assertEquals(agency.validActivityIndex("Gujarat", "ahmedabad", 1), false);
		assertEquals(agency.validActivityIndex("Gujarat", "Ahmedabad", 5), false);
		assertEquals(agency.validActivityIndex("gujarat", "Ahmedabad", 1), false);
	}
	
	@Test
	public void testActivityPurchaseForStandardUser() {
		String email = "abc1@gmail.com";
		agency.addUser(email, "ABC");
		PassengerGrade grade = agency.getPassengerGrades().get(
				GenericStringConstants.STANDARD);
		agency.setGradeOfUser(email, grade);
		agency.topUpUserBalance(email, 100.0);
		assertEquals(agency.affordableForThisPassenger(email, "Bangalore", 0), false);
		agency.topUpUserBalance(email, 400.0);
		assertEquals(agency.purchaseActivity(email, "Karnataka", "Bangalore", 0), null);
		assertEquals(agency.getBalance(email), 0.0);
	}
	
	@Test
	public void testActivityPurchaseFailures() {
		String email = "abc1@gmail.com";
		agency.addUser(email, "ABC");
		PassengerGrade grade = agency.getPassengerGrades().get(
				GenericStringConstants.STANDARD);
		agency.setGradeOfUser(email, grade);
		assertEquals(agency.purchaseActivity(email, "Karnataka", "Bangalore", 0), 
				PurchaseErrors.LOW_BALANCE);
		
		agency.topUpUserBalance(email, 100.0);
		assertEquals(agency.purchaseActivity(email, "Karnataka", "Bangalore", 5), 
				PurchaseErrors.INVALID_ACTIVITY_INDEX);
		
		agency.topUpUserBalance(email, 1000.0);
		assertEquals(agency.purchaseActivity(email, "Karnataka", "Bangalore", 0), 
				null);
		
		assertEquals(agency.getEnrolledCountInPackage("Karnataka"), 1);

		assertEquals(agency.purchaseActivity(email, "Karnataka", "Bangalore", 0), 
				PurchaseErrors.ALREADY_REGISTERED);
		
		email = "abc2@gmail.com";
		agency.addUser(email, "ABC");
		assertEquals(agency.purchaseActivity(email, "Karnataka", "Bangalore", 0), 
				PurchaseErrors.ACTIVITY_CAPACITY_FULL);
		
		email = "abc3@gmail.com";
		agency.addUser(email, "ABC");
		assertEquals(agency.isPackageCapacityFull("Karnataka"), false);
		
		agency.topUpUserBalance(email, 1000.0);
		assertEquals(agency.purchaseActivity(email, "Karnataka", "Bangalore", 1), 
				null);
		assertEquals(agency.isPackageCapacityFull("Karnataka"), true);
		
		assertEquals(agency.getCapacityInPackage("Karnataka"), 2);
		
		assertEquals(agency.getEnrolledCountInActivity("Bangalore", 0), 1);
		assertEquals(agency.getEnrolledCountInActivity("Bangalore", 1), 1);
	}
	
	@Test
	public void testActivityPurchaseForPremiumUser() {
		String email = "abc1@gmail.com";
		agency.addUser(email, "ABC");
		PassengerGrade grade = agency.getPassengerGrades().get(
				GenericStringConstants.PREMIUM);
		agency.setGradeOfUser(email, grade);
		assertEquals(agency.purchaseActivity(email, "Tamil_Nadu", "Chennai", 0), 
				null);
		assertEquals(agency.purchaseActivity(email, "Tamil_Nadu", "Chennai", 1), 
				null);
		assertEquals(agency.purchaseActivity(email, "Tamil_Nadu", "Ooty", 0), 
				null);
		assertEquals(agency.purchaseActivity(email, "Tamil_Nadu", "Ooty", 1), 
				null);
	}
	
	@Test
	public void testActivityPurchaseForGoldUser() {
		String email = "abc1@gmail.com";
		agency.addUser(email, "ABC");
		PassengerGrade grade = agency.getPassengerGrades().get(
				GenericStringConstants.GOLD);
		agency.setGradeOfUser(email, grade);
		agency.topUpUserBalance(email, 100.0);
		assertEquals(agency.purchaseActivity(email, "Tamil_Nadu", "Chennai", 0), 
				null);
		assertEquals(agency.getBalance(email), 10.0);
		agency.topUpUserBalance(email, 220.0);
		assertEquals(agency.purchaseActivity(email, "Tamil_Nadu", "Chennai", 1), 
				null);
		assertEquals(agency.getBalance(email), 5.0);
	}
}
