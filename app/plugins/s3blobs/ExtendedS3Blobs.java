/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.s3blobs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.exceptions.ConfigurationException;

/**
 *
 * @author Pluce
 */
public class ExtendedS3Blobs extends PlayPlugin {

	@Override
	public void onApplicationStart() {
		Logger.info("Starting the ExtendedS3Blobs module");
		if (!Play.configuration.containsKey("aws.access.key")) {
			throw new ConfigurationException("Bad configuration for s3: no access key");
		} else if (!Play.configuration.containsKey("aws.secret.key")) {
			throw new ConfigurationException("Bad configuration for s3: no secret key");
		} else if (!Play.configuration.containsKey("s3.bucket")) {
			throw new ConfigurationException("Bad configuration for s3: no s3 bucket");
		}
		ExtendedS3Blob.s3Bucket = Play.configuration.getProperty("s3.bucket");
		String accessKey = Play.configuration.getProperty("aws.access.key");
		String secretKey = Play.configuration.getProperty("aws.secret.key");
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		ExtendedS3Blob.s3Client = new AmazonS3Client(awsCredentials);
		if (!ExtendedS3Blob.s3Client.doesBucketExist(ExtendedS3Blob.s3Bucket)) {
			ExtendedS3Blob.s3Client.createBucket(ExtendedS3Blob.s3Bucket);
		}
	}
}
