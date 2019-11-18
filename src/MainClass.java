import java.io.File;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import Industry.*;

public class MainClass {

    public static int industries = 10;

    public static void main(String[] args) {
        MainClass();
    }

    private static void MainClass() {
        IndustryManager industryManager = new IndustryManager();
        industryManager.load();
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
