/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;

/**
 *
 * @author Pluce
 */
public interface Commentable {
    public List<Activity> getComments();
    public Activity comment(User writer, String message);
}
