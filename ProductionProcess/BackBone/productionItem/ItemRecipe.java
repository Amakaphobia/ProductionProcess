package productionItem;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boxes.Pair;
import machine.Machine;
import utils.XStringBuilder;

/**
 * Class used to Represents a recipe of a {@link CompositeItem}. Instanciate it with a Target CompositItem, setUpTime, processTime, and a array of pairs of items and integers.
 * On creation it uses the array to build a {@link HashMap}.
 * @author Dave
 * @since 0.00
 * 
 */
public class ItemRecipe
{
	/**The Target Item*/
	private final CompositeItem Target;
	/**Contains all input Materials(Key) and the needed Amount (Value)*/
	private final Map<Item,Integer> Input;
	/**Contains all Machines that can do this Recipe*/
	private final List<Machine> AvailableMachines;
	
	/**The setup time in minutes*/
	private final double setUpTime;
	/**The Processing time a single Unit takes in minutes*/
	private final double processTime;
	
	/**
	 * Constructor with an empty list of machines
	 * @param Target the Item that is to be created
	 * @param setUpTime the time it takes to set up the operation
	 * @param processTime the time a single operation will take
	 * @param input Array of Pairs of Items and the needed amount
	 */
	public ItemRecipe(CompositeItem Target, double setUpTime, double processTime, Pair<Item,Integer>[] input) {
		this.Target = Target;
		this.Target.setRec(this);
		this.setUpTime = setUpTime;
		this.processTime = processTime;

		this.Input = new HashMap<>();
		for(Pair<Item, Integer> e : input) {
			this.Input.put(	e.getKey(), 
						   	e.getValue());
		}
		
		this.AvailableMachines = new ArrayList<>();
	}
	
	/**
	 * Constructor with a List of Available {@link Machine}
	 * @param Target the Item that is to be created
	 * @param setUpTime the time it takes to set up the operation
	 * @param processTime the time a single operation will take
	 * @param input Array of Pairs of Items and the needed amount
	 * @param AvailableMachines Available Machines that can handle this Recipe
	 */
	public ItemRecipe(CompositeItem Target, double setUpTime, double processTime, Pair<Item,Integer>[] input, List<Machine> AvailableMachines) {
		this.Target = Target;
		this.Target.setRec(this);
		this.setUpTime = setUpTime;
		this.processTime = processTime;

		this.Input = new HashMap<>();
		for(Pair<Item, Integer> e : input) {
			this.Input.put(	e.getKey(), 
						   	e.getValue());
		}
		
		this.AvailableMachines = AvailableMachines;
	}
	
	/**
	 * Method used to add a {@link Machine} to the List of Available Machines.
	 * @param Machine the Machine you want to add.
	 */
	public void addMachine(Machine Machine) {
		this.AvailableMachines.add(Machine);
	}	
	/**
	 *  Method used to add a {@link List} of {@link Machine} to the List of Available Machines.
	 * @param Machines the MachineList you want to add.
	 */
	public void addMachine(List<Machine> Machines) {
		this.AvailableMachines.addAll(Machines);
	}	
	/**
	 * Method used to add multiple {@link Machine} to the List of Available Machines.
	 * @param Machines Machine the Machines you want to add.
	 */
	public void addMachine(Machine...Machines ) {
		for(Machine e : Machines) {
			this.AvailableMachines.add(e);
		}
	}
	/**
	 * Method used to access the {@link List} of {@link Machine}s that can build this Item
	 * @return The available Machines in a List.
	 */
	public List<Machine> getAvailableMachines() { return this.AvailableMachines; }
	
	/**
	 * Method used to get the TargetItem
	 * @return the target Item
	 */
	public CompositeItem getTarget() { return this.Target; }
	/**
	 * Method used to get the Set Up Time
	 * @return the SetUPTime in minutes (double)
	 */
	public double getSetUpTime() { return this.setUpTime; }
	/**
	 * Method used to get the Process Time
	 * @return the Process Time in Minutes
	 */
	public double getProcessTime() { return this.processTime; }
	/**
	 * Method used to get the Inputmap
	 * @return the Inputmap
	 */
	public Map<Item, Integer> getInput() {	return this.Input;	}
	
	@Override
	public String toString() {
		XStringBuilder strb = new XStringBuilder("Recipe: ");
		
		strb.append(
			this.Input.entrySet().stream()
				.map(e -> String.format("%s mal %s", e.getValue(), e.getKey()))
				.collect(joining(", ")));
		
		return strb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!(obj instanceof ItemRecipe)) return false;
		
		ItemRecipe other = (ItemRecipe) obj;
		
		boolean targetIdentifier = this.Target.getName().equals(other.Target.getName()); 
		boolean input = this.Input.equals(other.Input);
		boolean machines = this.AvailableMachines.equals(other.AvailableMachines);
		return targetIdentifier && input && machines;
	}
	
	@Override
	public int hashCode() {
		return (this.Target.hashCode() + 
				this.Input.entrySet().stream()
						.mapToInt(e -> e.getValue() + e.getKey().hashCode())
						.sum() / this.Input.size()				
				)/2;
	}
}
