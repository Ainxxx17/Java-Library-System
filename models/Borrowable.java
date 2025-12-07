
package models;

public interface Borrowable {
    
    // METHODS
    boolean isAvailable();

    void markBorrowed();
    
    void markAvailable();
}