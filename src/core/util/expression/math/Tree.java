package core.util.expression.math;

public class Tree {
	public static final int number_node = 1;
	public static final int operation_node = 2;
	private String item;
	private Double value;
	private int kind;
	
	public Tree() {
		// TODO Auto-generated constructor stub
		value = new Double(0);
		item = "";
	}
	
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	private Tree left;
	private Tree right;
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Tree getLeft() {
		return left;
	}
	public void setLeft(Tree left) {
		this.left = left;
	}
	public Tree getRight() {
		return right;
	}
	public void setRight(Tree right) {
		this.right = right;
	}
}
