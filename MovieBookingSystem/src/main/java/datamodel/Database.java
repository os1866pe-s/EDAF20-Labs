package datamodel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database is a class that specifies the interface to the
 * movie database. Uses JDBC and the MySQL Connector/J driver.
 */
public class Database {
    /**
     * The database connection.
     */
    private Connection conn;

    /**
     * Create the database interface object. Connection to the database
     * is performed later.
     */
    public Database() {
        conn = null;
    }

    /* --- TODO: Change this method to fit your choice of DBMS --- */

    /**
     * Open a connection to the database, using the specified user name
     * and password.
     *
     * @param userName The user name.
     * @param password The user's password.
     * @return true if the connection succeeded, false if the supplied
     * user name and password were not recognized. Returns false also
     * if the JDBC driver isn't found.
     */
    public boolean openConnection(String userName, String password) {
        try {
            // Connection strings for included DBMS clients:
            // [MySQL]       jdbc:mysql://[host]/[database]
            // [PostgreSQL]  jdbc:postgresql://[host]/[database]
            // [SQLite]      jdbc:sqlite://[filepath]

            // Use "jdbc:mysql://puccini.cs.lth.se/" + userName if you using our shared server
            // If outside, this statement will hang until timeout.
            conn = DriverManager.getConnection
                    ("jdbc:sqlite:movieBookings.db3", userName, password);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getUserName() {
        return CurrentUser.instance().getCurrentUserId();
    }

    public boolean openConnectionLocalhost(String userName, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/lab2?serverTimezone=UTC",
                    userName, password);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
            e.printStackTrace();
            CurrentUser.instance().loginAs(null);
            return false;
        }
        CurrentUser.instance().loginAs(userName);
        return true;
    }

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;

        System.err.println("Database connection closed.");
    }

    /**
     * Check if the connection to the database has been established
     *
     * @return true if the connection has been established
     */
    public boolean isConnected() {
        return conn != null;
    }

    public Show getShowData(String mTitle, String mDate) {
        Integer mFreeSeats = null;
        String mVenue = null;

        if (mTitle != null && mDate != null) {

            String query = "SELECT * " +
                    "FROM MovieShows " +
                    "WHERE movie_name = ? AND show_date = ? ";

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, mTitle);
                statement.setString(2, mDate);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    mFreeSeats = result.getInt("available_seats");

                    mVenue = result.getString("theater_name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new Show(mTitle, mDate, mVenue, mFreeSeats);
    }

    public boolean login(String userName) {

        String query = "SELECT * FROM Users WHERE username = '" + userName + "'";

        try {
            ResultSet result = conn.createStatement().executeQuery(query);
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CurrentUser.instance().loginAs(null);

        return false;
    }

    public List<String> getMovies() {
        List<String> movies = new ArrayList<>();

        String query = "SELECT DISTINCT movie_name " +
                "FROM MovieShows";

        try {
            ResultSet result = conn.createStatement().executeQuery(query);

            while (result.next()) {
                String movieName = result.getString("movie_name");
                movies.add(movieName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<String> getDates(String movieName) {
        List<String> dates = new ArrayList<>();

        String query = "SELECT show_date " +
                "FROM MovieShows " +
                "WHERE movie_name = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, movieName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String showDates = result.getString("show_date");
                dates.add(showDates);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;

    }

    public int getReservationNumber(String movieName, String date) {
        int reservationNumber = -1;
        if (movieName != null && date != null) {

            String queryGetBookingNumber = "SELECT reservation_number " +
                    "FROM Tickets " +
                    "WHERE username = ? " +
                    "AND movie_name = ? " +
                    "AND show_date = ? " +
                    "ORDER BY reservation_number DESC;";

            try (PreparedStatement statement = conn.prepareStatement(queryGetBookingNumber)) {
                statement.setString(1, getUserName());
                statement.setString(2, movieName);
                statement.setString(3, date);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    reservationNumber = result.getInt("reservation_number");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reservationNumber;
    }


    public int bookOneSeat(String movieName, String date) {

        subtractOneSeat(movieName, date);

        String queryAddOneTicket = "INSERT INTO Tickets(username, show_date, movie_name) " +
                "VALUES(?, ?, ?);";
        try (PreparedStatement statement = conn.prepareStatement(queryAddOneTicket)){
            statement.setString(1, getUserName());
            statement.setString(2, date);
            statement.setString(3, movieName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getReservationNumber(movieName, date);

    }

    private void subtractOneSeat(String movieName, String showDate) {
        String queryRemoveOneAvailableSeat = "UPDATE MovieShows " +
                "SET available_seats = available_seats - 1 " +
                "WHERE show_date = ? AND movie_name = ?";

        try (PreparedStatement statement = conn.prepareStatement(queryRemoveOneAvailableSeat)) {
            statement.setString(1, showDate);
            statement.setString(2, movieName);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();

        String queryAllReservations = "SELECT reservation_number, tickets.show_date, tickets.movie_name, theater_name " +
                " FROM Tickets JOIN MovieShows" +
                " ON Tickets.movie_name = MovieShows.movie_name" +
                " AND Tickets.show_date = MovieShows.show_date" +
                " ORDER BY Tickets.show_date";
        try(PreparedStatement statement = conn.prepareStatement(queryAllReservations)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Reservation reservation = new Reservation(
                        resultSet.getInt("reservation_number"),
                        resultSet.getString("movie_name"),
                        resultSet.getString("show_date"),
                        resultSet.getString("theater_name")
                );
                list.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
