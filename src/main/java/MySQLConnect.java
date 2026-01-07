import java.sql.*;
public class MySQLConnect {
    // Create the Singleton instance mysqlConn
    private static Connection mysqlConn = null;
    //this block will run only once when the class is loaded into memory
    static
    {
        String url = "jdbc:mysql:// " + "localhost/";
        String dbName ="xsb23174"; // your CIS username as this is your DB name
        String user = "xsb23174" ; // i.e., your CIS username as this is the DB user
        String password = "loh7AhS5ahji"; // your devweb password
        try {Class.forName("com.mysql.cj.jdbc.Driver"); // try to connect
            mysqlConn = DriverManager.getConnection(url+dbName, user,
                    password);
            System.out.println("MySQL Db Connection is successful");}
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();}
    }
    public static Connection getMysqlConnection()
    {return mysqlConn;}
}