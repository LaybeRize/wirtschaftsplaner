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

    private static String help;
    private static String Industry = "";
    private static String factory = "";

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
                            "Industry -help | prints the list of commands for industry\n" +
                            "Factory -help | prints the list of commands for factory\n" +
                            "end | ends the program";
                    System.out.println(help);
                    break;
                case "industry":
                    IndustryManagement(command,industryManager);
                    break;
                case "factory":
                    FactoryManagment(command,industryManager);
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

    private static void FactoryManagment (List<String> command, IndustryManager industryManager) {
        try {
            switch (command.get(1)) {
                case "-help":
                    help = "List of commands for factory:\n" +
                            "-help | shows this text\n" +
                            "-list | lists all Factories with there abbreviation\n" +
                            "call <Factory> | enters the factory mode for the factory called\n" +
                            "-info | can only be called when an factory is already called\n" +
                            "<Factory> refers to the abbreviation of the factory";
                    System.out.println(help);
                    break;
                case "-list":
                    if (Industry.equals("")) System.out.println("Please call a valid Industry first");
                    else {
                        Industry industry = industryManager.getSpecificIndustry(Industry);
                        System.out.println("ID: I" + industry.getOwnPosition() + "\nName: " + industry.getName() + "\nAll Factories:");
                        industry.printAllFactories();
                    }
                    break;
                case "call":
                    if (Industry.equals("")) System.out.println("Please call a valid Industry first");
                    else {
                        try {
                            if (industryManager.getSpecificFactory(industryManager.getSpecificIndustry(Industry), command.get(2)) != null) factory = command.get(2);
                            else System.out.println("Please enter a valid factory");
                        } catch (Exception e) {
                            System.out.println("Please enter a factory after 'call'");
                        }
                    }
                    break;
                case "-info":
                    if (Industry.equals("")) System.out.println("Please call a valid Industry first");
                    else {
                        if(factory.equals("")) System.out.println("Please call a valid Factory first");
                        else {
                            Factory RealFactory = industryManager.getSpecificFactory(industryManager.getSpecificIndustry(Industry), factory);
                            DecimalFormat f = new DecimalFormat("#.##");
                            String str = "ID: I" + RealFactory.getOwnPosition().get(0) + " F" + RealFactory.getOwnPosition().get(1) + "\n" +
                                    "Name: " + RealFactory.getName() + "\n" +
                                    "Information:\n" +
                                    "All Production: " + RealFactory.returnFullNeed() + "\n" +
                                    "Production for Consumers: " + RealFactory.getNeeded() + "\n" +
                                    "Cost per Unit for Factory: " + RealFactory.getWorkPerUnit() + "h\n" +
                                    "Total Cost for Unit: " + RealFactory.getWorkCostPerUnit() + "h";
                            System.out.println(str);
                            List<List<Integer>> pos = RealFactory.getPositionFromOtherFactories();
                            List<Double> use = RealFactory.getNeedFromOtherFactories();
                            if (use.size() > 0) {
                                System.out.println("Production for other Industries/Factories:\n\t|----");
                                for (int i = 0; i < use.size(); i++) {
                                    System.out.println("\t|ID: I" + pos.get(i).get(0) + " F" + pos.get(i).get(1));
                                    System.out.println("\t|Production: " + f.format(use.get(i)));
                                    System.out.println("\t|----");
                                }
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("This command is not recognized. Type 'Factory -help' for all commands.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("This command is not recognized. Type 'Factory -help' for all commands.");
        }
    }

    private static void IndustryManagement (List<String> command, IndustryManager industryManager) {
        try {
            switch (command.get(1)) {
                case "-help":
                    help = "List of commands for industry:\n" +
                            "-help | shows this text\n" +
                            "-list | lists all Industries with there abbreviation\n" +
                            "call <Industry> | enters the industry mode for the industry called\n" +
                            "-info | can only be called when an industry is already called\n" +
                            "<Industry> refers to the abbreviation of the industry";
                    System.out.println(help);
                    break;
                case "-list":
                    industryManager.printAllIndustryNames();
                    break;
                case "call":
                    try {
                        if (industryManager.getSpecificIndustry(command.get(2)) != null) Industry = command.get(2);
                        else System.out.println("Please enter a valid Industry");
                    } catch (Exception e) {
                        System.out.println("Please enter a Industry after 'call'");
                    }
                    break;
                case "-info":
                    if (Industry.equals("")) {
                        System.out.println("Please call a industry first");
                        break;
                    }
                    Industry industry = industryManager.getSpecificIndustry(Industry);
                    StringBuilder stringBuilder = new StringBuilder("ID: I");
                    stringBuilder.append(industry.getOwnPosition()).append("\nName: ").append(industry.getName()).append("\nAll Factories:");
                    System.out.println(stringBuilder.toString());
                    industry.printAllFactories();
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
