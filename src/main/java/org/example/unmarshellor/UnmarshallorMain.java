package org.example.unmarshellor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.example.model.Book;
import org.example.model.Books;

public class UnmarshallorMain {

  public static void main(String[] args) throws JAXBException, IOException {
    JAXBContext jaxbContext = JAXBContext.newInstance(Books.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    File file = new File("books.xml");
    //BufferedReader br = new BufferedReader(new FileReader(file));
    //String st ;
   // while((st = br.readLine()) != null) {
     // System.out.println(st);
    //}
   Books books = (Books)jaxbUnmarshaller.unmarshal(file);
   System.out.println(books);

  /* Book book = new Book("Java", "Eckert", 2003, 200);
   Book book1 = new Book("C#", "shdmit", 2000, 180);
   List<Book> bookList = new ArrayList<>();
   bookList.add(book1);
   bookList.add(book);
   UnmarshallorMain unmarshallorMain = new UnmarshallorMain();
   unmarshallorMain.marshallFile(bookList, new File("books.xml"));
*/

  }

  public void marshallFile(List<Book> books, File selectedFile) throws JAXBException, IOException {
    JAXBContext context;
    BufferedWriter writer = null;
    writer = new BufferedWriter(new FileWriter(selectedFile));
    context = JAXBContext.newInstance(Books.class);
    Marshaller m = context.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    m.marshal(new Books(books), writer);
    writer.close();
  }
}
