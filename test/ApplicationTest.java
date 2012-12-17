import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {
    
    /**
     *
     */
    @org.junit.Before
    public void init(){
        Fixtures.deleteDatabase();
        User u = new User();
        u.name = "Toto";
        u.password = "toto";
        u.email = "toto@toto.com";
        u.save();
        Key k = new Key();
        k.authenticationKey = "bipbip";
        k.user = u;
        k.save();
    }

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    
    @Test
    public void testThatUserGetCorrectKey() {
        Map<String,String> da = new HashMap<String,String>();
        da.put("email", "toto@toto.com");
        da.put("password", "toto");
        Response response = POST("/key",da);
        assertContentEquals("bipbip", response);
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    
    @Test
    public void testThatBadUserGetIncorrectKey() {
        Map<String,String> da = new HashMap<String,String>();
        da.put("accepted", "application/json");
        da.put("email", "toto@toto.com");
        da.put("password", "totuu");
        Response response = POST("/key",da);
        assertIsNotFound(response);
        //assertContentType("application/json", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    
}