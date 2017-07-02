/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tdat.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author akhil
 */


         
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private JFXButton loginBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked Login!");
        //label.setText("Hello World!");
//        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));        
//        Scene scene = new Scene(root);        
//        stage.setScene(scene);
//        stage.show();
        
    }
    
}
