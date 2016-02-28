package core.generator.basic;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;

import core.generator.IGenerate;
import core.generator.Item;

public class Date extends Item implements IGenerate{
	public Date(HttpSession session, Node n) {
		// TODO Auto-generated constructor stub
		this.setSession(session);
		this.parse(n);
	}
	
	@Override
	public void parse(Node node) {
		// TODO Auto-generated method stub
		super.parse(node);
	}
	
	@Override
	public String generate() {
		// TODO Auto-generated method stub
		return super.generate();
	}
	
	public String generateInGrid(){
		
		return "";
	}
}
