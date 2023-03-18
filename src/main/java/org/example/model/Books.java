package org.example.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "books")
public class Books {


  private List <Book> book;

  public Books() {

  }
  public Books(List<Book> book) {
    this.book = book;
  }

  @XmlElement(name = "book", type = Book.class)
  public List<Book> getBook() {
    return book;
  }

  public void setBook(List<Book> book) {
    this.book = book;
  }
}
