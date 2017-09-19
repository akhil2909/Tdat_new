/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banking.utils;

/**
 *
 * @author KS102234
 */
public class LoginInfo 
{
    private static String username1;
    private static String password1;
    private static String stat="module";

    public static String getStat() {
        return stat;
    }

    public static void setStat(String stat) {
        LoginInfo.stat = stat;
    }
    
    public static String getUsername() {
        return username1;
    }

    public static void setUsername(String username) {
        username1 = username;
    }

    public static String getPassword() {
        return password1;
    }

    public static void setPassword(String password) {
        password1 = password;
    }
    
}
