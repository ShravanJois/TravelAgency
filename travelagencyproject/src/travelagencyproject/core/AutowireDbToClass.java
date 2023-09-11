package travelagencyproject.core;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import travelagencyproject.utils.GenericStringConstants;
import travelagencyproject.utils.ReadFileutils;

class AutowireDbToClass {

	private static final String PACKAGES 
	= "packages.json";
	private static final String PASSENGER_GRADES_DISCOUNT 
	= "passenger_grades_discount.json";
	private static final String DESTINATIONS 
	= "destinations.json";
	private static final String ACTIVITIES 
	= "activities.json";

	AutowireDbToClass() {
	}
	
	static void autoWirePassengerGrades(
			HashMap<String, PassengerGrade> passengerGrades) {
		JSONObject passengerGradesDiscounts = ReadFileutils.
				readJsonResource(PASSENGER_GRADES_DISCOUNT);
		
		Iterator<String> keys = passengerGradesDiscounts.keys();

		while(keys.hasNext()) {
		    String key = keys.next();
		    try {
				passengerGrades.put(key,
						new PassengerGrade(key, 
								passengerGradesDiscounts.
									getJSONObject(key).
									getDouble(GenericStringConstants.DISCOUNT)));
			} catch (JSONException e) {
				System.out.println("error " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	static void autoWireActivities(
			HashMap<String, Activity> activities) {
		JSONObject activitiesJson = ReadFileutils.
				readJsonResource(ACTIVITIES);
		
		Iterator<String> keys = activitiesJson.keys();

		while(keys.hasNext()) {
		    String key = keys.next();
		    try {
		    	JSONObject activity = activitiesJson.getJSONObject(key);
		    	activities.put(key,
						new Activity(key, 
								activity.getString(GenericStringConstants.DESCRIPTION),
								activity.getDouble(GenericStringConstants.COST),
								activity.getInt(GenericStringConstants.CAPACITY)));
			} catch (JSONException e) {
				System.out.println("error " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	static void autoWireDestinations(
			HashMap<String, Destination> destinations,
			HashMap<String, Activity> activities) {
		JSONObject destinationsJson = ReadFileutils.
				readJsonResource(DESTINATIONS);
		
		Iterator<String> keys = destinationsJson.keys();

		while(keys.hasNext()) {
		    String key = keys.next();
		    try {
		    	JSONObject destination = destinationsJson.getJSONObject(key);
		    	destinations.put(key, new Destination(key));
		    	for (int i=0; i < 
		    			destination.getJSONArray(GenericStringConstants.ACTIVITIES).length(); 
		    			i++) {
		    		destinations.get(key).addActivity(
		    				activities.get(destination.
		    						getJSONArray(GenericStringConstants.ACTIVITIES).getString(i)));
		    	}
			} catch (JSONException e) {
				System.out.println("error " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void autoWirePackages(HashMap<String, Package> packages, 
			HashMap<String, Destination> destinations) {
		JSONObject packagesJson = ReadFileutils.
				readJsonResource(PACKAGES);
		
		Iterator<String> keys = packagesJson.keys();

		while(keys.hasNext()) {
		    String key = keys.next();
		    try {
		    	JSONObject eachPackage = packagesJson.getJSONObject(key);
		    	packages.put(key, new Package(key, eachPackage.
		    			getInt(GenericStringConstants.CAPACITY)));
		    	for (int i=0; i < 
		    			eachPackage.getJSONArray(GenericStringConstants.DESTINATIONS).length(); 
		    			i++) {
		    		packages.get(key).addDestination(
		    				destinations.get(eachPackage.
		    						getJSONArray(GenericStringConstants.DESTINATIONS).getString(i)));
		    	}
			} catch (JSONException e) {
				System.out.println("error " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
