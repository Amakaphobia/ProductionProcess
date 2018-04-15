package productionItem;

import java.util.Map;

import utils.XStringBuilder;

/**
 * Class used to represent a item that is build.
 * @author Dave
 * @since 0.00
 * 
 */
public class CompositeItem extends Item 
{
	/**The Recipe for this item*/
	private ItemRecipe rec;

	/**
	 * Constructor
	 * @param name The ItemIdentifier
	 */
	public CompositeItem(String name) {
		super(name);
	}
	
	/**
	 * Method used to get the Recipe
	 * @return the ItemRecipe
	 */
	public ItemRecipe getRec() { return this.rec; }
	/**
	 * Method used to set the Recipe
	 * @param rec the Recipe you want to set
	 */
	public void setRec(ItemRecipe rec) { this.rec = rec; }
	/**
	 * Method used to get a Map containing entries of (Needed Item, Needed Amount)
	 * @return the Map(Item, Integer)
	 */
	public Map<Item, Integer> getInputItems() { return this.rec.getInput(); }
	
	@Override
	public String toString() {
		XStringBuilder strb = new XStringBuilder(super.toString());
		
		strb.linesep()
			.append(" with ")
			.append(this.rec.toString());		
		
		return strb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!(obj instanceof CompositeItem)) return false;
		
		CompositeItem other = (CompositeItem) obj;
		return this.name.equals(other.getName())
			&&  this.rec.equals(other.getRec());
	}
	
	@Override
	public int hashCode() {
		return 
		   (this.name.hashCode() 
		   +this.rec.getInput().size())/2;
	}
}
