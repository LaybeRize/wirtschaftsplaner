import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import Industry.*;

public class IndustryManager {

    private List<Industry> industries = new ArrayList<>();

    public IndustryManager (boolean FirstInstallation) {
        try {
            if (FirstInstallation) firstInstallation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startManager() {
        IndustryReader industryReader = new IndustryReader();
        industryReader.updateDatabase();
        industries = industryReader.load();
        for (Industry industry : industries) {
            industry.calcAllIndustries(industries);
        }
    }

    public void printAllIndustryNames () {
        for (Industry industry : industries) {
            System.out.println("I" + industry.getOwnPosition() + ": " + industry.getName());
        }
    }

    public Industry getSpecificIndustry (String Name) {
        for (Industry industry : industries) {
            String temp = "i" + industry.getOwnPosition();
            if (Name.equals(temp)) {
                industry.calcAllIndustries(industries);
                return industry;
            }
        }
        return null;
    }

    public Factory getSpecificFactory (Industry industry, String name) {
        for (Factory factory : industry.getFactories()) {
            if (name.equals("f"+factory.getOwnPosition().get(1))) return factory;
        }
        return null;
    }

    public void firstInstallation () throws Exception {
       Class.forName("org.sqlite.JDBC");
       Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
       Statement stat = conn.createStatement();
       stat.executeUpdate("drop table if exists _Industries;");
       String string = "create table _Industries (I_Abbreviation, Industry);";
       stat.executeUpdate(string);
       conn.close();
    }


}
