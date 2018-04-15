package productionstep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import boxes.Pair;
import productionItem.CompositeItem;
import utils.XStringBuilder;
import workOrder.WorkOrder;

/**
 * BaseClass for ProductionSteps. 
 * @author Dave
 * @since 0.00
 * 
 */
public abstract class ProductionStepBase implements Iterable<ProductionStepBase>
{
	public class ProductionIterator implements Iterator<ProductionStepBase>
	{
		private int current = 0;
		
		public ProductionIterator() {
//			this.current = 0;
		}

		@Override
		public boolean hasNext() {
			return 
				ChildSteps.size() > 0 && this.current < ChildSteps.size() ?
						true :
						getParent()
							.<Boolean>map(parent -> 
								parent.iterator()
									  .hasNext())
							.orElse(false);			
		}

		@Override
		public ProductionStepBase next() {
			if(!this.hasNext()) throw new NoSuchElementException();		
			return 
				current < ChildSteps.size() ?
					ChildSteps.get(this.current++) :
					getParent()
						.get();
		}
	}	
	
	protected Iterator<ProductionStepBase> iterator;
	
	@Override
	public Iterator<ProductionStepBase> iterator() {
		return this.iterator;
	}
	
	public void initIterator() { 
		this.iterator = new ProductionIterator();
		this.ChildSteps.forEach(e -> e.initIterator());
	}
		
	/**The Item you want to Create with this step*/
	protected final CompositeItem TargetItem;
	/**A {@link List} of {@link ProductionStepBase} Children of this Branch of this ProductionTree.*/
	protected final List<ProductionStepBase> ChildSteps;
		
	/**Pair with FAZ, FEZ*/
	protected Pair<Double, Double> Earliest;
	/**Pair with SAZ, SEZ*/
	protected Pair<Double, Double> Latest;
	/**Pair with FP, GP*/
	protected Pair<Double, Double> Puffer;
	
	/**
	 * Constructor
	 * @param TargetItem the item you want to create
	 */
	public ProductionStepBase(CompositeItem TargetItem) {
		this.TargetItem = TargetItem;
		this.ChildSteps = new ArrayList<>();
	}
	
	/**
	 * Method used to access the Earliest Pair
	 * @return Pair with FAZ and FEZ
	 */
	public Pair<Double, Double> earliest(){
		if(this.Earliest == null) 
			this.buildEarliest();
		return this.Earliest;
	}
	/**
	 * Method used to access the latest Pair
	 * @return Pair with SAZ and SEZ
	 */
	public Pair<Double, Double> latest(){
		if(this.Latest == null) 
			this.buildLatest();
		return this.Latest;
	}
	/**
	 * Method used to access the puffer pair
	 * @return Pair with Fp and Gp
	 */
	public Pair<Double, Double> puffer(){
		if(this.Puffer == null) 
			this.buildPuffer();
		return this.Puffer;
	}
	
	/**
	 * Method used to get the WorkOrderMaster
	 * @return the WorkOrder associated with this project
	 */
	public abstract WorkOrder getWOMaster();
	/**Defines How FAZ and FEZ are build in the leaves branches and root*/
	public abstract void buildEarliest();
	/**Defines How SAZ and SEZ are build in the leaves branches and root*/
	public abstract void buildLatest();
	/**Defines How FP and GP are build in the leaves branches and root*/
	public abstract void buildPuffer();
	
	/**
	 * Calculates the total Time needed to perform the operation
	 * @return Double Containing the value
	 */
	protected abstract double getTotaltime();
	/**
	 * Method used to get to build Item
	 * @return the Build Item
	 */
	public CompositeItem getTargetItem() { return this.TargetItem; }
	
	/**
	 * Method used to add a Child ProductionStep to this step
	 * @param Child the step you want to add
	 */
	public abstract void addChildren(ProductionStepBase Child);
	/**
	 * Method used to add multiple Child ProductionSteps to this step
	 * @param Children the steps you want to add
	 */
	public abstract void addChildren(ProductionStepBase... Children);
	/**
	 * Method used to access a List containing all Child Steps
	 * @return a List of ProductionStepBase
	 */
	public abstract List<ProductionStepBase> getChildren();

	/**
	 * Method used to get a Optional ParentProductionStep. A Production Root will give an empty Optional
	 * @return a Optional(ProductionStepBase) with the Parent Step
	 */
	public abstract Optional<ProductionStepBase> getParent();
	
	/**
	 * Method used to get the earliest starting point
	 * @return the FAZ
	 */
	public double FAZ(){ return this.earliest().getKey(); }
	/**
	 * Method used to get the earliest ending point
	 * @return the FEZ
	 */
	public double FEZ(){ return this.earliest().getValue(); }
	/**
	 * Method used to get the latest starting point
	 * @return the SAZ
	 */
	public double SAZ(){ return this.latest().getKey(); }
	/**
	 * Method used to get the Latest Ending Point
	 * @return the SEZ
	 */
	public double SEZ(){ return this.latest().getValue(); }
	/**
	 * Method used to get the free Puffer
	 * @return the FP
	 */
	public double FP() { return this.puffer().getKey(); }
	/**
	 * Method used to get the Total Puffer
	 * @return the GP
	 */
	public double GP() { return this.puffer().getValue(); }
	
	@Override
	public String toString() {
		XStringBuilder strb = new XStringBuilder(this.getClass().getSimpleName());
		
		strb.linesep()
			.append(this.TargetItem)
			.append(
				this.getParent()
					.map(p -> 
						String.format(
							" w/ Parent: %s", 
							p.toString()))
					.orElse(""))
			.linesep()
			.append(String.format("Earliest: %s/%s", this.FAZ(), this.FEZ()))
			.linesep()
			.append(String.format("Latest: %s/%s", this.SAZ(), this.SEZ()))
			.linesep()
			.append(String.format("Puffer: %s/%s", this.FP(), this.GP()));
		
		return strb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!(obj instanceof ProductionStepBase)) return false;
		
		ProductionStepBase other = (ProductionStepBase) obj;
		return this.TargetItem.equals(other.getTargetItem())
			&&	this.getParent().equals(other.getParent())
			&&  this.getWOMaster().equals(other.getWOMaster());
	}
	
	@Override
	public int hashCode() {
		return 
			( this.TargetItem.hashCode() 
			+ this.getParent().hashCode() 
			+ this.Earliest.hashCode() 
			+ this.Latest.hashCode() 
			+ this.Puffer.hashCode())/5;
	}
}
