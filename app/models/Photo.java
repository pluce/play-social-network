/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import controllers.Security;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.Play;
import play.db.jpa.Model;
import play.libs.Codec;
import play.libs.Files;
import play.libs.Images;
import play.libs.MimeTypes;
import plugins.s3blobs.ExtendedS3Blob;

/**
 *
 * @author Pluce
 */
@Entity
public class Photo extends Model implements Commentable{
    
    public static final String PHOTOS_PATH = "public/shared/photos/";
    public static final String THUMBS_PATH = "thumbs/";
    public static final String AVATAR_PATH = "avatars/";
    
    public static final String NO_AVATAR_PATH = "public/images/no-avatar.jpg";
    
    
    public ExtendedS3Blob photo;
    public ExtendedS3Blob thumb;
    public ExtendedS3Blob avatar;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date timeUploaded;
    
    @OneToMany(mappedBy="relatedPhoto")
    public List<Activity> comments;
    
    public Photo(){}
    public Photo(File uploadedFile,Date timeUploaded ) throws IOException{
        
        String mimeType = MimeTypes.getContentType(uploadedFile.getName());
        if(!(mimeType.equals("image/jpg")||mimeType.equals("image/jpeg"))){
            throw new IllegalArgumentException("Not a jpg picture");
        }
        String tmpName = Codec.UUID();
        File photo = Play.getFile("public/shared/temp/"+tmpName);
        photo.createNewFile();
        Images.resize(uploadedFile, photo,800,600,true);
        String thumbName = Codec.UUID();
        File thumb = Play.getFile("public/shared/temp/"+thumbName);
        thumb.createNewFile();
        Images.resize(uploadedFile, thumb,200,150,true);
        
        this.photo = new ExtendedS3Blob();
        this.photo.set(new FileInputStream(photo),mimeType,tmpName);
        
        
        this.thumb = new ExtendedS3Blob();
        this.thumb.set(new FileInputStream(thumb),mimeType,thumbName);
        
        this.timeUploaded = timeUploaded;
    }
    
    public static InputStream getInputStream(ExtendedS3Blob image) throws IOException{
        
        return new FileInputStream(updateCache(image));
    }
    
    public static File updateCache(ExtendedS3Blob image) throws IOException{
        File pic = Play.getFile("public/shared/temp/"+image.key);
        if(!pic.exists()){
            Long size = image.length();
            InputStream is = image.get();
            FileOutputStream fos = new FileOutputStream(pic);
            while(size > 0){
                fos.write(is.read());
                size--;
            }
            is.close();
            fos.close();
        }
        return pic;
    }
    
    public void makeAvatar(Integer x1, Integer y1, Integer x2, Integer y2) throws FileNotFoundException, IOException{
        String tmpName = Codec.UUID();
        File photoFile = updateCache(this.photo);
        File avatarFile = Play.getFile("public/shared/temp/"+tmpName);
        avatarFile.createNewFile();
        Images.crop(photoFile, avatarFile, x1, y1, x2, y2);
        this.avatar = new ExtendedS3Blob();
        this.avatar.set(new FileInputStream(avatarFile),this.photo.type(),tmpName);
    }
    
    
    public List<Activity> getComments() {
        return comments;
    }

    public Activity comment(User writer, String message) {
        Activity comm = new Activity();
        comm.owner = writer;
        comm.message = message;
        comm.timeShared = new Date();
        comm.relatedPhoto = this;
        this.getComments().add(comm);
        comm.save();
        this.save();
        
        return comm;
    }
    
}
