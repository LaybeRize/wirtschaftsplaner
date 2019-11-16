import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Industry {

    private List<List<String>> Industries = new ArrayList<>();
    private String[] StandardColumns = new String[] {"F_Abbreviation", "Factory", "ExternNeeds"};
    private String[] StandardColumnsConfig = new String[] {"", "", ""};

    public Industry () {
        try {
            fillExistingParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {

    }

    private void fillExistingParameters() throws Exception{
        for (List<String> strings : readTable("Industries",new String[] {"I_Abbreviation", "Industry"})) {
            Industries.add(strings);
        }
        createIndustryTables(Industries);
        rewriteFactoryColumns();
    }

    private List<List<String>> readTable (String tableName,String[] rowNames) throws Exception{
        if (rowNames.length == 0) return null;
        if (tableName.equals("")) return null;
        List<List<String>> str = new ArrayList<>();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        String query = "select ";
        for (int i = 0; i < rowNames.length; i++) {
            if (i + 1 == rowNames.length) query = query + rowNames[i];
            else query = query + rowNames[i] + ", ";
        }
        query = query + " from " + tableName + ";";
        ResultSet rs = stat.executeQuery(query);
        while (rs.next()) {
            List<String> strings = new ArrayList<>();
            for (String string : rowNames) {
                strings.add(rs.getString(string));
            }
            str.add(strings);
        }
        rs.close();
        conn.close();
        return str;
    }

    private void createIndustryTables (List<List<String>> industries) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        for (List<String> strings : industries) {
            String string = "CREATE TABLE IF NOT EXISTS " + strings.get(0) + " (";
            for (int i = 0; i < StandardColumns.length-1; i++) string = string + StandardColumns[i] + StandardColumnsConfig[i] + ", ";
            string = string + StandardColumns[StandardColumns.length-1] + StandardColumnsConfig[StandardColumns.length-1] + ");";
            stat.executeUpdate(string);
        }
        conn.close();
    }

    private void rewriteFactoryColumns() throws Exception{
        List<List<String>> Factories = new ArrayList<>();
        List<String> TableColumns = new ArrayList<>();
        for (List<String> strings : Industries) {
            List<String> string = new ArrayList<>();
            for (List<String> stringList : readTable(strings.get(0),new String[] {"F_Abbreviation"})) {
                string.add(stringList.get(0));
            }
            Factories.add(string);
        }

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        for (List<String> Istrings : Industries) {
            String string = "PRAGMA table_info('" + Istrings.get(0) + "');";
            ResultSet rs = stat.executeQuery(string);
            while (rs.next()) {
                TableColumns.add(rs.getString("name"));
            }
            rs.close();
        }
        for (int i = 0; i < Factories.size(); i++) {
            if (Factories.get(i).size() + StandardColumns.length != TableColumns.size()) {
                if (Factories.get(i).size() + StandardColumns.length > TableColumns.size()) {
                    String string = "ALTER TABLE " + Industries.get(i).get(0) + " RENAME TO TempTable";
                    stat.executeUpdate(string);
                    string = "CREATE TABLE IF NOT EXISTS " + Industries.get(i).get(0) + " (";
                    for (int a = 0; a < StandardColumns.length; a++) string = string + StandardColumns[a] + StandardColumnsConfig[a] + ", ";
                    for (int a = 0; a < Factories.get(i).size() - 1; a++) string = string + Factories.get(i).get(a) + " STRING DEFAULT 0, ";
                    string = string + Factories.get(i).get(Factories.get(i).size()-1) + " STRING DEFAULT 0);";
                    stat.executeUpdate(string);
                    String oldColumns = "";
                    for (String s : TableColumns) oldColumns = oldColumns + s + ", ";
                    oldColumns = oldColumns.substring(0, oldColumns.length()-2);
                    string = "INSERT INTO " + Industries.get(i).get(0) + " (" + oldColumns + ") SELECT " + oldColumns + " FROM TempTable";
                    stat.executeUpdate(string);
                    stat.executeUpdate("DROP TABLE TempTable");
                } else {
                    String string = "ALTER TABLE " + Industries.get(i).get(0) + " RENAME TO TempTable";
                    stat.executeUpdate(string);
                    for (int a = 0; a < Factories.get(i).size(); a++) {
                        int temp = a+1;
                        String updateTable = "UPDATE TempTable SET F_Abbreviation='F" + temp + "' WHERE F_Abbreviation='" + Factories.get(i).get(a) + "';";
                        stat.executeUpdate(updateTable);
                    }
                    String oldColumns = "";
                    for (int a = 0; a < StandardColumns.length;a++) {
                        oldColumns = oldColumns + StandardColumns[a] + ", ";
                    }
                    string = "CREATE TABLE IF NOT EXISTS " + Industries.get(i).get(0) + " (";
                    String newColumns = "";
                    for (int a = 0; a < StandardColumns.length; a++) string = string + StandardColumns[a] + StandardColumnsConfig[a] + ", ";
                    for (int a = 0; a < Factories.get(i).size() - 1; a++) {
                        int temp = a+1;
                        newColumns = newColumns + "F" + temp + " STRING DEFAULT 0, ";
                    }
                    newColumns = newColumns + "F"+ Factories.get(i).size() + " STRING DEFAULT 0);";
                    string = string + newColumns;
                    stat.executeUpdate(string);
                    newColumns = "(";
                    for (int a = 0; a < StandardColumns.length; a++) newColumns = newColumns + StandardColumns[a] + ", ";
                    for (int a = 0; a < Factories.get(i).size() - 1; a++) {
                        int temp = a+1;
                        newColumns = newColumns + "F" + temp + ", ";
                    }
                    newColumns = newColumns + "F"+ Factories.get(i).size() + ")";
                    int b = 0;
                    for (String str : Factories.get(i)) {
                        oldColumns = oldColumns + str + ", ";
                    }
                    oldColumns = oldColumns.substring(0, oldColumns.length()-2);
                    string = "INSERT INTO " + Industries.get(i).get(0) + " " + newColumns + " SELECT " + oldColumns + " FROM TempTable";
                    stat.executeUpdate(string);
                    stat.executeUpdate("DROP TABLE TempTable");
                }
            }
        }
        conn.close();
    }

    public void firstInstallation () throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        stat.executeUpdate("drop table if exists Industries;");
        String string = "create table Industries (I_Abbreviation, Industry);";
        stat.executeUpdate(string);
        conn.close();
    }


}
