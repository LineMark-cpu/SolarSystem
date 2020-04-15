package application;
import java.sql.*;
import java.util.ArrayList;
//This class handles all interaction with the database
public class JDBCConnector {

	private Connection connection;

	public void connect(int portNo, String userName, String password) {
		// Establishing a PostgreSQL database connection
		String databaseUrl = "jdbc:postgresql://rogue.db.elephantsql.com:" + portNo + "/" + userName;

		try {
			connection = DriverManager.getConnection(databaseUrl, userName, password);
			System.out.println("Connection established to: " + databaseUrl);
		} 
		catch (Exception exception) {
			System.out.println("Connection failed");
			exception.printStackTrace();
		}
	}

	public void storeAsNewObservation(Observation observation) {
		String sql = "SELECT MAX(energy) FROM test;";

		int maxId = -1;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			maxId = resultSet.getInt(1);
			//System.out.println("Max Energy: " + maxId);
		} catch (SQLException e) {
			System.out.println("Error trying to find max energy");
			e.printStackTrace();
		}
		int id = maxId + 1; //New id should be one more than the current max

		sql = "INSERT INTO TrafficCamera.Observation (id, licenseNo, obsSpeed, obsTime) VALUES (" + id + ", '" + observation.getLicenseNo() + "', " + observation.getObsSpeed() + ", '" + convertToSqlTimestamp(observation.getObsTime()) + "');";

		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			System.out.println("Error trying to insert a new observation in Observation table");
			e.printStackTrace();
		}
	}

	public Object[][] retrieveAverageSpeeds() {
		String sql = "SELECT licenseNo, AVG(obsSpeed) FROM TrafficCamera.Observation GROUP BY licenseNo;";

		int noDiffCars = 0;
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			resultSet = statement.executeQuery(sql);
			resultSet.last(); //Set pointer to last row in the result set
			noDiffCars = resultSet.getRow(); //Gets the number of the last row in the result set
			//System.out.println("NoDiffCars: " + noDiffCars);
		} catch (SQLException e) {
			System.out.println("Error trying to find the number of different observed cars");
			e.printStackTrace();
		}

		Object[][] result = new Object[noDiffCars][2];
		try {
			resultSet.first();
			for (int i=0;i<noDiffCars;i++) {
				result[i][0] = resultSet.getString(1);
				result[i][1] = resultSet.getString(2);
				resultSet.next();
			}

		} catch (SQLException e) {
			System.out.println("Error trying to generate table of average speeds");
			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Observation> retrieveSpeedingViolations(int speedLimit) {
		ArrayList<Observation> result = new ArrayList<Observation>();

		String sql = "SELECT * FROM TrafficCamera.Observation WHERE obsSpeed > " + speedLimit + ";";

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql); // use the executeQuery() function when a result is expected

			while (resultSet.next()) { // Goes to the next row of data if available
				Observation observation = new Observation(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getDate(4));
				result.add(observation);
			}
			//System.out.println("Selection of speeding violations above " + speedLimit + " successful");
		} catch (SQLException exception) {
			System.out.println("Selection of speeding violations failed");
			exception.printStackTrace();
		}

		return result;
	}

	public void close() {
		// Close the connection
		try {
			connection.close();
			System.out.println("Connection closed");
		} catch (SQLException exception) {
			System.out.println("Connection closing failed");
			exception.printStackTrace();
		}
	}

	// Utility method to convert from one date/time format to another
	private java.sql.Timestamp convertToSqlTimestamp(java.util.Date uDate) {
		java.sql.Timestamp timestamp = new java.sql.Timestamp(uDate.getTime());
		return timestamp;
	}
	
	// The following is done by me:

	public void retrieveMaxEnergy() {
		try {
			Statement stmt = connection.createStatement();  //A statement object is created for sending SQL statements to the database.
			ResultSet rs = stmt.executeQuery("select MAX(energy) FROM public.test;");  
			while(rs.next())
			System.out.println("The maximum energy retrieved is: "+rs.getInt(1));
			
		} catch (SQLException e) {
			System.out.println("Error trying to retrieve maximum energy");
			e.printStackTrace();
			
		}
	}
	
	public void retrieveDateWithMaxEnergy() {
		
	try {
		Statement stmt = connection.createStatement();  
		ResultSet rs = stmt.executeQuery("SELECT time FROM test WHERE energy>2500;");  
		while(rs.next())
		System.out.println("The date with the maximum energy retrieved is: "+rs.getDate(1));
		
	} catch (SQLException e) {
		System.out.println("Error trying to retrieve date with maximum energy");
		e.printStackTrace();
		
	}
	}
}