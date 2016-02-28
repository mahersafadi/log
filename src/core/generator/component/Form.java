package core.generator.component;

import org.w3c.dom.*;
import java.util.*;

import javax.servlet.http.HttpSession;

import core.generator.IGenerate;
import core.generator.Item;
import core.generator.basic.*;

public class Form extends Item implements IGenerate{
	public final static String YESNO = "YESNO";
	public final static String OKCANCEL = "OKCANCEL";
	public final static String YESNOCANCEL = "YESNOCANCEL";
	public final static String CLOSE = "CLOSE";
	public final static String OK = "OK";
	
	private String type = YESNO;
	private String alternativeOk;
	private String alternativeCancel;
	private String entity;
	private List<core.generator.basic.Row> rows;
	
	public Form(){
		rows = new ArrayList<Row>();
	}
	
	public Form(HttpSession session, Node node){
		this.setSession(session);
		rows = new ArrayList<Row>();
		this.parse(node);
	}
	
	@Override
	public String generate(){
		String str = "<form ";
		str += super.generate();
		if(type != null)
			str += " type='"+this.type+"' ";
		if(alternativeOk != null)
			str += " alternativeOk='"+alternativeOk+"'";
		if(alternativeCancel != null)
			str += " alternativeCancel ='"+alternativeCancel+"' ";
		if(entity != null)
			str += " entity='"+entity+"' ";
		str += ">";
		str += "<table width='"+(this.getWidth() == null ? "100%":this.getWidth())+"'";
		if(getHorizentalAlign() != null)
			str += " align='"+getHorizentalAlign()+"' ";
		if(getVerticalAlign() != null)
			str += " valign='"+getVerticalAlign()+"' ";
		//if(getCsClass() != null)
		//	str += " class='"+getCsClass()+"' "; 
		str += " >";
		for(int i=0; i<rows.size(); i++){
			str += rows.get(i).generate();
		}
		str += "</table>";
		str += "</form>";
		return str;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getAlternativeOk() {
		return alternativeOk;
	}

	public void setAlternativeOk(String alternativeOk) {
		this.alternativeOk = alternativeOk;
	}

	public String getAlternativeCancel() {
		return alternativeCancel;
	}

	public void setAlternativeCancel(String alternativeCancel) {
		this.alternativeCancel = alternativeCancel;
	}

	@Override
	public void parse(Node node) {
		// TODO Auto-generated method stub
		super.parse(node);
		Node typeNode = node.getAttributes().getNamedItem("type");
		Node alternativeOkNode = node.getAttributes().getNamedItem("alternativeOk");
		Node alternaticeCancelNode = node.getAttributes().getNamedItem("alternativeCancel");
		Node handlerNode = node.getAttributes().getNamedItem("entity");
		if(typeNode != null)
			this.type = typeNode.getNodeValue();
		if(alternativeOkNode != null)
			this.alternativeOk = alternativeOkNode.getNodeValue();
		if(alternaticeCancelNode != null)
			this.alternativeCancel = alternaticeCancelNode.getNodeValue();
		if(handlerNode != null)
			this.entity = handlerNode.getNodeValue();
		NodeList nl = node.getChildNodes();
		for(int k =0; k<nl.getLength(); k++){
			Node n = nl.item(k);
			if(n.getNodeType() != Node.TEXT_NODE){
				Row row = new Row(this.getSession(), n);
				this.rows.add(row);
			}
		}
	}
	@Override
	public String callAfterBuild() {
		String res = "";
		for(int i=0; i<rows.size(); i++){
			res += rows.get(i).callAfterBuild()+";";
		}
		return res;
	}
	
}
