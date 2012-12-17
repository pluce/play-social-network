/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.Model;

/**
 *
 * @author Pluce
 */
@Entity
public class Activity extends Model {
    @ManyToOne
    public User owner;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date timeShared;
    
    @ManyToMany
    public Set<User> likes = new HashSet<User>();
    
    @OneToMany
    public Set<Photo> photos = new HashSet<Photo>();
    
    public String message;
    
    public boolean isPublic = false;
}
