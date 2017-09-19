/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banking.controllers;

import com.banking.utils.LoginInfo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;

/**
 * FXML Controller class
 *
 * @author ks102234
 */
public class DataFolderController implements Initializable {
    @FXML
    private AnchorPane dataFolderAnchorPane;
    @FXML
    private Label title;
    @FXML
    private JFXButton nextButton;
    @FXML
    private JFXButton browseButton;
    @FXML
    private JFXTextField folderPathField;
    @FXML
    private JFXButton createButton;
    @FXML
    private JFXTextField runNameField;
    @FXML
    private Label runDirectory;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        title.setText("Test Data Management");
//        title.setTextFill(Color.web("red"));
//        title.setFont(Font.font(16));
        AnchorPane.setTopAnchor(dataFolderAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(dataFolderAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(dataFolderAnchorPane, 0.0);
        AnchorPane.setRightAnchor(dataFolderAnchorPane, 0.0);
       // Date d=new Date();
        SimpleDateFormat s=new SimpleDateFormat("dd-MM-yyyy");
        Date d=null;
        try {
            d = s.parse(s.format(new Date()));
        } catch (ParseException ex) {
            Logger.getLogger(DataFolderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        runNameField.setText(LoginInfo.getUsername()+"_"+s.format(new Date())+"_");
    }    

    @FXML
    private void handleNextButton(ActionEvent event) throws IOException 
    {
        dataFolderAnchorPane.getChildren().remove(0);
        Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/module_list.fxml"));
        dataFolderAnchorPane.getChildren().add(n);
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) 
    {
        DirectoryChooser d=new DirectoryChooser();
        d.setTitle("Choose Folder");
        File f=d.showDialog(dataFolderAnchorPane.getScene().getWindow());
        System.out.println(f.getAbsoluteFile());
        folderPathField.setText(f.getAbsolutePath());
    }

    @FXML
    private void handleCreateButton(ActionEvent event) {
        String filePath=folderPathField.getText()+"//"+runNameField.getText();
        File f=new File(filePath);
        if(!f.exists())
        {
            f.mkdir();
        }
        runDirectory.setText("Run Directory Created Successfully");
        runDirectory.setTextFill(Color.web("blue"));
        runDirectory.setFont(Font.font(16));
        System.setProperty("user.direc", f.getAbsolutePath());
        System.out.println(f.getAbsoluteFile());
    }
    
}
