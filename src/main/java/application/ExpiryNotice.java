package application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import util.S3Bucket;
import util.SNSNotification;
import util.ZipFileReader;
import util.Date;

public class ExpiryNotice implements RequestHandler<Object, String> {

	private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
	private static final String TMP_PATH = "/tmp/";
	
	public static final String ZIP_TYPE = (String) "zip";
	public static final String TOPIC_NAME = System.getenv("TOPIC_NAME");

	@Override
	public String handleRequest(Object object, Context context) {
		LambdaLogger log = context.getLogger();
		
		if(BUCKET_NAME == null) log.log("Environment variable BUCKET_NAME is not defined.");
		if(TOPIC_NAME == null) log.log("Environment variable TOPIC_NAME is not defined.");
	
		S3Bucket.download(BUCKET_NAME, TMP_PATH, log);
		expiryDateCheck();
		
		File file = new File(TMP_PATH);
		ZipFileReader.deleteFile(file);
		
		return "done";
	}
	
	private static void expiryDateCheck() {
        List<String> fileNames;
		fileNames = ZipFileReader.getFileName(TMP_PATH);
		if (fileNames.size() == 0){
			System.err.println("No files found in path " + TMP_PATH.toString());
			System.exit(-1);
		}

		for(String fileName : fileNames){
			if(fileName.endsWith(".zip")){
				int maxEndDate =-1;
				File file = new File(TMP_PATH + fileName);
				try {
					maxEndDate = ZipFileReader.getMaxDate(file);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if(maxEndDate - Date.getCurrentDate() <= 7)
					SNSNotification.sendSNSNotification(fileName, maxEndDate);
			}
		}
		}
	}

