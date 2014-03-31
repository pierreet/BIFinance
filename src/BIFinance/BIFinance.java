/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BIFinance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 *
 * @author lambda
 */
public class BIFinance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        readFile();
    }
    
    public static String constructURL(){
        return "";
    }
    
    public static void readFile(){
              try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("input.xml"));

            // normalize text representation
            doc.getDocumentElement().normalize ();
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(doc.getDocumentElement().getAttribute("stardate"));            
            Date end = new GregorianCalendar().getTime();
            
            NodeList stocks = doc.getElementsByTagName("stock");
            
            for(int s=0; s<stocks.getLength() ; s++){


                Node stock = stocks.item(s);
                
                System.out.println(stock.getAttributes().getNamedItem("name").getNodeValue());
                System.out.println(stock.getAttributes().getNamedItem("id").getNodeValue());
                System.out.println(stock.getAttributes().getNamedItem("benchmark").getNodeValue());
                System.out.println(stock.getAttributes().getNamedItem("benchID").getNodeValue());                
                String url = "https://ichart.finance.yahoo.com/table.csv?"
                        + "s="+stock.getAttributes().getNamedItem("id").getNodeValue()+"&"
                        + "d=2&e=31&f="+ end.getYear()
                        + "&g=d"
                        + "&a=0&b=3&c="+ start.getYear()
                        + "&ignore=.csv";
                System.out.println(url);
            
            }   


        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
 
    }
}
