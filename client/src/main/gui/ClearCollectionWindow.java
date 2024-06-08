package main.gui;

import main.collection.UserCollection;
import main.console.Request;
import main.console.Response;
import main.socket.Client;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static javax.swing.SwingUtilities.getRootPane;

public class ClearCollectionWindow {

    private static JLabel clearLabel;
    private static JButton clearButton;

    public static void createAndShowGUI(ResourceBundle bundle) {
        JFrame frame = new JFrame(bundle.getString("window.title.clearcollection"));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 125);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        getRootPane(frame).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        //Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.CENTER;
        clearLabel = new JLabel(bundle.getString("label.clear"));
        frame.add(clearLabel, gbc);

        // Remove button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        clearButton = new JButton(bundle.getString("button.clear"));
        frame.add(clearButton, gbc);

        // Add button listener
        clearButton.addActionListener(e -> {
                try {
                    Request request = new Request("clear", new UserCollection(), "", Client.username, Client.password);
                    Response response = Client.sendAndGet(request);
                    System.out.println(response.getText());

                    JOptionPane.showMessageDialog(frame, bundle.getString("success.clear"), bundle.getString("success.title"), JOptionPane.INFORMATION_MESSAGE);
                    TableWindow.updateTableWindowTexts();
                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, bundle.getString("error.number"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
                }
        });

        frame.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
