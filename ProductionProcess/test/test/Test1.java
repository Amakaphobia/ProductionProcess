package test;
import boxes.Pair;
import productionItem.CompositeItem;
import productionItem.Item;
import productionItem.ItemRecipe;

public class Test1 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Item a = new Item("a");
		Item b = new Item("b");
		CompositeItem i = new CompositeItem("i");
		ItemRecipe r1 = new ItemRecipe(i, 1, 2,new Pair[] {new Pair<Item, Integer>(a,2), new Pair<Item, Integer>(b,1)});
		
		Item a2 = new Item("a");
		Item b2 = new Item("b");
		CompositeItem i2 = new CompositeItem("i");
		ItemRecipe r2 = new ItemRecipe(i2, 1, 2,new Pair[] {new Pair<Item, Integer>(a2,2), new Pair<Item, Integer>(b2,1)});
		
		System.out.println(a.equals(a2));
		System.out.println(a.equals(b));
		System.out.println(i.equals(i2));
		System.out.println(i.equals(a2));
		
		System.out.println(r1.equals(r2));

	}

}
