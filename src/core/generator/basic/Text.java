package core.generator.basic;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;

import core.generator.IGenerate;
import core.generator.Item;

public class Text extends Item implements IGenerate{
	private String type = "text";
	private boolean autoFill;
	private String entity;
	private String validators;
	public Text(){
		
	}
	
	public Text(HttpSession session, Node n){
		setSession(session);
		this.parse(n);
	}
	
	@Override
	public String generate() {
		// TODO Auto-generated method stub
		String str = "";
		if(type.equals("password"))
			str = "<input type='password' ";
		else if (type.equals("textarea"))
			str = "<textarea ";
		else 
			str = "<input type='text' ";
			
		str += super.generate();
		if(entity != null)
			str += " entity='"+entity+"' ";
		if(validators != null)
			str += " onkeypress='validate(this)' validators='"+validators+"' ";
		
		if (type.equals("textarea"))
			str += " </textArea>";
		else
			str += " />";
		return str;
	}
	@Override
	public void parse(Node node) { 
		super.parse(node);
		Node autoFillNode =  node.getAttributes().getNamedItem("auto");
		if(autoFillNode != null && autoFillNode.getNodeValue() != null){
			if("true".equals(autoFillNode.getNodeValue().toLowerCase()))
				this.autoFill = true;
			else if("t".equals(autoFillNode.getNodeValue().toLowerCase()))
					this.autoFill = true;
			else {
				try{
					Integer i = new Integer(autoFillNode.getNodeValue());
					if(i == 0)
						this.autoFill = false;
					else
						this.autoFill = true;
				}
				catch(Exception eee){
					this.autoFill = false;
				}
			}
		}
		Node validatorNode = node.getAttributes().getNamedItem("validator");
		if(validatorNode != null)
			this.validators = validatorNode.getNodeValue();
		
		Node entityNode = node.getAttributes().getNamedItem("entity");
		if(entityNode != null)
			this.entity = entityNode.getNodeValue();
		
		Node typeNode = node.getAttributes().getNamedItem("type");
		if(typeNode != null && typeNode.getNodeValue() != null && !"".equals(typeNode.getNodeValue()))
			type = typeNode.getNodeValue();
		else
			type = "text";
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isAutoFill() {
		return autoFill;
	}

	public void setAutoFill(boolean autoFill) {
		this.autoFill = autoFill;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getValidators() {
		return validators;
	}

	public void setValidators(String validators) {
		this.validators = validators;
	}
}
