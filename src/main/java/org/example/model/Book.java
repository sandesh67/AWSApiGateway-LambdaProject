package org.example.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Book {

  private String title;
  private String author;
  private int year;
  private int pages;

  public Book() {

  }

  public Book(String title, String author, int year, int pages) {
    this.title = title;
    this.author = author;
    this.year = year;
    this.pages = pages;
  }
  @XmlElement
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @XmlElement
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }
 @XmlElement
  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  @XmlElement
  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }
}
