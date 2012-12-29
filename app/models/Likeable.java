/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Set;

/**
 *
 * @author Pluce
 */
public interface Likeable {
    public Set<User> getLikers();
    public void like(User liker);
    public void dislike(User liker);
}
