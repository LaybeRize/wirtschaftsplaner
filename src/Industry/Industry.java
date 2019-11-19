package Industry;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Industry {

    List<Factory> factories;
    private int ownPosition;
    private String name;

    public Industry(List<Factory> factories, int IndustryPosition, String name) {
        this.factories = factories;
        this.name = name;
        ownPosition = IndustryPosition;
    }

    public String getName() {
        return name;
    }

    public int getOwnPosition() {
        return ownPosition;
    }

    public void printAllFactories () {
        for (Factory factory : factories) {
            System.out.println("F"+factory.getOwnPosition().get(1) + ": " + factory.getName());
        }
    }

    //checks if the industry produces only for the the costumer or for industry/industry and costumer
    /*public static boolean checkIfIndustryIsFinal (int position, List<Factory> factories) {
        for (Factory factory : factories) {
            List<Integer> integers = factory.getPosition();
            for (Integer specificPosition : integers) {
                if (specificPosition == position) return false;
            }
        }
        return true;
    }*/

    //starts the recursive calculation for the all units/industries/total work costs
    public void calcAllIndustries(List<Industry> industries) {
        for (Factory factory : industries.get(ownPosition).factories) {
            if (factory.isOwnOutput() ||factory.isHasNewValues()) factory.recCalcNeed(Arrays.asList(-1,-1),(double) factory.getNeeded(), industries);
            if (factory.isHasNewValues()) factory.recCalcWork(factory.getWorkPerUnit(),industries);
        }
    }

    //
}
