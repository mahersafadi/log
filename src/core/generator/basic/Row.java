package core.generator.basic;

import core.generator.*;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.w3c.dom.*;;

public class Row extends Item implements IGenerate{
	List<Cell> cells;
	public Row(){
		cells = new ArrayList<Cell>();
		
	}
	public Row(HttpSession session, Node n){
		setSession(session);
		cells = new ArrayList<Cell>();
		if(n != null){
			parse(n);
		}
	}
	
	
	
	@Override
	public String generate(){
		String str = "<tr ";
		str += super.generate();
		str += " >";
		for(int i=0;i<this.cells.size(); i++){
			str += this.cells.get(i).generate();
		}
		str += "</tr>";
		return str;
	}
	
	@Override
	public void parse(org.w3c.dom.Node node){
		super.parse(node);
		
		NodeList nl = node.getChildNodes();
		for(int i=0; i<nl.getLength(); i++){
			Node n = nl.item(i);
			if(n.getNodeType() != Node.TEXT_NODE){
				Cell c = new Cell(this.getSession(),n);
				if(c != null)
					cells.add(c);
			}
		}
	}
	
	public List<Cell> getCells() {
		return cells;
	}
	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
	
	public String callAfterBuild() {
		String res = "";
		for(int i=0; i<this.cells.size(); i++){
			res += cells.get(i).callAfterBuild()+";";
		}
		return res;
	}
}
