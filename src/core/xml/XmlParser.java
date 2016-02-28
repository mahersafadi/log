package core.xml;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.*;

public class XmlParser {
	public static Document getXmlFileAsDocument(String xmlPath, String charset){
		BufferedReader br = null;
        try{
            Reader r = new InputStreamReader(new FileInputStream(xmlPath), charset);
            br = new BufferedReader(r);
            StringBuilder sb = new StringBuilder();
            while(br.ready()){
              sb.append(br.readLine());
            }
            br.close();
            InputStream is = new ByteArrayInputStream(sb.toString().toLowerCase().getBytes());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            try{
              return builder.parse(is);
            }
            catch(Exception ex){
              System.out.println("Error is done during execution "+ new java.util.Date().toString());
              return builder.parse(new ByteArrayInputStream("<error>Error is done; Execution is faild</error>".getBytes()));
            }
        }
        catch(Exception e){
            e.printStackTrace();
            if(br != null){
              try{
                br.close();
              }
              catch(Exception ex){
                ex.printStackTrace();
              }
            }
        }
        return null;
	}
}
