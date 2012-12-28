package service;


import controllers.Security;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import models.Activity;
import models.Photo;
import models.User;
import play.Play;
import play.db.jpa.JPABase;
import play.libs.Codec;
import play.libs.F;
import play.libs.Images;
import play.libs.MimeTypes;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pluce
 */
public class ShareService {
    
    
    public static F.EventStream photoStream = new F.EventStream();
    
    
    public static Activity share(User writer, String message){
        Activity act = new Activity();
        act.owner = writer;
        act.message = message;
        act.timeShared = new Date();
        act.save();
        return act;
    }
    public static Activity shareWithPhoto(User writer, String message, File uploadedFile) throws IOException{
        Photo p = computePhotoUpload(uploadedFile);
        return shareLonelyPhoto(writer, p, message);
    }
    
    private static Photo computePhotoUpload(File uploadedFile) throws IOException{
        Photo p = new Photo(uploadedFile, new Date());
        p.save();
        photoStream.publish(""+p.id);
        return p;
    }
    
    public static Activity sharePhoto(User writer, File uploadedFile) throws IOException{     
        Photo p = computePhotoUpload(uploadedFile);
        Date oldDate = new Date();
        oldDate = new Date(oldDate.getTime() - 3600*1000);
        Activity act = Activity.find("SELECT a FROM Activity a WHERE a.owner = ? AND a.timeShared > ? ORDER BY a.timeShared DESC",writer,oldDate).first();
        if(act != null && act.photos.size() > 0){
            return updateActivityWithNewPhoto(act, p);
        } else {
            return shareLonelyPhoto(writer,p);
        }
    }
    
    public static Activity updateActivityWithNewPhoto(Activity activity, Photo photo){
        activity.owner.photos.add(photo);
        activity.owner.save();
        activity.photos.add(photo);
        activity.timeShared = new Date();
        activity.save();
        return activity;
    }
    
    public static Activity shareLonelyPhoto(User writer, Photo photo, String message){
        
        writer.photos.add(photo);
        writer.save();  
        
        Activity newAct = new Activity();
        newAct.message = message;
        newAct.photos.add(photo);
        newAct.owner = writer;
        newAct.timeShared = new Date();
        newAct.save();      
        return newAct;
    }
    public static Activity shareLonelyPhoto(User writer, Photo photo){
        return shareLonelyPhoto(writer,photo,writer.name + " a partagé une photo.");
    }

    public static void unsharePhoto(User owner, Photo photo) {
        Activity ac = Activity.find("SELECT a FROM Activity a, IN(a.photos) p WHERE p = ? AND a.owner = ?",photo,owner).first();
        ac.photos.remove(photo);
        if(ac.photos.isEmpty()){
            ac.delete();
        } else {
            ac.save();
        }
        owner.photos.remove(photo);
        owner.save();
        photo.delete();
    }
}
