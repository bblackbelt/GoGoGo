package co.uk.goeuro.model;


/**
 * 
 * Holds the representation of the Station object
 * 
 * @ThreadSafe yes
 * @author emanuele
 * 
 */

public class Station {

	public final String id;
	public final String name;
	public final String _type;
	public final String type;
	public final Coordinate coordinate;

	public Station(String id, String name, String _type, String type, Coordinate coordinate) {
		this.id = id;
		this.name = name;
		this._type = _type;
		this.coordinate = coordinate;
		this.type = type;
	}
	
	@Override
	public String toString() {

	///	String toReturn = 
		//	"[ id = " + id + " name = " + name + " coordinate = " + coordinate + "]";
			
		return name;
	
	}

}
