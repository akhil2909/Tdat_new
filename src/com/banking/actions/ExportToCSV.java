package com.banking.actions;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import au.com.bytecode.opencsv.CSVWriter;
import com.banking.utils.ExcelReader;
import com.opencsv.CSVWriter;

public class ExportToCSV
{
    private static ArrayList<String[]> listOfRows;
    private static String[] data;
    private static FileWriter writer;
    private static CSVWriter csvWriter;
    
    public static void convertToCSV(String filePath,String fileName) {
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.direc")+"\\"+ExcelReader.getModuleSelected()+"\\"+ExcelReader.getProcessSelected()+"\\"+fileName+".xlsx");
            XSSFWorkbook csvWorkbook = new XSSFWorkbook(fis);
            
         //   for (int l = 0; l < csvWorkbook.getNumberOfSheets(); l++) {
                XSSFSheet sheet = csvWorkbook.getSheetAt(0);
                int noOfRows = sheet.getPhysicalNumberOfRows();
               // if (ExcelReader.getProcessSelected().equals(sheet.getRow(0).getCell(0)
                //        .getStringCellValue())) {
                    listOfRows = new ArrayList<String[]>();
                    int noOfColums=sheet.getRow(0).getPhysicalNumberOfCells();
                    for (int n = 0; n < noOfRows; n++) {
                        XSSFRow row1 = sheet.getRow(n);
                        data = new String[noOfColums];
                        for (int k = 0; k <noOfColums; k++) {
                            try
                            {
                                if (row1.getCell(k).getCellType() == 1) {
                                    data[k] = row1.getCell(k)
                                            .getStringCellValue();
                                } else if (row1.getCell(k).getCellType() == 0) {
                                    data[k] = Double.toString(row1.getCell(
                                            k).getNumericCellValue());
                                } else if (row1.getCell(k).getCellType() == 3) {
                                    data[k] = "\\[Don't Care]";
                                } else if (row1.getCell(k).getCellType() == 4) {
                                    data[k] = Boolean.toString(row1
                                            .getCell(k).getBooleanCellValue());
                                }
                                else
                                {
                                    data[k] = Boolean.toString(row1
                                            .getCell(k).getBooleanCellValue());
                                }
                            }catch (NullPointerException e)
                            {
                                data[k] = "\\[Don't Care]";
                            }
                        }
                        listOfRows.add(data);
                    }
                 //   break;
               // }
          //  }
            writer = new FileWriter(filePath+fileName+".csv");
            csvWriter = new CSVWriter(writer);
            csvWriter.writeAll(listOfRows);
            csvWriter.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
