/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.banking.controllers;

import com.banking.actions.CreateExcel;
import com.banking.actions.ExportToCSV;
import com.banking.actions.SLFileExtractor;
import com.banking.utils.ExcelReader;
import com.banking.utils.LoginInfo;
import com.banking.utils.PropertiesConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.PopupBuilder;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author KS102234
 */
public class FieldsController implements Initializable {
    @FXML
    private AnchorPane fieldsAnchorPane;
    @FXML
    private Label titleLable;
    @FXML
    private Button backButton;
    @FXML
    private TreeView<?> treeActions;
    @FXML
    private GridPane formGrid;
    @FXML
    private TableView<TableCols> fieldsTable;
    @FXML
    private TableColumn<?, ?> fieldNameCol;
    @FXML
    private TableColumn<?, ?> fieldTypeCol;
    
     ObservableList olist=FXCollections.observableArrayList();
    @FXML
    private Label statusLabel;
    
    public HBox tableHBox;
    public static String formSelected;
    private boolean noForm=false;
    private String moduleName;
    @FXML
    private TableColumn<TableCols, Boolean> combinationsCol;
 
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        moduleName=ExcelReader.getModuleSelected();
        fieldsAnchorPane.setStyle("-fx-background-image: url(\"/com/banking/css/"+moduleName.replace(" ","")+".jpg\");");
        AnchorPane.setTopAnchor(fieldsAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(fieldsAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(fieldsAnchorPane, 0.0);
        AnchorPane.setRightAnchor(fieldsAnchorPane, 0.0);
        titleLable.setText("Fields for "+ExcelReader.getProcessSelected()+" Business Process");
        titleLable.setTextFill(Color.web("red"));
        titleLable.setFont(Font.font(16));
        statusLabel.setTextFill(Color.web("blue"));
        TreeItem rootItem = new TreeItem<> ("Actions");
        rootItem.setExpanded(true);
        treeActions.setShowRoot(false);
        treeActions.setRoot(rootItem);
        processActionsTree(rootItem);
        fieldActionsTree(rootItem);
        excelActionsTree(rootItem);
       // tableView("Add New Customer");
       if(noForm)
       {
           Popup pop = PopupBuilder.create().content(new Label("Update Profile/Profile is not required")).width(50).height(100).autoFix(true).build();
           Stage s=new Stage();
           s.setHeight(150);
           s.setWidth(250);
           s.show();
           pop.show(s);
       }
        
    }
    public void fieldActionsTree(TreeItem rootItem)
    {
        TreeItem<Hyperlink> fieldActions = new TreeItem<>(new Hyperlink("Field Actions"));
        rootItem.getChildren().add(fieldActions);
        Hyperlink add=new Hyperlink("Add Field");
        TreeItem<Hyperlink> addField=new TreeItem<>(add);
        fieldActions.getChildren().add(addField);
        Hyperlink delete=new Hyperlink("Delete Field");
        TreeItem<Hyperlink> deleteField=new TreeItem<>(delete);
        fieldActions.getChildren().add(deleteField);
        Hyperlink edit=new Hyperlink("Edit Field");
        TreeItem<Hyperlink> editField=new TreeItem<>(edit);
        fieldActions.getChildren().add(editField);
        Hyperlink addValues=new Hyperlink("Add Enum Values");
        TreeItem<Hyperlink> addEnumValues=new TreeItem<>(addValues);
        fieldActions.getChildren().add(addEnumValues);
        fieldActions.setExpanded(true);
        
        add.setOnAction(add());
        delete.setOnAction(delete());
        edit.setOnAction(edit());
        addValues.setOnAction(addValues());
    }
    public void processActionsTree(TreeItem rootItem)
    {
        TreeItem<Hyperlink> processActions = new TreeItem<>(new Hyperlink("Process Actions"));
        rootItem.getChildren().add(processActions);
        List<String> formsList=SLFileExtractor.getFormsLilst("");
        for(String s:formsList)
        {
            Hyperlink form=new Hyperlink(s);
            TreeItem<Hyperlink> formItem=new TreeItem<>(form);
            processActions.getChildren().add(formItem);
            form.setOnAction((ActionEvent event) -> {
                formSelected=s;
               tableView(s);
               if(!olist.isEmpty())
                {
                    formGrid.getChildren().removeAll(olist);
                }
            });
        }
        processActions.setExpanded(true);
        if(formsList.isEmpty())
        {
            noForm=true;
        }
    }
    public void excelActionsTree(TreeItem rootItem)
    {
        TreeItem<Hyperlink> excelActions = new TreeItem<>(new Hyperlink("Excel Actions"));
        rootItem.getChildren().add(excelActions);
        Hyperlink create=new Hyperlink("Create Excel");
        TreeItem<Hyperlink> createExcel=new TreeItem<>(create);
        excelActions.getChildren().add(createExcel);
        Hyperlink open=new Hyperlink("Open");
        TreeItem<Hyperlink> openExcel=new TreeItem<>(open);
        excelActions.getChildren().add(openExcel);
        Hyperlink export=new Hyperlink("Export To CSV");
        TreeItem<Hyperlink> exportToCSV=new TreeItem<>(export);
        excelActions.getChildren().add(exportToCSV);
        excelActions.setExpanded(true);
        create.setOnAction(create());
        open.setOnAction(open());
        export.setOnAction(export());
    }
    public void createTable()
    {
        File file=new File("d://Sample.xlsx");
        try {
            InputStream is=new FileInputStream(file);
            XSSFWorkbook workbook=new XSSFWorkbook(is);
            XSSFSheet sheet=workbook.getSheetAt(0);
            XSSFRow r=sheet.getRow(0);
            int colCount=r.getPhysicalNumberOfCells();
            List<String[]> l=new ArrayList();
           
        TableView<Integer> t=new TableView();
        t.setEditable(true);
        for (int i = 0; i < sheet.getPhysicalNumberOfRows()-1; i++) {
            t.getItems().add(i);
        }
        for(int col=0;col<colCount;col++)
        {
            String[] s=new String[sheet.getPhysicalNumberOfRows()-1];
            for(int j=1;j<sheet.getPhysicalNumberOfRows();j++)
            {  
                XSSFRow row=sheet.getRow(j);
//                System.out.println("cell "+j+" "+col);
                XSSFCell cell=row.getCell(col,Row.RETURN_NULL_AND_BLANK);
//                System.out.println("cell "+j+" "+col);
                if ((cell == null) || (cell.equals("")) || (cell.getCellType() == cell.CELL_TYPE_BLANK))
                {
                    s[j-1]="";
                }
                else
                {
                    switch (cell.getCellType()) {
                        case 0:
                            s[j-1]= Double.toString(cell.getNumericCellValue());
                            break;
                        case 1:
                            s[j-1]=cell.getStringCellValue();
                            break;
                        case 2:
                            s[j-1]= cell.getCellFormula();
                            break;
                        case 4:
                            s[j-1]=Boolean.toString(cell.getBooleanCellValue());
                            break;
                        case 3:
                            s[j-1]="x";
                            break;
                        case 5:
                            s[j-1]=cell.getErrorCellString();
                            break;
                        default:
                            s[j-1]="";
                    }
                }
            }
             l.add(s);
        }
        for(int i=0;i<r.getPhysicalNumberOfCells();i++)
        {
            TableColumn<Integer, String> tc=new TableColumn();
            tc.setText(r.getCell(i).getStringCellValue());
            String[] s1=l.get(i);
            tc.setCellValueFactory(cellData -> {
            Integer row = cellData.getValue();
//                System.out.println(row+"   "+ s1[row]);
            return new ReadOnlyStringWrapper(s1[row]);
        });
            t.getColumns().add(tc);
        }
        
//            System.out.println(l.size());
        tableHBox.getChildren().add(t);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
    public void tableView(String formName)
    {
        List<String> fieldsList=SLFileExtractor.readSL(formName,ExcelReader.getModuleSelected(),ExcelReader.getProcessSelected());
//        System.out.println(fieldsList.size());
        
        
       // List<String> fieldsList=SLFileExtractor.readSL(formName,ExcelReader.getModuleSelected(),ExcelReader.getProcessSelected());
       // System.out.println(fieldsList.size());
        ObservableList<TableCols> ol= FXCollections.observableArrayList();
        fieldsList.stream().map((s) -> {
            String fieldName=s.substring(s.indexOf("=")+1);
            String fieldType=s.substring(0, s.indexOf("="));
            TableCols tc=new TableCols(fieldName,fieldType);
            return tc;
        }).forEach((tc) -> {
            ol.add(tc);
        });
        
        fieldsTable.setItems(ol);
        fieldNameCol.setCellValueFactory(new PropertyValueFactory<>("fieldName"));  
        fieldTypeCol.setCellValueFactory(new PropertyValueFactory<>("fieldType"));
        combinationsCol.setCellValueFactory(new PropertyValueFactory<>("combinations"));
        combinationsCol.setCellFactory(CheckBoxTableCell.forTableColumn(combinationsCol));
        combinationsCol.setEditable(true);
        fieldsTable.setEditable(true);
       // fieldsTable.getItems().get(1).setCombinations(true);
    }
    public EventHandler<ActionEvent> add()
    {
        EventHandler<ActionEvent> actionEvent=(ActionEvent event) -> {
            if(!olist.isEmpty())
            {
                formGrid.getChildren().removeAll(olist);
            }
            Label fieldNameLabel=new Label("Field Name");
            formGrid.add(fieldNameLabel, 0, 0);
            olist.add(fieldNameLabel);
            TextField tf=new TextField();
            olist.add(tf);
            formGrid.add(tf, 1, 0);
            Label fieldTypeLabel=new Label("Field Type");
            olist.add(fieldTypeLabel);
            formGrid.add(fieldTypeLabel, 0, 1);
            ComboBox cb=new ComboBox();
            olist.add(cb);
            cb.getItems().addAll(
                    "textbox",
                    "dropdown",
                    "checkbox",
                    "radiobutton",
                    "listbox",
                    "calendar");
            formGrid.add(cb, 1, 1);
            Button b=new Button("Add");
            olist.add(b);
            formGrid.add(b, 1, 2);
            b.setOnAction((ActionEvent event1) -> {
                try {
                    SLFileExtractor.addField(tf.getText(), cb.getValue().toString(), formSelected);
                    fieldsAnchorPane.getChildren().remove(0);
                    Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/Fields.fxml"));
                    fieldsAnchorPane.getChildren().add(n);
                } catch (IOException ex) {
                    Logger.getLogger(FieldsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        };
        return actionEvent;
    }
    public EventHandler<ActionEvent> delete()
    { 
        EventHandler<ActionEvent> actionEvent=(ActionEvent event) -> {
            if(!olist.isEmpty())
            {
                formGrid.getChildren().removeAll(olist);
            }
            Label fieldNameLabel=new Label("Field Name");
            olist.add(fieldNameLabel);
            formGrid.add(fieldNameLabel, 0, 0);
            ComboBox cb=new ComboBox();
            olist.add(cb);
            List<String> list=SLFileExtractor.readSL(formSelected,ExcelReader.getModuleSelected(),ExcelReader.getProcessSelected());
            list.stream().forEach((s1) -> {
                cb.getItems().add(s1.substring(s1.indexOf("=")+1));
            });
            formGrid.add(cb, 1, 0);
            Button b=new Button("Delete");
            olist.add(b);
            formGrid.add(b, 1, 1);
            b.setOnAction((ActionEvent event1) -> {
                try {
                    SLFileExtractor.deleteField(cb.getValue().toString(), formSelected);
                    fieldsAnchorPane.getChildren().remove(0);
                    Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/Fields.fxml"));
                    fieldsAnchorPane.getChildren().add(n);
                } catch (IOException ex) {
                    Logger.getLogger(FieldsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        };
        return actionEvent;
    }
    public EventHandler<ActionEvent> edit()
    {
        EventHandler<ActionEvent> actionEvent=(ActionEvent event) -> {
            if(!olist.isEmpty())
            {
                formGrid.getChildren().removeAll(olist);
            }
            Label selectFieldLabel=new Label("Select Field");
            formGrid.add(selectFieldLabel, 0, 0);
            olist.add(selectFieldLabel);
            ComboBox cb=new ComboBox();
            olist.add(cb);
            List<String> list=SLFileExtractor.readSL(formSelected,ExcelReader.getModuleSelected(),ExcelReader.getProcessSelected());
            list.stream().forEach((s1) -> {
                cb.getItems().add(s1.substring(s1.indexOf("=")+1));
            });
            formGrid.add(cb, 1, 0);
            Label newFieldLabel=new Label("New Field");
            formGrid.add(newFieldLabel, 0, 1);
            olist.add(newFieldLabel);
            TextField tf=new TextField();
            olist.add(tf);
            formGrid.add(tf, 1, 1);
            Button b=new Button("Edit");
            olist.add(b);
            formGrid.add(b, 1, 2);
            b.setOnAction((ActionEvent event1) -> {
                try {
                    SLFileExtractor.modifyField(cb.getValue().toString(),tf.getText(), formSelected);
                    fieldsAnchorPane.getChildren().remove(0);
                    Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/Fields.fxml"));
                    fieldsAnchorPane.getChildren().add(n);
                } catch (IOException ex) {
                    Logger.getLogger(FieldsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        };
        return actionEvent;
    }
    public EventHandler<ActionEvent> addValues()
    {
        EventHandler<ActionEvent> actionEvent=(ActionEvent event) -> {
            if(!olist.isEmpty())
            {
                formGrid.getChildren().removeAll(olist);
            }
            Label selectFieldLabel=new Label("Select Field");
            formGrid.add(selectFieldLabel, 0, 0);
            olist.add(selectFieldLabel);
            ComboBox cb=new ComboBox();
            olist.add(cb);
            List<String> list=SLFileExtractor.readSL(formSelected,ExcelReader.getModuleSelected(),ExcelReader.getProcessSelected());
            
            
            list.stream().filter((s1) -> (s1.contains("dropdown")||s1.contains("radiobutton"))).forEach((s1) -> {
                cb.getItems().add(s1.substring(s1.indexOf("=")+1));
            });
            
            
            formGrid.add(cb, 1, 0);
            Label enumValues=new Label("Enum Values");
            formGrid.add(enumValues, 0, 1);
            olist.add(enumValues);
            TextArea ta=new TextArea();
            olist.add(ta);
            formGrid.add(ta, 1, 1);
            cb.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                List<String> values=SLFileExtractor.getEnumValues(cb.getValue().toString(), formSelected);
//                System.out.println(cb.getValue().toString()+" selected field");
//                System.out.println(values.size());
                StringBuilder str=new StringBuilder();
                for(int i=0;i<values.size();i++)
                {
                    str.append(values.get(i));
                    if(i!=values.size()-1)
                    {
                        str.append("\n");
                    }
                }
                ta.setText(str.toString());
            });
            Button b=new Button("Update");
            olist.add(b);
            formGrid.add(b, 1, 2);
            b.setOnAction((ActionEvent event1) -> {
                try {
                    String[] str1=ta.getText().split("\n");
                    List<String> list12=new ArrayList<>();
                    list12.addAll(Arrays.asList(str1));
                    SLFileExtractor.modifyEnumValues(cb.getValue().toString(),list12, formSelected);
                    fieldsAnchorPane.getChildren().remove(0);
                    Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/Fields.fxml"));
                    fieldsAnchorPane.getChildren().add(n);
                } catch (IOException ex) {
                    Logger.getLogger(FieldsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        };
        return actionEvent;
    } 
    
    public EventHandler<ActionEvent> create()
    {
       
        EventHandler<ActionEvent> event=(ActionEvent event1) -> {
            List<String> combiList=new ArrayList<>();
            for (TableCols p : fieldsTable.getItems())
            {
                if(p.isCombinations())
                {
                    combiList.add(p.getFieldName());
                }
            }
            if(!olist.isEmpty())
            {
                formGrid.getChildren().removeAll(olist);
            }
            CreateExcel.createNewExcel(ExcelReader.getModuleSelected(),ExcelReader.getProcessSelected(),formSelected);
            CreateExcel.modifyExcel(ExcelReader.getModuleSelected(),ExcelReader.getProcessSelected(),combiList,formSelected);
            statusLabel.setText("Excel Data File Created Successfully");
        };
        return event;
    }
    public EventHandler<ActionEvent> open()
    {
        if(!olist.isEmpty())
            {
                formGrid.getChildren().removeAll(olist);
            }
        EventHandler<ActionEvent> event=(ActionEvent event1) -> {
            try {
                File vbsFile=new File(System.getProperty("user.dir")+"\\excel.vbs");
                if(!vbsFile.exists())
                {
//                    System.out.println("OPen");
                    Runtime.getRuntime().exec("jar xf TDAT.jar excel.vbs");
                    Thread.sleep(5000);
                }
                String path=System.getProperty("user.direc")+"\\"+ExcelReader.getModuleSelected()+"\\"+ExcelReader.getProcessSelected()+"\\"+formSelected+".xlsx";
//                System.out.println("Executing");
                Runtime.getRuntime().exec(
                        "wscript "
                                + "excel.vbs "
                                + path.replace(" ", "^") + " "
                                + ExcelReader.getProcessSelected().replace(" ", "^"));
//                System.out.println("Executed");
            } catch (IOException e1) {
            } catch (InterruptedException ex) {
                Logger.getLogger(FieldsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        return event;
    }
    
    public EventHandler<ActionEvent> export()
    {
        if(!olist.isEmpty())
            {
                formGrid.getChildren().removeAll(olist);
            }
        EventHandler<ActionEvent> event=(ActionEvent event1) -> {
            File f=new File(PropertiesConfig.getProperty(ExcelReader.getModuleSelected().replace(" ", "_"))+ExcelReader.getProcessSelected()+"\\csv");
            if(!f.isDirectory())
            {
                f.mkdir();
            }
            ExportToCSV.convertToCSV(PropertiesConfig.getProperty(ExcelReader.getModuleSelected().replace(" ", "_"))+ExcelReader.getProcessSelected()+"\\csv\\", formSelected);
            statusLabel.setText("Exported Successfully");
        };
        return event;
    }
    public static class TableCols
    {
        private final SimpleStringProperty fieldName;
        private final SimpleStringProperty fieldType;
        private final SimpleBooleanProperty combinations;
        
        private TableCols(String fieldName, String fieldType) {
            this.fieldName = new SimpleStringProperty(fieldName);
            this.fieldType = new SimpleStringProperty(fieldType);
            this.combinations=new SimpleBooleanProperty(false);
        }
        public String getFieldName() {
            return fieldName.get();
        }
        
        public String getFieldType() {
            return fieldType.get();
        }
        public void setFieldName(String fName) {
            this.fieldName.set(fName);
        }
        public void setFieldType(String fType) {
            this.fieldType.set(fType);
        }
        public void setCombinations(boolean combi) {
            this.combinations.set(combi);
        }
        public boolean isCombinations() {
            return combinations.get();
        }
        public StringProperty fieldNameProperty() {
            return fieldName;
        }
        public StringProperty fieldTypeProperty() {
            return fieldType;
        }
        public BooleanProperty combinationsProperty() {
            return combinations;
        }
        
    }
    
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        fieldsAnchorPane.getChildren().remove(0);
        LoginInfo.setStat("process");
        Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/ModulesList.fxml"));
        fieldsAnchorPane.getChildren().add(n);
    }
    
    
}