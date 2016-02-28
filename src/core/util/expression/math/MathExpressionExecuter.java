package core.util.expression.math;

import java.util.*;
public class MathExpressionExecuter {
	private String expr;
	private MathExpressionParser mep;	
	public MathExpressionExecuter() {
		// TODO Auto-generated constructor stub
		mep = new MathExpressionParser();
	}
	public MathExpressionExecuter(MathExpressionParser mep){
		this.mep = mep;
	}
	
	public MathExpressionParser getMep() {
		return mep;
	}
	public void setMep(MathExpressionParser mep) {
		this.mep = mep;
	}
	
	public Map<String, Double> execute(){
		mep.build();
		for(int i=0; i<= mep.getExpressionTrees().size(); i++){
			Tree t = mep.getExpressionTrees().get(i);
			t = execute(t);
			//correct values;
			correctValues(t);
			return mep.getRealValues();
		}
		return null;
	}
	
	private Tree correctValues(Tree t){
		if(t != null){
			Double v = mep.getRealValues().get(t.getItem());
			if(t.getValue() != null && t.getItem().startsWith("c")){
				mep.getRealValues().put(t.getItem(), t.getValue());
			}
			correctValues(t.getLeft());
			correctValues(t.getRight());
		}
		return t;
	}
	private Tree execute(Tree t){
		if(t == null)
			return t;
		else if(t.getLeft() == null && t.getRight() == null)
			return t;
		else{
			t.setLeft(execute(t.getLeft()));
			t.setRight(execute(t.getRight()));
			int c = (int)t.getItem().charAt(0);
			if(t.getLeft() == null)
				t.setLeft(new Tree());
			if(t.getRight() == null)
				t.setRight(new Tree());
			//		*			+			-			/			=		%
			//  (c == 42 || c == 43 || c == 45 || c == 47 || c == 61 || c == 37)
			
			if(t.getLeft().getValue() == null)
				t.getLeft().setValue(new Double(0));
			if(t.getRight().getValue() == null)
				t.getRight().setValue(new Double(0));
			
			switch(c){
			case 42:
				t.setValue(t.getLeft().getValue()*t.getRight().getValue());
				break;
			case 43:
				t.setValue(t.getLeft().getValue()+t.getRight().getValue());
				break;
			case 45:
				t.setValue(t.getLeft().getValue()-t.getRight().getValue());
				break;
			case 47:
				t.setValue(t.getLeft().getValue()/t.getRight().getValue());
				break;
			case 61:
				t.setValue(t.getRight().getValue());
				t.getLeft().setValue(t.getRight().getValue());
				break;
			case 37:
				t.setValue(t.getLeft().getValue()%t.getRight().getValue());
				break;
			}
		}
		return t;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
}
