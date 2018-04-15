package productionstep;

import java.util.List;
import java.util.Optional;

import boxes.Pair;
import productionItem.CompositeItem;
import workOrder.WorkOrder;

/**
 * A ProductionRoot is an extension of the {@link ProductionStepBase} Class. It represents the Root of the ProductionTree.
 * @author Dave
 * @since 0.00
 * 
 */
public class ProductionRoot extends ProductionStepBase 
{
	/**The WorkOrder for this ProductionTree.*/
	private final WorkOrder WOMaster;
	
	/**
	 * Constructor
	 * @param TargetItem The {@link CompositeItem} that is being build.
	 * @param WOMaster The WorkOrder for this ProductionTree.
	 */
	public ProductionRoot(CompositeItem TargetItem, WorkOrder WOMaster) {
		super(TargetItem);
		this.WOMaster = WOMaster;
	}

	@Override
	protected double getTotaltime() {
		return
			 this.TargetItem.getRec().getSetUpTime() +      		//Setuptime
			(this.TargetItem.getRec().getProcessTime() *			//ProcessTime
			 this.getWOMaster().getAnzahl()); 						//Anzahl EndProdukte 
	}
	
	@Override
	public void buildEarliest() {
		double start = 
			this.ChildSteps.stream()
				.mapToDouble(e -> e.FEZ())
				.max()
				.orElseThrow(() -> new RuntimeException("Something Went Wrong"));
		double end = start + this.getTotaltime();
		this.Earliest = new Pair<>(start, end);	
	}

	@Override
	public void buildLatest() {
		double end = this.getWOMaster().getMinsToOrderDate();
		double start = end - this.getTotaltime();
		this.Latest = new Pair<>(start, end);
		this.ChildSteps.forEach(e -> e.buildLatest());		
	}

	@Override
	public void buildPuffer() {
		double FP = 0;
		double GP = this.SEZ() - this.FEZ();
		this.Puffer = new Pair<>(FP, GP);
		this.ChildSteps.forEach(e -> e.puffer());
	}

	@Override
	public void addChildren(ProductionStepBase... Children) {
		for(ProductionStepBase e : Children) {
			this.ChildSteps.add(e);
		}
	}

	@Override
	public void addChildren(ProductionStepBase Child) { this.ChildSteps.add(Child); }

	@Override
	public Optional<ProductionStepBase> getParent() { return Optional.empty(); }

	@Override
	public WorkOrder getWOMaster() { return this.WOMaster; }

	@Override
	public List<ProductionStepBase> getChildren() { return this.ChildSteps; }

	
}
