package productionstep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import boxes.Pair;
import productionItem.CompositeItem;
import workOrder.WorkOrder;

/**
 * A ProductionLeaf is an extension of the {@link ProductionStepBase} Class. It represents the Leaves of the ProductionTree.
 * @author Dave
 * @since 0.00
 * 
 */
public class ProductionLeaf extends ProductionStepBase
{
	/**this is the ParentStep of this Leaf*/
	private final ProductionStepBase NextStep;
	
	/**
	 * Constructor
	 * @param TargetItem The {@link CompositeItem} that is being build.
	 * @param NextStep The {@link ProductionStepBase} Parent that holds this Leaf.
	 */
	public ProductionLeaf(CompositeItem TargetItem, ProductionStepBase NextStep) {
		super(TargetItem);
		this.NextStep = NextStep;
		this.NextStep.addChildren(this);
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
	public void addChildren(ProductionStepBase... Children) {
		ProductionBranch neuer = new ProductionBranch(this);
		neuer.addChildren(Children);
	}
	
	@Override
	public void addChildren(ProductionStepBase Child) {
		ProductionBranch neuer = new ProductionBranch(this);
		neuer.addChildren(Child);
	}
	
	@Override
	public WorkOrder getWOMaster() { return this.NextStep.getWOMaster(); }

	@Override
	public List<ProductionStepBase> getChildren() { return new ArrayList<ProductionStepBase>(); }

	@Override
	public Optional<ProductionStepBase> getParent() { return Optional.of(this.NextStep); }

	@Override
	public void buildEarliest() {
		double start = 0;
		double end = this.getTotaltime();
		this.Earliest = new Pair<>(start, end);		
	}

	@Override
	public void buildLatest() {
		double end = this.NextStep.SAZ();
		double start = 	end - this.getTotaltime();
		this.Latest = new Pair<>(start, end);		
	}

	@Override
	public void buildPuffer() {
		double FP = this.NextStep.FAZ() - this.FEZ();
		double GP = this.SEZ() - this.FEZ();
		this.Puffer = new Pair<>(FP, GP);		
	}
}
