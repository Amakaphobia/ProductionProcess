package machine;

import java.util.ArrayList;
import java.util.List;

import time.TimedDuration;

/**
 * Machine represents a single Machine for Production use. It contains a List of {@link TimedDuration} that represents when it is in use.
 * @author Dave
 * @since 0.00
 * 
 */
public class Machine 
{
	/**Machine Identifier*/
	private final String name;
	/**List of TimedDurations describing when it is in use*/
	private List<TimedDuration> inUse;
	
	/**
	 * Constructor
	 * @param name The MachinesIdentifier
	 */
	public Machine(String name) {
		this.name = name;
		this.inUse = new ArrayList<>();
	}
	
	/**
	 * Method used to try and book a {@link TimedDuration} to this Machine. if the TimedDuration Overlapps with a already booked one it will not book that timed duration. 
	 * @param Time when you will try to book one.
	 * @return true if booking occurred.
	 */
	public boolean bookUsage(TimedDuration Time) {
		boolean isFree = 
			!inUse.stream()
				.filter(Time::overlaps)
				.findFirst()
				.isPresent();
		
		if(isFree) {
			this.inUse.add(Time);
		}
		return isFree;
	}
	
	/**
	 * Method used to get the Identifier of this Machine
	 * @return String with the Identifier.
	 */
	public String getName() { return this.name; }
	/**
	 * Method used to access the {@link List} of {@link TimedDuration}s during which this Machine is used.
	 * @return This list of TimedDurations.
	 */
	public List<TimedDuration> getInUse() { return this.inUse; }
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!(obj instanceof Machine)) return false;
		
		Machine other = (Machine) obj;
		return this.name.equals(other.name)
			&&  this.inUse.equals(other.inUse);
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("Machine: %s", this.name);
	}
}
