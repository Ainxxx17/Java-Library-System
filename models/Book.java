
package models;

public class Book implements Borrowable, Comparable<Book> {

    private String bookId, title, author, genre, status; // status - "Available" or "Borrowed"

    // CONSTRUCTOR
    public Book(String bookId, String title, String author, String genre, String status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.status = status;
    }

    // GETTERS
    public String getBookId() {
        return bookId;
    }

     public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

     public String getStatus() {
        return status;
    }

    // SETTERS
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //METHODS
    // implementation of Borrowable Interface 
    @Override
    public boolean isAvailable() {
        // a book is available if its status is "Available"
        return "Available".equalsIgnoreCase(this.status);
    }

    @Override
    public void markBorrowed() {
        // set the status to "Borrowed"
        this.status = "Borrowed";
    }

    @Override
    public void markAvailable() {
        // set the status to "Available"
        this.status = "Available";
    }
    
    // implementation of comparable interface - it tells Collections.sort() how to order two Book objects.
    @Override
    public int compareTo(Book otherBook) {
        return this.title.compareToIgnoreCase(otherBook.getTitle());
    }

    public String toFileString() {
        // Format: B001,The Time Machine,H.G. Wells,Sci-Fi,Available
        return String.join(",", bookId, title, author, genre, status);
    }

}
    
    