/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.banking.controllers;

import com.banking.utils.ExcelReader;
import com.banking.utils.LoginInfo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.commons.logging.Log;

/**
 * FXML Controller class
 *
 * @author ks102234
 */
public class ModulesListController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public AnchorPane modulesListAnchorPane;
    @FXML
    private VBox modulesListVBox;
    
   // ExcelReader er=new ExcelReader();
    List<Button> buttonList=null;
    
    @FXML
    private Button backButton;
    
    String status=null;
    Map subProcess=null;
    @FXML
    private Label headerLabel;
    @FXML
    private HBox hBox;
    @FXML
    private StackPane vboxSP;
    private String moduleName;
    StackPane sp=new StackPane();
    VBox vb=new VBox();
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("$$$$-ModulesListController");
        modulesListAnchorPane.setStyle("-fx-background-image: url(\"/com/banking/css/CoreBanking1.jpg\");");
        vb.setPrefHeight(200);
        vb.setPrefWidth(300);
        vb.setMaxWidth(VBox.USE_PREF_SIZE);
        sp.getChildren().add(vb);
        sp.setPrefHeight(200);
        sp.setPrefWidth(300);
        vb.setAlignment(Pos.CENTER);
        sp.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        headerLabel.setText("Banking Modules");
        headerLabel.setTextFill(Color.web("red"));
        headerLabel.setFont(Font.font(16));
        AnchorPane.setTopAnchor(modulesListAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(modulesListAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(modulesListAnchorPane, 0.0);
        AnchorPane.setRightAnchor(modulesListAnchorPane, 0.0);
        if(LoginInfo.getStat().equals("process"))
        {
            moduleName=ExcelReader.getModuleSelected();
           modulesListAnchorPane.setStyle("-fx-background-image: url(\"/com/banking/css/"+moduleName.replace(" ","")+".jpg\");"); 
            LoginInfo.setStat("module");
            if(ExcelReader.getProcessList(ExcelReader.getModuleSelected()).size()>10)
            {
                hBox.getChildren().add(sp);
            }
            displayProcessList(ExcelReader.getProcessList(ExcelReader.getModuleSelected()));
        }
        else
        {    
            displayModulesList();
        }
    }
    public void displayModulesList()
    {
        
        List<String> list=ExcelReader.getModulesList();
        System.out.println("Module count:"+list.size());
        
        buttonList=new ArrayList<>();
        status="module";
        
       
        int split=0;
        if(list.size()>10)
        {
            hBox.getChildren().add(sp);
            split=list.size()/2;
        }
        int i=1;
        for(String s:list)
        {   
           // System.out.println("S"+s);
            Button b=new Button();
            if(i>split)
            { 
                System.out.println("i>split"+s);
                b.setText(s);
                b.setMaxWidth(200);
                b.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                b.setFont(Font.font("Times New Roman", 16));
                b.setStyle("-fx-font-weight:bold");
                buttonList.add(b);
                vb.getChildren().add(b);
            }
            else
            {
                System.out.println("i<split"+s);
              //  Button b=new Button(s);
                b.setMaxWidth(200);
                b.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                b.setFont(Font.font("Times New Roman", 16));
                b.setStyle("-fx-font-weight:bold");
                buttonList.add(b);
                modulesListVBox.getChildren().add(b); 
            }
            
            b.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    System.out.println(b.getText());
                    ExcelReader.setModuleSelected(b.getText());
                    modulesListVBox.getChildren().removeAll(buttonList);
                    vb.getChildren().removeAll(buttonList);
                    moduleName=b.getText();
                    modulesListAnchorPane.setStyle("-fx-background-image: url(\"/com/banking/css/"+moduleName.replace(" ","")+".jpg\");");
                    displayProcessList(ExcelReader.getProcessList(ExcelReader.getModuleSelected()));
                }
            });
//            b.setOnAction( (ActionEvent event) -> {
//                System.out.println(b.getText());
//                    ExcelReader.setModuleSelected(b.getText());
//                    modulesListVBox.getChildren().removeAll(buttonList);
//                    vb.getChildren().removeAll(buttonList);
//                    moduleName=b.getText();
//                    modulesListAnchorPane.setStyle("-fx-background-image: url(\"/com/banking/css/"+moduleName.replace(" ","")+".jpg\");");
//                    displayProcessList(ExcelReader.getProcessList(ExcelReader.getModuleSelected()));
//                });
            i++;
        }
    }
    public void displayProcessList(List<String> processList)
    {
        status="process";
        buttonList=new ArrayList<>();
        headerLabel.setText("Business Processes for "+ExcelReader.getModuleSelected());
        System.out.println(ExcelReader.getModuleSelected());
        int len=0;
        int split=0;
        if(processList.size()>10)
        {
            split=processList.size()/2;
        }
        else
        {
            hBox.getChildren().remove(sp);
        }
        for(String s:processList)
        {
            if(s.length()>len)
            {
                len=s.length();
            }
        }
        int splitCount=1;
        for(String s:processList)
        {
            Button b=new Button(s);
            if(splitCount>split&&processList.size()>10)
            {
                b.setMaxWidth(150);
                buttonList.add(b);
                vb.getChildren().add(b);
            }     
            else
            {
                b.setMaxWidth(150);
                buttonList.add(b);
                modulesListVBox.getChildren().add(b);
            }
            splitCount++;
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event)
                {
                    ExcelReader.setProcessSelected(b.getText());
                    System.out.println("process Action"+b.getText());
                    subProcess=ExcelReader.getSubProcess(ExcelReader.getModuleSelected());
                    if(subProcess.containsKey(b.getText()))
                    {
                        //status="subProcess";
                        headerLabel.setText("Sub Processes for "+ExcelReader.getProcessSelected());
                        modulesListVBox.getChildren().removeAll(buttonList);
                        vb.getChildren().removeAll(buttonList);
                        String ar[]=subProcess.get(b.getText()).toString().split("\n");
                        int sflen=0;
                        for(int l=0;l<ar.length;l++)
                        {
                            if(ar[l].length()>sflen)
                            {
                                sflen=ar[l].length();
                            }
                        }
                        for(int l=0;l<ar.length;l++)
                        {
                            Button spb=new Button(ar[l]);
                            spb.setMaxWidth(150);
                            modulesListVBox.getChildren().add(spb);
                            spb.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    ExcelReader.setSubFunctionSelected(spb.getText());                                   
                                    modulesListAnchorPane.getChildren().remove(0);
                                    try {
                                        Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/Fields.fxml"));
                                        modulesListAnchorPane.getChildren().add(n);
                                    } catch (IOException ex) {
                                    }
                                }
                            });
                        }
                                
                    }
                    else
                    {
                        modulesListAnchorPane.getChildren().remove(0);
                        try {
                            Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/Fields.fxml"));
                            modulesListAnchorPane.getChildren().add(n);
                        } catch (IOException ex) {}
                    }
                }
            });
        
        }
        
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException 
    {
        modulesListAnchorPane.getChildren().remove(0);
        if(status.equals("module"))
        {
            Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/DataFolder.fxml"));
            modulesListAnchorPane.getChildren().add(n);
        }
        else if(status.equals("process"))
        {
            Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/ModulesList.fxml"));
            modulesListAnchorPane.getChildren().add(n);
        }
    }
    
}
