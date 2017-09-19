package com.banking.actions;

import com.banking.utils.ExcelReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.banking.utils.PropertiesConfig;

public class SLFileExtractor {

    static File file = null;
    static Reader fis = null;
    static Writer fos = null;
    static BufferedInputStream bis = null;
    static int count = 0;
    static StringBuilder fileContent = new StringBuilder();
    private static List<String> list1 = null;
    private static String enumarte = null;
    static ExcelReader er = new ExcelReader();
    private static String moduleName = null;
    private static String processName=null;
    static Map<String, List<String>> map = new Hashtable<>();
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static List<String> getFormsLilst(String processName) {
        //  file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ","_"))+moduleName.replaceAll("_", " ")+".sl");
        moduleName=ExcelReader.getModuleSelected();
        processName=ExcelReader.getProcessSelected();
        file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + processName + ".sl");
        if(!file.exists())
        {
            file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + ExcelReader.getSubFunctionSelected()+ ".sl");
        }
//        System.out.println(file.getAbsolutePath());
        List<String> formsList = new ArrayList<String>();
        try {
            fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null) {
                count++;
                if (str.contains("creator.gui.form")&&str.length()>54) 
                {
                    int firstIndex = str.indexOf('"');
                    int lastIndex = str.lastIndexOf('"');
                    String formName = str.substring(firstIndex + 1, lastIndex);
                    if(!br.readLine().contains("deleted"))
                    {
                        formsList.add(formName);
                    }
                }
                else if(str.contains("creator.gui.form")&&str.length()<54)
                {
                    str=br.readLine();
                    int firstIndex = str.indexOf('"');
                    int lastIndex = str.lastIndexOf('"');
                    String formName = str.substring(firstIndex + 1, lastIndex);
                    if(!br.readLine().contains("deleted"))
                    {
                        formsList.add(formName);
                    }
                }
                str = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(formsList.size());
        return formsList;
    }

    public static List<String> readSL(String formName, String moduleName, String processName) {
        //moduleName=ExcelReader.getModuleSelected();
//        System.out.println(moduleName);
//        System.out.println(formName);
//        System.out.println(PropertiesConfig.getProperty(moduleName.replace(" ", "_")));
        file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + processName + ".sl");
        if(!file.exists())
        {
            file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + ExcelReader.getSubFunctionSelected()+ ".sl");
        }
//        System.out.println(file.getAbsolutePath());
        list1 = new ArrayList<String>();
        try {
            fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null) {
                count++;
                if (str.contains("creator.gui.form")) {
                    if (str.endsWith(formName+"\"")) {
                        if (br.readLine().contains("{")) {
                            str = br.readLine();
                            while (!str.contains("}")) {

                                if (str.contains("creator.gui.textbox")) {
                                    if (str.length() > 59) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("Textbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("Textbox=" + str.substring(firstIndex + 1, lastIndex));										
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("Textbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("Textbox=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                } else if (str.contains("creator.gui.dropdown")) {
                                    String fName = null;
                                    if (str.length() > 60) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("dropdown=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("dropdown=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("dropdown=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("dropdown=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                    str = br.readLine();
                                    String enumRef = str.substring(str.indexOf("q"));
                                    BufferedReader ibr = new BufferedReader(new FileReader(file));
                                    String str1 = ibr.readLine();
                                    while (true) {
                                        if (str1 != null) {
                                            if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                List<String> values = new ArrayList<String>();
                                                String str2 = ibr.readLine();
                                                str2 = ibr.readLine();
                                                while (!str2.contains("}")) {
                                                    if (str2.length() > 63&&str2.contains(";")) {
                                                        int firstIndex = str2.indexOf('"');
                                                        int lastIndex = str2.lastIndexOf('"');
//                                                        System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                        values.add(str2.substring(firstIndex + 1, lastIndex));
                                                    } else {
                                                        str2=ibr.readLine();
                                                        if(!str2.contains("deleted;"))
                                                        {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(firstIndex);
                                                            values.add(str2.substring(firstIndex + 1, lastIndex));
                                                        }
                                                    }
                                                    str2 = ibr.readLine();
                                                }
                                                map.put(fName, values);
                                            }
                                        } else {
                                            break;
                                        }
                                        str1 = ibr.readLine();
                                    }
                                    ibr.close();
                                } else if (str.contains("creator.gui.checkbox")) {
                                    if (str.length() > 60) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("checkbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("checkbox=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("checkbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("checkbox=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                } else if (str.contains("creator.gui.radiobutton")) {
                                    String fName = null;
                                    if (str.length() > 63) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                    str = br.readLine();
                                    String enumRef = str.substring(str.indexOf("q"));
                                    BufferedReader ibr = new BufferedReader(new FileReader(file));
                                    String str1 = ibr.readLine();
                                    while (true) {
                                        if (str1 != null) {
                                            if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                List<String> values = new ArrayList<String>();
                                                String str2 = ibr.readLine();
                                                str2 = ibr.readLine();
                                                while (!str2.contains("}")) {
                                                    if (str2.length() > 63&&str2.contains(";")) {
                                                        int firstIndex = str2.indexOf('"');
                                                        int lastIndex = str2.lastIndexOf('"');
//                                                        System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                        values.add(str2.substring(firstIndex + 1, lastIndex));
                                                    } else {
                                                        str2=ibr.readLine();
                                                        if(!str2.contains("deleted;"))
                                                        {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(firstIndex);
                                                            values.add(str2.substring(firstIndex + 1, lastIndex));
                                                        }
                                                    }
                                                    str2 = ibr.readLine();
                                                }
                                                map.put(fName, values);
                                            }
                                        } else {
                                            break;
                                        }
                                        str1 = ibr.readLine();
                                    }
                                    ibr.close();
                                } else if (str.contains("creator.gui.listbox")) {
                                    if (str.length() > 59) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("listbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("listbox=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("listbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("listbox=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                } else if (str.contains("creator.gui.calendar")) {
                                    if (str.length() > 60) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("calendar=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("calendar=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("calendar=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("calendar=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                }
                                str = br.readLine();
                            }
                        }
                    } else if (br.readLine().endsWith(formName+"\"")) {
                        if (br.readLine().contains("{")) {
                            str = br.readLine();
                            while (!str.contains("}")) {

                                if (str.contains("creator.gui.textbox")) {
                                    if (str.length() > 59) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("Textbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("Textbox=" + str.substring(firstIndex + 1, lastIndex));										
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("Textbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("Textbox=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                } else if (str.contains("creator.gui.dropdown")) {
                                    String fName = null;
                                    if (str.length() > 60) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("dropdown=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("dropdown=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("dropdown=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("dropdown=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                    str = br.readLine();
                                    String enumRef = str.substring(str.indexOf("q"));
                                    BufferedReader ibr = new BufferedReader(new FileReader(file));
                                    String str1 = ibr.readLine();
                                    while (true) {
                                        if (str1 != null) {
                                            if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                List<String> values = new ArrayList<String>();
                                                String str2 = ibr.readLine();
                                                str2 = ibr.readLine();
                                                while (!str2.contains("}")) {
                                                    if (str2.length() > 63&&str2.contains(";")) {
                                                        int firstIndex = str2.indexOf('"');
                                                        int lastIndex = str2.lastIndexOf('"');
//                                                        System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                        values.add(str2.substring(firstIndex + 1, lastIndex));
                                                    } else {
                                                        str2=ibr.readLine();
                                                        if(!str2.contains("deleted;"))
                                                        {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(firstIndex);
                                                            values.add(str2.substring(firstIndex + 1, lastIndex));
                                                        }
                                                    }
                                                    str2 = ibr.readLine();
                                                }
                                                map.put(fName, values);
                                            }
                                        } else {
                                            break;
                                        }
                                        str1 = ibr.readLine();
                                    }
                                    ibr.close();
                                } else if (str.contains("creator.gui.checkbox")) {
                                    if (str.length() > 60) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("checkbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("checkbox=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("checkbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("checkbox=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                } else if (str.contains("creator.gui.radiobutton")) {
                                    String fName = null;
                                    if (str.length() > 63) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        fName = str.substring(firstIndex + 1, lastIndex);
                                        list1.add("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("radiobutton=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                    str = br.readLine();
                                    String enumRef = str.substring(str.indexOf("q"));
                                    BufferedReader ibr = new BufferedReader(new FileReader(file));
                                    String str1 = ibr.readLine();
                                    while (true) {
                                        if (str1 != null) {
                                            if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                List<String> values = new ArrayList<String>();
                                                String str2 = ibr.readLine();
                                                str2 = ibr.readLine();
                                                while (!str2.contains("}")) {
                                                    if (str2.length() > 63&&str2.contains(";")) {
                                                        int firstIndex = str2.indexOf('"');
                                                        int lastIndex = str2.lastIndexOf('"');
//                                                        System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                        values.add(str2.substring(firstIndex + 1, lastIndex));
                                                    } else {
                                                        str2=ibr.readLine();
                                                        if(!str2.contains("deleted;"))
                                                        {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(firstIndex);
                                                            values.add(str2.substring(firstIndex + 1, lastIndex));
                                                        }
                                                    }
                                                    str2 = ibr.readLine();
                                                }
                                                map.put(fName, values);
                                            }
                                        } else {
                                            break;
                                        }
                                        str1 = ibr.readLine();
                                    }
                                    ibr.close();
                                } else if (str.contains("creator.gui.listbox")) {
                                    if (str.length() > 59) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("listbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("listbox=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("listbox=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("listbox=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                } else if (str.contains("creator.gui.calendar")) {
                                    if (str.length() > 60) {
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("calendar=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("calendar=" + str.substring(firstIndex + 1, lastIndex));
                                    } else {
                                        str = br.readLine();
                                        int firstIndex = str.indexOf('"');
                                        int lastIndex = str.lastIndexOf('"');
                                        list1.add("calendar=" + str.substring(firstIndex + 1, lastIndex));
//                                        System.out.println("calendar=" + str.substring(firstIndex + 1, lastIndex));
                                    }
                                }
                                str = br.readLine();
                            }
                        }
                    }
                } else {
                }
                str = br.readLine();
            }
//            	System.out.println(count);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list1;

    }

    public static void addField(String fieldName, String fieldType, String formName) {
        moduleName=ExcelReader.getModuleSelected();
        processName=ExcelReader.getProcessSelected();
        file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + processName + ".sl");
        try {

            fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null) {
                count++;
                if (str != null) {

                    if (str.contains("creator.gui.form") && str.contains(formName)) {
                        fileContent.append(str);
                        fileContent.append("\n");
                        str = br.readLine();
                        while (!str.contains("}")) {
                            fileContent.append(str);
                            fileContent.append("\n");
                            str = br.readLine();
                        }
                        if (fieldType.equals("textbox")) {
                            fileContent.append("		creator.gui.textbox qml" + randomAlphaNumeric(32) + " \"" + fieldName + "\"");
                            fileContent.append("\n");
                            fileContent.append("			type = String");
                            fileContent.append("\n");
                            fileContent.append("			status = dontcare;");
                        } else if (fieldType.equals("dropdown")) {
                            fileContent.append("		creator.gui.dropdown qml" + randomAlphaNumeric(32) + " \"" + fieldName + "\"");
                            fileContent.append("\n");
                            enumarte = "qml" + randomAlphaNumeric(32);
                            fileContent.append("			type = " + enumarte);
                            fileContent.append("\n");
                            fileContent.append("			status = dontcare;");
                        } else if (fieldType.equals("radiobutton")) {
                            fileContent.append("		creator.gui.radiobutton qml" + randomAlphaNumeric(32) + " \"" + fieldName + "\"");
                            fileContent.append("\n");
                            enumarte = "qml" + randomAlphaNumeric(32);
                            fileContent.append("			type = " + enumarte);
                            fileContent.append("\n");
                            fileContent.append("			status = dontcare;");
                        } else if (fieldType.equals("checkbox")) {
                            fileContent.append("		creator.gui.checkbox qml" + randomAlphaNumeric(32) + " \"" + fieldName + "\"");
                            fileContent.append("\n");
                            fileContent.append("			status = dontcare");
                            fileContent.append("\n");
                            fileContent.append("			checked = dontcare;");
                        } else if (fieldType.equals("listbox")) {
                            fileContent.append("		creator.gui.listbox qml" + randomAlphaNumeric(32) + " \"" + fieldName + "\"");
                            fileContent.append("\n");
                            fileContent.append("			status = dontcare");
                            fileContent.append("\n");
                            fileContent.append("			items = [ ];");
                        } else if (fieldType.equals("calendar")) {
                            fileContent.append("		creator.gui.calendar qml" + randomAlphaNumeric(32) + " \"" + fieldName + "\"");
                            fileContent.append("\n");
                            fileContent.append("			status = dontcare;");
                        }
                        fileContent.append("\n");
                        fileContent.append(str);

                    } else {
                        fileContent.append(str);
                    }
                    fileContent.append("\n");
                    str = br.readLine();
                }
            }
            if (enumarte != null) {
                fileContent.append("creator.enum " + enumarte + " \"" + fieldName + randomAlphaNumeric(3) + "\"");
                fileContent.append("\n");
                fileContent.append("{");
                fileContent.append("\n");
                fileContent.append("	creator.enumerationvalue qml" + randomAlphaNumeric(32) + " \"" + "value" + "\";");
                fileContent.append("\n");
                fileContent.append("}");
                fileContent.append("\n");
                enumarte = null;
            }

            fos = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fos);
            bw.write(fileContent.toString());
            br.close();
            bw.close();
            fos.close();
            fileContent = new StringBuilder();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static void modifyField(String fieldName, String newfieldName, String formName) {
        moduleName=ExcelReader.getModuleSelected();
        processName=ExcelReader.getProcessSelected();
        file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + processName + ".sl");
        try {

            fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null) {
                count++;
                if (str != null) {
                    if (str.contains("creator.gui.form") && str.contains(formName)) {
                        fileContent.append(str);
                        fileContent.append("\n");
                        str = br.readLine();
                        while (!str.contains("}")) {
                            if (str.contains(fieldName)) {
                                fileContent.append(str.replace(fieldName, newfieldName));
                                fileContent.append("\n");
                            } else {
                                fileContent.append(str);
                                fileContent.append("\n");
                            }
                            str = br.readLine();
                        }
                        fileContent.append(str);
                    } else {
                        fileContent.append(str);
                    }
                    fileContent.append("\n");
                    str = br.readLine();
                }
            }
            fos = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fos);
            bw.write(fileContent.toString());
            br.close();
            bw.close();
            fos.close();
            fileContent = new StringBuilder();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteField(String fieldName, String formName) {
        moduleName=ExcelReader.getModuleSelected();
        processName=ExcelReader.getProcessSelected();
        file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + processName + ".sl");
        try {

            fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null) {
                count++;
                if (str != null) {
                    if (str.contains("creator.gui.form") && str.contains(formName)) {
                        fileContent.append(str);
                        fileContent.append("\n");
                        str = br.readLine();
                        while (!str.contains("}")) {
                            if (str.contains(fieldName) && str.contains("creator.gui.calendar")) {
                                br.readLine();
                            } else if (str.contains(fieldName) && !str.contains("creator.gui.calendar")) {
                                br.readLine();
                                br.readLine();
                            } else {
                                fileContent.append(str);
                                fileContent.append("\n");
                            }
                            str = br.readLine();
                        }
                        fileContent.append(str);
                    } else {
                        fileContent.append(str);
                    }
                    fileContent.append("\n");
                    str = br.readLine();
                }
            }
            fos = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fos);
            bw.write(fileContent.toString());
            br.close();
            bw.close();
            fos.close();
            fileContent = new StringBuilder();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void modifyEnumValues(String fieldName, List<String> values, String formName) {
        moduleName=ExcelReader.getModuleSelected();
        processName=ExcelReader.getProcessSelected();
        file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + processName + ".sl");
        try {
            fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null) {
                count++;
                if (str != null) {
                    
                    if (str.contains("creator.enum") && str.contains(fieldName)) {
                        fileContent.append(str);
                        fileContent.append("\n");
                        str = br.readLine();
                        fileContent.append(str);
                        fileContent.append("\n");
                        for (int i = 0; i < values.size(); i++) {
                            str = "	creator.enumerationvalue qml" + randomAlphaNumeric(32) + " \"" + values.get(i) + "\";";
                            fileContent.append(str);
                            fileContent.append("\n");
                        }
                        str = br.readLine();
                        while (!str.contains("}")) {
                            str = br.readLine();
                        }
                        fileContent.append(str);
                    } else {
                        fileContent.append(str);
                    }
                    fileContent.append("\n");
                    str = br.readLine();
                }
            }
            fos = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fos);
            bw.write(fileContent.toString());
            br.close();
            bw.close();
            fos.close();
            fileContent = new StringBuilder();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getEnumValues(String fieldName, String formName) {
        moduleName=ExcelReader.getModuleSelected();
        processName=ExcelReader.getProcessSelected();
        file = new File(PropertiesConfig.getProperty(moduleName.replaceAll(" ", "_")) + processName + "\\" + processName + ".sl");
        List<String> list2 = null;
        try {
            fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null) {
                count++;
                if (str.contains("creator.gui.form")) {
                    if (str.contains(formName)) {
                        if (br.readLine().contains("{")) {
                            str = br.readLine();
                            while (!str.contains("}")) {
                                if ((str.contains("creator.gui.dropdown")||str.contains("creator.gui.radiobutton"))&&str.contains(fieldName)) {
                                    
                                    str = br.readLine();
                                    String enumRef=str.substring(str.indexOf("q"));
                                    BufferedReader ibr = new BufferedReader(new FileReader(file));
                                    String str1 = ibr.readLine();
                                    while (true) {
                                        if (str1 != null) {
                                            if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                String str2 = ibr.readLine();
                                                str2 = ibr.readLine();
                                                list2 = new ArrayList<String>();
                                                while (!str2.contains("}")) {
                                                    if (str2.length() > 63&&str2.contains(";")) {
                                                        int firstIndex = str2.indexOf('"');
                                                        int lastIndex = str2.lastIndexOf('"');
//                                                        System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                        list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                    } else {
                                                        str2=ibr.readLine();
                                                        if(!str2.contains("deleted;"))
                                                        {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(firstIndex);
                                                            list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                        }
                                                    }
                                                    str2 = ibr.readLine();
                                                }
                                            }
                                        } else {
                                            break;
                                        }
                                        str1 = ibr.readLine();
                                    }
                                    ibr.close();
                                } else if ((str.contains("creator.gui.dropdown")||str.contains("creator.gui.radiobutton"))&&!str.contains(fieldName)) {
                                    if(br.readLine().contains(fieldName))
                                    {
                                        str = br.readLine();
                                        String enumRef=str.substring(str.indexOf("q"));
                                        BufferedReader ibr = new BufferedReader(new FileReader(file));
                                        String str1 = ibr.readLine();
                                        while (true) {
                                            if (str1 != null) {
                                                if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                    String str2 = ibr.readLine();
                                                    str2 = ibr.readLine();
                                                    list2 = new ArrayList<String>();
                                                    while (!str2.contains("}")) {
                                                        if (str2.length() > 63&&str2.contains(";")) {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                            list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                        } else {
                                                            str2=ibr.readLine();
                                                            if(!str2.contains("deleted;"))
                                                            {
                                                                int firstIndex = str2.indexOf('"');
                                                                int lastIndex = str2.lastIndexOf('"');
//                                                                System.out.println(firstIndex);
                                                                list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                            }
                                                        }
                                                        str2 = ibr.readLine();
                                                    }
                                                }
                                                
                                            } else {
                                                break;
                                            }
                                            str1 = ibr.readLine();
                                        }
                                        ibr.close();
                                    }
                                }
                                str = br.readLine();
                            }
                        }
                    } else {
                        str = br.readLine();
                        if (br.readLine().contains("{")) {
                            str = br.readLine();
                            while (!str.contains("}")) {
                                if ((str.contains("creator.gui.dropdown")||str.contains("creator.gui.radiobutton"))&&str.contains(fieldName)) {
                                    
                                    str = br.readLine();
                                    String enumRef=str.substring(str.indexOf("q"));
                                    BufferedReader ibr = new BufferedReader(new FileReader(file));
                                    String str1 = ibr.readLine();
                                    while (true) {
                                        if (str1 != null) {
                                            if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                String str2 = ibr.readLine();
                                                str2 = ibr.readLine();
                                                list2 = new ArrayList<String>();
                                                while (!str2.contains("}")) {
                                                    if (str2.length() > 63&&str2.contains(";")) {
                                                        int firstIndex = str2.indexOf('"');
                                                        int lastIndex = str2.lastIndexOf('"');
//                                                        System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                        list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                    } else {
                                                        str2=ibr.readLine();
                                                        if(!str2.contains("deleted;"))
                                                        {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(firstIndex);
                                                            list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                        }
                                                    }
                                                    str2 = ibr.readLine();
                                                }
                                            }
                                        } else {
                                            break;
                                        }
                                        str1 = ibr.readLine();
                                    }
                                    ibr.close();
                                } else if ((str.contains("creator.gui.dropdown")||str.contains("creator.gui.radiobutton"))&&!str.contains(fieldName)) {
                                    if(br.readLine().contains(fieldName))
                                    {
                                        str = br.readLine();
                                        String enumRef=str.substring(str.indexOf("q"));
                                        BufferedReader ibr = new BufferedReader(new FileReader(file));
                                        String str1 = ibr.readLine();
                                        while (true) {
                                            if (str1 != null) {
                                                if (str1.contains("creator.enum") && str1.contains(enumRef)) {
                                                    String str2 = ibr.readLine();
                                                    str2 = ibr.readLine();
                                                    list2 = new ArrayList<String>();
                                                    while (!str2.contains("}")) {
                                                        if (str2.length() > 63&&str2.contains(";")) {
                                                            int firstIndex = str2.indexOf('"');
                                                            int lastIndex = str2.lastIndexOf('"');
//                                                            System.out.println(str2.substring(firstIndex + 1, lastIndex));
                                                            list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                        } else {
                                                            str2=ibr.readLine();
                                                            if(!str2.contains("deleted;"))
                                                            {
                                                                int firstIndex = str2.indexOf('"');
                                                                int lastIndex = str2.lastIndexOf('"');
//                                                                System.out.println(firstIndex);
                                                                list2.add(str2.substring(firstIndex + 1, lastIndex));
                                                            }
                                                        }
                                                        str2 = ibr.readLine();
                                                    }
                                                }
                                                
                                            } else {
                                                break;
                                            }
                                            str1 = ibr.readLine();
                                        }
                                        ibr.close();
                                    }
                                }
                                str = br.readLine();
                            }
                        }
                    }
                } else {
                }
                str = br.readLine();
            }
            //	System.out.println(count);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list2;

    }
}
