package main.gui;

import main.console.Request;
import main.console.Response;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static javax.swing.SwingUtilities.getRootPane;

public class LoginWindow implements Serializable {
    private static JLabel userLabel;
    private static JLabel passwordLabel;
    private static JTextField userText;
    private static JPasswordField passwordText;
    private static JButton loginButton;
    private static JLabel messageLabel;
    private static JComboBox<String> languageBox;

    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    static ResourceBundle bundle = main.socket.Client.bundle;
    static JFrame frame = main.socket.Client.frame;
    public static void showLoginWindow(Locale currentLocale) {
        frame = new JFrame(bundle.getString("window.title.login"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        getRootPane(frame).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        userLabel = new JLabel(bundle.getString("label.username"));
        userText = new JTextField();
        passwordLabel = new JLabel(bundle.getString("label.password"));
        passwordText = new JPasswordField();
        loginButton = new JButton(bundle.getString("button.login"));
        messageLabel = new JLabel("");

        String[] languages = { "English", "Русский", "Slovenščina", "Magyar", "Español" };
        languageBox = new JComboBox<>(languages);
        languageBox.setSelectedIndex(getLanguageIndex(currentLocale));

        inputPanel.add(userLabel);
        inputPanel.add(userText);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordText);
        inputPanel.add(languageBox);
        inputPanel.add(loginButton);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(messageLabel, BorderLayout.SOUTH);

        languageBox.addActionListener(e -> {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedLanguage = (String) comboBox.getSelectedItem();
            Locale newLocale = getLocaleForLanguage(selectedLanguage);

            bundle = ResourceBundle.getBundle("messages", newLocale);
            updateLoginWindowTexts();
        });

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText(bundle.getString("message.invalid"));
                return;
            }

            int info = authenticate(username, password);

            if (info == -1) {
                return;
            }

            else if (info == 0) {
                JOptionPane.showMessageDialog(frame, bundle.getString("message.success"), bundle.getString("title.success"), JOptionPane.INFORMATION_MESSAGE);
            }
            else if (info == 1) {
                TableWindow.showTableWindow(bundle.getLocale());
            }
            else{
                JOptionPane.showMessageDialog(frame, bundle.getString("message.error"), bundle.getString("title.error"), JOptionPane.ERROR_MESSAGE);
            }

        });

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }


    private static void updateLoginWindowTexts() {
        frame.setTitle(bundle.getString("window.title.login"));
        userLabel.setText(bundle.getString("label.username"));
        passwordLabel.setText(bundle.getString("label.password"));
        loginButton.setText(bundle.getString("button.login"));
        messageLabel.setText("");
    }

    private static int authenticate(String username, String password) {
        main.socket.Client.username = username;
        main.socket.Client.password = password;
        int info = main.socket.Client.connect(username, password);
        if (info == -1) {
            messageLabel.setText(bundle.getString("message.error"));
        }
        if (info == 0) {
            messageLabel.setText(bundle.getString("message.success"));
        }
        return info;
    }

    private static Locale getLocaleForLanguage(String language) {
        switch (language) {
            case "Русский":
                return new Locale("ru", "RU");
            case "Slovenščina":
                return new Locale("sl", "SI");
            case "Magyar":
                return new Locale("hu", "HU");
            case "Español":
                return new Locale("es", "EC");
            case "English":
            default:
                return new Locale("en", "US");
        }
    }

    private static int getLanguageIndex(Locale locale) {
        switch (locale.getLanguage()) {
            case "ru":
                return 1;
            case "sl":
                return 2;
            case "hu":
                return 3;
            case "es":
                return 4;
            case "en":
            default:
                return 0;
        }
    }
}
