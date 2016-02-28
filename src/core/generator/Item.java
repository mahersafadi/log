package core.generator;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;

public class Item implements IGenerate{
	private String id;
	private String width;
	private String height;
	private String horizentalAlign;
	private String verticalAlign;
	private String backgroundColor;
	private String style;
	private String csClass;
	private String onClick;
	private String ondbClick;
	private String onHover;
	private String onBlure;
	private String onChanged;
	private String entity;
	private HttpSession session;
	private String readOnly;
	private String title;
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	public static int counter = 1;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String isHorizentalAlign() {
		return horizentalAlign;
	}
	public void setHorizentalAlign(String horizentalAlign) {
		this.horizentalAlign = horizentalAlign;
	}
	public String isVerticalAlign() {
		return verticalAlign;
	}
	public void setVerticalAlign(String verticalAlign) {
		this.verticalAlign = verticalAlign;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	@Override
	public void parse(Node node) {
		// TODO Auto-generated method stub
		if(node != null && !"#text".equals(node.getNodeName())){
			Node idNode = node.getAttributes().getNamedItem("id");
				if(idNode != null)
					this.setId(idNode.getNodeValue());
			
			Node widthNode = node.getAttributes().getNamedItem("width");			
			if(widthNode != null)
				this.setWidth(widthNode.getNodeValue());
			
			Node heightNode = node.getAttributes().getNamedItem("height");			
			if(heightNode != null)
				this.setHeight(heightNode.getNodeValue());
			
			Node hAlignNode = node.getAttributes().getNamedItem("align");			
			if(hAlignNode != null)
				this.setHorizentalAlign(hAlignNode.getNodeValue());
			
			Node vAlignNode = node.getAttributes().getNamedItem("valign");			
			if(vAlignNode != null)
				this.setVerticalAlign(vAlignNode.getNodeValue());
			
			Node bgNode = node.getAttributes().getNamedItem("background");
			if(bgNode != null)
				this.setBackgroundColor(bgNode.getNodeValue());
			
			Node styleNode = node.getAttributes().getNamedItem("style");
			if(styleNode != null)
				this.style = styleNode.getNodeValue();
			
			Node classNode = node.getAttributes().getNamedItem("class");
			if(classNode != null)
				this.csClass = classNode.getNodeValue();
			
			Node titleNode = node.getAttributes().getNamedItem("title");
			if(titleNode != null)
				this.title = titleNode.getNodeValue();
			
			Node onClickNode = node.getAttributes().getNamedItem("onclick");
			if(onClickNode != null)
				this.onClick = onClickNode.getNodeValue();
			else
				this.onClick = null;
			
			Node onDBClickNode = node.getAttributes().getNamedItem("ondbclick");
			if(onDBClickNode != null)
				this.ondbClick = onDBClickNode.getNodeValue();
			else
				this.ondbClick = null;
			
			Node onHoverNode = node.getAttributes().getNamedItem("onhover");
			if(onHoverNode != null)
				this.onHover = onHoverNode.getNodeValue();
			else
				this.onHover = null;
			
			Node onBlureNode = node.getAttributes().getNamedItem("onblure");
			if(onBlureNode != null)
				this.onBlure = onBlureNode.getNodeValue();
			else
				this.onBlure = null;
			
			Node onChangedNode = node.getAttributes().getNamedItem("onchanged");
			if(onChangedNode != null)
				this.onChanged = onChangedNode.getNodeValue();
			else
				this.onChanged = null;
			
			Node entityNode = node.getAttributes().getNamedItem("entity");
			if(entityNode != null)
				this.entity = entityNode.getNodeValue();
			else
				this.entity = null;
			
			Node readOnlyNode = node.getAttributes().getNamedItem("readonly");
			if(readOnlyNode != null)
				this.readOnly = readOnlyNode.getNodeValue();
			else
				this.readOnly = "false";
		}
	}
	
	public String generateWithoutId(){
		String str = "";
		if(width != null && !"".equals(width))
			str += " width='"+width+"' ";
		if(height != null)
			str += " height='"+height+"' ";
		if(verticalAlign != null)
			str += " valign='"+verticalAlign+"'";
		if(horizentalAlign != null)
			str += " align='"+horizentalAlign+"'";
		if(backgroundColor != null)
			str += "bgcolor='"+backgroundColor+"'";
		if(csClass != null && !"".equals(csClass))
			str += " class='"+csClass+"' ";
		if(style != null && !"".equals(style))
			str += " style='"+style+"' ";
		if(title != null && !"".equals(title))
			str += " title='"+core.lang.LanguageHandler.get("", this.title)+"' ";
		
		if(onClick != null)
			str += " onclick=\""+generateEvent(onClick)+"\" ";
		if(ondbClick != null)
			str += " ondbclick=\""+generateEvent(ondbClick)+"\" ";
		if(onHover != null)
			str += " onhover=\""+generateEvent(onHover)+"\" ";
		if(onBlure != null)
			str += " onblure=\""+generateEvent(onBlure)+"\" ";
		if(onChanged != null)
			str += " onchanged=\""+generateEvent(onChanged)+"\" ";
		return str;
	}
	
	public String generate(){
		String str = "";
		if(id != null)
			str += " id='"+id+"' ";
		str += generateWithoutId();
		return str;
	}
	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getCsClass() {
		return csClass;
	}
	public void setCsClass(String csClass) {
		this.csClass = csClass;
	}
	public String getHorizentalAlign() {
		return horizentalAlign;
	}
	public String getVerticalAlign() {
		return verticalAlign;
	}
	public String getOnClick() {
		return onClick;
	}
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}
	public String getOndbClick() {
		return ondbClick;
	}
	public void setOndbClick(String ondbClick) {
		this.ondbClick = ondbClick;
	}
	public String getOnHover() {
		return onHover;
	}
	public void setOnHover(String onHover) {
		this.onHover = onHover;
	}
	public String getOnBlure() {
		return onBlure;
	}
	public void setOnBlure(String onBlure) {
		this.onBlure = onBlure;
	}
	public String getOnChanged() {
		return onChanged;
	}
	public void setOnChanged(String onChanged) {
		this.onChanged = onChanged;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	private String generateEvent(String e){
		if(e.contains("("))
			e = e.substring(0, e.indexOf("("));
		e = e+"(event)";
		return e;
	}
	public HttpSession getSession() {
		return this.session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public String callAfterBuild(){
		return "";
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
