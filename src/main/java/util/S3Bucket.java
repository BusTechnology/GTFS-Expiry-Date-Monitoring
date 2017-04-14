package util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import application.ExpiryNotice;

public class S3Bucket {

	public static void download(String bucketName, String path, LambdaLogger log) {
		AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
		final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
        ListObjectsV2Result result;
        
        do {               
            result = s3Client.listObjectsV2(req);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            	String dataKey = objectSummary.getKey().replace('+', ' ');
                if(dataKeyVerify(dataKey, log)) {

            		S3ObjectInputStream s3Stream = s3Client.getObject(new GetObjectRequest(bucketName, dataKey)).getObjectContent();
            		String filePath = path + System.currentTimeMillis() + dataKey;
            		try {
            			Files.copy(s3Stream, new File(filePath).toPath());
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
            		
            	    try {
            			s3Stream.close();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
            	}
            }
            req.setContinuationToken(result.getNextContinuationToken());
         } while(result.isTruncated() == true); 
	}
	
	public static boolean dataKeyVerify(String dataKey, LambdaLogger log) {
		try {
			dataKey = URLDecoder.decode(dataKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Matcher dataMatcher = Pattern.compile(".*\\.([^\\.]*)").matcher(dataKey);
		if (!dataMatcher.matches()) {
			log.log("Unable to infer file type for key " + dataKey);
			return false;
		}
		
		String dataType = dataMatcher.group(1).toLowerCase();
		if (!(ExpiryNotice.ZIP_TYPE.equals(dataType))) {
			log.log("Skipping not correct data type " + dataKey);
			return false;
		}
		return true;
	}
}
