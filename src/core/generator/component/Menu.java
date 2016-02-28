package core.generator.component;

import org.w3c.dom.Node;

import core.generator.*;
import core.generator.basic.Cell;
import org.w3c.dom.*;
import java.util.*;

import javax.servlet.http.HttpSession;

public class Menu extends Item implements IGenerate{
	List<Cell> cells;

	public Menu(){
		cells = new ArrayList<Cell>();
	}

	public Menu(HttpSession session, Node node){
		setSession(session);
		cells = new ArrayList<Cell>();
		this.parse(node);
		NodeList nl = node.getChildNodes();
		if(nl != null){
			for(int i=0; i< nl.getLength(); i++){
				Node n = nl.item(i);
				if(n.getNodeType() != Node.TEXT_NODE){
					Cell c = new Cell(getSession(), n);
					cells.add(c);
				}
				
			}
		}
	}
	
	@Override
	public String generate() {
		String str = "<table " +super.generate();
		str+= ">";
		str+=	"<tr>";
		//generate cells
		for(int i=0; i<cells.size(); i++){
			str += cells.get(i).generate();
		}
		str+=	"</tr>";
		str += "</table>";
		return str;
	}

	@Override
	public void parse(Node node) {
		super.parse(node);
	}
	
}
