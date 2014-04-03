/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BIFinance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author PEC
 */
public class DateCalculator {
         /**
         * liste des dates de references
         * et du Benchmark
         * @param startdate
         * @param enddate
         * @param step
         * @return
         */
        public static ArrayList<Date> fixSteps(Date startdate, Date enddate, int step){
                ArrayList<Date> stepDates = new ArrayList<Date>(); // Liste des étapes 
                stepDates.add((Date) startdate.clone());
                Date date = (Date) startdate.clone();
                try {
                        while(enddate.compareTo(date)> 0){
                                Date date2 = (Date) stepDates.get(stepDates.size()-1).clone();
                                date2.setTime(date2.getTime()+(step*24*3600*1000));
                                stepDates.add(date2);
                                date = (Date) date2.clone();
                        }
                        stepDates.remove(stepDates.size()-1);
                        return stepDates;               
                        
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
                
        }
        
        /**
         * valeurs sur les mêmes dates
         * @param stepsDates
         * @param data
         * @return
         */
        public static TreeMap<Date, Double> getDatesValues(ArrayList<Date> stepsDates, HashMap<Date, Double> data){
                
                TreeMap<Date, Double> values = new TreeMap<Date, Double>();
                
                for(Date stepDates : stepsDates){ // Penser à revenir ici
                        if( data.get(stepDates) != null){
                                values.put(stepDates, data.get(stepDates));
                        }
                        else{
                                boolean found = false;
                                int i = -1;
                                while(!found && i > -10){
                                        try {
                                                stepDates.setTime(stepDates.getTime()+(-24*3600*1000)); // -1 jour
                                                if( data.get(stepDates) != null){
                                                        double tmpvalue = data.get(stepDates);
                                                        stepDates.setTime(stepDates.getTime()+(-i*24*3600*1000));
                                                        values.put(stepDates, tmpvalue);
                                                        found = true;
                                                }
                                        } catch (Exception e) {
                                                e.printStackTrace();
                                        }
                                        i--;
                                }
                        }
                        
                }
                return values;
        }
        
        //centre les valeurs
        public static TreeMap<Date, Double> changeValues (TreeMap<Date, Double> datas ){
                
                TreeMap<Date, Double> datasAdjusted = new TreeMap<Date, Double>();
                int cpt = 0;
                double lastValue = 0.0;
                
                for(Date date : datas.keySet()){
                        if(cpt < 1){
                                datasAdjusted.put(date, 100.0);
                                lastValue = datas.get(date);
                                cpt++;
                        }
                        else{
                                datasAdjusted.put(date, 100.0*datas.get(date)/lastValue);
                                lastValue = datas.get(date);
                        }
                        
                }
                
                return datasAdjusted;
        }
}
