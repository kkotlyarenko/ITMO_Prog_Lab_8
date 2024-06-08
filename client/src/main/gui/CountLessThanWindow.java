package main.gui;

import main.collection.UserCollection;
import main.console.Request;
import main.console.Response;
import main.socket.Client;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static javax.swing.SwingUtilities.getRootPane;

public class CountLessThanWindow {

    private static JPanel distPanel;
    private static JTextField distField;
    private static JButton countButton;

    public static void createAndShowGUI(ResourceBundle bundle) {
        JFrame frame = new JFrame(bundle.getString("window.title.countless"));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 175);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        getRootPane(frame).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        //ID panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel cntLabel = new JLabel(bundle.getString("label.countlessthan"));
        frame.add(cntLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        distField = new JTextField();
        distField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(distField, gbc);

        // Count button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        countButton = new JButton(bundle.getString("button.count"));
        frame.add(countButton, gbc);

        // Add button listener
        countButton.addActionListener(e -> {
            String dist = distField.getText();
            if (dist.isEmpty()) {
                JOptionPane.showMessageDialog(frame, bundle.getString("error.empty"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int idValue = Integer.parseInt(dist);
                    if (idValue < 1) {
                        JOptionPane.showMessageDialog(frame, bundle.getString("error.id"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
                    } else {
                        Request request = new Request("count_less_than_distance", new UserCollection(), String.valueOf(idValue), Client.username, Client.password);
                        Response response = Client.sendAndGet(request);

                        JOptionPane.showMessageDialog(frame, bundle.getString("label.countresult") + response.getText(), bundle.getString("success.title"), JOptionPane.INFORMATION_MESSAGE);

                        TableWindow.updateTableWindowTexts();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, bundle.getString("error.number"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
