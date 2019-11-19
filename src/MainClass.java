import java.io.File;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Industry.*;

public class MainClass {

    public static int industries = 10;

    public static void main(String[] args) {
        MainClass();
    }

    private static void MainClass() {
        IndustryManager industryManager = new IndustryManager();
        industryManager.updateDatabase();
        industryManager.load();

        SystemManagment(industryManager);
    }

    private static void SystemManagment (IndustryManager industryManager) {
        Boolean run = true;
        Scanner input = new Scanner(System.in);
        System.out.println("Type 'help' for a list of commands");
        while (run) {
            List<String> command = Arrays.asList(input.nextLine().toLowerCase().split(" "));
            String help;
            switch (command.get(0)) {
                case "help":
                    help = "List of commands:\n" +
                            "help | shows this text\n" +
                            "Industry -help | prints the list commands for industry\n" +
                            "end | ends the program";
                    System.out.println(help);
                    break;
                case "industry":
                    IndustryManagement(command,industryManager);
                    break;
                case "end":
                    run = false;
                    break;
                default:
                    System.out.println("This command is not recognized. Type 'help' for all commands.");
                    break;
            }
        }
    }

    private static void IndustryManagement (List<String> command, IndustryManager industryManager) {
        String help;
        try {
            switch (command.get(1)) {
                case "-help":
                    help = "List of commands for industry:\n" +
                            "-help | shows this text\n" +
                            "-list | lists all Industries with there abbreviation\n" +
                            "-get <Industry> | prints all the Factories that are associated with the Industry.\n" +
                            "<Industry> refers to the abbreviation of the industry";
                    System.out.println(help);
                    break;
                case "-list":
                    industryManager.printAllIndustryNames();
                    break;
                case "-get":
                    try {
                        industryManager.getSpecificIndustry(command.get(2)).printAllFactories();
                    } catch (Exception e) {
                        System.out.println("Industry does not exist or wasn't correctly written");
                    }
                    break;
                default:
                    System.out.println("This command is not recognized. Type 'Industry -help' for all commands.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("This command is not recognized. Type 'Industry -help' for all commands.");
        }
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
