import java.io.File;
import java.nio.file.Paths;
import java.sql.*;

public class MainClass {
    public static void main(String[] args) {
        MainClass();
    }

    private static void MainClass() {
        //System.out.println(Paths.get("wirtschaft.db").toAbsolutePath().toString());
        try {
            initDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void initDatabase() throws Exception{
        Class.forName("org.sqlite.JDBC");
        boolean firstStart = !testForDatabase();
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        if (firstStart) {
            System.out.println("init");
            stat.executeUpdate("drop table if exists people;");
            stat.executeUpdate("create table people (name, occupation);");
        }
        PreparedStatement prep = conn.prepareStatement(
                "insert into people values (?, ?);");

        prep.setString(1, "Gandhi");
        prep.setString(2, "politics");
        prep.addBatch();
        prep.setString(1, "Turing");
        prep.setString(2, "computers");
        prep.addBatch();
        prep.setString(1, "Wittgenstein");
        prep.setString(2, "smartypants");
        prep.addBatch();

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("select * from people;");
        while (rs.next()) {
            System.out.println("name = " + rs.getString("name"));
            System.out.println("job = " + rs.getString("occupation"));
        }
        rs.close();
        conn.close();
    }

    private static boolean testForDatabase() {
        File DBFileTest = new File(Paths.get("wirtschaft.db").toAbsolutePath().toString());
        if (DBFileTest.exists()) return true;
        return false;
    }
}
