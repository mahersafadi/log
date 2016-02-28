package core.executers.business;

import java.util.*;

import javax.servlet.http.HttpSession;
public interface IBusiness {
	public void setRequestData(Map<String, String> m);
	public void setHttpSession(HttpSession session);
	public String getResponseAsString();
	public void execute();
}
