package org.example;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

    private final SimpleLongProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty publishedDate;

    public Book(Long id, String title, String author, String isbn, String publishedDate) {
        this.id = new SimpleLongProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.publishedDate = new SimpleStringProperty(publishedDate);
    }

    public Long getId() {return this.id.get();}
    public void setId(Long id) {this.id.set(id);}
    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }

    public String getAuthor() { return author.get(); }
    public void setAuthor(String author) { this.author.set(author); }

    public String getIsbn() { return isbn.get(); }
    public void setIsbn(String isbn) { this.isbn.set(isbn); }

    public String getPublishedDate() { return publishedDate.get(); }
    public void setPublishedDate(String publishedDate) { this.publishedDate.set(publishedDate); }
}
