/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.User;
import play.mvc.Controller;

/**
 *
 * @author Pluce
 */
public class Inscription extends Controller{
    
    public static void inscription(){
        render();
    }
    
    public static void doInscription(User user){
        if(user.email != null && user.name != null && user.password != null){
            user.save();
        }
        Application.index();
    }
}
