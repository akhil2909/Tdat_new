/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banking.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.AnchorPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author JM100831
 */
public class ExcelReader 
{
    private static File file=null;
    private static InputStream is=null;
    private static FileInputStream fis=null;
    private static XSSFWorkbook workbook=null;
    private static XSSFSheet sheet=null;
    private static List<String> modulesList=null;
    private static List<String> processList=null;
    private static String moduleSelected=null;
    private static String processSelected=null;
    private static Map<String,String> subProcess=null;
    private static String subFunctionSelected=null;

    public static String getSubFunctionSelected() {
        return subFunctionSelected;
    }

    public static void setSubFunctionSelected(String subFunctionSelected1) {
        subFunctionSelected = subFunctionSelected1;
    }
    

    public static String getModuleSelected() {
        return moduleSelected;
    }

    public static void setModuleSelected(String moduleSelected1) {
        moduleSelected = moduleSelected1;
    }

    public static String getProcessSelected() {
        return processSelected;
    }

    public static void setProcessSelected(String processSelected1) {
        processSelected = processSelected1;
    }
    
    
    static
    {
        file=new File("/com/banking/utils/Banking.xlsx");
        try {
            is=ExcelReader.class.getClass().getResourceAsStream("/com/banking/utils/Banking.xlsx");
            workbook=new XSSFWorkbook(is);
            sheet=workbook.getSheetAt(0);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    public static List<String> getModulesList()
    {
        System.out.println("@@@ getModulesList");
        modulesList=new ArrayList<>();
        for(int i=0;i<sheet.getPhysicalNumberOfRows();i++)
        {
            Row row=sheet.getRow(i);
            modulesList.add(row.getCell(0).getStringCellValue());
        }
        return modulesList;
    }
    public static List<String> getProcessList(String moduleName)
    {
        processList=new ArrayList<>();
        XSSFSheet processSheet=workbook.getSheet(moduleName);
        for(int i=0;i<processSheet.getPhysicalNumberOfRows();i++)
        {
            Row row=processSheet.getRow(i);
            processList.add(row.getCell(0).getStringCellValue());
        }
        return processList;
    }
    public static Map<String,String> getSubProcess(String moduleName)
    {
        subProcess=new Hashtable<String, String>();
        
      //  processList=new ArrayList<>();
        XSSFSheet processSheet=workbook.getSheet(moduleName);
        System.out.println(processSheet.getPhysicalNumberOfRows());
        for(int i=0;i<processSheet.getPhysicalNumberOfRows();i++)
        {
            Row row=processSheet.getRow(i);
           // System.out.println(row.getCell(0).getStringCellValue());
            if(row.getCell(1)==null){}
            else
            {
                subProcess.put(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
            }
            //processList.add(row.getCell(0).getStringCellValue());
        }
        return subProcess;
    }
    public static void main(String[] args)
    {
       Map p= getSubProcess("Internet Banking");
       for(int i=0;i<p.size();i++)
       {
           String key=p.keySet().toArray()[i].toString();
           String[] s=p.get(key).toString().split("\n");
           for(int k=0;k<s.length;k++)
           {
               System.out.println(s[k]);
           }
           //System.out.println(key+"    "+p.get(key));
       }
    }
}
