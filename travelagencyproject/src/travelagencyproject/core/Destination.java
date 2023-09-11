/**
 * 
 */
package travelagencyproject.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shravan
 *
 */
class Destination {
	
	private String name;
    private List<Activity> activities;

	Destination() {
		activities = new ArrayList<>();
	}
	
	protected Destination(String name) {
		this();
		this.name = name;
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected List<Activity> getActivities() {
		return activities;
	}

	protected void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	protected void addActivity(Activity activity) {
		this.getActivities().add(activity);
	}

	@Override
	public String toString() {
		return "Destination [name=" + name + ",\n "
				+ "activities=\n" + 
				activities.stream().map(Object::toString)
                .collect(Collectors.joining(",\n")) + "]";
	}

}
