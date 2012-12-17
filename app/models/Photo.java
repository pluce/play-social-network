/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.Model;

/**
 *
 * @author Pluce
 */
@Entity
public class Photo extends Model{
    
    public static final String PHOTOS_PATH = "public/shared/photos/";
    public static final String THUMBS_PATH = "public/shared/thumbs/";
    public static final String AVATAR_PATH = "public/shared/avatars/";
    
    public String fileName;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date timeUploaded;
    
    public Photo(){}
    public Photo(String fileName, Date timeUploaded ){
        this.fileName = fileName;
        this.timeUploaded = timeUploaded;
    }
    
    public String getPhotoPath(){
        return PHOTOS_PATH+fileName;
    }
    public String getThumbPath(){
        return THUMBS_PATH+fileName;
    }
}
