/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BIFinance;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

/**
 *
 * @author PEC
 */
public class Indicator {
    
    public static double getPerf(TreeMap<Date, Double> data, int period){
        Date startDate = (Date) data.lastKey().clone();
        try {
                Date endDate = (Date) startDate.clone();
                for(int i=0; i < (int) period*4;i++){
                    Calendar c = Calendar.getInstance(); 
                    c.setTime(endDate); 
                    c.add(Calendar.DATE, -7);
                    endDate = c.getTime();
                }

                return Math.pow(data.get(startDate)/data.get(endDate),365/(period*4*7)) - 1;
        } catch (Exception e) {
                return 0.0;
        }  
    }
            
    public static double getVol(TreeMap<Date, Double> data, int period){
        double vol = 0.0;
                
        Date startDate = (Date) data.lastKey().clone();
        try {
                Date endDate = (Date) startDate.clone();
                for(int i = 0; i < period*4;i++){
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(endDate); 
                        c.add(Calendar.DATE, -7);
                        endDate = c.getTime();
                        vol += Math.pow(Math.log(data.get(startDate)/data.get(endDate))-moyennePerf(data, period), 2);
                        c.setTime(startDate); 
                        c.add(Calendar.DATE, -7);
                        startDate = c.getTime();
                }
                return Math.sqrt(52*vol/(period*4-1));
        } catch (Exception e) {
                e.printStackTrace();
        }
        return 0.0;
    }
    
    public static double getAlpha(TreeMap<Date, Double> dataAction, TreeMap<Date, Double> dataBench, int period){
        return getPerf(dataAction, period)-getBeta(dataAction, dataBench, period)*getPerf(dataBench, period); 
    }
    
    public static double getBeta(TreeMap<Date, Double> dataAction, TreeMap<Date, Double> dataBench, int period){
        try {
                double cov = covariance(dataAction, dataBench, period);
                double var = covariance(dataBench, dataBench, period);
                return cov/var;

        } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
        }
    }
    
    public static double getIR(TreeMap<Date, Double> dataAction, TreeMap<Date, Double> dataBench, int period){
        return (getPerf(dataAction, period) - getPerf(dataBench, period))/getTrackingError(dataAction, dataBench, period);
    }
    
    public static double getTrackingError(TreeMap<Date, Double> dataAction, TreeMap<Date, Double> dataBench, int period){
        try {

            Date startDate = (Date) dataAction.lastKey().clone();
            Calendar c = Calendar.getInstance(); 
            
            try {
                    Date endDate = (Date) startDate.clone();
                    
                    double res = 0.0;
                    double perfMA = 0.0, perfMB = 0.0, perfM = 0.0;

                    for(int i = 0; i < period*4;i++){
                            c.setTime(endDate); 
                            c.add(Calendar.DATE, -7);
                            endDate = c.getTime();
                            perfMA += dataAction.get(startDate)/dataAction.get(endDate)-1;
                            perfMB += dataBench.get(startDate)/dataBench.get(endDate)-1;
                            c.setTime(startDate); 
                            c.add(Calendar.DATE, -7);
                            startDate = c.getTime();
                    }
                    perfM = (perfMA -perfMB)/(period*4);
                    
                    startDate = (Date) dataAction.lastKey().clone();
                    endDate = (Date) startDate.clone();
                    for(int i = 0; i < period*4;i++){
                            c.setTime(endDate); 
                            c.add(Calendar.DATE, -7);
                            endDate = c.getTime();
                            double perfAi = (dataAction.get(startDate)/dataAction.get(endDate)-1) - (dataBench.get(startDate)/dataBench.get(endDate)-1);
                            res += Math.pow(perfAi - perfM,2);
                            c.setTime(startDate); 
                            c.add(Calendar.DATE, -7);
                            startDate = c.getTime();
                    }
                    return Math.sqrt(52*res/(period*4-1));
            } catch (Exception e) {
                    e.printStackTrace();
            }
                return 0.0;
            } catch (Exception e) {
                    e.printStackTrace();
                    return 0.0;
            }
        }
    public static double moyenne(TreeMap<Date, Double> datas, int period){
       
        double moy = 0.0;

        Date startDate = (Date) datas.lastKey().clone();
        try {
                Date endDate = (Date) startDate.clone();
                for(int i = 0; i < period*4;i++){
                        moy += datas.get(endDate);
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(endDate); 
                        c.add(Calendar.DATE, -7);
                        endDate = c.getTime();
                }               
                return moy/(period*4);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return 0.0;
    }
    
    public static double moyennePerf(TreeMap<Date, Double> datas, int period){
        double moy = 0.0;

        Date startDate = (Date) datas.lastKey().clone();
        try {
                Date endDate = (Date) startDate.clone();
                for(int i=0;i<period*4;i++){
                        Calendar c = Calendar.getInstance(); 
                        c.setTime(endDate); 
                        c.add(Calendar.DATE, -7);
                        endDate = c.getTime();
                        moy += Math.log(datas.get(startDate)/datas.get(endDate));
                        c.setTime(startDate); 
                        c.add(Calendar.DATE, -7);
                        startDate = c.getTime();
                }
                return moy/(period*4);
        } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
        }
    }
    
    public static double covariance(TreeMap<Date, Double> dataAction, TreeMap<Date, Double> dataBench, int period){
        double moyA = moyenne(dataAction, period);
        double moyB = moyenne(dataBench, period);
        double moyAB = 0.0;
        Date date = (Date) dataAction.lastKey().clone();
        for(int i = dataAction.size()-1; i < dataAction.size()-1-period*4;i--){
                Calendar c = Calendar.getInstance(); 
                c.setTime(date); 
                moyAB += dataAction.get(date)*dataBench.get(date);
                c.add(Calendar.DATE, -7);
                date = c.getTime();
        }
        moyAB = moyAB/(period*4);

        return moyAB - moyA*moyB;
    }
}
