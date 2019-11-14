import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static int industries = 20;
    public static List<Industry.SingleIndustry> singleIndustries;

    public static void main(String[] args) {
        MainClass();
    }

    private static void MainClass() {
        //System.out.println(Paths.get("wirtschaft.db").toAbsolutePath().toString());
        try {
            //initDatabase();
            singleIndustries = allIndustries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (singleIndustries != null) {
            Industry.SingleIndustry singleIndustry = singleIndustries.get(1);
            System.out.println("Industry " + singleIndustry.getOwnPosition() + ":");
            System.out.println(singleIndustry.getPosition());
            System.out.println(singleIndustry.getUsage());
            System.out.println(Industry.checkIfIndustryIsFinal(singleIndustry.getOwnPosition(),singleIndustries));
        }
    }

    private static void initDatabase() throws Exception{
        Class.forName("org.sqlite.JDBC");
        boolean firstStart = !testForDatabase();
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        if (!firstStart) {
            System.out.println("init");
            stat.executeUpdate("drop table if exists industries;");
            String stringIndustries = "create table industries (";
            for (int i = 0; i < industries; i++) {
                stringIndustries = stringIndustries + "I" + i + " STRING DEFAULT 0, ";
            }
            stringIndustries = stringIndustries + "I" + industries + " STRING DEFAULT 0);";
            stat.executeUpdate(stringIndustries);
        }
        System.out.println(generatePosition() + "|" + generateUsage());

        for (int i = 0; i <= industries; i++) {
            List<String> strings = generateAllPositions();
            String string = "insert into industries (I" + strings.get(0) + ", I" + strings.get(1) + ", I" + strings.get(2) + ") values ("+ generateUsage() + ", "+ generateUsage() + ", "+ generateUsage() + ");";
            stat.executeUpdate(string);
        }

        conn.close();
    }

    private static List<Industry.SingleIndustry> allIndustries () throws Exception{
        List<Industry.SingleIndustry> singleIndustries = new ArrayList<>();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from industries;");
        Integer industry = 0;
        while (rs.next()) {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i <= industries; i++) {
                strings.add(rs.getString("I"+i));
            }
            List<Double> doubles = new ArrayList<>();
            for (String string : strings) doubles.add(Double.parseDouble(string));
            Industry.SingleIndustry singleIndustry = new Industry.SingleIndustry(industry++,doubles, 100);
            singleIndustries.add(singleIndustry);
        }
        rs.close();
        conn.close();
        return singleIndustries;
    }

    private static boolean testForDatabase() {
        File DBFileTest = new File(Paths.get("wirtschaft.db").toAbsolutePath().toString());
        if (DBFileTest.exists()) return true;
        return false;
    }

    private static String generateUsage() {
        double x = Math.random();
        int i = (int) (x*100);
        x = ((double) i)/100;
        return "" + x;
    }

    private static List<String> generateAllPositions() {
        List<String> strings = new ArrayList<>();
        strings.add(generatePosition());
        strings.add(generatePosition());
        while (strings.get(0).equals(strings.get(1))) {
            strings.set(1, generatePosition());
        }
        strings.add(generatePosition());
        while (strings.get(0).equals(strings.get(2)) || strings.get(1).equals(strings.get(2))) {
            strings.set(2, generatePosition());
        }
        return strings;
    }

    private static String generatePosition () {
        double x = Math.random();
        int i = (int) (industries*x) + 1;
        return "" + i;
    }
}
