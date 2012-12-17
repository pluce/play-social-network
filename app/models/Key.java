/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import play.db.jpa.Model;

/**
 *
 * @author Pluce
 */
@Entity
public class Key extends Model {
    public String authenticationKey;
    
    @OneToOne
    public User user;
}
