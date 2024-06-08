package main.format;

import main.models.Coordinates;
import main.models.LocationFrom;
import main.models.LocationTo;
import main.models.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostgresFormatWorker implements FormatWorker<Route> {
    private final String url;
    private final String user;
    private final String password;

    public PostgresFormatWorker(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public List<Route> readFile() {
        String SQL = "SELECT * FROM routes";
        List<Route> routes = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Route route = new Route();
                route.setId(rs.getInt("id"));
                route.setName(rs.getString("name"));

                Coordinates coordinates = new Coordinates();
                coordinates.setX(rs.getLong("coordinates_x"));
                coordinates.setY(rs.getDouble("coordinates_y"));
                route.setCoordinates(coordinates);

                LocationFrom locationFrom = new LocationFrom();
                locationFrom.setX(rs.getLong("from_x"));
                locationFrom.setY(rs.getInt("from_y"));
                locationFrom.setName(rs.getString("from_name"));
                route.setFrom(locationFrom);

                LocationTo locationTo = new LocationTo();
                locationTo.setX(rs.getLong("to_x"));
                locationTo.setY(rs.getLong("to_y"));
                locationTo.setZ(rs.getDouble("to_z"));
                route.setTo(locationTo);

                route.setDistance(rs.getLong("distance"));

                routes.add(route);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return routes;
    }

    @Override
    public List<Route> readString(String csvContent) {
        throw new UnsupportedOperationException("readString method is not supported for PostgreSQL");
    }

    @Override
    public void writeFile(Collection<Route> values) {
        String SQL = "INSERT INTO routes (name, coordinates_x, coordinates_y, from_x, from_y, from_name, to_x, to_y, to_z, distance, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            for (Route route : values) {
                pstmt.setString(1, route.getName());
                pstmt.setDouble(2, route.getCoordinates().getX());
                pstmt.setDouble(3, route.getCoordinates().getY());
                pstmt.setDouble(4, route.getFrom().getX());
                pstmt.setDouble(5, route.getFrom().getY());
                pstmt.setString(6, route.getFrom().getName());
                pstmt.setDouble(7, route.getTo().getX());
                pstmt.setDouble(8, route.getTo().getY());
                pstmt.setDouble(9, route.getTo().getZ());
                pstmt.setLong(10, route.getDistance());
                pstmt.executeUpdate();
            }
            System.out.println("Collection saved to PostgreSQL database");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String writeString(List<Route> values) {
        throw new UnsupportedOperationException("writeString method is not supported for PostgreSQL");
    }

    @Override
    public void removeById(Long id) {
        String SQL = "DELETE FROM routes WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Route with ID " + id + " has been deleted.");
            } else {
                System.out.println("No route found with ID " + id);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
