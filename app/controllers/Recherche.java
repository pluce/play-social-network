/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.List;
import models.User;
import play.data.validation.Email;
import play.mvc.Controller;
import play.mvc.With;

/**
 *
 * @author Pluce
 */
@Check("user")
@With(Secure.class)
public class Recherche extends Controller {
    public static void index(){
       List<User> luser = new ArrayList<User>();
        render(luser);
    }
    public static void chercher(String byName, @Email String byEmail){
       List<User> luser = new ArrayList<User>();
       System.err.println("byName: "+byName);
       System.err.println("byEmail: "+byEmail);
       if(byEmail != null && !byEmail.isEmpty()){
           
            System.err.println("in mail");
            User u = User.find("byEmail",byEmail).first();
            if(u != null){
                Profil.fiche(u.getId(), "");
            }
        }
        if(byName != null && byName.length() >= 3){
            System.err.println("in name");
            luser = User.find("select u from User u where u.name like ?","%"+byName+"%").fetch();
        }
        renderTemplate("Recherche/index.html",luser);
    }
}
