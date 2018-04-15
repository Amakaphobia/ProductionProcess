package productionItem;

/**
 * CLass used to Represent a Item that is not build.
 * @author Dave
 * @since 0.00
 * 
 */
public class Item 
{
	/**The Item Identifier*/
	protected final String name;
	
	/**
	 * Constructor
	 * @param name The Item Identifier
	 */
	public Item(String name) {
		this.name = name;
	}
	
	/**
	 * Method used to get the Item Identifier
	 * @return String w/ the Item Identifier
	 */
	public String getName() { return this.name; }
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!(obj instanceof Item)) return false;
		
		Item other = (Item) obj;
		return this.name.equals(other.getName());
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("Item: %s", this.name);
	}
}
