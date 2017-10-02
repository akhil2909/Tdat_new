/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banking.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;



/**
 *
 * @author AV00373090
 */
public class FieldController implements Initializable {
    
    @FXML
    private JFXTreeTableView fieldsTable1;
    
    @FXML
    private JFXButton addButton1;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        TreeTableColumn emailCol = new TreeTableColumn("Email");
        
        fieldsTable1.getColumns().addAll(firstNameCol, lastNameCol, emailCol);    
        
        
        
    }

   
    
    
    
}
