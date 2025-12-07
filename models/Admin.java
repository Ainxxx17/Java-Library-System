package models;

public class Admin extends User {

    // CONSTRUCTOR
    public Admin(String libraryId, String fullName, String username, String password) {
        super(libraryId, fullName, username, password);
    }

    @Override
    public boolean canBorrow() {
        return false;
    }

    public String toString() {
        return String.join(",",
            getLibraryId(),
            getFullName(),
            getUsername(),
            getPassword(),
            "ADMIN"
        );
    }
}