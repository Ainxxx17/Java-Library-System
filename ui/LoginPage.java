package ui;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPage extends JPanel {

    private Main mainApp;

    private JTextField usernameField;  
    private JPasswordField passwordField;  
    private Image backgroundImage;

    private JPanel loginFormPanel;
    private JLabel titleLabel, unLabel, pwLabel, signupLink, guestLink;
    private JButton loginButton;

    public LoginPage(Main mainApp) {
        this.mainApp = mainApp;
        
        // BG IMAGE
        // we load the background image from the resource folder in the project.
        backgroundImage = new ImageIcon(
             getClass().getResource("/bgimages/login_pagebg.png")
        ).getImage();

        // this method adds all the labels and buttons to the page.
        initComponents();
    }

    // this method is special and is used to draw things on the panel, like our background image.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw scaled background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // this is where we create and set up all the parts of the page (buttons, text).
    private void initComponents() {

        // we use 'null' layout so we can set the exact position of every item using setBounds.
        setLayout(null);
        // make transparent to see background.
        setOpaque(false);

        // CREATE THE WHITE LOGIN PANEL
        // this is the white box in the middle where the user types their info.
        loginFormPanel = new JPanel();
        loginFormPanel.setLayout(null);
        loginFormPanel.setBackground(Color.WHITE);
        loginFormPanel.setBounds(780, 120, 630, 590);

        // ADD COMPONENTS TO THE WHITE PANEL

        // TITLE: Login Title
        titleLabel = new JLabel("Log in", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 40));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(0, 90, 630, 60);
        loginFormPanel.add(titleLabel);

        // TITLE: Username Label
        unLabel = new JLabel("User Name:", SwingConstants.LEFT);
        unLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        unLabel.setForeground(Color.BLACK);
        unLabel.setBounds(135, 170, 360, 60);
        loginFormPanel.add(unLabel);

        // the text field where the user enters the username.
        usernameField = new JTextField();
        usernameField.setBounds(135, 220, 360, 35);
        loginFormPanel.add(usernameField);

        // TITLE: Password Label
        pwLabel = new JLabel("Password:", SwingConstants.LEFT);
        pwLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        pwLabel.setForeground(Color.BLACK);
        pwLabel.setBounds(135, 275, 630, 60);
        loginFormPanel.add(pwLabel);

        // the text field where the user enters the password (hides text).
        passwordField = new JPasswordField();
        passwordField.setBounds(135, 330, 360, 35);
        loginFormPanel.add(passwordField);

        // TITLE: Log In Button
        loginButton = new JButton("Log in");
        loginButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLACK);
        loginButton.setBounds(240, 435, 150, 35);
        loginFormPanel.add(loginButton);

        // TITLE: Sign Up Link
        signupLink = new JLabel("Create an account");
        signupLink.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        signupLink.setForeground(Color.decode("#18417f"));

        // changes the mouse icon to a hand when hovering over the link.
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLink.setBounds(183, 480, 130, 30);
        loginFormPanel.add(signupLink);

        // TITLE: Guest Link
        guestLink = new JLabel("|  Guest Account");
        guestLink.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        guestLink.setForeground(Color.decode("#18417f"));
        guestLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guestLink.setBounds(320, 480, 130, 30);
        loginFormPanel.add(guestLink);

        add(loginFormPanel);

        // ACTION LISTENERS FOR BUTTONS AND LINKS

        // when the Login button is clicked, we run the handleLogin method.
        loginButton.addActionListener(e -> handleLogin());

        // when the user presses Enter in the password box, we also run handleLogin.
        passwordField.addActionListener(e -> handleLogin()); // allow login on Enter key

        // when the Guest link is clicked, we clear the boxes and switch to the Home page as a guest.
        guestLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                clearFields();
                // this MUST call the method that sets the current user to null (Guest)
                mainApp.continueAsGuest();
            }
        });

        // when the Sign Up link is clicked, we clear the boxes and switch to the Sign Up page.
        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                clearFields();
                mainApp.switchToPage(Main.SIGNUP_PAGE);
            }
        });
    }

    // this method checks the username and password against the user list.
    private void handleLogin() {
        String username = usernameField.getText();

        // new String() because they turned as char array.
        String password = new String(passwordField.getPassword());

        // check if either box is empty and show an error if they are.
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Username and password cannot be empty.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ask the main application to try logging in with the typed information.
        boolean loginSuccess = mainApp.login(username, password);

        // if login worked, clear the boxes. If it failed, show an error message.
        if (loginSuccess) {
            clearFields();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Invalid username or password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // this method simply clears the text from the username and password boxes.
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
