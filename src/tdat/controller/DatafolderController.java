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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author akhil
 */
public class DatafolderController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXButton btn_nxt_datafolder; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        
        
        btn_nxt_datafolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
        
    } 
    
    
    public void actionOnNext(){
        
    }
    
    
    
    
    
    
    
    
}
