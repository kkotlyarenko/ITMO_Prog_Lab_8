package main.format;

import main.models.*;
import java.sql.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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

    private void dropAndCreateTables() {
        String dropSQL = "DROP TABLE IF EXISTS routes CASCADE; " +
                "DROP TABLE IF EXISTS coordinates CASCADE; " +
                "DROP TABLE IF EXISTS location_from CASCADE; " +
                "DROP TABLE IF EXISTS location_to CASCADE;";

        String createCoordinatesSQL = "CREATE TABLE coordinates (" +
                "id SERIAL PRIMARY KEY, " +
                "x BIGINT NOT NULL, " +
                "y DOUBLE PRECISION NOT NULL CHECK (y <= 716)" +
                ");";

        String createLocationFromSQL = "CREATE TABLE location_from (" +
                "id SERIAL PRIMARY KEY, " +
                "x DOUBLE PRECISION NOT NULL, " +
                "y DOUBLE PRECISION NOT NULL, " +
                "name VARCHAR(255)" +
                ");";

        String createLocationToSQL = "CREATE TABLE location_to (" +
                "id SERIAL PRIMARY KEY, " +
                "x DOUBLE PRECISION NOT NULL, " +
                "y DOUBLE PRECISION NOT NULL, " +
                "z DOUBLE PRECISION NOT NULL" +
                ");";

        String createRoutesSQL = "CREATE TABLE routes (" +
                "id SERIAL PRIMARY KEY, " +
                "created_by VARCHAR(255) NOT NULL, " +
                "name VARCHAR(255) NOT NULL, " +
                "coordinates_id INTEGER NOT NULL REFERENCES coordinates(id), " +
                "creation_date TIMESTAMP NOT NULL, " +
                "from_id INTEGER REFERENCES location_from(id), " +
                "to_id INTEGER NOT NULL REFERENCES location_to(id), " +
                "distance BIGINT NOT NULL CHECK (distance > 0)" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dropSQL);
            stmt.executeUpdate(createCoordinatesSQL);
            stmt.executeUpdate(createLocationFromSQL);
            stmt.executeUpdate(createLocationToSQL);
            stmt.executeUpdate(createRoutesSQL);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Route> readFile() {
        String SQL = "SELECT r.id, r.created_by, r.name, r.creation_date, r.distance, "
                + "c.x as coordinates_x, c.y as coordinates_y, "
                + "lf.x as from_x, lf.y as from_y, lf.name as from_name, "
                + "lt.x as to_x, lt.y as to_y, lt.z as to_z "
                + "FROM routes r "
                + "JOIN coordinates c ON r.coordinates_id = c.id "
                + "LEFT JOIN location_from lf ON r.from_id = lf.id "
                + "JOIN location_to lt ON r.to_id = lt.id";

        List<Route> routes = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Route route = new Route();
                route.setId(rs.getInt("id"));
                route.setCreatedBy(rs.getString("created_by"));
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
                route.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());

                routes.add(route);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return routes;
    }

    @Override
    public void writeFile(Collection<Route> values) {
        dropAndCreateTables();
        String coordinatesSQL = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        String fromSQL = "INSERT INTO location_from (x, y, name) VALUES (?, ?, ?) RETURNING id";
        String toSQL = "INSERT INTO location_to (x, y, z) VALUES (?, ?, ?) RETURNING id";
        String routeSQL = "INSERT INTO routes (id, created_by, name, coordinates_id, creation_date, from_id, to_id, distance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement coordinatesStmt = conn.prepareStatement(coordinatesSQL);
                 PreparedStatement fromStmt = conn.prepareStatement(fromSQL);
                 PreparedStatement toStmt = conn.prepareStatement(toSQL);
                 PreparedStatement routeStmt = conn.prepareStatement(routeSQL)) {

                for (Route route : values) {
                    // Insert coordinates
                    coordinatesStmt.setLong(1, route.getCoordinates().getX());
                    coordinatesStmt.setDouble(2, route.getCoordinates().getY());
                    ResultSet coordinatesRs = coordinatesStmt.executeQuery();
                    coordinatesRs.next();
                    int coordinatesId = coordinatesRs.getInt(1);

                    // Insert location_from
                    int fromId = 0;
                    if (route.getFrom() != null) {
                        fromStmt.setDouble(1, route.getFrom().getX());
                        fromStmt.setDouble(2, route.getFrom().getY());
                        fromStmt.setString(3, route.getFrom().getName());
                        ResultSet fromRs = fromStmt.executeQuery();
                        fromRs.next();
                        fromId = fromRs.getInt(1);
                    }

                    // Insert location_to
                    toStmt.setDouble(1, route.getTo().getX());
                    toStmt.setDouble(2, route.getTo().getY());
                    toStmt.setDouble(3, route.getTo().getZ());
                    ResultSet toRs = toStmt.executeQuery();
                    toRs.next();
                    int toId = toRs.getInt(1);

                    // Insert route
                    routeStmt.setInt(1, route.getId());
                    routeStmt.setString(2, route.getCreatedBy());
                    routeStmt.setString(3, route.getName());
                    routeStmt.setInt(4, coordinatesId);
                    routeStmt.setTimestamp(5, Timestamp.valueOf(route.getCreationDate()));
                    if (fromId > 0) {
                        routeStmt.setInt(6, fromId);
                    } else {
                        routeStmt.setNull(6, Types.INTEGER);
                    }
                    routeStmt.setInt(7, toId);
                    routeStmt.setLong(8, route.getDistance());
                    routeStmt.executeUpdate();
                }

                conn.commit();
                System.out.println("Collection saved to PostgreSQL database");
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println(ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
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


    @Override
    public void addUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        String insertUserSQL = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            System.out.println("User " + username + " added to PostgreSQL database");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public Optional<User> getUser(String username, String password) {
        String selectUserSQL = "SELECT * FROM users WHERE username = ?";
        String hashedPassword = hashPassword(password);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(selectUserSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getString("password").equals(hashedPassword)) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setLogin(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return Optional.of(user);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Optional.empty();
    }

    public boolean existByUsername(String username) {
        String selectUserSQL = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(selectUserSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }
}
