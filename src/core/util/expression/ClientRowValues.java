package core.util.expression;

import java.util.*;
public class ClientRowValues {
	private String valsAsStr;
	private List<Field> fields;
	public ClientRowValues(String str) {
		this.valsAsStr = str;
		fields = new ArrayList<Field>(); 
	}
	public String getValsAsStr() {
		return valsAsStr;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void convertFromStringToList(){
		String str = valsAsStr;
		if(str!= null&&!"".equals(str)){
			if(fields == null)
				fields = new ArrayList<Field>();
			if(fields.size() > 0)
				fields.clear();
			
			if(str.startsWith("["))
				str = str.substring(1);
			if(str.endsWith("]"))
				str = str.substring(0, str.length()-1);
			StringTokenizer st = new StringTokenizer(str, "][");
			int id = 0;
			while(st.hasMoreElements()){
				String s = st.nextToken();
				if(s!=null && !"".equals(s)){
					String [] ss = s.split(":");
					String name = ss[0];
					String val = null;
					if(ss.length>1)
						val = ss[1];
					fields.add(new Field(""+id, name, val));
				}
				id++;
			}
		}
	} 
	public void convertFromListToString(){
		String str = "";
		if(fields != null && fields.size() > 0){
			for(int i=0; i<fields.size(); i++){
				Field f = fields.get(i);
				if(f.name != null && !"".equals(f.name)){
					if(f.value != null && !"".equals(f.value)){
						str += "["+i+":"+f.value+"]";
					}
				}
			}
			valsAsStr = str;
		}
	}
	
	public Map<String, Double> getMathExpRealValues(){
		Map<String, Double> m = new HashMap<String, Double>();
		for(int i=0; i<fields.size(); i++){
			Field currField = fields.get(i);
			Double d;
			try{
				d = new Double(currField.value);
			}
			catch(Exception e){d = null;}
			m.put("c"+i, d);
		}
		return m;
	}
	public void mergeMathExpRealValsWithFields(Map<String, Double> m){
		Iterator<String> it = m.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Double d = m.get(key);
			if(d != null){
				key = key.substring(1);
				int index = Integer.parseInt(key);
				fields.get(index).value = ""+((int)Math.ceil(d));
			}
		}
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public void setColumnsNames(String str){
		if(str != null && !"".equals(str)){
			String [] strArr = str.split(",");
			for(int i=0; i<strArr.length; i++){
				if(i<this.fields.size()){
					this.fields.get(i).name = strArr[i];
				}
			}
		}
	}
}
