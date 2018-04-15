package workOrder;

import java.time.LocalDateTime;
import java.util.function.Function;

import productionstep.ProductionRoot;

public class WorkOrderFactory 
{
	private WorkOrder toBuild;
	@SuppressWarnings("unused")
	private boolean hasStartTime;
	@SuppressWarnings("unused")
	private boolean hasEndTime;
	
	
	/**block constructor from outside access*/
	private WorkOrderFactory() {
		this.toBuild = new WorkOrder();
		this.hasStartTime = false;
		this.hasEndTime = false;
	}
	
	public WorkOrderFactory setItemAndAmount(ProductionRoot Item, int anzahl){
		this.toBuild.setItemAndAmount(Item, anzahl);
		return this;
	}
	
	
	public WorkOrderFactory setStartDate(LocalDateTime StartDate) {
		this.hasStartTime = true;
		this.toBuild.setStartDate(StartDate);
		return this;
	}
	
	public WorkOrderFactory setEndDate(LocalDateTime EndDate) {
		this.hasEndTime = true;
		this.toBuild.setShipmentdate(EndDate);
		return this;
	}
	
	private WorkOrder getWO() { return this.toBuild; }
	private static WorkOrder getToBuild(WorkOrderFactory in) { return in.getWO(); }
	public static WorkOrder build(Function<WorkOrderFactory, WorkOrderFactory> recipe) {
		return 
			recipe.andThen(WorkOrderFactory::getToBuild)
					.apply(new WorkOrderFactory());
	}
}
