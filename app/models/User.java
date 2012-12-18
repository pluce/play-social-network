/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import play.Play;
import play.db.jpa.Model;

/**
 *
 * @author Pluce
 */
@Entity
@Table(name="membre")
public class User extends Model{
    public String name;
    public String password;
    public String email;  
    
    @OneToMany(cascade=CascadeType.REMOVE)
    public List<Photo> photos = new ArrayList<Photo>();
    
    @ManyToMany(mappedBy="leads")
    public Set<User> follows = new HashSet<User>();
    
    @ManyToMany
    public Set<User> leads = new HashSet<User>();
    
    @OneToMany(mappedBy="owner",cascade=CascadeType.REMOVE)
    public List<Activity> partages = new ArrayList<Activity>();

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
    
    public String getAvatarPath(){
        File f = Play.getFile(Photo.AVATAR_PATH+id+".jpg");
        if(f.exists()){
            return Photo.AVATAR_PATH+id+".jpg";
        } else {
            return Photo.AVATAR_PATH+"no-avatar.jpg";
        }
    }
    
    @PreRemove
    public void beforeRemove(){
        for(User u: follows){
            u.follows.remove(this);
            u.save();
        }
        for(User u: leads){
            u.leads.remove(this);
            u.save();
        }
    }
}
