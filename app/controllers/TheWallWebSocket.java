/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import play.mvc.WebSocketController;
import service.ShareService;

/**
 *
 * @author Pluce
 */
public class TheWallWebSocket extends WebSocketController{
    
    
    
    public static void update(){
        while(inbound.isOpen()){
            System.err.println("__socket__ ");
            String photoId = (String) await(ShareService.photoStream.nextEvent());
            
            System.err.println("__socket event__ "+photoId);
            outbound.send(photoId);
        }
    }
}
