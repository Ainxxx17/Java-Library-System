package models;

public abstract class User {

    // ENCAPSULATION: Fields are 'private', accessed via getters/setters.
    private String libraryId, fullName, username, password;

    // CONSTRUCTOR
    public User(String libraryId, String fullName, String username, String password) {
        this.libraryId = libraryId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    // ABSTRACT METHOD
    //@return true if the user type is allowed to borrow, false otherwise.
    public abstract boolean canBorrow();

    // GETTERS AND SETTERS (Encapsulation)   
    public String getLibraryId() {
        return libraryId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    
    // SETTERS
    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

     public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}