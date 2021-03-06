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
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import play.Play;
import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import plugins.s3blobs.ExtendedS3Blob;

/**
 *
 * @author Pluce
 */
@Entity
@Table(name="membre")
public class User extends Model{
    
    @Required
    public String name;
    
    @Required
    public String password;
    
    @Unique
    @Email
    @Required
    public String email;  
    
    @OneToOne
    public Photo avatar;
    
    @OneToMany(cascade=CascadeType.REMOVE,mappedBy="owner")
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
