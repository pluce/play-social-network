/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.Activity;
import models.User;
import play.db.jpa.GenericModel;
import play.mvc.Controller;
import play.mvc.With;

/**
 *
 * @author Pluce
 */
@Check("user")
@With(Secure.class)
public class Partage extends Controller {
    public static void like(Long activity){
        User u = Security.connectedUser();
        Activity a = Activity.findById(activity);
        if(a == null) notFound();
        if(u == null) forbidden();
        
        a.like(u);
        a.save();
        ok();
    }
    
    public static void comment(Long activity, String message){
        Activity toBeCommented = Activity.findById(activity);
        toBeCommented.comment(Security.connectedUser(), message);
    }    
    
    public static void dislike(Long activity){
        User u = Security.connectedUser();
        Activity a = Activity.findById(activity);
        if(a == null) notFound();
        if(u == null) forbidden();
        
        a.dislike(u);
        a.save();
        ok();
    }
}
