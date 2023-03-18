package org.example;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.example.entity.BookEntity;
import org.example.model.Book;
import org.example.model.Books;
import org.example.model.response.ApiResponse;

public class BookLambdahandler implements RequestHandler<APIGatewayProxyRequestEvent,ApiResponse> {

 private static final String bucket_name = "parsexmlthroughs3";
 private static final String fileName = "books.xml";


  private DynamoDBMapper dynamoDBMapper;

  private static final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

  private void initDynamoDB() {
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    dynamoDBMapper = new DynamoDBMapper(client);
  }

  private ApiResponse buildApiResponse() {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setBody("Books saved");
    apiResponse.setStatusCode(200);
    apiResponse.setHeaders(headers());
    return apiResponse;
  }

  private Map<String,String> headers() {
    Map<String,String> headerMap = new HashMap<>();
    headerMap.put("X-amazon-author", "Sandesh");
    headerMap.put("X-amazon-apiversion", "V1");
    headerMap.put("Content-Type", "application/json");
    return headerMap;
  }

  @Override
  public ApiResponse handleRequest(APIGatewayProxyRequestEvent inputStream, Context context) {
     final LambdaLogger lambdaLogger = context.getLogger();
    JAXBContext jaxbContext = null;
    try {
      lambdaLogger.log(inputStream.getBody());
      String body = inputStream.getBody();
      InputStream inputStream1 = new ByteArrayInputStream(body.getBytes());
       jaxbContext = JAXBContext.newInstance(Books.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      lambdaLogger.log(inputStream.toString());
      lambdaLogger.log("Before marshalling");
      Books books = (Books)jaxbUnmarshaller.unmarshal(inputStream1);
      List<Book> bookList = books.getBook();
      initDynamoDB();
      bookList.stream().forEach( b -> {
        lambdaLogger.log(b.getAuthor() + b.getTitle() + b.getYear());
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(b.getTitle());
        bookEntity.setAuthor(b.getAuthor());
        bookEntity.setYear(b.getYear());
        bookEntity.setPages(b.getPages());
        dynamoDBMapper.save(bookEntity);
        lambdaLogger.log("Record Saved");
      });
      File file = new File(body);
      InputStream inputStream2 = new FileInputStream(body);
      PutObjectRequest putObjectRequest = new PutObjectRequest(bucket_name,fileName,inputStream2,new ObjectMetadata());
      s3Client.putObject(putObjectRequest);
      lambdaLogger.log("File saved in bucket" + bucket_name + "with fileName" + fileName );

    } catch (JAXBException e) {
      lambdaLogger.log("Failed the process");
      return new ApiResponse("Failed", 400,headers());
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return buildApiResponse();
  }
}
