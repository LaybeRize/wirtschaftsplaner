import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import Industry.*;

public class MainClass {

    public static int industries = 10;
    public static List<SingleIndustry> singleIndustries;

    public static void main(String[] args) {
        MainClass();
    }

    private static void MainClass() {
        try {
            //initDatabase();
            singleIndustries = allIndustries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (singleIndustries != null) {
            Industry.calcAllIndustries(singleIndustries);
            try {
                writeDownCalc(singleIndustries);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeDownCalc(List<SingleIndustry> singleIndustries) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();

        //creating the table for the detailed calculations
        stat.executeUpdate("drop table if exists calculation;");
        String stringIndustries = "create table calculation (";
        for (int i = 0; i < industries; i++) {
            stringIndustries = stringIndustries + "I" + i + " STRING DEFAULT 0, ";
        }
        stringIndustries = stringIndustries + "I" + industries + " STRING DEFAULT 0, Cost STRING, Needed String, IndustryName String);";
        stat.executeUpdate(stringIndustries);

        //put all Information down
        for (SingleIndustry singleIndustry : singleIndustries) {
            String string = "insert into calculation (";
            for (int i : singleIndustry.getPositionFromOtherIndustries()) {
                string = string + "I" + i + ", ";
            }
            string = string + "Cost, Needed, IndustryName) values (";
            for (Double i : singleIndustry.getNeedFromOtherIndustries()) {
                string = string + Math.ceil(i) + ", ";
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            string = string + decimalFormat.format(singleIndustry.getWorkCostPerUnit()) + ", " + singleIndustry.returnFullNeed() + ", 'Industry " + singleIndustry.getOwnPosition() + "');";
            stat.executeUpdate(string);
        }
    }

    private static void initDatabase() throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();

        //information about the industries must be saved in this table to be used for the calculation process
        stat.executeUpdate("drop table if exists industries;");
        String stringIndustries = "create table industries (";
        for (int i = 0; i < industries; i++) {
            stringIndustries = stringIndustries + "I" + i + " STRING DEFAULT 0, ";
        }
        stringIndustries = stringIndustries + "I" + industries + " STRING DEFAULT 0, Work STRING, Needed String, IndustryName String);";
        stat.executeUpdate(stringIndustries);

        //fills the table for test purpose with random values
        for (int i = 0; i <= industries; i++) {
            List<String> strings = generateAllPositions();
            String string = "insert into industries (I" + strings.get(0) + ", I" + strings.get(1) + ", I" + strings.get(2) + ", Work, Needed, IndustryName) values ("+ generateUsage() + ", "+ generateUsage() + ", "+ generateUsage() + ", 1, 100, 'Industry " + i + "');";
            stat.executeUpdate(string);
        }

        conn.close();
    }

    private static List<SingleIndustry> allIndustries () throws Exception{

        //creates the SingleIndustry Objects from the database for program intern calculation
        List<SingleIndustry> singleIndustries = new ArrayList<>();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from industries;");
        Integer industry = 0;
        Double work;
        while (rs.next()) {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i <= industries; i++) {
                strings.add(rs.getString("I"+i));
            }
            work = Double.parseDouble(rs.getString("Work"));
            Integer needed = Integer.parseInt(rs.getString("Needed"));
            List<Double> doubles = new ArrayList<>();
            for (String string : strings) doubles.add(Double.parseDouble(string));
            SingleIndustry singleIndustry = new SingleIndustry(industry++,doubles,needed, work);
            singleIndustries.add(singleIndustry);
        }
        rs.close();
        conn.close();
        return singleIndustries;
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
