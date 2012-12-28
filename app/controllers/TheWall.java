/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Date;
import java.util.List;
import models.Photo;
import play.db.jpa.GenericModel;
import play.mvc.Controller;

/**
 *
 * @author Pluce
 */
public class TheWall extends Controller{
    public static void index(){
        List<Photo> theWall = Photo.find("order by timeUploaded desc").fetch(48);
        
        render(theWall);
    }
}
