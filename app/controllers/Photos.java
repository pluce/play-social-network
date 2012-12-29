/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.IOException;
import models.Photo;
import models.User;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;
import plugins.s3blobs.ExtendedS3Blob;
import service.ShareService;

/**
 *
 * @author Pluce
 */
@Check("user")
@With(Secure.class)
public class Photos extends Controller{
    
    public static void index(){        
        render();
    }
    
    public static void upload(File image) throws IOException{
        if(image == null){
            index();
        }
        ShareService.sharePhoto(Security.connectedUser(), image);
        
        index();
    }
    
    public static void remove(Long id){
        Photo p = Photo.findById(id);
        if(p != null){
            ShareService.unsharePhoto(Security.connectedUser(),p);
        }
        index();
    }
    
    public static void avatar(Long id){
        Photo photo = Photo.findById(id);
        if(photo==null){
            notFound();
        }
        render(photo);
    }
    
    public static void makeAvatar(Long id, Integer x1, Integer y1, Integer x2, Integer y2) throws IOException{
        Photo photoBase = Photo.findById(id);
        photoBase.makeAvatar(x1, y1, x2, y2);
        photoBase.save();
        User current = Security.connectedUser();
        current.avatar = photoBase;
        current.save();
        Profil.index();
    }
    
    public static void photo(Long id){
        Photo photo = Photo.findById(id);
        if(photo == null){
            notFound();
        }
        render(photo);
    }
    
    public static void comment(Long photoId,String message){
        Photo photo = Photo.findById(photoId);
        if(photo == null){
            notFound();
        }
        photo.comment(Security.connectedUser(), message);
        Photos.photo(photoId);
    }
    
    public static void like(Long photo){
        User u = Security.connectedUser();
        Photo a = Photo.findById(photo);
        if(a == null) notFound();
        if(u == null) forbidden();
        
        a.like(u);
        a.save();
        ok();
    }
     
    
    public static void dislike(Long photo){
        User u = Security.connectedUser();
        Photo a = Photo.findById(photo);
        if(a == null) notFound();
        if(u == null) forbidden();
        
        a.dislike(u);
        a.save();
        ok();
    }
    
    private static void renderImage(ExtendedS3Blob s3blob){
        notFoundIfNull(s3blob);
        response.setContentTypeIfNotSet("image/jpg");
        try {
            renderBinary(Photo.getInputStream(s3blob));
        } catch (IOException ex) {
            notFound();
        }
    }
    
    public static void renderPhoto(Long id){
        renderImage(Photo.<Photo>findById(id).photo);
    }
    
    public static void renderThumb(Long id){
        renderImage(Photo.<Photo>findById(id).thumb);
    }
    
    public static void renderAvatar(Long id){
        Photo av = User.<User>findById(id).avatar;
        if(av != null){
            renderImage(User.<User>findById(id).avatar.avatar);
        } else {
            renderBinary(Play.getFile("/public/images/no-avatar.jpg"));
        }
        
    }
}
