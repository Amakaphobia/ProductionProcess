package productionstep;

import java.util.List;
import java.util.Optional;

import boxes.Pair;
import productionItem.CompositeItem;
import workOrder.WorkOrder;

/**
 * A ProductionBranch is an extension of the {@link ProductionStepBase} Class. It represents the Branches of the ProductionTree.
 * @author Dave
 * @since 0.00
 * 
 */
public class ProductionBranch extends ProductionStepBase
{
	/**this is the {@link ProductionStepBase} ParentStep of this Leaf*/
	private final ProductionStepBase NextStep;
	
	/**
	 * Constructor
	 * @param TargetItem The {@link CompositeItem} that is being build.
	 * @param NextStep The {@link ProductionStepBase} Parent of this Step.
	 */
	public ProductionBranch(CompositeItem TargetItem, ProductionStepBase NextStep) {
		super(TargetItem);
		this.NextStep = NextStep;
		this.NextStep.addChildren(this);
	}
	
	/**
	 * Constructor used to transform a {@link ProductionLeaf} to a {@code ProductionBranch} Object.
	 * @param Leaf the Leaf to be transformed.
	 */
	public ProductionBranch(ProductionLeaf Leaf) {
		super(Leaf.getTargetItem());
		this.NextStep = 
			Leaf.getParent()
				.orElseThrow(() -> new RuntimeException(
						String.format(
								"This Leaf: %s \n has no Parents :(", 
								this.toString())));
		
		this.NextStep.getChildren().remove(Leaf);
		this.NextStep.addChildren(this);
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
		double end = this.NextStep.SAZ();
		double start = 	end - this.getTotaltime();		
		this.Latest = new Pair<>(start, end);
		this.ChildSteps.forEach(e -> e.latest());
	}

	@Override
	public void buildPuffer() {
		double FP = this.NextStep.FAZ() - this.FEZ();
		double GP = this.SEZ() - this.FEZ();
		this.Puffer = new Pair<>(FP, GP);
		this.ChildSteps.forEach(e -> e.puffer());
	}
	
	@Override
	protected double getTotaltime() {
		return
			 this.TargetItem.getRec().getSetUpTime() +      							//Setuptime
			(this.TargetItem.getRec().getProcessTime() *								//ProcessTime
			 this.getWOMaster().getAnzahl() * 											//Anzahl EndProdukte 
			 this.NextStep.getTargetItem().getInputItems().get(this.TargetItem));		//Menge gebrauchten targetprodukte für 1 endprodukt
	}

	@Override
	public WorkOrder getWOMaster() {	return this.NextStep.getWOMaster(); }

	@Override
	public List<ProductionStepBase> getChildren() { return this.ChildSteps; }

	@Override
	public Optional<ProductionStepBase> getParent() { return Optional.of(this.NextStep); }

	@Override
	public void addChildren(ProductionStepBase Child) { this.ChildSteps.add(Child); }

	@Override
	public void addChildren(ProductionStepBase... Children) {
		for(ProductionStepBase e : Children) {
			this.ChildSteps.add(e);
		}
	}
}
