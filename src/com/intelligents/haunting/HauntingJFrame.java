package com.intelligents.haunting;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HauntingJFrame extends JWindow implements ActionListener {

    private final JWindow window = new JWindow();

    private final JTextField userInput = new JTextField();
    private final JButton showJournal = new JButton("Journal");
    private final JButton showMap = new JButton("Map");
    private final JTextPane textDisplayGameWindow = new JTextPane();
    private JTextPane textDisplayJournal;
    private JFrame gameFrame;
    private boolean calledOnce=false;
    private final Game game;
    private final Controller controller;
    private final ClassLoader cl;
    private final String pathStartResources = "com/intelligents/resources/";
    private final String pathStartImages = pathStartResources + "Images/";
    JTextArea playerLocationArea = new JTextArea();
    private final MusicPlayer themeSong;
    private JFrame mapFrame;
    private JFrame journalFrame;
    private JFrame rulesFrame;
    private final JPanel gamePanel = new JPanel(new BorderLayout(0,5));
    private final JPanel contentPanel = new JPanel(new FlowLayout());
    boolean playerWantsToContinuePlaying = true;

    public HauntingJFrame() {
        cl = getClass().getClassLoader();
        String pathStartSounds = pathStartResources + "Sounds/";
        themeSong = new MusicPlayer(pathStartSounds + "VIKINGS THEME SONG.wav", cl);
        splashWindow(cl);
        gameWindow();
        game = new Game(this, pathStartSounds, pathStartResources, cl);
        controller = new Controller(game);
    }

    public void setControllerFlag() {
        controller.setReadyToGuess(false);
    }


    private void gameWindow() {
        gameFrame = new JFrame("The Haunting of Amazon Hill");
        gameFrame.setMinimumSize(new Dimension(1000, 800));
        gamePanel.setBackground(Color.DARK_GRAY);

        JPanel textDisplayPanel = new JPanel();
        JPanel userInputPanel = new JPanel(new GridLayout());
        JPanel buttonsAndInfoPanel = new JPanel();
        JPanel playerLocationPanel = new JPanel(new BorderLayout());


        textDisplayPanel.setBackground(Color.black);
        buttonsAndInfoPanel.setBackground(Color.DARK_GRAY);
        buttonsAndInfoPanel.setPreferredSize(new Dimension(200, 300));
        buttonsAndInfoPanel.setLayout(new FlowLayout());
        buttonsAndInfoPanel.add(showJournal);
        buttonsAndInfoPanel.add(Box.createHorizontalGlue());
        buttonsAndInfoPanel.add(showMap);
        buttonsAndInfoPanel.add(Box.createHorizontalGlue());

        showJournal.addActionListener(this);
        showMap.addActionListener(this);

        // Unchangeable to user, a textbox to display game text
        DefaultCaret caret = (DefaultCaret) textDisplayGameWindow.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplayGameWindow.setCaretPosition(0);
        textDisplayGameWindow.setText("What game would you like to play?\n " +
                "Chapter 1. The Haunting of Amazon Hill\n " +
                "Chapter 2. Chasing Ghosts (COMING SOON!)\n " +
                "Chapter 3. Hangman's Gallows (COMING SOON!)\n " +
                "Press 4. to load saved game\n" +
                "Please enter a number for Chapter: ");
        textDisplayGameWindow.setBorder(BorderFactory.createBevelBorder(1));
        textDisplayGameWindow.setForeground(Color.white);
        textDisplayGameWindow.setFont(new Font("Comic Sans", Font.BOLD, 15));
        textDisplayGameWindow.setEditable(false);
        textDisplayGameWindow.setBackground(Color.DARK_GRAY);

        // Allows for scrolling if text extends beyond panel
        JScrollPane scrollPane = new JScrollPane(textDisplayGameWindow);
        scrollPane.setPreferredSize(new Dimension(700, 600));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Text field for user to input
        userInputPanel.setPreferredSize(new Dimension(200, 150));
        userInput.setPreferredSize(new Dimension(200, 50));
        userInput.setFont(new Font("Consolas", Font.CENTER_BASELINE, 15));
        userInput.setForeground(Color.white);
        userInput.setBackground(Color.DARK_GRAY);
        userInput.setCaretColor(Color.YELLOW);


        userInputPanel.setBackground(Color.BLACK);
        textDisplayPanel.add(scrollPane);
        userInputPanel.setLayout(new GridLayout(3, 1));
        userInputPanel.add(userInput);
        userInputPanel.add(buttonsAndInfoPanel);
        userInputPanel.add(playerLocationPanel);


        playerLocationPanel.setBackground(Color.white);
        playerLocationPanel.setLayout(new GridBagLayout());
        playerLocationArea.setPreferredSize(new Dimension(200, 75));
        playerLocationArea.setForeground(Color.blue);
        playerLocationArea.setEditable(false);
        playerLocationPanel.add(playerLocationArea);

        JMenuBar menubar = new JMenuBar();
        JMenu utilities = new JMenu("Help");
        JMenuItem rules = new JMenuItem(new AbstractAction("Rules") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showRules();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        JMenuItem save = new JMenuItem(new AbstractAction("Save Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.processInput(true, new String[]{"save"}, 0);
            }
        });
        JMenuItem load = new JMenuItem(new AbstractAction("Load Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.intro(new String[]{"4"});
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        utilities.add(rules);
        utilities.add(save);
        utilities.add(load);
        menubar.add(utilities);
        menubar.setPreferredSize(new Dimension(100, 50));
        contentPanel.setBackground(Color.DARK_GRAY);
        gamePanel.add(menubar, BorderLayout.PAGE_START);
        gamePanel.add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(textDisplayPanel);
        contentPanel.add(userInputPanel);
        gameFrame.add(gamePanel);
        gameFrame.pack();


        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        if (!calledOnce) {
            userInput.addActionListener(this);
            calledOnce = true;
        }
        userInput.requestFocusInWindow();

        gameFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Journal")) {
            game.processInput(true, new String[]{"read"}, 0);
        }
        if (e.getActionCommand().equals("Map")) {
            game.processInput(true, new String[]{"open"}, 0);
        }
        if (e.getSource() == userInput) {
            String[] userResponse = userInput.getText().strip().toLowerCase().split(" ");
            userInput.setText("");
            try {
                controller.kickoffResponse(userResponse, textDisplayGameWindow.getText());
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void setTextBox(String text, Color color) {
        textDisplayGameWindow.setForeground(color);
        textDisplayGameWindow.setText(text);
    }

    public void appendTextColorAndDisplay(String textToDisplay, Color color) throws BadLocationException {
        StyledDocument doc = textDisplayGameWindow.getStyledDocument();
        Style style = textDisplayGameWindow.addStyle("", null);
        StyleConstants.setForeground(style, color);
        doc.insertString(doc.getLength(), textToDisplay, style);
    }

    private void showJournal() {
        // Closes old window if opened before
        if (journalFrame != null) journalFrame.dispatchEvent(new WindowEvent(journalFrame, WindowEvent.WINDOW_CLOSING));
        // Opens new window at current room
        journalFrame = new JFrame("Journal");
        journalFrame.setSize(700, 700);

        textDisplayJournal = new JTextPane();
        DefaultCaret caret = (DefaultCaret) textDisplayJournal.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplayJournal.setCaretPosition(0);
        textDisplayJournal.setBorder(BorderFactory.createBevelBorder(1));
        textDisplayJournal.setForeground(new Color(0, 60, 70));
        textDisplayJournal.setFont(new Font("Comic Sans", Font.BOLD, 15));
        textDisplayJournal.setEditable(false);
        textDisplayJournal.setBackground(Color.DARK_GRAY);

        // Allows for scrolling if text extends beyond panel
        JScrollPane scrollPane = new JScrollPane(textDisplayJournal);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        journalFrame.add(scrollPane, BorderLayout.CENTER);

        journalFrame.setLocationRelativeTo(null);
        journalFrame.setVisible(true);
    }

    public void setTextBoxJournal(String text, Color color) {
        showJournal();
        textDisplayJournal.setForeground(color);
        textDisplayJournal.setText(text);
    }

    public void appendTextColorAndDisplayJournal(String textToDisplay, Color color) throws BadLocationException {
        StyledDocument doc = textDisplayJournal.getStyledDocument();
        Style style = textDisplayJournal.addStyle("", null);
        StyleConstants.setForeground(style, color);
        doc.insertString(doc.getLength(), textToDisplay, style);
    }

    void showMap() {
        String currentRoom = game.currentRoom.replaceAll("\\s", "");

        // Closes old window if opened before
        if (mapFrame != null) mapFrame.dispatchEvent(new WindowEvent(mapFrame, WindowEvent.WINDOW_CLOSING));
        // Opens new window at current room
        mapFrame = new JFrame("Map");
        mapFrame.setSize(450, 500);

        JLabel picLabel = new JLabel();
        picLabel.setIcon(new ImageIcon(Objects.requireNonNull(cl.getResource(pathStartImages + "Map(" + currentRoom + ").png"))));


        mapFrame.add(picLabel, BorderLayout.CENTER);

        mapFrame.setLocationRelativeTo(null);
        mapFrame.setVisible(true);
    }

    void showRules() throws IOException {
        // Closes old window if opened before
        if (rulesFrame != null) rulesFrame.dispatchEvent(new WindowEvent(rulesFrame, WindowEvent.WINDOW_CLOSING));
        // Opens new window at current room
        rulesFrame = new JFrame("Rules");
        rulesFrame.setSize(700, 500);

        JTextPane textDisplayRules = new JTextPane();
        DefaultCaret caret = (DefaultCaret) textDisplayRules.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textDisplayRules.setCaretPosition(0);
        textDisplayRules.setBorder(BorderFactory.createBevelBorder(1));
        textDisplayRules.setForeground(new Color(255,255,255));
        textDisplayRules.setFont(new Font("Comic Sans", Font.BOLD, 15));
        textDisplayRules.setEditable(false);
        textDisplayRules.setBackground(Color.DARK_GRAY);

        // Allows for scrolling if text extends beyond panel
        JScrollPane scrollPane = new JScrollPane(textDisplayRules);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        String text = game.getFileReader().fileReader(pathStartResources, "Rules", cl);
        textDisplayRules.setText(text);
        rulesFrame.add(scrollPane, BorderLayout.CENTER);

        rulesFrame.setLocationRelativeTo(null);
        rulesFrame.setVisible(true);
    }

    private void splashWindow(ClassLoader cl) {
        themeSong.playSoundEffect();
        themeSong.setVolume((float) -10.69);

        JLabel image = new JLabel();
        image.setIcon(new ImageIcon(Objects.requireNonNull(cl.getResource(pathStartImages + "asciiSplashScreen.png"))));


        window.getContentPane().add(image);
        window.setBounds(500, 150, 300, 200);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        window.setVisible(false);
        window.dispose();
    }

    public void stopThemeSong() {
        themeSong.stopSoundEffect();
    }

    boolean quitGame() throws InterruptedException {
        if (!playerWantsToContinuePlaying) {
            // Disable from continuing the game
            userInput.setEditable(false);
            closeWindows();
        }

        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you would like to quit?\n" +
                        "Click yes to quit and close game.\n" +
                        "Click no to return to game.",
                "QUIT GAME?",
                JOptionPane.YES_NO_OPTION);

        if (JOptionPane.YES_OPTION == result) {
            // Disable from continuing the game
            userInput.setEditable(false);
            // Set a timer before closing windows
            TimeUnit.SECONDS.sleep(3);
            closeWindows();
            return true;
        }else{
            return false;
        }
    }

   private void closeWindows() {
        // Close outside windows
        if (mapFrame != null) mapFrame.dispatchEvent(new WindowEvent(mapFrame, WindowEvent.WINDOW_CLOSING));
        if (journalFrame != null) journalFrame.dispatchEvent(new WindowEvent(journalFrame, WindowEvent.WINDOW_CLOSING));
       if (rulesFrame != null) rulesFrame.dispatchEvent(new WindowEvent(rulesFrame, WindowEvent.WINDOW_CLOSING));
        // Close main game window
        if (gameFrame != null) gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING));
    }

}