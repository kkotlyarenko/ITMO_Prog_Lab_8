package main.gui;

import main.collection.UserCollection;
import main.console.Request;
import main.console.Response;
import main.socket.Client;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static javax.swing.SwingUtilities.getRootPane;

public class RemoveByDistWindow {

    private static JPanel distPanel;
    private static JTextField distField;
    private static JButton removeButton;

    public static void createAndShowGUI(ResourceBundle bundle) {
        JFrame frame = new JFrame(bundle.getString("window.title.remove"));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 175);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        getRootPane(frame).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        //Dist panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel idLabel = new JLabel(bundle.getString("label.countlessthan"));
        frame.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        distField = new JTextField();
        distField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(distField, gbc);

        // Remove button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        removeButton = new JButton(bundle.getString("button.remove"));
        frame.add(removeButton, gbc);

        // Add button listener
        removeButton.addActionListener(e -> {
            String id = distField.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, bundle.getString("error.empty"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int idValue = Integer.parseInt(id);
                    if (idValue < 1) {
                        JOptionPane.showMessageDialog(frame, bundle.getString("error.number"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
                    } else {
                        Request request = new Request("remove_any_by_distance", new UserCollection(), String.valueOf(idValue), Client.username, Client.password);
                        Response response = Client.sendAndGet(request);
                        System.out.println(response.getText());

                        JOptionPane.showMessageDialog(frame, bundle.getString("success.remove"), bundle.getString("success.title"), JOptionPane.INFORMATION_MESSAGE);
                        TableWindow.updateTableWindowTexts();
                        frame.dispose();
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
