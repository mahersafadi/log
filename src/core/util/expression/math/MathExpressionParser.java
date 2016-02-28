package core.util.expression.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathExpressionParser {
	String expressions;

	Map<String, Double> realValues;
	List<Tree> expressionTrees;
	public MathExpressionParser() {
		// TODO Auto-generated constructor stub
		realValues = new HashMap<String, Double>();
		expressionTrees = new ArrayList<Tree>();
	}
	
	public boolean build(){
		try{
			String [] strArr = expressions.split(";");
			for(int i=0; i<strArr.length; i++){
				Tree t = buildExpr(strArr[i]);
				t = setRealValue(t, realValues);
				expressionTrees.add(t);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private Tree buildExpr(String expr) throws Exception{
		if(!validate(expr))
			throw new NotValidExpression();
		String[] items = convertToArray(expr);
		Tree t = null;
		t = buildSubExpr(items, 0, items.length, t);
		return t;
	}
	
	private Tree buildSubExpr(String [] arr, int s, int e, Tree t) throws Exception{
		if(s==e){
			t = new Tree();
			t.setItem(arr[s]);
			return t;
		}
		if(s<=e){
			int mainOp = getMainOpIndex(s, e, arr);
			t = new Tree();
			t.setItem(arr[mainOp]);
			t.setLeft(buildSubExpr(arr, s+1, mainOp-1, t.getLeft()));
			t.setRight(buildSubExpr(arr, mainOp+1, e-1, t.getRight()));
		}
		return t;
	}
	
	private boolean validate(String expr){
		int numberOfP = 0;
		if(expr == null)
			return false;
		if("".equals(expr.trim()))
			return false;
		for(int i=0; i<expr.length(); i++){
			if(expr.charAt(i) == '(')
				numberOfP ++;
			if(expr.charAt(i) == ')')
				numberOfP --;
		}
		if(numberOfP == 0)
			return true;
		else
			return false;
	}
	
	private String[] convertToArray(String expr) throws NotValidExpression{
		List<String> ll = new ArrayList<String>();
		String curr = "";
		int index = 0;
		for(int i=0; i<expr.length(); i++){
			System.out.println(i+":"+expr.charAt(i));
			int c = (int)expr.charAt(i);
			if(c == 40){
				ll.add("(");
				index++;
			}
			if(c==99 || c == 67){
				int currC = c;
				String num = "";
				while((expr.charAt(i)<= 57 && expr.charAt(i)>=48) || (expr.charAt(i)==99) || (expr.charAt(i) == 67)){
					currC = expr.charAt(i);
					num+= (char)currC;
					i++;
				}
				ll.add(num);
				index++;
				c = expr.charAt(i);
			}
			if(c == 41){
				ll.add(")");
				index++;
			}
			if(c >=48 && c<=57){
				//collect the number number finishes when no more numbers appear, . may appear and just once
				int numberOfDot = 0;
				int currC = c;
				String num = ""+(char)c;
				while((currC<= 57 && currC>=48) || currC == 46){
					if(currC == 46)
						numberOfDot ++;
					i++;
					currC = expr.charAt(i);
					num+= (char)currC;
				}
				if(numberOfDot>1)
					throw new NotValidExpression();
				ll.add(num);
				index++;
				c = expr.charAt(i);
			}
			//		*			+			-			/			=		%
			if(c == 42 || c == 43 || c == 45 || c == 47 || c == 61 || c == 37){
				ll.add(""+expr.charAt(i));
				index++;
			}
		}
		String [] retArr = new String[ll.size()];
		for(int i=0; i<ll.size(); i++){
			retArr[i] = ll.get(i);
		}
		return retArr;
		/*
		 *32: 

			40:(					41:)  					42:*					43:+					45:-
			47:/					61:=					46:.					48:0					49:1
			50:2					51:3					52:4					53:5					54:6
			55:7					56:8					57:9					67:C					99:c
			37:%
		 * */
	}
	
	private int getMainOpIndex(int s, int e, String[] arr)throws NotValidExpression{
		int index = s;
		int numOfP = 0;
		for(int i=s; i<=e; i++){
			char c = (char)arr[i].charAt(0);
			if(c == '(')
				numOfP++;
			if(c == ')')
				numOfP--;
			if(c == 42 || c == 43 || c == 45 || c == 47 || c == 61 || c == 37){
				if(numOfP == 1){
					index = i;
					i = e+2;
				}
			}
		}
		return index;
	}
	
	private Tree setRealValue(Tree t, Map<String, Double> m){
		if(t !=  null){
			t.setValue(m.get(t.getItem()));
			t.setLeft(setRealValue(t.getLeft(), m));
			t.setRight(setRealValue(t.getRight(), m));
		}
		return t;
	}
	
	public String getExpressions() {
		return expressions;
	}

	public void setExpressions(String expressions) {
		this.expressions = expressions;
	}

	public Map<String, Double> getRealValues() {
		return realValues;
	}

	public void setRealValues(Map<String, Double> realValues) {
		this.realValues = realValues;
	}

	public List<Tree> getExpressionTrees() {
		return expressionTrees;
	}

	public void setExpressionTrees(List<Tree> expressionTrees) {
		this.expressionTrees = expressionTrees;
	}
}
