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
import javax.persistence.Entity;
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
public class Photo extends Model{
    
    public static final String PHOTOS_PATH = "public/shared/photos/";
    public static final String THUMBS_PATH = "thumbs/";
    public static final String AVATAR_PATH = "avatars/";
    
    public static final String NO_AVATAR_PATH = "public/share/no_avatar.jpg";
    
    
    public ExtendedS3Blob photo;
    public ExtendedS3Blob thumb;
    public ExtendedS3Blob avatar;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date timeUploaded;
    
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
        File thumb = Play.getFile("public/shared/temp/th_"+tmpName);
        thumb.createNewFile();
        Images.resize(uploadedFile, thumb,200,150,true);
        
        this.photo = new ExtendedS3Blob();
        this.photo.set(new FileInputStream(photo),mimeType);
        
        this.thumb = new ExtendedS3Blob();
        this.thumb.set(new FileInputStream(thumb),mimeType);
        
        this.timeUploaded = timeUploaded;
    }
    
    public void makeAvatar(Integer x1, Integer y1, Integer x2, Integer y2) throws FileNotFoundException, IOException{
        String tmpName = Codec.UUID();
        File photoFile = Play.getFile("public/shared/temp/"+tmpName);
        FileOutputStream fos = new FileOutputStream(photoFile);
        InputStream is = this.photo.get();
        long length = this.photo.length();
        while( length > 0 ){
            fos.write(is.read());
            length--;
        }
        is.close();
        fos.close();        
        File avatarFile = Play.getFile("public/shared/temp/a"+tmpName);
        avatarFile.createNewFile();
        Images.crop(photoFile, avatarFile, x1, y1, x2, y2);
        this.avatar = new ExtendedS3Blob();
        this.avatar.set(new FileInputStream(avatarFile),this.photo.type());
    }
    
}
