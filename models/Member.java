
package models;

public class Member extends User {

    // CONSTRUCTOR
    public Member(String libraryId, String fullName, String username, String password) {
        super(libraryId, fullName, username, password);
    }

    @Override
    public boolean canBorrow() {
        return true;
    }

    // FILE FORMAT - CSV [Comma Separated Values]
    public String toString() {
        return String.join(",",
            getLibraryId(),
            getFullName(),
            getUsername(),
            getPassword(),
            "MEMBER"
        );
    }
}