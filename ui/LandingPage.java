package ui;

import main.Main;

import javax.swing.*;
import java.awt.*;

public class LandingPage extends JPanel {

    // this is the main class that controls everything in the app. We need it to switch pages.
    private Main mainApp;

    // holds the background picture for this page.
    private Image backgroundImage;

    private JLabel fLabel, sLabel, tLabel;   
    private JButton loginButton, signupButton, guestButton;

    public LandingPage(Main mainApp) {
        this.mainApp = mainApp;

        // BG IMAGE
        // we load the background image from the resource folder.
        backgroundImage = new ImageIcon(
                getClass().getResource("/bgimages/landing_pagebg.png")
        ).getImage();

        // this method adds all the labels and buttons to the page.
        initComponents();
    }

    // this method is special and is used to draw things on the panel, like our background image.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // we draw the background image to fill the whole screen.
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // this is where we create and set up all the parts of the page (buttons, text).
    private void initComponents() {

        // we use 'null' layout so we can set the exact position of every item using setBounds.
        setLayout(null);

        // TITLE: Welcome Text 
        fLabel = new JLabel("WELCOME TO", SwingConstants.CENTER);
        fLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 65));
        fLabel.setForeground(Color.WHITE);
        fLabel.setBounds(380, 150, 800, 60);
        add(fLabel);

        // TITLE: Library Name
        sLabel = new JLabel("F4 LIBRARY", SwingConstants.CENTER);
        sLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 150));
        sLabel.setForeground(Color.WHITE);
        sLabel.setBounds(300, 210, 950, 110);
        add(sLabel);

        // TITLE: Tagline
        tLabel = new JLabel("Discover knowledge, borrow with ease.", SwingConstants.CENTER);
        tLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
        tLabel.setForeground(Color.WHITE);
        tLabel.setBounds(298, 290, 950, 110);
        add(tLabel);

        // BUTTON: Login
        loginButton = new JButton("Log In");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 25));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.decode("#18417f"));
        loginButton.setBounds(870, 600, 250, 65);
        add(loginButton);

        // when the Log In button is clicked, we tell the main app to show the Login page.
        loginButton.addActionListener(e -> {
            mainApp.switchToPage(Main.LOGIN_PAGE);
        });

        // BUTTON: Sign Up
        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Verdana", Font.BOLD, 25));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(Color.decode("#18417f"));
        signupButton.setBounds(1170, 600, 250, 65);
        add(signupButton);

        // when the Sign Up button is clicked, we tell the main app to show the Sign Up page.
        signupButton.addActionListener(e -> {
            mainApp.switchToPage(Main.SIGNUP_PAGE);
        });

        // BUTTON: Guest
        guestButton = new JButton("Guest");
        guestButton.setFont(new Font("Verdana", Font.BOLD, 25));
        guestButton.setForeground(Color.WHITE);
        guestButton.setBackground(Color.decode("#18417f"));
        guestButton.setBounds(1020, 690, 250, 65);
        add(guestButton);

        // when the Guest button is clicked, we go to the home page without logging in. this is example of lambda expression.
        guestButton.addActionListener(e -> {
            mainApp.continueAsGuest();
        });

        /*non lambda 
        guestButton.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.continueAsGuest();
            }
        });
        */
    }
}
