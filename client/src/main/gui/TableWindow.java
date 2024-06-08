package main.gui;

import main.collection.CollectionManager;
import main.collection.UserCollection;
import main.console.Request;
import main.console.Response;
import main.models.Coordinates;
import main.models.LocationFrom;
import main.models.LocationTo;
import main.models.Route;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TableWindow {

    private static String[] columnNames;
    static Object[][] data;
    private static DefaultTableModel model;
    private static JTable table;
    private static JScrollPane scrollPane;
    private static JButton visualizeButton;
    private static JButton addButton;
    private static JButton editButton;
    private static JButton removeButton;
    private static JComboBox<String> langComboBox;
    private static JPanel sortPanel;
    private static JPanel upperPanel;
    private static JPanel buttonPanel;
    private static JPanel visButPannel;
    private static JComboBox<String> sortComboBox;
    private static JComboBox<String> moreComboBox;

    static ResourceBundle bundle = LoginWindow.bundle;
    static JFrame frame = LoginWindow.frame;

    private static String moreComboBoxFixedLabel;

    public static void showTableWindow(Locale locale) {
        frame.getContentPane().removeAll();
        frame.setTitle(bundle.getString("window.title.table"));
        frame.setSize(1000, 400);
        frame.setLayout(new BorderLayout());

        columnNames = new String[]{
                bundle.getString("table.column1"),
                bundle.getString("table.column2"),
                bundle.getString("table.column3"),
                bundle.getString("table.column4"),
                bundle.getString("table.column5"),
                bundle.getString("table.column6"),
                bundle.getString("table.column7"),
                bundle.getString("table.column8"),
                bundle.getString("table.column9"),
                bundle.getString("table.column10"),
                bundle.getString("table.column11"),
                bundle.getString("table.column12"),
                bundle.getString("table.column13")
        };

        updateRoutes();

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        visualizeButton = new JButton(bundle.getString("button.visualize"));
        addButton = new JButton(bundle.getString("button.add"));
        editButton = new JButton(bundle.getString("button.edit"));
        removeButton = new JButton(bundle.getString("button.remove"));

        String[] moreOptions = {
                bundle.getString("more.option1"),
                bundle.getString("more.option2"),
                bundle.getString("more.option3"),
                bundle.getString("more.option4")
        };
        moreComboBoxFixedLabel = bundle.getString("button.more");
        moreComboBox = createFixedLabelComboBox(moreComboBoxFixedLabel, moreOptions);

        String[] languages = {"English", "Русский", "Slovenščina", "Magyar", "Español"};

        langComboBox = new JComboBox<>(languages);
        langComboBox.setSelectedIndex(getLanguageIndex(locale));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(moreComboBox);
        buttonPanel.add(langComboBox);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());

        sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] sortOptions = {bundle.getString("sort.option.none")};
        sortOptions = Arrays.copyOf(sortOptions, sortOptions.length + columnNames.length);
        System.arraycopy(columnNames, 0, sortOptions, 1, columnNames.length);
        sortComboBox = new JComboBox<>(sortOptions);

        langComboBox.addActionListener(e -> {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedLanguage = (String) comboBox.getSelectedItem();
            Locale newLocale = getLocaleForLanguage(selectedLanguage);

            bundle = ResourceBundle.getBundle("messages", newLocale);
            updateTableWindowTexts();
        });

        sortComboBox.addActionListener(e -> {
            String selectedOption = (String) sortComboBox.getSelectedItem();
            if (selectedOption != null && !selectedOption.equals(bundle.getString("sort.option.none"))) {
                int columnIndex = Arrays.asList(columnNames).indexOf(selectedOption);
                sortTableData(model, columnIndex);
            } else {
                model.setDataVector(data, columnNames);
            }
        });

        // More ComboBox Listener
        moreComboBox.addActionListener(e -> {
            String selectedOption = (String) moreComboBox.getSelectedItem();
            if (selectedOption != null) {
                if (selectedOption.equals(bundle.getString("more.option1"))) {
                    ClearCollectionWindow.createAndShowGUI(bundle);
                } else if (selectedOption.equals(bundle.getString("more.option2"))) {
                    CountLessThanWindow.createAndShowGUI(bundle);
                } else if (selectedOption.equals(bundle.getString("more.option3"))) {
                    RemoveByDistWindow.createAndShowGUI(bundle);
                } else if (selectedOption.equals(bundle.getString("more.option4"))) {
                    Request request = new Request("info", new UserCollection(), "", main.socket.Client.username, main.socket.Client.password);
                    Response response = main.socket.Client.sendAndGet(request);
                    JOptionPane.showMessageDialog(frame, response.getText(), bundle.getString("success.title"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Buttons Listeners
        addButton.addActionListener(e -> {
            AddWindow.createAndShowGUI(bundle);
        });

        editButton.addActionListener(e -> {
            EditWindow.createAndShowGUI(bundle);
        });

        removeButton.addActionListener(e -> {
            RemoveWindow.createAndShowGUI(bundle);
        });

        visualizeButton.addActionListener(e -> {
            VisualizationWindow.createAndShowGUI(bundle);
        });

        sortPanel.add(new JLabel(bundle.getString("label.sortBy")));
        sortPanel.add(sortComboBox);

        upperPanel.add(sortPanel, BorderLayout.WEST);
        visButPannel = new JPanel();
        visButPannel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        visButPannel.add(visualizeButton);
        upperPanel.add(visButPannel, BorderLayout.EAST);

        frame.add(upperPanel, BorderLayout.NORTH);

        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    private static void sortTableData(DefaultTableModel model, int columnIndex) {
        int rowCount = model.getRowCount();
        int colCount = model.getColumnCount();
        Object[][] tableData = new Object[rowCount][colCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                tableData[i][j] = model.getValueAt(i, j);
            }
        }

        Arrays.sort(tableData, Comparator.comparing(row -> (Comparable) row[columnIndex]));

        Object[] columnIdentifiers = new Object[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            columnIdentifiers[i] = model.getColumnName(i);
        }

        model.setDataVector(tableData, columnIdentifiers);
    }

    public static void updateTableWindowTexts() {
        frame.setTitle(bundle.getString("window.title.table"));
        addButton.setText(bundle.getString("button.add"));
        editButton.setText(bundle.getString("button.edit"));
        removeButton.setText(bundle.getString("button.remove"));
        visualizeButton.setText(bundle.getString("button.visualize"));

        data = new Object[][]{{}};
        updateRoutes();

        columnNames = new String[]{
                bundle.getString("table.column1"),
                bundle.getString("table.column2"),
                bundle.getString("table.column3"),
                bundle.getString("table.column4"),
                bundle.getString("table.column5"),
                bundle.getString("table.column6"),
                bundle.getString("table.column7"),
                bundle.getString("table.column8"),
                bundle.getString("table.column9"),
                bundle.getString("table.column10"),
                bundle.getString("table.column11"),
                bundle.getString("table.column12"),
                bundle.getString("table.column13")
        };

        model = new DefaultTableModel(data, columnNames);

        table.setModel(model);

        sortPanel.removeAll();
        sortPanel.add(new JLabel(bundle.getString("label.sortBy")));
        sortComboBox.removeAllItems();
        sortComboBox.addItem(bundle.getString("sort.option.none"));
        for (String columnName : columnNames) {
            sortComboBox.addItem(columnName);
        }
        sortPanel.add(sortComboBox);
        sortPanel.revalidate();
        sortPanel.repaint();

        String[] moreOptions = {
                bundle.getString("more.option1"),
                bundle.getString("more.option2"),
                bundle.getString("more.option3"),
                bundle.getString("more.option4")
        };
        moreComboBoxFixedLabel = bundle.getString("button.more");

        ActionListener[] listeners = moreComboBox.getActionListeners();
        for (ActionListener listener : listeners) {
            moreComboBox.removeActionListener(listener);
        }

        updateFixedLabelComboBox(moreComboBox, moreComboBoxFixedLabel, moreOptions);

        for (ActionListener listener : listeners) {
            moreComboBox.addActionListener(listener);
        }

        frame.revalidate();
        frame.repaint();
    }

    private static void updateRoutes() {

        Request request = new Request("show", new UserCollection(), "", main.socket.Client.username, main.socket.Client.password);
        Response response = main.socket.Client.sendAndGet(request);

        List<Route> newroutes = parseRoutes(response.getText()); //kostyl'

        data = new Object[newroutes.size()][13];
        for (int i = 0; i < newroutes.size(); i++) {
            Route route = newroutes.get(i);
            data[i] = new Object[]{
                    route.getId(),
                    route.getCreatedBy(),
                    route.getName(),
                    route.getCoordinates().getX(),
                    route.getCoordinates().getY(),
                    route.getCreationDate().toString(),
                    route.getFrom().getX(),
                    route.getFrom().getY(),
                    route.getFrom().getName(),
                    route.getTo().getX(),
                    route.getTo().getY(),
                    route.getTo().getZ(),
                    route.getDistance(),
            };
        }
    }

    private static void updateFixedLabelComboBox(JComboBox<String> comboBox, String fixedLabel, String[] items) {
        comboBox.removeAllItems();
        for (String item : items) {
            comboBox.addItem(item);
        }
        comboBox.setRenderer(new ListCellRenderer<String>() {
            private final JLabel label = new JLabel(fixedLabel);

            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                if (index == -1) {
                    return label;
                } else {
                    return new JLabel(value);
                }
            }
        });
    }

    private static JComboBox<String> createFixedLabelComboBox(String fixedLabel, String[] comboBoxItems) {
        JComboBox<String> comboBox = new JComboBox<>(comboBoxItems);

        comboBox.setRenderer(new ListCellRenderer<String>() {
            private final JLabel label = new JLabel(fixedLabel);

            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                if (index == -1) {
                    return label;
                } else {
                    return new JLabel(value);
                }
            }
        });

        return comboBox;
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

    public static List<Route> parseRoutes(String text) {
        List<Route> routes = new ArrayList<>();
        Pattern routePattern = Pattern.compile(
                "Route\\{id=(\\d+), created_by='(\\d+)', name='(\\w+)', coordinates=x: (\\d+), y: (\\d+\\.\\d+), creationDate=(.*?), from=x: (\\d+), y: (\\d+), name: (\\w+), to=x: (\\d+), y: (\\d+), z: (\\d+\\.\\d+), distance=(\\d+)\\}"
        );
        Matcher matcher = routePattern.matcher(text);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        while (matcher.find()) {
            Route route = new Route();
            route.setId(Integer.parseInt(matcher.group(1)));
            route.setCreatedBy(matcher.group(2));
            route.setName(matcher.group(3));
            Coordinates coordinates = new Coordinates();
            coordinates.setX(Long.parseLong(matcher.group(4)));
            coordinates.setY(Double.parseDouble(matcher.group(5)));
            route.setCoordinates(coordinates);
            route.setCreationDate(LocalDateTime.parse(matcher.group(6), formatter));
            LocationFrom from = new LocationFrom();
            from.setX(Long.parseLong(matcher.group(7)));
            from.setY(Integer.parseInt(matcher.group(8)));
            from.setName(matcher.group(9));
            route.setFrom(from);
            LocationTo to = new LocationTo();
            to.setX(Long.parseLong(matcher.group(10)));
            to.setY(Long.parseLong(matcher.group(11)));
            to.setZ(Double.parseDouble(matcher.group(12)));
            route.setTo(to);
            route.setDistance(Long.parseLong(matcher.group(13)));
            routes.add(route);
        }
        return routes;
    }
}
