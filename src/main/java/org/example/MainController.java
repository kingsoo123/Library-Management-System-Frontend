package org.example;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

public class MainController {

    private boolean isEdit = false;
    int pageable = 0;
    private final BookApiService apiService = new BookApiService();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();


    private void loadBooksFromApi(int page, int size) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    List<Book> books = apiService.fetchBooks(page, size);
                    Platform.runLater(()->{
                        bookList.setAll(books);

                        if (books.isEmpty()) {
                            System.out.println("No books available from API.");
                            System.out.println(books + "BBBBBBBB" + books.size());

                            // Optionally show a Label in the UI: "No books found"
                        }
                    });
                }catch (Exception e){
                    System.out.println("AAAAAAAAAAAAAA");
                    Platform.runLater(()->{
                        e.printStackTrace();
                    });
                }
                return null;
            }
        };

        new Thread(task).start();
    }




    public void showSuccess(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null); // no header
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null); // no header
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void populateFields(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        publishedDateField.setText(book.getPublishedDate());
    }

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Long> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private TableColumn<Book, String> publishColumn;

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField publishedDateField;
    @FXML
    private TextField searchBook;



    @FXML
    public void initialize() {
        // Link table columns to Book properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        // Set the table items
        bookTable.setItems(bookList);

        // Listen for row selection
        bookTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });

        loadBooksFromApi(pageable,10);
    }


    @FXML
    private void handleAdd(ActionEvent event) {
        Long id = bookTable.getSelectionModel().getSelectedItem().getId();
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String publishedDate = publishedDateField.getText();

        if(!title.isEmpty() && !author.isEmpty() && !isbn.isEmpty() && !publishedDate.isEmpty()) {
            Book book = new Book(id,title, author, isbn, publishedDate);
            //bookList.add(book);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    apiService.createBook(book);
                    return null;
                }
            };

            task.setOnSucceeded(e -> System.out.println(task.getValue()+"KKKKKK"));

            new Thread(task).start();

            System.out.println("Book added: " + book.getTitle());
            showSuccess("Book added!  " + book.getTitle());
        }else {
            System.out.println("Please fill all fields");
            showError("Please fill all fields");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Long id = bookTable.getSelectionModel().getSelectedItem().getId();
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String publishedDate = publishedDateField.getText();

        if(!title.isEmpty() && !author.isEmpty() && !isbn.isEmpty() && !publishedDate.isEmpty()) {
            Book book = new Book(id,title, author, isbn, publishedDate);
            //bookList.add(book);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    apiService.updateBook(book, 7);
                    return null;
                }
            };

            task.setOnSucceeded(e -> System.out.println(task.getValue()+"KKKKKK"));

            new Thread(task).start();

            System.out.println("Book updated: " + book.getTitle());
            showSuccess("Book updated!  " + book.getTitle());

            titleField.clear();
            authorField.clear();
            isbnField.clear();
            publishedDateField.clear();
        }else {
            System.out.println("Please fill all fields");
            showError("Please fill all fields");
        }
    }

    @FXML
    private void  handleDelete(ActionEvent event) throws IOException {
        Long id = bookTable.getSelectionModel().getSelectedItem().getId();

        if(id != null) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    apiService.deleteBook( id);
                    return null;
                }
            };

            task.setOnSucceeded(e -> System.out.println(task.getValue()+"KKKKKK"));

            new Thread(task).start();

            System.out.println("Book deleted");
            showSuccess("Book deleted!");
        }else {
            showError("Please select a row to delete");
        }


}

    @FXML
    private void  handleSearch(ActionEvent event) throws IOException {
        String search = searchBook.getText();
        if(!search.isEmpty()) {
            //bookList.add(book);
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        List<Book> book = apiService.searchBook(search);
                        Platform.runLater(()->{
                            bookList.setAll(book);

                            if (bookList.isEmpty()) {
                                System.out.println("No books available from API.");
                                System.out.println("BBBBBBBB" + book.size());

                                // Optionally show a Label in the UI: "No books found"
                            }
                        });
                    }catch (Exception e){
                        System.out.println("AAAAAAAAAAAAAA");
                        Platform.runLater(()->{
                            e.printStackTrace();
                        });
                    }
                    return null;
                }
            };

            new Thread(task).start();
        }else {
            System.out.println("Please fill all fields");
            showError("Please fill all fields");
        }
        System.out.println("Search button clicked!" + search);
    }



@FXML
    private void  handleLogin(ActionEvent event) throws IOException {
        System.out.println("Login button clicked!" + titleField.getText() + authorField.getText() + isbnField.getText() + publishedDateField.getText());
    }

    @FXML
    public void handleRefresh(){
        loadBooksFromApi(pageable,10);
    }

    @FXML
    public void handlePrev(){
        if (pageable > 0) {
            pageable = pageable - 1;
        }
        loadBooksFromApi(pageable, 10);
    }

    @FXML
    public void handleNext(){
        if(pageable < bookList.size()){
            pageable = pageable + 1;
        }
        loadBooksFromApi(pageable, 10);
    }

//    @FXML
//    private void handleAdd(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddNewBook.fxml"));
//        Parent root = fxmlLoader.load();
//        Stage stage = new Stage();
//        stage.setTitle("Add Book");
//        stage.setScene(new Scene(root, 500, 400));
//        stage.show();
//    }
}
