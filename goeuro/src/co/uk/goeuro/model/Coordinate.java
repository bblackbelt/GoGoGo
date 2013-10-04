package co.uk.goeuro.model;

/**
 * 
 * Holds the representation of the Coordinate object
 * 
 * @ThreadSafe yes
 * @author emanuele
 * 
 */

public class Coordinate {

	public final String latitude;
	public final String longitude;

	public Coordinate(String x, String y) {
		this.latitude = x;
		this.longitude = y;
	}

	@Override
	public String toString() {
		String toReturn = "{ x = " + latitude + " y = " + longitude +  "} ";
		return toReturn;
	}
}
