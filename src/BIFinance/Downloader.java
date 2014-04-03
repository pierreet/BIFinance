/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BIFinance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author PEC
 */
public class Downloader {
     /**
         * 
         * @param adresse
         * @param dest
         */
        public static void Downloader(String adresse, File dest) {
                try {

                        URL url = new URL(adresse);
                        URLConnection conn = url.openConnection();

                        InputStream in = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(dest);
                        byte[] buff = new byte[1024];
                        int l = in.read(buff);
                        while (l > 0) {
                                fos.write(buff, 0, l);
                                l = in.read(buff);
                        }
                } catch (IOException e) {
                }
        }
}
