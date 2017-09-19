/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banking.controllers;

import com.banking.utils.ExcelReader;
import com.banking.utils.LoginInfo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToolbar;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author AV00373090
 */
public class ModuleListController implements Initializable {

    @FXML
    JFXListView<Label> jfxListView;

    @FXML
    AnchorPane JFXAnchorPane;

    @FXML
    private VBox modulesListVBox;
    @FXML
    private HBox hBox;
    @FXML
    private StackPane vboxSP;
    StackPane sp = new StackPane();
    VBox vb = new VBox();
    List<JFXButton> buttonList = null;

    @FXML
    private JFXButton homeButton;
    
    @FXML
    JFXToolbar jfxToolbar;

    String status = null;
    private String moduleName;
    Map subProcess = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        vb.setMaxWidth(VBox.USE_PREF_SIZE);
        vb.setSpacing(10);
        sp.getChildren().add(vb);
        sp.setPrefHeight(200);
        sp.setPrefWidth(300);
        vb.setAlignment(Pos.CENTER);
        sp.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(JFXAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(JFXAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(JFXAnchorPane, 0.0);
        AnchorPane.setRightAnchor(JFXAnchorPane, 0.0);

        if (LoginInfo.getStat().equals("process")) {
            moduleName = ExcelReader.getModuleSelected();
            // JFXAnchorPane.setStyle("-fx-background-image: url(\"/com/banking/css/"+moduleName.replace(" ","")+".jpg\");"); 
            LoginInfo.setStat("module");

            displayProcessList(ExcelReader.getProcessList(ExcelReader.getModuleSelected()));
        } else {
            displayModulesList();
        }

        JFXAnchorPane.getStylesheets().add(ModuleListController.class.getResource("/com/banking/css/jfoenix-components.css").toExternalForm());

    }

    public void displayModulesList() {

        List<String> list = ExcelReader.getModulesList();
        System.out.println("Module count:" + list.size());

       // homeButton.setVisible(false);

        buttonList = new ArrayList<>();
        status = "module";

        int split = 0;
        if (list.size() > 10) {
            hBox.getChildren().add(sp);
            split = list.size() / 2;
        }
        int i = 1;
        for (String s : list) {
            // System.out.println("S"+s);
            JFXButton button = new JFXButton();
            button.getStyleClass().add("button-raised");

            if (i > split) {
                System.out.println("i>split" + s);
                button.setText(s);
                buttonList.add(button);
                vb.getChildren().add(button);
            } else {
                System.out.println("i<split" + s);
                // Button b=new Button(s);
                button.setText(s);
                buttonList.add(button);
                modulesListVBox.getChildren().add(button);
            }

            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    System.out.println(button.getText());
                    ExcelReader.setModuleSelected(button.getText());
                    modulesListVBox.getChildren().removeAll(buttonList);
                    vb.getChildren().removeAll(buttonList);
                    moduleName = button.getText();
                    // modulesListAnchorPane.setStyle("-fx-background-image: url(\"/com/banking/css/"+moduleName.replace(" ","")+".jpg\");");
                    displayProcessList(ExcelReader.getProcessList(ExcelReader.getModuleSelected()));
                }
            });
            i++;
        }

    }

    public void displayProcessList(List<String> processList) {

        homeButton.setVisible(true);
        homeButton.getStyleClass().add("button-home-raised");

        status = "process";
        buttonList = new ArrayList<>();
        //headerLabel.setText("Business Processes for "+ExcelReader.getModuleSelected());
        System.out.println(ExcelReader.getModuleSelected());
        int len = 0;
        int split = 0;
        if (processList.size() > 10) {
            split = processList.size() / 2;
        } else {
            hBox.getChildren().remove(sp);
        }
        for (String s : processList) {
            if (s.length() > len) {
                len = s.length();
            }
        }
        int splitCount = 1;
        for (String s : processList) {
            System.out.println("S : " + s);
            JFXButton button1 = new JFXButton();
            button1.getStyleClass().add("button-sub-raised");
            button1.setWrapText(true);
            if (splitCount > split && processList.size() > 10) {
                button1.setText(s);
                buttonList.add(button1);
                vb.getChildren().add(button1);
            } else {
                button1.setText(s);
                buttonList.add(button1);
                modulesListVBox.getChildren().add(button1);
            }
            splitCount++;
            button1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                     JFXAnchorPane.getStylesheets().add(ModuleListController.class.getResource("/com/banking/css/jfoenix-components.css").toExternalForm());
                    ExcelReader.setProcessSelected(button1.getText());
                    System.out.println("process Action" + button1.getText());
                    subProcess = ExcelReader.getSubProcess(ExcelReader.getModuleSelected());
                    if (subProcess.containsKey(button1.getText())) {
                        //status="subProcess";
                        //  headerLabel.setText("Sub Processes for "+ExcelReader.getProcessSelected());
                        modulesListVBox.getChildren().removeAll(buttonList);
                        vb.getChildren().removeAll(buttonList);
                        String ar[] = subProcess.get(button1.getText()).toString().split("\n");
                        int sflen = 0;
                        for (int l = 0; l < ar.length; l++) {
                            if (ar[l].length() > sflen) {
                                sflen = ar[l].length();
                            }
                        }
                        for (int l = 0; l < ar.length; l++) {
//                            System.out.println("AR:" + ar[1]);
                            JFXButton spb = new JFXButton(ar[l]);
                            spb.getStyleClass().add("button-field-raised");
                            spb.setWrapText(true);
                            modulesListVBox.getChildren().add(spb);
                            spb.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    ExcelReader.setSubFunctionSelected(spb.getText());
                                    JFXAnchorPane.getChildren().remove(0);
                                    try {
                                        Node n = FXMLLoader.load(getClass().getResource("/com/banking/fxmls/field1.fxml"));
                                        JFXAnchorPane.getChildren().add(n);
                                    } catch (IOException ex) {
                                    }
                                }
                            });
                        }

                    } else {
                        System.out.println("Fields......");
                        JFXAnchorPane.getChildren().remove(0);
                        try {
                            Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/field1.fxml"));
                            JFXAnchorPane.getChildren().add(n);
                        } catch (IOException ex) {}
                    }
                }
            });

        }

//        homeButton.setOnAction((ActionEvent t) -> {
//            try {
//                
//                handleBackButton(t);
//            } catch (IOException ex) {
//                Logger.getLogger(ModuleListController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });

    }

      @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        JFXAnchorPane.getChildren().remove(0);
        if (status.equals("module")) {
            Node n = FXMLLoader.load(getClass().getResource("/com/banking/fxmls/DataFolder.fxml"));
            JFXAnchorPane.getChildren().add(n);
        } else if (status.equals("process")) {
            Node n = FXMLLoader.load(getClass().getResource("/com/banking/fxmls/module_list.fxml"));
            JFXAnchorPane.getChildren().add(n);
        }
    }

}
