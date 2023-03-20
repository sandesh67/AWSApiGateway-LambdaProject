package org.example;


import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class S3EventHandler implements RequestHandler<S3Event, Boolean> {

  private static final AmazonS3 s3Client = AmazonS3Client.builder().withCredentials(new DefaultAWSCredentialsProviderChain()).build();

  @Override
  public Boolean handleRequest(S3Event s3Event, Context context) {
    final LambdaLogger lambdaLogger = context.getLogger();
    if(s3Event.getRecords().isEmpty()) {
      lambdaLogger.log("No records founds");
      return false;
    }
    List<S3EventNotification.S3EventNotificationRecord> s3EventNotificationRecordList = s3Event.getRecords();

    for(S3EventNotification.S3EventNotificationRecord  record: s3EventNotificationRecordList) {
       String bucketName = record.getS3().getBucket().getName();
       String objectKey = record.getS3().getObject().getKey();
       S3Object s3Object =  s3Client.getObject(bucketName,objectKey);
       S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

       try(final BufferedReader br = new BufferedReader(new InputStreamReader(s3ObjectInputStream, StandardCharsets.UTF_8))) {
             br.lines().forEach( line -> lambdaLogger.log(line + "\n"));
       } catch (IOException e) {
         lambdaLogger.log("Caught in Exception");
         return false;
       }
    }
    return true;
  }
}
