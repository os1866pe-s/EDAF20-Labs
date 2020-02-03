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

	private String userName;

	/**
	 * Create the database interface object. Connection to the database
	 * is performed later.
	 */
	public Database() {
		conn = null;
	}

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
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection
					("jdbc:mysql://puccini.cs.lth.se/" + userName,
							userName, password);
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
			this.userName = null;
			return false;
		}
		this.userName = userName;
		return true;
	}

	public String getUserName() {
		return userName;
	}

	public boolean openConnectionLocalhost(String userName, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://192.168.1.214/lab2?serverTimezone=UTC",
					userName, password);
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
			this.userName = null;
			return false;
		}
		this.userName = userName;
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
					"WHERE movie_name = '" + mTitle +
					"' AND show_date = '" + mDate + "'";

			try {
				ResultSet result = conn.createStatement().executeQuery(query);
				while (result.next()) {
					mFreeSeats = result.getInt(result.findColumn("available_seats"));

					mVenue = result.getString(result.findColumn("theater_name"));
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
		return false;
	}

	public List<String> getMovies() {
		List<String> movies = new ArrayList<>();

		String query = "SELECT DISTINCT movie_name " +
				"FROM MovieShows";

		try {
			ResultSet result = conn.createStatement().executeQuery(query);

			while (result.next()) {
				String movieName = result.getString(result.findColumn("movie_name"));
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
				"WHERE movie_name = '" + movieName + "'";

		try {
			ResultSet result = conn.createStatement().executeQuery(query);
			while (result.next()) {
				String showDates = result.getString(result.findColumn("show_date"));
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
					"WHERE username = '" + getUserName() + "'" +
					" AND movie_name = '" + movieName + "'" +
					" AND show_date = '" + date + "'";

			try {
				ResultSet result = conn.createStatement().executeQuery(queryGetBookingNumber);
				if (result.next()) {
					reservationNumber = result.getInt(result.findColumn("reservation_number"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reservationNumber;
	}


	public int bookOneSeat(String movieName, String date) {

		if (hasBookedASeat(movieName, date)) {
			subtractOneSeat(movieName, date);

			String queryAddOneTicket = "INSERT INTO Tickets(username, show_date, movie_name) " +
					"VALUES('" + getUserName() + "', '" + date + "', '" + movieName + "');";
			try {
				conn.createStatement().execute(queryAddOneTicket);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return getReservationNumber(movieName, date);
		}
		return -1;
	}

	private void subtractOneSeat(String movieName, String showDate) {
		String queryRemoveOneAvailableSeat = "UPDATE MovieShows " +
				"SET available_seats = available_seats -1 " +
				"WHERE show_date = '" + showDate + "' AND movie_name = '" + movieName + "'";
		try {
			conn.createStatement().execute(queryRemoveOneAvailableSeat);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean hasBookedASeat(String movieName, String showDate) {
		int reservationNumber = getReservationNumber(movieName, showDate);
		return reservationNumber == -1;
	}

}
