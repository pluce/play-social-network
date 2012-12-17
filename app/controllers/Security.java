/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.User;

/**
 *
 * @author Pluce
 */
public class Security extends Secure.Security {
    
    static User connectedUser(){
        return User.find("byEmail",connected()).first();
    }
    
    static boolean authenticate(String username, String password) {
        User u = User.find("byEmailAndPassword",username,password).first();
        if(u == null){
            return false;
        }
        return true;
    }
    
    static boolean check(String profile) {
        if("admin".equals(profile)) {
            return User.find("byEmail", connected()).<User>first().email.equals("root");
        }
        if("user".equals(profile)) {
            return isConnected();
        }
    return false;
}
}