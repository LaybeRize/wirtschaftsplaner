package Industry;

import java.util.List;

public class FullIndustry {

    private List<SingleIndustry> singleIndustries;
    private int ownPosition;

    public FullIndustry(List<SingleIndustry> singleIndustries, int FullIndustryPosition) {
        this.singleIndustries = singleIndustries;
        ownPosition = FullIndustryPosition;
    }

    //checks if the industry produces only for the the costumer or for industry/industry and costumer
    public static boolean checkIfIndustryIsFinal (int position, List<SingleIndustry> singleIndustries) {
        for (SingleIndustry singleIndustry : singleIndustries) {
            List<Integer> integers = singleIndustry.getPosition();
            for (Integer specificPosition : integers) {
                if (specificPosition == position) return false;
            }
        }
        return true;
    }

    //starts the recursive calculation for the all units/industries/total work costs
    public static void calcAllIndustries(List<SingleIndustry> singleIndustries) {
        for (SingleIndustry singleIndustry : singleIndustries) {
            if (singleIndustry.isOwnOutput()) singleIndustry.recCalcNeed(-1,(double) singleIndustry.getNeeded(),singleIndustries);
            singleIndustry.recCalcWork(singleIndustry.getWorkPerUnit(),singleIndustries);
        }
    }

    //
}
