# gtfs-expiry-date-monitoring

This Lambda function will check the expiry date of the GTFS files which are stored in an AWS S3 bucket every week automatically. SNS notification will be sent if the GTFS files will expire within 7 days.
  
## Setup:
  Upload the *.jar to AWS Lambda, and set environment variables BUCKET_NAME , and TOPIC_NAME. 

  Create a CloudWatch rule for this lambda function.

