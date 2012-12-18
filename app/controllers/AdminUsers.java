/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.User;
import play.mvc.With;

/**
 *
 * @author Pluce
 */
@CRUD.For(User.class)
@With(Secure.class)
@Check("admin")
public class AdminUsers extends CRUD{
    
}
