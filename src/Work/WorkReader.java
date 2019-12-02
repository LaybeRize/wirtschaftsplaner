package Work;

import Industry.Industry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkReader {

    private List<Industry> industries = new ArrayList<>();

    public WorkReader(boolean FirstInstallation) {
        try {
            if (FirstInstallation) firstInstallation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTable(List<Industry> industries) {
        this.industries = industries;
        try {
            updateIndustryWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateIndustryWork() throws Exception {
        
    }

    public WorkTable load() {
        try {
            return workTableReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private WorkTable workTableReturn() throws Exception {
        WorkTable workTable = new WorkTable();

        return workTable;
    }

    public void firstInstallation() throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:arbeit.db");
        Statement stat = conn.createStatement();
        stat.executeUpdate("drop table if exists _Work;");
        String string = "create table _Work (I_Abbreviation, Industry);";
        stat.executeUpdate(string);
        conn.close();
    }

    private List<List<String>> readTable(String tableName, String[] rowNames) throws Exception {
        if (rowNames.length == 0) return null;
        if (tableName.equals("")) return null;
        List<List<String>> str = new ArrayList<>();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:arbeit.db");
        Statement stat = conn.createStatement();
        StringBuilder query = new StringBuilder("select ");
        for (int i = 0; i < rowNames.length; i++) {
            if (i + 1 == rowNames.length) query.append(rowNames[i]);
            else query.append(rowNames[i]).append(", ");
        }
        query.append(" from ").append(tableName).append(";");
        ResultSet rs = stat.executeQuery(query.toString());
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

    private void dropColumn(String tableName, String columnName) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:arbeit.db");
        Statement stat = conn.createStatement();
        StringBuilder string = new StringBuilder("SELECT sql FROM sqlite_master WHERE name = '" + tableName + "';");
        ResultSet rs = stat.executeQuery(string.toString());
        String TableColumn = "";
        while (rs.next()) {
            TableColumn = rs.getString("sql");
        }
        string = new StringBuilder("ALTER TABLE " + tableName + " RENAME TO TempTable");
        stat.executeUpdate(string.toString());
        List<String> TableColumns = Arrays.asList(TableColumn.split(","));
        StringBuilder stringT = new StringBuilder();
        for (String stringTC : TableColumns) if (!stringTC.contains(columnName)) stringT.append(stringTC).append(",");
        stringT = new StringBuilder(stringT.toString().substring(0,stringT.toString().length()-1));
        stat.executeUpdate(stringT.toString());
        TableColumns = new ArrayList<>();
        string = new StringBuilder("PRAGMA table_info('" + tableName + "');");
        rs = stat.executeQuery(string.toString());
        while (rs.next()) {
            TableColumns.add(rs.getString("name"));
        }
        string = new StringBuilder();
        for (String table : TableColumns) string.append(table).append(", ");
        string = new StringBuilder(string.toString().substring(0,string.toString().length()-2));
        TableColumn = "INSERT INTO " + tableName + " (" + string.toString() + ") SELECT " + string.toString() + " FROM TempTable";
        stat.executeUpdate(TableColumn);
        stat.executeUpdate("DROP TABLE TempTable");
        rs.close();
        conn.close();
    }
}
