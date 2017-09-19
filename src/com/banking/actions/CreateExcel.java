package com.banking.actions;

import com.banking.utils.ExcelReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateExcel {

    static File file = null;
    static OutputStream os = null;
    static Workbook workbook = null;
    static Sheet sheet = null;
    static InputStream is=null;
    static List<Integer> indexes=new ArrayList<>();
    static List<List<String>> mapValues=new ArrayList<>();
    static int rowNum=1;

   
    public static void createDirectories()
    {
        for(String moduleN:ExcelReader.getModulesList())
        {
            file = new File(System.getProperty("user.direc")+"/"+moduleN);
            if(!file.isDirectory())
            {
                file.mkdir();
            }
            for(String processN: ExcelReader.getProcessList(moduleN))
            {
                String path=file.getAbsolutePath();
                file=new File(path+"/"+processN);
                if(!file.isDirectory())
                {
                    file.mkdir();
                }
                file=new File(path);
            }
        }
         
    }
    public static void createDirectory()
    {
        //for(String moduleN:ExcelReader.getModulesList())
        //{
            file = new File(System.getProperty("user.direc")+"/"+ExcelReader.getModuleSelected());
            if(!file.isDirectory())
            {
                file.mkdir();
            }
            for(String processN: ExcelReader.getProcessList(ExcelReader.getModuleSelected()))
            {
                String path=file.getAbsolutePath();
                file=new File(path+"/"+processN);
                if(!file.isDirectory())
                {
                    file.mkdir();
                }
                file=new File(path);
            }
        //}
         
    }
    public static void createNewExcel(String moduleName, String processName,String formName) {
        createDirectory();
    //    List<String> formslist = SLFileExtractor.getFormsLilst(processName);
        //int colCount = 1;
    //    for (String formName : formslist) {
            List<String> list = SLFileExtractor.readSL(formName, moduleName, processName);
            file = new File(System.getProperty("user.direc") + "/" + moduleName+"/"+processName+"/"+formName+".xlsx");
            //if (!file.exists()) {
            try {
                file.createNewFile();
                os = new FileOutputStream(file);
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Sheet1");
                Row row = sheet.createRow(0);
                //row.createCell(0).setCellValue(processName);
                for (int k = 0; k <list.size(); k++) {     
                    String val = list.get(k);
                    Cell cell = row.createCell(k);
                    cell.setCellValue(val.substring(val.indexOf("=") + 1));
                //    colCount++;
                    }
                workbook.write(os);
                os.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
                }
          //  }
     //   }
//        file = new File(System.getProperty("user.direc") + "/" + moduleName + ".xlsx");
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                os = new FileOutputStream(file);
//                workbook = new XSSFWorkbook();
//                sheet = workbook.createSheet("Sheet1");
//                Row row = sheet.createRow(0);
//                row.createCell(0).setCellValue(processName);
//                List<String> formslist = SLFileExtractor.getFormsLilst(processName);
//                int colCount = 1;
//                for (String formName : formslist) {
//                    List<String> list = SLFileExtractor.readSL(formName, moduleName, processName);
//
//                    for (int k = 1; k <= list.size(); k++) {
//                        String val = list.get(k - 1);
//                        Cell cell = row.createCell(colCount);
//                        cell.setCellValue(val.substring(val.indexOf("=") + 1));
//                        colCount++;
//                    }
//                }
//
//                workbook.write(os);
//                os.close();
//            } catch (IOException ex) {
//                Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            try {
//                is=new FileInputStream(file);
//                workbook=new XSSFWorkbook(is);
//                os=new FileOutputStream(file);
//                boolean isSheetExists=false;
//                for(int i=0;i<workbook.getNumberOfSheets();i++)
//                {
//                    sheet=workbook.getSheetAt(i);
//                    String process=sheet.getRow(0).getCell(0).getStringCellValue();
//                    if(process.equals(processName))
//                    {
//                        isSheetExists=true;
//                    }
//                }
//                sheet=workbook.createSheet("Sheet"+(workbook.getNumberOfSheets()+1));
//                Row row=sheet.createRow(0);
//                row.createCell(0).setCellValue(processName);
//                List<String> formslist = SLFileExtractor.getFormsLilst(processName);
//                int colCount = 1;
//                for (String formName : formslist) {
//                    List<String> list = SLFileExtractor.readSL(formName, moduleName, processName);
//
//                    for (int k = 1; k <= list.size(); k++) {
//                        String val = list.get(k - 1);
//                        Cell cell = row.createCell(colCount);
//                        cell.setCellValue(val.substring(val.indexOf("=") + 1));
//                        colCount++;
//                    }
//                }
//                workbook.write(os);
//                os.close();
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
    }
    public static void permute(List<List<String>> list, int index, ArrayList<String> output,XSSFSheet sh,int colCount)
    {
        if(index == list.size()){
            Row dataRow = sh.createRow(rowNum);
            rowNum++;
            int a=0;
            for(int k=0;k<colCount;k++)
            {
                Cell cell=  dataRow.createCell(k);
                if(indexes.contains(k))
                {
                    cell.setCellValue(output.get(a));
                    a++;
                }
            }
            //System.out.println(output.toString());
        }
        else{
            for(int i=0 ; i<list.get(index).size() ; i++){
                output.add(list.get(index).get(i));
                permute(list,index+1,output,sh,colCount);
                output.remove(output.size() - 1); 
            }
        }
    }
    public static void modifyExcel(String moduleName,String processName,List<String> combiList,String formName)
    {
       // System.out.println("modifyexcel");
       // List<String> formslist = SLFileExtractor.getFormsLilst(processName);
        int len=0;
      //  for (String formName : formslist) 
     //   {
            int j = 0;
            List<String> fieldsList = SLFileExtractor.readSL(formName, moduleName, processName);
            len=fieldsList.size();
            for (String s : fieldsList) {
                String fieldName=s.substring(s.indexOf("=")+1);
               // String s1=s;
                if(combiList.contains(fieldName))
                {
                    indexes.add(fieldsList.indexOf(s));
                    mapValues.add(SLFileExtractor.map.get(fieldName));
                }
                else if (SLFileExtractor.map.containsKey(fieldName)) {
                    if (SLFileExtractor.map.get(fieldName).size() > j) {
                        j = SLFileExtractor.map.get(fieldName).size();
                    }
                }
            }
            try
            {
                File file1 = new File(System.getProperty("user.direc") + "/" + moduleName+"/" +processName+ "/"+formName+".xlsx");
                FileInputStream fis = new FileInputStream(file1);
                XSSFWorkbook w = new XSSFWorkbook(fis);
                XSSFSheet sh = w.getSheetAt(0);
                ArrayList<String> output=new ArrayList();
                permute(mapValues, 0, output, sh, len);
                int m=0;
                for(int i=1;i<=j;i++)
                {
                    Row dataRow;
                    if(sh.getRow(i)!=null)
                    {
                        dataRow=sh.getRow(i);
                    }
                    else
                    {
                        dataRow=sh.createRow(i);
                    }
                    List<String> fieldsList1 = SLFileExtractor.readSL(formName, moduleName, processName);
                    int l=0;
                    for (String field : fieldsList1) {
                        field = field.substring(field.indexOf("=") + 1); 
                        if (SLFileExtractor.map.containsKey(field)&&!combiList.contains(field)) 
                        {
                            System.out.println(field);
                            Cell cell = dataRow.createCell(l);
                            List<String> v = SLFileExtractor.map.get(field);
                            if (m < v.size()) {
                                cell.setCellValue(v.get(i-1));
                            }
                        }
                        l++;
                    }
                    m++;
                }
                rowNum=1;
                mapValues=new ArrayList<>();
                indexes=new ArrayList<>();
                fis.close();
                FileOutputStream fos = new FileOutputStream(file1);
                w.write(fos);
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
      //  }
    }
//        try {
//            File file1 = new File(System.getProperty("user.direc") + "/" + moduleName +processName+ ".xlsx");
//            FileInputStream fis = new FileInputStream(file1);
//            XSSFWorkbook w = new XSSFWorkbook(fis);
//            XSSFSheet s = null;
//            int count = w.getNumberOfSheets();
//            for (int i = 0; i < count; i++) {
//                s = w.getSheetAt(i);
//                Row r = s.getRow(0);
//                if (r.getCell(0).getStringCellValue().equals(processName)) {
//                    break;
//                }
//            }
//            int m=0;
//            
//            for (int k = 0; k < j; k++) {
//                Row dataRow = s.createRow(k + 1);
//                int l=1;
//                for (String formName : formslist) {
//                    List<String> fieldsList = SLFileExtractor.readSL(formName, moduleName, processName);
//                    for (String field : fieldsList) {
//                        field = field.substring(field.indexOf("=") + 1);
//                        Cell cell = dataRow.createCell(l++);
//                        if (SLFileExtractor.map.containsKey(field)) {
//                            List<String> v = SLFileExtractor.map.get(field);
//                            if (m < v.size()) {
//                                cell.setCellValue(v.get(m));
//                            }
//                        }
//                    }
//                }
//                m++;
//            }
//            fis.close();
//            FileOutputStream fos = new FileOutputStream(file1);
//            w.write(fos);
//            fos.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    public static void modifyExcel() 
//    {
//        List<String> fieldsList = SLFileExtractor.readSL(ExcelReader.getProcessSelected(), ExcelReader.getModuleSelected(), ExcelReader.getProcessSelected());
//        int j = 0;
//        for (String s : fieldsList) {
//            if (SLFileExtractor.map.containsKey(s.substring(s.indexOf("=") + 1))) {
//                if (SLFileExtractor.map.get(s.substring(s.indexOf("=") + 1)).size() > j) {
//                    j = SLFileExtractor.map.get(s.substring(s.indexOf("=") + 1)).size();
//                    System.out.println(j);
//                }
//            }
//        }
//        try {
//            File file1 = new File(System.getProperty("user.direc") + "/" + ExcelReader.getModuleSelected() + ".xlsx");
//            FileInputStream fis = new FileInputStream(file1);
//            XSSFWorkbook w = new XSSFWorkbook(fis);
//            XSSFSheet s = null;
//            int count = w.getNumberOfSheets();
//            System.out.println(count);
//            System.out.println("size" + SLFileExtractor.map.size());
//            for (int i = 0; i < count; i++) {
//                s = w.getSheetAt(i);
//                Row r = s.getRow(0);
//                if (r.getCell(0).getStringCellValue().equals(ExcelReader.getProcessSelected())) {
//                    System.out.println(r.getCell(0).getStringCellValue());
//                    break;
//                }
//            }
//            int m = 0;
//            for (int k = 0; k < j; k++) {
//                Row dataRow = s.createRow(k + 1);
//                for (int l = 0; l < fieldsList.size(); l++) {
//                    String field = fieldsList.get(l);
//                    field = field.substring(field.indexOf("=") + 1);
//                    Cell cell = dataRow.createCell(l + 1);
//                    if (SLFileExtractor.map.containsKey(field)) {
//                        List<String> v = SLFileExtractor.map.get(field);
//                        if (m < v.size()) {
//                            cell.setCellValue(v.get(m));
//                        }
//                    }
//                }
//                m++;
//            }
//            fis.close();
//            FileOutputStream fos = new FileOutputStream(file1);
//            w.write(fos);
//            fos.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(CreateExcel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
