package utils;

import models.User;
    
import java.util.ArrayList;

public class IDGenerator {

    // GENERATE MEMBER ID
    public static String generateNextMemberId(ArrayList<User> users) {
        int maxId = 1000; 
        
        for (User user : users) {
            if (user.getLibraryId().startsWith("M")) {
                try {
                    // parse "M1001" -> 1001
                    int idNum = Integer.parseInt(user.getLibraryId().substring(1));
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    // ignore badly formatted IDs
                }
            }
        }
        // return the next ID in the "MXXXX" format
        return "M" + (maxId + 1);
    }
    
    // GENERATE BOOK ID
    public static String generateNextBookId(ArrayList<models.Book> books) {
        int maxId = 0; 
        
        for (models.Book book : books) {
            if (book.getBookId().startsWith("B")) {
                try {
                    // parse "B001" -> 1
                    int idNum = Integer.parseInt(book.getBookId().substring(1));
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        // return the next ID, padding with zeros (e.g., B007, B010, B100)
        return "B" + String.format("%03d", maxId + 1);
    }

    
}