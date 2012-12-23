/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import java.io.File;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.Files;

/**
 *
 * @author Pluce
 */
@OnApplicationStart
@Every("1h")
public class CleanTempFolderJob extends Job{
    public void doJob() {
        File tmpDir = Play.getFile("/public/shared/temp/");
        Files.deleteDirectory(tmpDir);
        tmpDir.mkdir();
    }
}
