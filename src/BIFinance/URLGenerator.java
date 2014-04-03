/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BIFinance;
import java.text.DateFormat;
import java.util.Calendar;
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
        
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(startdate);
                Calendar calEnd = Calendar.getInstance();
                calEnd.setTime(enddate);
                                
                stockURL ="http://ichart.yahoo.com/table.csv?s=";
                stockURL += stock.getAttributes().getNamedItem("id").getNodeValue()
                        +"&a="+(calStart.get(Calendar.MONTH)+1)
                        +"&b="+calStart.get(Calendar.DAY_OF_MONTH)
                        +"&c="+calStart.get(Calendar.YEAR)
                        +"&d="+(calEnd.get(Calendar.MONTH)+1)
                        +"&e="+calEnd.get(Calendar.DAY_OF_MONTH)
                        +"&f="+calEnd.get(Calendar.YEAR)
                        +"&g=d&ignore=.csv";
                
                benchURL ="http://ichart.yahoo.com/table.csv?s=";
                benchURL += stock.getAttributes().getNamedItem("benchID").getNodeValue()
                        +"&a="+(calStart.get(Calendar.MONTH)+1)
                        +"&b="+calStart.get(Calendar.DAY_OF_MONTH)
                        +"&c="+calStart.get(Calendar.YEAR)
                        +"&d="+(calEnd.get(Calendar.MONTH)+1)
                        +"&e="+calEnd.get(Calendar.DAY_OF_MONTH)
                        +"&f="+calEnd.get(Calendar.YEAR)
                        +"&g=d&ignore=.csv";
        }
    
        public String getStockURL() {
                return stockURL;
        }

        public String getBenchURL() {
                return benchURL;
        }
}
