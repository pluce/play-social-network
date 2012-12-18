/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;

/**
 *
 * @author Pluce
 */
public class Inscription extends Controller{
    
    public static void inscription(){
        render();
    }
    
    public static void doInscription(@Valid User user) throws Throwable{
        if (validation.hasErrors()) {
            renderTemplate("Inscription/inscription.html",user);
        } else {
            if(user.email != null && user.name != null && user.password != null){
                user.save();
            }
            Secure.login();
        }
    }
}
