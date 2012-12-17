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
        User u = User.find("byEmail", Security.connected()).first();
        Activity a = Activity.findById(activity);
        if(a == null) notFound();
        if(u == null) forbidden();
        
        a.likes.add(u);
        a.save();
        ok();
    }
    public static void dislike(Long activity){
        User u = User.find("byEmail", Security.connected()).first();
        Activity a = Activity.findById(activity);
        if(a == null) notFound();
        if(u == null) forbidden();
        
        a.likes.remove(u);
        a.save();
        ok();
    }
}
