package org.example.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerated;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "BookEntity")
public class BookEntity {

  @DynamoDBHashKey(attributeName = "id")
  @DynamoDBAutoGeneratedKey
  private String id;

  @DynamoDBAttribute(attributeName = "title")
  private String title;
  @DynamoDBAttribute(attributeName = "author")
  private String author;
  @DynamoDBAttribute(attributeName = "year")
  private int year;
  @DynamoDBAttribute(attributeName = "pages")
  private int pages;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public BookEntity() {

  }

  public BookEntity(String title, String author, int year, int pages) {
    this.title = title;
    this.author = author;
    this.year = year;
    this.pages = pages;
  }
}
