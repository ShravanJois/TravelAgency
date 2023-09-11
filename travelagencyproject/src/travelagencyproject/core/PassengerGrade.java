package travelagencyproject.core;

/**
 * @author Shravan
 * many grades & discount levels can be added
 */
public class PassengerGrade {
	
	private String name;
	private double discount;

	PassengerGrade(String name, double discount) {
		setName(name);
		setDiscount(discount);
	}

	protected double getDiscount() {
		return discount;
	}

	protected void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getDescription() {
		return name + " grade offers " +  discount + "% discount on each activity";
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}
}
