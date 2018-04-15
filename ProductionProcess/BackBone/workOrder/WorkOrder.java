package workOrder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

import productionstep.ProductionRoot;
import productionstep.ProductionStepBase;
import utils.XStringBuilder;

/**
 * A WorkOrder Object describes a ProductionTree via a given {@link ProductionRoot}. 
 * It holds information about the shipment such as ordered amount and ShipmentDate.
 * @author Dave
 * @since 0.00
 * 
 */
public class WorkOrder implements Iterable<ProductionStepBase>
{
	
	public class ProductionIterator implements Iterator<ProductionStepBase>{
		boolean current;
		public ProductionIterator() {
			this.current = true;
		}
		
		@Override
		public boolean hasNext() {
			if(!this.current) {
				return TargetItem.iterator().hasNext();
			}
			return true;
		}

		@Override
		public ProductionStepBase next() {
			if(!this.current) {
				return TargetItem.iterator().next();
			}			
			this.current = false;
			return TargetItem;
		}
		
	}
	
	/**The Item {@link ProductionStepBase} ProductionRoot that is being build.*/
	private ProductionRoot TargetItem;
	/**How Many things were ordered*/
	private int anzahl;
	/**The Date when the Items should be shipped*/
	private LocalDateTime ShipmentDate;
	/**Earliest possible Starting Date*/
	private LocalDateTime StartDate;
	
	/**Empty Constructor*/
	public WorkOrder() {}
	
	/**
	 * Constructor
	 * @param TargetItem The Root of the ProductionTree.
	 * @param anzahl How Many Items were ordered.
	 * @param ShipmentDate The Date when the Items should be shipped
	 */
	public WorkOrder(ProductionRoot TargetItem, int anzahl, LocalDateTime ShipmentDate) {
		this.TargetItem = TargetItem;
		this.anzahl = anzahl;
		this.ShipmentDate = ShipmentDate;
	}
	
	/**
	 * Method used to set the ShipmentDate of this Object.
	 * @param ShipmentDate the {@link LocalDateTime} you want to set as ShipmentDate.
	 */
	public void setShipmentdate(LocalDateTime ShipmentDate) { this.ShipmentDate = ShipmentDate; }
	/**
	 * Method used to set the StartDate of this Object.
	 * @param StartDate the {@link LocalDateTime} you want to set as StartDate.
	 */
	public void setStartDate(LocalDateTime StartDate) { this.StartDate = StartDate; }
	/**
	 * Method used to set the {@link ProductionRoot} and its ordered amount.
	 * @param Item the Root you want to set.
	 * @param anzahl the amount ordered.
	 */
	public void setItemAndAmount(ProductionRoot Item, int anzahl) {
		this.TargetItem = Item;
		this.anzahl = anzahl;
	}
	
	/**
	 * Method used to access the ordered amount.
	 * @return the Anzahl Integer.
	 */
	public int getAnzahl() { return this.anzahl; }
	
	/**
	 * Method used to access the ShipmentDate
	 * @return the {@link LocalDateTime} you have set.
	 */
	public LocalDateTime getOrderDate() { return this.ShipmentDate; }
	
	public LocalDateTime getStartDate() { return this.StartDate; }
	
	
	/**
	 * Method used to access the TargetItem {@link ProductionRoot}.
	 * @return the Item that is being build.
	 */
	public ProductionRoot getTargetItem() { return this.TargetItem; } 
	/**
	 * Method used to calculate the Minutes from a given {@link LocalDateTime} until the ShipmentDate.
	 * @param ZeroDate the Date you want to use for calculation
	 * @return double containing the minutes
	 */
	public double getMinsToOrderDate(LocalDateTime ZeroDate) {
		return ZeroDate.until(this.ShipmentDate, ChronoUnit.MINUTES);
	}
	/**
	 * Method used to calculate the Minutes from a now until the ShipmentDate.
	 * @return double containing the minutes
	 */
	public double getMinsToOrderDate() { return this.getMinsToOrderDate(LocalDateTime.now()); }
		
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!(obj instanceof WorkOrder)) return false;
		
		WorkOrder other = (WorkOrder) obj;
		return this.anzahl == other.getAnzahl()
			&&  this.TargetItem.equals(other.getTargetItem())
			&&  this.ShipmentDate.equals(other.getOrderDate());
	}
	
	@Override
	public String toString() {
		XStringBuilder strb = new XStringBuilder("WorkOrder: ");
		strb.append(this.anzahl)
			.append(" mal ")
			.append(this.TargetItem.toString())
			.linesep()
			.append("ShipmentDate: ")
			.append(this.ShipmentDate);
		
		return strb.toString();
	}
	
	@Override
	public int hashCode() { 
		return 
			( this.TargetItem.hashCode() 
			+ this.anzahl 
			+ this.ShipmentDate.hashCode())/3; 
	}

	@Override
	public Iterator<ProductionStepBase> iterator() {
		this.TargetItem.initIterator();
		return new ProductionIterator();
	}
}
