package Industry;

import java.util.List;

public class Industry {

    private List<Factory> factories;
    private int ownPosition;
    private String name;

    public Industry(List<Factory> factories, int IndustryPosition, String name) {
        this.factories = factories;
        this.name = name;
        ownPosition = IndustryPosition;
    }

    //checks if the industry produces only for the the costumer or for industry/industry and costumer
    public static boolean checkIfIndustryIsFinal (int position, List<Factory> factories) {
        for (Factory factory : factories) {
            List<Integer> integers = factory.getPosition();
            for (Integer specificPosition : integers) {
                if (specificPosition == position) return false;
            }
        }
        return true;
    }

    //starts the recursive calculation for the all units/industries/total work costs
    public static void calcAllIndustries(List<Factory> factories) {
        for (Factory factory : factories) {
            if (factory.isOwnOutput()) factory.recCalcNeed(-1,(double) factory.getNeeded(),factories);
            factory.recCalcWork(factory.getWorkPerUnit(),factories);
        }
    }

    //
}
