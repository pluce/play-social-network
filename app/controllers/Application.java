package controllers;

import play.mvc.*;


import models.*;

public class Application extends Controller {

    public static void index() {
        if(!Security.isConnected()){
            Inscription.inscription();
        }
        Profil.index();
    }
    
    public static void getAKey(String email,String password){
        User u = User.find("byEmailAndPassword",email,password).first();
        Key k = Key.find("byUser",u).first();
        if(u == null){
            notFound();
        }
        else{
            renderJSON(k.authenticationKey);
        }
    }

}