/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BIFinance;
import java.text.DateFormat;
import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3c.dom.*;
/**
 *
 * @author PEC
 */
public class URLGenerator {
        String stockURL;
        String benchURL;
        
    /**
     *
     * @param stock
     * @param startdate
     * @param enddate
     */
    public URLGenerator(Node stock, Date startdate, Date enddate){
                                
                stockURL ="http://ichart.yahoo.com/table.csv?s=";
                stockURL += stock.getAttributes().getNamedItem("id").getNodeValue()
                        +"&a="+String.valueOf(startdate.getMonth()-1)
                        +"&b="+String.valueOf(startdate.getDay())
                        +"&c="+String.valueOf(startdate.getYear())
                        +"&d="+String.valueOf(enddate.getMonth()-1)
                        +"&e="+String.valueOf(enddate.getDay())
                        +"&f="+String.valueOf(enddate.getYear())
                        +"&g=d&ignore=.csv";
                
                benchURL ="http://ichart.yahoo.com/table.csv?s=";
                benchURL += stock.getAttributes().getNamedItem("benchID").getNodeValue()
                +"&a="+String.valueOf(startdate.getMonth()-1)
                +"&b="+String.valueOf(startdate.getDay())
                +"&c="+String.valueOf(startdate.getYear())
                +"&d="+String.valueOf(enddate.getMonth()-1)
                +"&e="+String.valueOf(enddate.getDay())
                +"&f="+String.valueOf(enddate.getYear())
                +"&g=d&ignore=.csv";
        }
    
        public String getStockURL() {
                return stockURL;
        }

        public String getBenchURL() {
                return benchURL;
        }
}
