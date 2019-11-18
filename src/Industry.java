import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Industry {

    private List<String> Industries = new ArrayList<>();
    private String[] StandardColumns = new String[] {"F_Abbreviation", "Factory"};
    private String[] StandardColumnsConfig = new String[] {"", ""};

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
        List<List<String>> industries = Objects.requireNonNull(readTable("_Industries", new String[]{"I_Abbreviation"}));
        for (List<String> strings : industries) Industries.add(strings.get(0));
        //dropColumn("I1","F1");
        //renameColumn("I1","lol", "I3");
        rewriteIndustryTables();
        rewriteFactoryColumns();
    }

    private List<List<String>> readTable (String tableName,String[] rowNames) throws Exception{
        if (rowNames.length == 0) return null;
        if (tableName.equals("")) return null;
        List<List<String>> str = new ArrayList<>();
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
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

    private void rewriteIndustryTables () throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        String allTables = "SELECT name FROM sqlite_master WHERE type='table' AND name LIKE 'I%' ORDER BY name;";
        List<String> industries = new ArrayList<>();
        ResultSet rs = stat.executeQuery(allTables);
        while (rs.next()) {
            industries.add(rs.getString("name"));
        }
        if (Industries.size() != industries.size()) {
            if (Industries.size() > industries.size()) {
                for (String strings : Industries) {
                    StringBuilder string = new StringBuilder("CREATE TABLE IF NOT EXISTS " + strings + " (");
                    for (int i = 0; i < StandardColumns.length; i++) string.append(StandardColumns[i]).append(StandardColumnsConfig[i]).append(", ");
                    for (String stringI : Industries) {
                        if (!stringI.equals(strings)) string.append(stringI).append(" STRING NOT NULL DEFAULT 0, ");
                    }
                    string = new StringBuilder(string.toString().substring(0,string.toString().length()-2) + ");");
                    stat.executeUpdate(string.toString());
                }
                for (int i = 0; i < industries.size(); i++) {
                    for (String string : Industries) {
                        if (!industries.contains(string)) {
                            String alterTable = "ALTER TABLE " + industries.get(i) + " ADD '" + string + "' STRING NOT NULL DEFAULT 0;";
                            stat.executeUpdate(alterTable);
                        }
                    }
                }
            } else {
                for (String string : industries) {
                    if (!Industries.contains(string)) {
                        stat.executeUpdate("DROP TABLE " + string);
                    }
                }
                for (String stringI : Industries) {
                    for (String string : industries) {
                        if (!Industries.contains(string)) {
                            dropColumn(stringI,string);
                        }
                    }
                }
                List<String> Industry = new ArrayList<>();
                for (int i = 0; i < Industries.size(); i++) {
                    int a = i+1;
                    String string = "I" + a;
                    Industry.add(string);
                }
                for (String string : Industries) {
                    for (int i = 0; i < Industry.size(); i++) {
                        if (!Industries.get(i).equals(Industry.get(i)) && !string.equals(Industries.get(i))) {
                            renameColumn(string,Industries.get(i),Industry.get(i));
                        }
                    }
                }
                for (int i = 0; i < Industry.size(); i++) {
                    if (!Industries.get(i).equals(Industry.get(i))) {
                        String string = "ALTER TABLE " + Industries.get(i) + " RENAME TO " + Industry.get(i);
                        stat.executeUpdate(string);
                    }
                }
                for (int i = 0; i < Industry.size(); i++) {
                    if (!Industries.get(i).equals(Industry.get(i))) {
                        String string = "UPDATE _Industries SET I_Abbreviation='" + Industry.get(i) + "' WHERE I_Abbreviation='" + Industries.get(i) + "';";
                        stat.executeUpdate(string);
                    }
                }
                Industries = Industry;
            }
        }
        rs.close();
        conn.close();
    }

    private void dropColumn(String tableName, String columnName) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
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

    private void renameColumn(String tableName, String columnNameOld, String columnNameNew) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
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
        for (String stringTC : TableColumns) {
            if (!stringTC.contains(columnNameOld)) stringT.append(stringTC).append(",");
            else {
                String tempString = stringTC.substring(columnNameOld.length()+1);
                stringT.append(" ").append(columnNameNew).append(tempString).append(",");
            }
        }
        stringT = new StringBuilder(stringT.toString().substring(0,stringT.toString().length()-1));
        stat.executeUpdate(stringT.toString());
        TableColumns = new ArrayList<>();
        string = new StringBuilder("PRAGMA table_info('" + tableName + "');");
        rs = stat.executeQuery(string.toString());
        while (rs.next()) {
            TableColumns.add(rs.getString("name"));
        }
        string = new StringBuilder("");
        for (String table : TableColumns) string.append(table).append(", ");
        string = new StringBuilder(string.toString().substring(0,string.toString().length()-2));
        TableColumn = "INSERT INTO " + tableName + " (" + string.toString() + ") SELECT ";
        string = new StringBuilder();
        for (String table : TableColumns) {
            if (!table.equals(columnNameNew)) string.append(table).append(", ");
            else {
                string.append(columnNameOld).append(", ");
            }
        }
        string = new StringBuilder(string.toString().substring(0,string.toString().length()-2));
        TableColumn = TableColumn + string.toString() + " FROM TempTable";
        stat.executeUpdate(TableColumn);
        stat.executeUpdate("DROP TABLE TempTable");
        rs.close();
        conn.close();
    }

    private void rewriteFactoryColumns() throws Exception{
        List<List<String>> Factories = new ArrayList<>();
        List<List<String>> TableColumns = new ArrayList<>();
        for (String string : Industries) {
            List<String> strings = new ArrayList<>();
            for (List<String> stringList : Objects.requireNonNull(readTable(string, new String[]{"F_Abbreviation"}))) {
                strings.add(stringList.get(0));
            }
            Factories.add(strings);
        }

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        for (String Istrings : Industries) {
            String string = "PRAGMA table_info('" + Istrings + "');";
            List<String> TableColumnsSingle = new ArrayList<>();
            ResultSet rs = stat.executeQuery(string);
            while (rs.next()) {
                TableColumnsSingle.add(rs.getString("name"));
            }
            TableColumns.add(TableColumnsSingle);
            rs.close();
        }
        for (int i = 0; i < Factories.size(); i++) {
            if (Factories.get(i).size() + StandardColumns.length + Industries.size() - 1 != TableColumns.get(i).size()) {
                if (Factories.get(i).size() + StandardColumns.length + Industries.size() - 1 > TableColumns.get(i).size()) {
                    StringBuilder string = new StringBuilder("ALTER TABLE " + Industries.get(i) + " RENAME TO TempTable");
                    stat.executeUpdate(string.toString());

                    string = new StringBuilder("CREATE TABLE IF NOT EXISTS " + Industries.get(i) + " (");
                    for (int a = 0; a < StandardColumns.length; a++) string.append(StandardColumns[a]).append(StandardColumnsConfig[a]).append(", ");
                    for (int a = 0; a < Factories.get(i).size(); a++) string.append(Factories.get(i).get(a)).append(" STRING NOT NULL DEFAULT 0, ");
                    for (int a = 0; a < Industries.size(); a++) if (a != i) string.append(Industries.get(a)).append(" STRING NOT NULL DEFAULT 0, ");
                    string = new StringBuilder(string.toString().substring(0,string.toString().length() - 2) + ");");
                    stat.executeUpdate(string.toString());

                    StringBuilder oldColumns = new StringBuilder();
                    for (String s : TableColumns.get(i)) oldColumns.append(s).append(", ");
                    oldColumns = new StringBuilder(oldColumns.substring(0, oldColumns.length() - 2));
                    string = new StringBuilder("INSERT INTO " + Industries.get(i) + " (" + oldColumns + ") SELECT " + oldColumns + " FROM TempTable");
                    stat.executeUpdate(string.toString());

                    stat.executeUpdate("DROP TABLE TempTable");
                } else {
                    StringBuilder string = new StringBuilder("ALTER TABLE " + Industries.get(i) + " RENAME TO TempTable");
                    stat.executeUpdate(string.toString());

                    for (int a = 0; a < Factories.get(i).size(); a++) {
                        int temp = a+1;
                        String updateTable = "UPDATE TempTable SET F_Abbreviation='F" + temp + "' WHERE F_Abbreviation='" + Factories.get(i).get(a) + "';";
                        stat.executeUpdate(updateTable);
                    }
                    string = new StringBuilder("CREATE TABLE IF NOT EXISTS " + Industries.get(i) + " (");
                    StringBuilder newColumns = new StringBuilder();
                    for (int a = 0; a < StandardColumns.length; a++) string.append(StandardColumns[a]).append(StandardColumnsConfig[a]).append(", ");
                    for (int a = 0; a < Factories.get(i).size(); a++) {
                        int temp = a+1;
                        newColumns.append("F").append(temp).append(" STRING NOT NULL DEFAULT 0, ");
                    }
                    for (int a = 0; a < Industries.size(); a++) if (a != i) newColumns.append(Industries.get(a)).append(" STRING NOT NULL DEFAULT 0, ");
                    newColumns = new StringBuilder(newColumns.toString().substring(0,newColumns.toString().length() - 2) + ");");
                    string.append(newColumns);
                    stat.executeUpdate(string.toString());

                    newColumns = new StringBuilder("(");
                    for (String standardColumn : StandardColumns) newColumns.append(standardColumn).append(", ");
                    for (int a = 0; a < Factories.get(i).size(); a++) {
                        int temp = a+1;
                        newColumns.append("F").append(temp).append(", ");
                    }
                    for (int a = 0; a < Industries.size(); a++) if (a != i) newColumns.append(Industries.get(a)).append(", ");
                    newColumns = new StringBuilder(newColumns.toString().substring(0,newColumns.toString().length() - 2) + ")");
                    StringBuilder oldColumns = new StringBuilder();
                    for (String standardColumn : StandardColumns) {
                        oldColumns.append(standardColumn).append(", ");
                    }
                    for (String str : Factories.get(i)) {
                        oldColumns.append(str).append(", ");
                    }
                    for (int a = 0; a < Industries.size(); a++) if (a != i) oldColumns.append(Industries.get(a)).append(", ");
                    oldColumns = new StringBuilder(oldColumns.substring(0, oldColumns.length() - 2));
                    string = new StringBuilder("INSERT INTO " + Industries.get(i) + " " + newColumns + " SELECT " + oldColumns + " FROM TempTable");
                    stat.executeUpdate(string.toString());

                    stat.executeUpdate("DROP TABLE TempTable");

                    List<String> oldFactoryNames = Arrays.asList(oldColumns.toString().split(", "));
                    List<String> newFactoryNames = Arrays.asList(newColumns.toString().split(", "));

                    for (int a = 0; a < Industries.size(); a++) {
                        if (a != i) {
                            List<List<String>> IFstrings = Objects.requireNonNull(readTable(Industries.get(a), new String[]{"F_Abbreviation", Industries.get(i)}));
                            List<String> IndustryFactories = new ArrayList<>();
                            List<String> UsedByThisIndustry = new ArrayList<>();
                            for (List<String> strings : IFstrings) {
                                IndustryFactories.add(strings.get(0));
                                UsedByThisIndustry.add(strings.get(1));
                            }
                            int counter = 0;
                            for (String str : UsedByThisIndustry) {
                                if (!str.equals("0")) {
                                    StringBuilder newIndustryInformation = new StringBuilder();
                                    for (String strSmall : str.split(":")) {
                                        List<String> temp = Arrays.asList(strSmall.split("-"));
                                        Boolean isPartOf = false;
                                        for (int b = 0; b < oldFactoryNames.size(); b++) if (temp.get(0).equals(oldFactoryNames.get(b))) {
                                            temp.set(0,newFactoryNames.get(b));
                                            isPartOf = true;
                                        }
                                        if (isPartOf) newIndustryInformation.append(temp.get(0)).append("-").append(temp.get(1)).append(":");
                                    }
                                    if (newIndustryInformation.toString().length() > 0) newIndustryInformation = new StringBuilder(newIndustryInformation.toString().substring(0,newIndustryInformation.toString().length()-1));
                                    else newIndustryInformation = new StringBuilder("0");
                                    String UpdateTable = "UPDATE " + Industries.get(a) + " SET " + Industries.get(i) + "='" + newIndustryInformation.toString() + "' WHERE F_Abbreviation='" + IndustryFactories.get(counter) + "';";
                                    stat.executeUpdate(UpdateTable);
                                }
                                counter++;
                            }

                        }
                    }
                }
            }
        }
        conn.close();
    }

    public void firstInstallation () throws Exception {
       Class.forName("org.sqlite.JDBC");
       Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
       Statement stat = conn.createStatement();
       stat.executeUpdate("drop table if exists _Industries;");
       stat.executeUpdate("drop table if exists TempSave;");
       String string = "create table tempSave (Abbreviation, InfoType, Info);";
       stat.executeUpdate(string);
       string = "create table Industries (I_Abbreviation, Industry);";
       stat.executeUpdate(string);
       conn.close();
    }


}
