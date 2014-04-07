/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BIFinance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
    
              try {
                  
            //input
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("input.xml"));

            //output
            Document out = docBuilder.newDocument();
            Element root = out.createElement("output");
            out.appendChild(root);
            
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
               
                Element stockXML = out.createElement("stock");
		root.appendChild(stockXML);
                
                //attributs du stock
		Attr id = out.createAttribute("id");
		id.setValue(stock.getAttributes().getNamedItem("id").getNodeValue());
		stockXML.setAttributeNode(id);
		Attr name = out.createAttribute("name");
		name.setValue(stock.getAttributes().getNamedItem("name").getNodeValue());
		stockXML.setAttributeNode(name);
		Attr benchID = out.createAttribute("benchID");
		benchID.setValue(stock.getAttributes().getNamedItem("benchID").getNodeValue());
		stockXML.setAttributeNode(benchID);
		Attr benchmark = out.createAttribute("benchmark");
		benchmark.setValue(stock.getAttributes().getNamedItem("benchmark").getNodeValue());
		stockXML.setAttributeNode(benchmark);
                 
                URLGenerator url = new URLGenerator(stock, start, end);
                
                //telecharge les fichiers
                File fileStock = new File("data/"+stock.getAttributes().getNamedItem("id").getNodeValue()+".csv");
                Downloader.Downloader(url.getStockURL(), fileStock);
                File fileBench = new File("data/"+stock.getAttributes().getNamedItem("benchID").getNodeValue()+".csv");
                Downloader.Downloader(url.getBenchURL(), fileBench);
                
                //garde les données utiles
                TreeMap<Date, Double> datasAction = reduceData(fileStock);
                TreeMap<Date, Double> datasBenchmark = reduceData(fileBench);
             
                //liste des dates de reference
                ArrayList<Date> listeDates = DateCalculator.fixSteps(start, end, 7);
                
                //cours de l'action et du bench a chaque date choise au dessus
                datasAction = DateCalculator.getDatesValues(listeDates, datasAction);
                datasBenchmark = DateCalculator.getDatesValues(listeDates, datasBenchmark);
                
                //on centre les valeurs
                datasAction = DateCalculator.changeValues(datasAction);
                datasBenchmark = DateCalculator.changeValues(datasBenchmark);
                
                //price
		Element prices = out.createElement("prices");
		stockXML.appendChild(prices);
                for(Entry e : datasAction.entrySet()){
                    Element obs = out.createElement("obs");
                    prices.appendChild(obs);
                    Attr date = out.createAttribute("date");
                    date.setValue(format.format((Date)e.getKey()));
                    obs.setAttributeNode(date);
                    Attr price = out.createAttribute("price");
                    price.setValue(e.getValue().toString());
                    obs.setAttributeNode(price);
                    Attr priceBench = out.createAttribute("priceBench");
                    priceBench.setValue(datasBenchmark.get((Date)e.getKey()).toString());
                    obs.setAttributeNode(priceBench);
                    Attr mm4 = out.createAttribute("mm4");
                    mm4.setValue(String.valueOf(Indicator.getMoyenneMobile(datasAction, 1, (Date)e.getKey())));
                    obs.setAttributeNode(mm4);
                    Attr mm12 = out.createAttribute("mm12");
                    mm12.setValue(String.valueOf(Indicator.getMoyenneMobile(datasAction, 3, (Date)e.getKey())));
                    obs.setAttributeNode(mm12);
                    Attr mm24 = out.createAttribute("mm24");
                    mm24.setValue(String.valueOf(Indicator.getMoyenneMobile(datasAction, 6, (Date)e.getKey())));
                    obs.setAttributeNode(mm24);
                }
                
                //indicator
		Element indicators = out.createElement("indicators");
		stockXML.appendChild(indicators);
		Attr te = out.createAttribute("TE");
		te.setValue(String.valueOf(Indicator.getTrackingError(datasAction, datasBenchmark, 3)));
		indicators.setAttributeNode(te);                
                int i=3;
                while(i <= 12){
                    Element ind = out.createElement("indicator");
                    indicators.appendChild(ind);
                    Attr period = out.createAttribute("period");
                    period.setValue(i+"M");
                    ind.setAttributeNode(period);
                    
                    
                    Attr alpha = out.createAttribute("alpha");
                    alpha.setValue(String.valueOf(Indicator.getAlpha(datasAction, datasBenchmark, i)));
                    ind.setAttributeNode(alpha);
                    Attr beta = out.createAttribute("beta");
                    beta.setValue(String.valueOf(Indicator.getBeta(datasAction, datasBenchmark, i)));
                    ind.setAttributeNode(beta);
                    Attr vol = out.createAttribute("vol");
                    vol.setValue(String.valueOf(Indicator.getVol(datasAction, i)));
                    ind.setAttributeNode(vol);
                    Attr perf = out.createAttribute("perf");
                    perf.setValue(String.valueOf(Indicator.getPerf(datasAction, i)));
                    ind.setAttributeNode(perf);
                    Attr ratio = out.createAttribute("informationRatio");
                    ratio.setValue(String.valueOf(Indicator.getIR(datasAction, datasBenchmark, i)));
                    ind.setAttributeNode(ratio);
                    
                    i *= 2;
                }
                                                
            }   
                
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(out);
            StreamResult result = new StreamResult(new File("output.xml"));
            transformer.transform(source, result);


        }catch (Exception err) {
            System.out.println(" " + err.getMessage ());
        }
 
    }
    
    protected static TreeMap<Date, Double> reduceData(File f) throws Exception{
       
        TreeMap<Date, Double> data = new TreeMap<Date, Double>();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String split = ",";
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();//skip first line
        while((line = br.readLine()) !=null){
             String[] b = line.split(split);
             data.put(format.parse(b[0]), Double.parseDouble(b[b.length-1]));
        }
        br.close();
        
        return data;
    }
}
