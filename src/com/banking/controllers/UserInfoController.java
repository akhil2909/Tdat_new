/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banking.controllers;

import com.banking.utils.LoginInfo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author KS102234
 */
public class UserInfoController implements Initializable {
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private JFXButton loginButton;
    @FXML
    private PasswordField passwordField;
    
    @FXML
   private TextField usernameField;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
           //usernameField.requestFocus();
        
    }    

    @FXML
    private void handleLoginButton(ActionEvent event) 
    {
        String username=usernameField.getText();
        LoginInfo.setUsername(username);
        mainAnchorPane.getChildren().remove(0);
        try {
            Node n=FXMLLoader.load(getClass().getResource("/com/banking/fxmls/DataFolder.fxml"));
            mainAnchorPane.getChildren().add(n);
        } catch (IOException ex) {
            Logger.getLogger(UserInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
