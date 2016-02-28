package core.generator.basic;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;

import core.generator.IGenerate;
import core.generator.Item;

public class Image  extends Item implements IGenerate{
	private String src;
	
	public Image(HttpSession session, Node n){
		setSession(session);
		this.parse(n);
	}
	@Override
	public String generate() {
		String str = "<img class='image' "+super.generate();
		str+= " src='"+this.getSrc()+"'  />";
		return str;
	}

	@Override
	public void parse(Node node) {
		super.parse(node);
		setCsClass("image");
		Node srcNode = node.getAttributes().getNamedItem("src");
		if(srcNode != null){
			this.src = srcNode.getNodeValue();
		}
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	
}

