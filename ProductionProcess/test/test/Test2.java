package test;
import boxes.Pair;
import productionItem.CompositeItem;
import productionItem.Item;
import productionItem.ItemRecipe;
import productionstep.ProductionLeaf;
import productionstep.ProductionRoot;
import productionstep.ProductionStepBase;
import workOrder.WorkOrder;

@SuppressWarnings({ "unused", "unchecked", "javadoc"})
public class Test2 {

	public static void main(String[] args) {
		Item a = new Item("a");
		Item b = new Item("b");

		CompositeItem c1 = new CompositeItem("c1");
		ItemRecipe r1 = new ItemRecipe(c1, 2, 5, new Pair[] {new Pair<>(a, 5)});
		
		CompositeItem c2 = new CompositeItem("c2");
		ItemRecipe r2 = new ItemRecipe(c2, 2, 5, new Pair[] {new Pair<>(b, 5)});
		
		CompositeItem cEnd = new CompositeItem("cEnd");
		ItemRecipe rEnd = new ItemRecipe(cEnd, 1, 2, new Pair[] {new Pair<>(c1,1), new Pair<>(c2,1)});
		
		WorkOrder wom = new WorkOrder();
		ProductionRoot p1 = new ProductionRoot(cEnd, wom);
		
		ProductionLeaf l1 = new ProductionLeaf(c2, p1);
		ProductionLeaf l2 = new ProductionLeaf(c1, p1);
		
		wom.setItemAndAmount(p1, 1);
		
		for(ProductionStepBase e : wom) {
			System.out.println(e.getTargetItem());
		}
	}

}
