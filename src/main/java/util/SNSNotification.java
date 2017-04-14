package util;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;

import application.ExpiryNotice;

public class SNSNotification {
	
	public static void sendSNSNotification(String fileName, int expiryDate) {
		AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

		//create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(ExpiryNotice.TOPIC_NAME);
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		
		//publish to an SNS topic
		int index = String.valueOf(String.valueOf(System.currentTimeMillis())).length();
		String msg = "Please update your GTFS file " + fileName.substring(index) + " which expires on " + expiryDate;
		PublishRequest publishRequest = new PublishRequest(createTopicResult.getTopicArn(), msg);
		snsClient.publish(publishRequest);
	}
}
