package main.gui;

import main.collection.UserCollection;
import main.console.Request;
import main.console.Response;
import main.models.Coordinates;
import main.models.LocationFrom;
import main.models.LocationTo;
import main.models.Route;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static javax.swing.SwingUtilities.getRootPane;

public class EditWindow {

    private static JPanel idPanel;
    private static JPanel namePanel;
    private static JPanel coordinatesPanel;
    private static JPanel xy1Panel;
    private static JPanel fromPanel;
    private static JPanel xyzPanel;
    private static JPanel toPanel;
    private static JPanel xy2Panel;
    private static JPanel distancePanel;
    private static JButton saveButton;
    private static JTextField idField;
    private static JTextField titleField;
    private static JTextField x1Field;
    private static JTextField y1Field;
    private static JTextField x2Field;
    private static JTextField y2Field;
    private static JTextField z2Field;
    private static JTextField x3Field;
    private static JTextField y3Field;
    private static JTextField name3Field;
    private static JTextField distanceField;

    public static void createAndShowGUI(ResourceBundle bundle) {
        JFrame frame = new JFrame(bundle.getString("window.title.edit"));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 415);
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
        JLabel idLabel = new JLabel("id");
        frame.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        idField = new JTextField();
        idField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(idField, gbc);

        // Name panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel nameLabel = new JLabel(bundle.getString("label.title"));
        frame.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        titleField = new JTextField();
        titleField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(titleField, gbc);

        // Coordinates panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel coordinatesLabel = new JLabel(bundle.getString("label.coordinates"));
        frame.add(coordinatesLabel, gbc);

        xy1Panel = new JPanel();
        xy1Panel.setLayout(new GridLayout(2, 2, 5, 5));
        JLabel xLabel = new JLabel("x");
        xLabel.setHorizontalAlignment(SwingConstants.CENTER);
        x1Field = new JTextField();
        x1Field.setHorizontalAlignment(JTextField.CENTER);
        JLabel yLabel = new JLabel("y");
        yLabel.setHorizontalAlignment(SwingConstants.CENTER);
        y1Field = new JTextField();
        y1Field.setHorizontalAlignment(JTextField.CENTER);
        xy1Panel.add(xLabel);
        xy1Panel.add(x1Field);
        xy1Panel.add(yLabel);
        xy1Panel.add(y1Field);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        frame.add(xy1Panel, gbc);

        // From panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel fromLabel = new JLabel(bundle.getString("label.from"));
        frame.add(fromLabel, gbc);

        xyzPanel = new JPanel();
        xyzPanel.setLayout(new GridLayout(3, 2, 5, 5));
        JLabel x2Label = new JLabel("x");
        x2Label.setHorizontalAlignment(SwingConstants.CENTER);
        x2Field = new JTextField();
        x2Field.setHorizontalAlignment(JTextField.CENTER);
        JLabel y2Label = new JLabel("y");
        y2Label.setHorizontalAlignment(SwingConstants.CENTER);
        y2Field = new JTextField();
        y2Field.setHorizontalAlignment(JTextField.CENTER);
        JLabel z2Label = new JLabel("z");
        z2Label.setHorizontalAlignment(SwingConstants.CENTER);
        z2Field = new JTextField();
        z2Field.setHorizontalAlignment(JTextField.CENTER);
        xyzPanel.add(x2Label);
        xyzPanel.add(x2Field);
        xyzPanel.add(y2Label);
        xyzPanel.add(y2Field);
        xyzPanel.add(z2Label);
        xyzPanel.add(z2Field);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        frame.add(xyzPanel, gbc);

        // To panel
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel toLabel = new JLabel(bundle.getString("label.to"));
        frame.add(toLabel, gbc);

        xy2Panel = new JPanel();
        xy2Panel.setLayout(new GridLayout(3, 2, 5, 5));
        JLabel x3Label = new JLabel("x");
        x3Label.setHorizontalAlignment(SwingConstants.CENTER);
        x3Field = new JTextField();
        x3Field.setHorizontalAlignment(JTextField.CENTER);
        JLabel y3Label = new JLabel("y");
        y3Label.setHorizontalAlignment(SwingConstants.CENTER);
        y3Field = new JTextField();
        y3Field.setHorizontalAlignment(JTextField.CENTER);
        JLabel name3Label = new JLabel(bundle.getString("label.name"));
        name3Label.setHorizontalAlignment(SwingConstants.CENTER);
        name3Field = new JTextField();
        name3Field.setHorizontalAlignment(JTextField.CENTER);
        xy2Panel.add(x3Label);
        xy2Panel.add(x3Field);
        xy2Panel.add(y3Label);
        xy2Panel.add(y3Field);
        xy2Panel.add(name3Label);
        xy2Panel.add(name3Field);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        frame.add(xy2Panel, gbc);

        // Distance panel
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel distanceLabel = new JLabel(bundle.getString("label.distance"));
        frame.add(distanceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        distanceField = new JTextField();
        distanceField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(distanceField, gbc);

        // Submit button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        saveButton = new JButton(bundle.getString("button.save"));
        frame.add(saveButton, gbc);

        // Add button listener
        saveButton.addActionListener(e -> {
            String id = idField.getText();
            String title = titleField.getText();
            String x1 = x1Field.getText();
            String y1 = y1Field.getText();
            String x2 = x2Field.getText();
            String y2 = y2Field.getText();
            String z2 = z2Field.getText();
            String x3 = x3Field.getText();
            String y3 = y3Field.getText();
            String name3 = name3Field.getText();
            String distance = distanceField.getText();
            if (id.isEmpty() || title.isEmpty() || x1.isEmpty() || y1.isEmpty() || x2.isEmpty() || y2.isEmpty() || z2.isEmpty() || x3.isEmpty() || y3.isEmpty() || name3.isEmpty() || distance.isEmpty()) {
                JOptionPane.showMessageDialog(frame, bundle.getString("error.empty"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int idVal = Integer.parseInt(id);
                    Long x1Value = Long.parseLong(x1);
                    double y1Value = Double.parseDouble(y1);
                    Long x2Value = Long.parseLong(x2);
                    int y2Value = Integer.parseInt(y2);
                    double z2Value = Double.parseDouble(z2);
                    Long x3Value = Long.parseLong(x3);
                    Long y3Value = Long.parseLong(y3);
                    Long distanceValue = Long.parseLong(distance);
                    if (idVal < 1) {
                        JOptionPane.showMessageDialog(frame, bundle.getString("error.id"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
                    } else
                    if (distanceValue <= 0) {
                        JOptionPane.showMessageDialog(frame, bundle.getString("error.number"), bundle.getString("error.title"), JOptionPane.ERROR_MESSAGE);
                    } else {
                        Route newroute = new Route();
                        newroute.setCreatedBy(main.socket.Client.username);
                        newroute.setName(title);

                        Coordinates coordinates = new Coordinates();
                        coordinates.setX(x1Value);
                        coordinates.setY(y1Value);
                        newroute.setCoordinates(coordinates);

                        LocationFrom locationFrom = new LocationFrom();
                        locationFrom.setX(x2Value);
                        locationFrom.setY(y2Value);
                        locationFrom.setName(name3);
                        newroute.setFrom(locationFrom);

                        LocationTo locationTo = new LocationTo();
                        locationTo.setX(x3Value);
                        locationTo.setY(y3Value);
                        locationTo.setZ(z2Value);
                        newroute.setTo(locationTo);

                        newroute.setDistance(distanceValue);

                        Request addroute = new Request("update", new UserCollection(), id, main.socket.Client.username, main.socket.Client.password);
                        addroute.getCollection().add(newroute);

                        Response resp = main.socket.Client.sendAndGet(addroute);
                        System.out.println(resp.getText());

                        JOptionPane.showMessageDialog(frame, bundle.getString("success.edit"), bundle.getString("success.title"), JOptionPane.INFORMATION_MESSAGE);
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
