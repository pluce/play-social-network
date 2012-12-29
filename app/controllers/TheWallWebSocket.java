/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import play.mvc.WebSocketController;
import play.mvc.With;
import service.ShareService;

/**
 *
 * @author Pluce
 */

@Check("user")
@With(Secure.class)
public class TheWallWebSocket extends WebSocketController{
    
    
    
    public static void update(){
        while(inbound.isOpen()){
            String photoId = (String) await(ShareService.photoStream.nextEvent());
            
            outbound.send(photoId);
        }
    }
}
