package main;

import models.*;
import ui.*;
import utils.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {

    //TITLE: Main variables of the whole app
    private JFrame window;          // the main window of the program
    private JPanel mainPanel;       // this holds all the pages
    private CardLayout cardLayout;  // this helps switch between pages

    //TITLE: Lists that store all data
    private ArrayList<Book> allBooks; 
    private ArrayList<User> allUsers; 
    private ArrayList<BorrowRecord> allRecords;

    private User currentUser; // who is logged in (or null if guest)

    //TITLE: Names for each page so CardLayout knows which is which
    public static final String LANDING_PAGE = "LANDING";
    public static final String LOGIN_PAGE = "LOGIN";
    public static final String SIGNUP_PAGE = "SIGNUP";
    public static final String HOME_PAGE = "HOME";
    public static final String CATEGORIES_PAGE = "CATEGORIES";
    public static final String BORROW_PAGE = "BORROW";
    public static final String RETURN_PAGE = "RETURN";
    public static final String ADMIN_PAGE = "ADMIN";
    public static final String ABOUT_PAGE = "ABOUT";

    //TITLE: File locations for loading data
    public static final String BOOKS_FILE = "data/books.txt";
    public static final String MEMBERS_FILE = "data/members.txt";
    public static final String RECORDS_FILE = "data/borrow_records.txt";

    //TITLE: Program starts here
    public static void main(String[] args) {
        // run the GUI safely in the Swing thread
        SwingUtilities.invokeLater(() -> {
            Main app = new Main(); 
            app.loadData();          // load books, users, records from files
            app.createAndShowGUI();  // build and show the window
        });
    }

    //TITLE: This sets up the main window and adds all the pages
    private void createAndShowGUI() {

        //TITLE: Create the main window
        window = new JFrame("F4 Library System");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setLocationRelativeTo(null); // center the window
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen

        //TITLE: Set app icon on the window
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/bgimages/f4_librarylogo.png"));
            window.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Warning: Could not load window icon image.");
        }

        //TITLE: Create CardLayout so we can switch pages easily
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        //TITLE: Create each page of the program
        LandingPage landingPage = new LandingPage(this);
        LoginPage loginPage = new LoginPage(this);
        SignUpPage signUpPage = new SignUpPage(this);
        HomePage homePage = new HomePage(this);
        CategoriesPage categoriesPage = new CategoriesPage(this);
        BorrowPage borrowPage = new BorrowPage(this);
        ReturnPage returnPage = new ReturnPage(this);
        AdminPage adminPage = new AdminPage(this);
        AboutPage aboutPage = new AboutPage(this);

        //TITLE: Add each page to CardLayout
        mainPanel.add(landingPage, LANDING_PAGE);
        mainPanel.add(loginPage, LOGIN_PAGE);
        mainPanel.add(signUpPage, SIGNUP_PAGE);
        mainPanel.add(homePage, HOME_PAGE);
        mainPanel.add(categoriesPage, CATEGORIES_PAGE);
        mainPanel.add(borrowPage, BORROW_PAGE);
        mainPanel.add(returnPage, RETURN_PAGE);
        mainPanel.add(adminPage, ADMIN_PAGE);
        mainPanel.add(aboutPage, ABOUT_PAGE);

        //TITLE: Add everything to the window
        window.add(mainPanel);
        window.setVisible(true);

        // show landing page first
        switchToPage(LANDING_PAGE);
    }

    //TITLE: Load book, user, and borrowing data from text files
    public void loadData() {
        allBooks = FileHandler.readBooks(BOOKS_FILE);
        allUsers = FileHandler.readUsers(MEMBERS_FILE);
        allRecords = FileHandler.readBorrowRecords(RECORDS_FILE);

        System.out.println("Data loaded. Books: " + allBooks.size() + ", Users: " + allUsers.size() + ", Records: " + allRecords.size());
    }

    //TITLE: Switch to another page using CardLayout
    public void switchToPage(String pageName) {
        cardLayout.show(mainPanel, pageName); // change page

        // refresh Home Page when entering it
        if (pageName.equals(HOME_PAGE)) {
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof HomePage) {
                    ((HomePage) comp).refresh();
                }
            }
        }

        // refresh Admin Page when entering it
        if (pageName.equals(ADMIN_PAGE)) {
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof AdminPage) {
                    ((AdminPage) comp).refreshData();
                }
            }
        }
    }

    //TITLE: Login function
    public boolean login(String username, String password) {

        // check each user to see if username and password match
        for (User user : allUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.currentUser = user; // save who logged in

                // go to admin page if admin, otherwise home page
                if (user instanceof Admin) {
                    switchToPage(ADMIN_PAGE);
                } else {
                    switchToPage(HOME_PAGE);
                }
                return true;
            }
        }
        return false; // no user found
    }

    //TITLE: Logout function
    public void logout() {
        this.currentUser = null; // clear logged in user
        switchToPage(LANDING_PAGE);
    }

    //TITLE: Enter as guest (no login)
    public void continueAsGuest() {
        this.currentUser = null; // guest has no account
        switchToPage(HOME_PAGE);
    }

    //TITLE: Getters so other pages can use the data
    public ArrayList<Book> getAllBooks() {
        return allBooks;
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public ArrayList<BorrowRecord> getAllRecords() {
        return allRecords;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
