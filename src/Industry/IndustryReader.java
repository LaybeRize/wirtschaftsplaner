package Industry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IndustryReader {

    private String[] StandardColumns = new String[] {"F_Abbreviation", "Factory", "Needed", "WorkPerUnit"};
    private String[] StandardColumnsConfig = new String[] {"", "", " STRING NOT NULL DEFAULT 0", " STRING NOT NULL DEFAULT 0"};
    private List<String> industriesString = new ArrayList<>();
    private List<Industry> industries = new ArrayList<>();


    public void updateDatabase() {
        try {
            fillExistingParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillExistingParameters() throws Exception{
        List<List<String>> industries = Objects.requireNonNull(readTable("_Industries", new String[]{"I_Abbreviation"}));
        for (List<String> strings : industries) industriesString.add(strings.get(0));
        rewriteIndustryTables();
        rewriteFactoryColumns();
    }

    public List<Industry> load() {
        try {
            List<List<String>> industriesDatabase = Objects.requireNonNull(readTable("_Industries", new String[]{"I_Abbreviation", "Industry"}));
            for (String string : industriesString) {
                List<String> factories = new ArrayList<>();
                List<List<String>> fs = Objects.requireNonNull(readTable(string, new String[]{"F_Abbreviation"}));
                for (List<String> strings : fs) factories.add(strings.get(0));
                List<Factory> factoryList = new ArrayList<>();
                for (String factory : factories) {
                    List<Integer> ownPosition = new ArrayList<>();
                    List<Double> usage = new ArrayList<>();
                    List<List<Integer>> position = new ArrayList<>();
                    int needed = 0;
                    Double WorkPerUnit = 0.0;
                    String name = "";
                    ownPosition.add(Integer.parseInt(string.substring(1)));
                    ownPosition.add(Integer.parseInt(factory.substring(1)));
                    Class.forName("org.sqlite.JDBC");
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
                    Statement stat = conn.createStatement();
                    String allTables = "SELECT * FROM " + string + " WHERE F_Abbreviation='" + factory + "';";
                    ResultSet rs = stat.executeQuery(allTables);
                    name = rs.getString("Factory");
                    needed = Integer.parseInt(rs.getString("Needed"));
                    WorkPerUnit = Double.parseDouble(rs.getString("WorkPerUnit"));
                    for (String stringF : factories) {
                        String StringF = rs.getString(stringF);
                        if (!StringF.equals("0")) {
                            usage.add(Double.parseDouble(StringF));
                            position.add(Arrays.asList(Integer.parseInt(string.substring(1)), Integer.parseInt(stringF.substring(1))));
                        }
                    }

                    for (String industryS : industriesString) {
                        String Input = "";
                        if (!string.equals(industryS)) {
                            Input = rs.getString(industryS);
                            if (!Input.equals("0")) {
                                String[] stringList = Input.split(":");
                                for (String Info : stringList) {
                                    position.add(Arrays.asList(Integer.parseInt(industryS.substring(1)), Integer.parseInt(Arrays.asList(Info.split("-")).get(0).substring(1))));
                                    usage.add(Double.parseDouble(Arrays.asList(Info.split("-")).get(1)));
                                }
                            }
                        }
                    }
                    factoryList.add(new Factory(ownPosition,usage,position,needed,WorkPerUnit,name));
                }
                String name = "";
                for (List<String> strings : industriesDatabase) if (strings.get(0).equals(string)) name = strings.get(1);
                industries.add(new Industry(factoryList,Integer.parseInt(string.substring(1)),name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return industries;
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
        if (industriesString.size() != industries.size()) {
            if (industriesString.size() > industries.size()) {
                for (String strings : industriesString) {
                    StringBuilder string = new StringBuilder("CREATE TABLE IF NOT EXISTS " + strings + " (");
                    for (int i = 0; i < StandardColumns.length; i++) string.append(StandardColumns[i]).append(StandardColumnsConfig[i]).append(", ");
                    for (String stringI : industriesString) {
                        if (!stringI.equals(strings)) string.append(stringI).append(" STRING NOT NULL DEFAULT 0, ");
                    }
                    string = new StringBuilder(string.toString().substring(0,string.toString().length()-2) + ");");
                    stat.executeUpdate(string.toString());
                }
                for (int i = 0; i < industries.size(); i++) {
                    for (String string : industriesString) {
                        if (!industries.contains(string)) {
                            String alterTable = "ALTER TABLE " + industries.get(i) + " ADD '" + string + "' STRING NOT NULL DEFAULT 0;";
                            stat.executeUpdate(alterTable);
                        }
                    }
                }
            } else {
                for (String string : industries) {
                    if (!industriesString.contains(string)) {
                        stat.executeUpdate("DROP TABLE " + string);
                    }
                }
                for (String stringI : industriesString) {
                    for (String string : industries) {
                        if (!industriesString.contains(string)) {
                            dropColumn(stringI,string);
                        }
                    }
                }
                List<String> Industry = new ArrayList<>();
                for (int i = 0; i < industriesString.size(); i++) {
                    int a = i+1;
                    String string = "I" + a;
                    Industry.add(string);
                }
                for (String string : industriesString) {
                    for (int i = 0; i < Industry.size(); i++) {
                        if (!industriesString.get(i).equals(Industry.get(i)) && !string.equals(industriesString.get(i))) {
                            renameColumn(string,industriesString.get(i),Industry.get(i));
                        }
                    }
                }
                for (int i = 0; i < Industry.size(); i++) {
                    if (!industriesString.get(i).equals(Industry.get(i))) {
                        String string = "ALTER TABLE " + industriesString.get(i) + " RENAME TO " + Industry.get(i);
                        stat.executeUpdate(string);
                    }
                }
                for (int i = 0; i < Industry.size(); i++) {
                    if (!industriesString.get(i).equals(Industry.get(i))) {
                        String string = "UPDATE _Industries SET I_Abbreviation='" + Industry.get(i) + "' WHERE I_Abbreviation='" + industriesString.get(i) + "';";
                        stat.executeUpdate(string);
                    }
                }
                industriesString = Industry;
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
        for (String string : industriesString) {
            List<String> strings = new ArrayList<>();
            for (List<String> stringList : Objects.requireNonNull(readTable(string, new String[]{"F_Abbreviation"}))) {
                strings.add(stringList.get(0));
            }
            Factories.add(strings);
        }

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:wirtschaft.db");
        Statement stat = conn.createStatement();
        for (String Istrings : industriesString) {
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
            if (Factories.get(i).size() + StandardColumns.length + industriesString.size() - 1 != TableColumns.get(i).size()) {
                if (Factories.get(i).size() + StandardColumns.length + industriesString.size() - 1 > TableColumns.get(i).size()) {
                    StringBuilder string = new StringBuilder("ALTER TABLE " + industriesString.get(i) + " RENAME TO TempTable");
                    stat.executeUpdate(string.toString());

                    string = new StringBuilder("CREATE TABLE IF NOT EXISTS " + industriesString.get(i) + " (");
                    for (int a = 0; a < StandardColumns.length; a++) string.append(StandardColumns[a]).append(StandardColumnsConfig[a]).append(", ");
                    for (int a = 0; a < Factories.get(i).size(); a++) string.append(Factories.get(i).get(a)).append(" STRING NOT NULL DEFAULT 0, ");
                    for (int a = 0; a < industriesString.size(); a++) if (a != i) string.append(industriesString.get(a)).append(" STRING NOT NULL DEFAULT 0, ");
                    string = new StringBuilder(string.toString().substring(0,string.toString().length() - 2) + ");");
                    stat.executeUpdate(string.toString());

                    StringBuilder oldColumns = new StringBuilder();
                    for (String s : TableColumns.get(i)) oldColumns.append(s).append(", ");
                    oldColumns = new StringBuilder(oldColumns.substring(0, oldColumns.length() - 2));
                    string = new StringBuilder("INSERT INTO " + industriesString.get(i) + " (" + oldColumns + ") SELECT " + oldColumns + " FROM TempTable");
                    stat.executeUpdate(string.toString());

                    stat.executeUpdate("DROP TABLE TempTable");
                } else {
                    StringBuilder string = new StringBuilder("ALTER TABLE " + industriesString.get(i) + " RENAME TO TempTable");
                    stat.executeUpdate(string.toString());

                    for (int a = 0; a < Factories.get(i).size(); a++) {
                        int temp = a+1;
                        String updateTable = "UPDATE TempTable SET F_Abbreviation='F" + temp + "' WHERE F_Abbreviation='" + Factories.get(i).get(a) + "';";
                        stat.executeUpdate(updateTable);
                    }
                    string = new StringBuilder("CREATE TABLE IF NOT EXISTS " + industriesString.get(i) + " (");
                    StringBuilder newColumns = new StringBuilder();
                    for (int a = 0; a < StandardColumns.length; a++) string.append(StandardColumns[a]).append(StandardColumnsConfig[a]).append(", ");
                    for (int a = 0; a < Factories.get(i).size(); a++) {
                        int temp = a+1;
                        newColumns.append("F").append(temp).append(" STRING NOT NULL DEFAULT 0, ");
                    }
                    for (int a = 0; a < industriesString.size(); a++) if (a != i) newColumns.append(industriesString.get(a)).append(" STRING NOT NULL DEFAULT 0, ");
                    newColumns = new StringBuilder(newColumns.toString().substring(0,newColumns.toString().length() - 2) + ");");
                    string.append(newColumns);
                    stat.executeUpdate(string.toString());

                    newColumns = new StringBuilder("(");
                    for (String standardColumn : StandardColumns) newColumns.append(standardColumn).append(", ");
                    for (int a = 0; a < Factories.get(i).size(); a++) {
                        int temp = a+1;
                        newColumns.append("F").append(temp).append(", ");
                    }
                    for (int a = 0; a < industriesString.size(); a++) if (a != i) newColumns.append(industriesString.get(a)).append(", ");
                    newColumns = new StringBuilder(newColumns.toString().substring(0,newColumns.toString().length() - 2) + ")");
                    StringBuilder oldColumns = new StringBuilder();
                    for (String standardColumn : StandardColumns) {
                        oldColumns.append(standardColumn).append(", ");
                    }
                    for (String str : Factories.get(i)) {
                        oldColumns.append(str).append(", ");
                    }
                    for (int a = 0; a < industriesString.size(); a++) if (a != i) oldColumns.append(industriesString.get(a)).append(", ");
                    oldColumns = new StringBuilder(oldColumns.substring(0, oldColumns.length() - 2));
                    string = new StringBuilder("INSERT INTO " + industriesString.get(i) + " " + newColumns + " SELECT " + oldColumns + " FROM TempTable");
                    stat.executeUpdate(string.toString());

                    stat.executeUpdate("DROP TABLE TempTable");

                    List<String> oldFactoryNames = Arrays.asList(oldColumns.toString().split(", "));
                    List<String> newFactoryNames = Arrays.asList(newColumns.toString().split(", "));

                    for (int a = 0; a < industriesString.size(); a++) {
                        if (a != i) {
                            List<List<String>> IFstrings = Objects.requireNonNull(readTable(industriesString.get(a), new String[]{"F_Abbreviation", industriesString.get(i)}));
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
                                    String UpdateTable = "UPDATE " + industriesString.get(a) + " SET " + industriesString.get(i) + "='" + newIndustryInformation.toString() + "' WHERE F_Abbreviation='" + IndustryFactories.get(counter) + "';";
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
}
