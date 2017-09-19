package com.banking.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig
{
    
    public static String getProperty(String key)
    {
        Properties properties = null;
        try {
            InputStream fis = PropertiesConfig.class.getClass().getResourceAsStream("/com/banking/utils/config.properties");
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
        
    }
    
    public static void setProperty(String key, String value) {
        try {
            Properties properties = new Properties();
            properties.setProperty(key, value);
            File file = new File(System.getProperty("user.dir")+"\\config.properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Favorite Things");
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
