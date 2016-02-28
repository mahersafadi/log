package core.executers.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class Customer implements IBusiness{
	private HttpSession session;
	private Map<String, String> m;
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setRequestData(Map<String, String> m) {
		// TODO Auto-generated method stub
		this.m = m;
	}

	@Override
	public String getResponseAsString() {
		// TODO Auto-generated method stub
		return "ok";
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		try{
			if("setcustomer".equals(m.get("op"))){
				this.session.setAttribute("curr_customer", m.get("data"));
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void setHttpSession(HttpSession session) {
		// TODO Auto-generated method stub
		this.session = session;
	}

}
