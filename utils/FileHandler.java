package utils;

import models.*;

import javax.swing.JOptionPane;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileHandler {

    // date formatter for parsing dates in borrow records
    // used in BorrowRecord read/write
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // checks if the file exists and is readable; shows error dialog and exits if not.
    private static File checkFile(String filePath, String fileName) {
        File file = new File(filePath);
        
        // get the absolute path for debugging
        String absolutePath = file.getAbsolutePath();
        
        // check if file exists
        // shows detailed error message and exits if not found for easier debugging
        if (!file.exists()) {
            String message = "CRITICAL ERROR: Cannot find file: " + fileName + "\n\n"
                            + "The application expected to find it at:\n" + absolutePath + "\n\n"
                            + "This usually means the app is not being run from the correct directory.\n"
                            + "Please set your IDE's 'Working Directory' to the project root folder (F4LibrarySystem).\n\n"
                            + "Application will now exit.";
            System.err.println(message);
            JOptionPane.showMessageDialog(null, message, "File Not Found", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // check if file is readable
        if (!file.canRead()) {
            String message = "CRITICAL ERROR: Cannot read file: " + fileName + "\n"
                            + "Location: " + absolutePath + "\n\n"
                            + "Please check file permissions.\n\n"
                            + "Application will now exit.";
            System.err.println(message);
            JOptionPane.showMessageDialog(null, message, "File Permission Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return file;
    }

    //reads the books.txt file and returns a list of Book objects.
    public static ArrayList<Book> readBooks(String filePath) {
        ArrayList<Book> books = new ArrayList<>();
        File file = checkFile(filePath, "books.txt");

        // try-with-resources automatically closes the reader, even if an error occurs
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                // Format: B001,The Time Machine,H.G. Wells,Sci-Fi,Available
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Book book = new Book(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
        return books;
    }


    // overwrites the entire books.txt file with the current list of books.
    public static void writeBooks(String filePath, ArrayList<Book> books) {
        // false = overwrite (don't append)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Book book : books) {
                writer.write(book.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing books file: " + e.getMessage());
        }
    }

    // reads the members.txt file and returns a list of User objects. 
    public static ArrayList<User> readUsers(String filePath) {
        ArrayList<User> users = new ArrayList<>();
        File file = checkFile(filePath, "members.txt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                // format: M1001,Ian Cabalquinto,ian123,password123,MEMBER
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0];
                    String name = parts[1];
                    String username = parts[2];
                    String password = parts[3];
                    String role = parts[4].trim();
                    
                    // create the correct object type based on role
                    if ("MEMBER".equalsIgnoreCase(role)) {
                        users.add(new Member(id, name, username, password));
                    } else if ("ADMIN".equalsIgnoreCase(role)) {
                        users.add(new Admin(id, name, username, password));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading members file: " + e.getMessage());
        }
        return users;
    }


    // appends a new User (Member) to the members.txt file.
    // used for Sign Up.
    public static void appendMember(String filePath, Member member) {
        File file = new File(filePath);
        
        // true = append (don't overwrite)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            
            // if file is not empty, write a newline first to ensure the new record starts on its own line
            if (file.length() > 0) {
                writer.newLine();
            }
            
            writer.write(member.toString());
            
        } catch (IOException e) {
            System.err.println("Error appending member to file: " + e.getMessage());
        }
    }

    // overwrites members.txt with the full updated list of Users.
    public static void writeUsers(String filePath, ArrayList<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing users file: " + e.getMessage());
        }
    }

    //reads the borrow_records.txt file and returns a list of BorrowRecord objects.
    public static ArrayList<BorrowRecord> readBorrowRecords(String filePath) {
        ArrayList<BorrowRecord> records = new ArrayList<>();
        File file = checkFile(filePath, "borrow_records.txt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                // format: M1001,Ian Cabalquinto,B002,Dune,2025-11-14,2025-11-21,-,Borrowed,-
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    LocalDate borrowDate = LocalDate.parse(parts[4], DATE_FORMATTER);
                    LocalDate dueDate = LocalDate.parse(parts[5], DATE_FORMATTER);
                    
                    // handle return date, which might be "-" (not yet returned)
                    LocalDate returnDate = parts[6].equals("-") ? null : LocalDate.parse(parts[6], DATE_FORMATTER);
                    
                    BorrowRecord record = new BorrowRecord(
                        parts[0], parts[1], parts[2], parts[3],
                        borrowDate, dueDate, returnDate,
                        parts[7], parts[8]
                    );
                    records.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading borrow records file: " + e.getMessage());
        }
        return records;
    }

    // overwrites the entire borrow_records.txt file with the current list.
    public static void writeBorrowRecords(String filePath, ArrayList<BorrowRecord> records) {
        // false = overwrite (don't append)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (BorrowRecord record : records) {
                writer.write(record.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing borrow records file: " + e.getMessage());
        }
    }
}

/*
Example data format:
Members:
M1001,Ian Cabalquinto,ian123,password123,MEMBER
M1002,Jane Doe,jane,doe456,MEMBER
M1003,Reimar Gutierrez,rei123,123,MEMBER
A001,Admin User,admin,admin123,ADMIN
*/