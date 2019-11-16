package Industry;

import java.util.ArrayList;
import java.util.List;

public class Factory {
    //position of all the usages for all needed Industry
    private List<Integer> position;
    //needed Industry Outputs from other Industries
    private List<Double> usage;
    //own Position in the Industry List
    private int ownPosition;
    //count of needed Output for this Industry
    private int needed = 0;
    //true if it produces for the consumer false if it produces only for the industry
    private boolean ownOutput = false;
    //looks if a change has been made, since the last start up of the program
    private boolean hasNewValues = false;
    //saves all the requested Output for this Industry from other Industries
    private List<Double> NeedFromOtherFactories = new ArrayList<>();
    //saves the position of the requesting Industries
    private List<Integer> PositionFromOtherFactories = new ArrayList<>();
    //cost of Work in h per unit
    private Double workPerUnit;
    //the total cost in h for every unit
    private Double totalWorkCost = 0.0;
    //Name of the factory
    private String name;

    //Constructor
    public Factory(Integer ownPosition, List<Double> usage, int needed, Double workPerUnit, String name) {
        List<Integer> positions = new ArrayList<>();
        List<Double> usages = new ArrayList<>();

        for (int i = 0; i < usage.size(); i++) {
            if (usage.get(i) != 0) {
                usages.add(usage.get(i));
                positions.add(i);
            }
        }

        this.name = name;
        this.workPerUnit = workPerUnit;
        this.ownPosition = ownPosition;
        this.position = positions;
        this.usage = usages;
        if (needed != 0) {
            this.needed = needed;
            ownOutput = true;
        }
    }

    /** return for all relevant values **/

    public void setHasNewValues(boolean hasNewValues) {
        this.hasNewValues = hasNewValues;
    }

    public boolean isHasNewValues() {
        return hasNewValues;
    }

    public String getName() {
        return name;
    }

    public Double getWorkCostPerUnit() {
        return totalWorkCost;
    }

    public Double getWorkPerUnit() {
        return workPerUnit;
    }

    public List<Double> getNeedFromOtherFactories() {
        return NeedFromOtherFactories;
    }

    public List<Integer> getPositionFromOtherFactories() {
        return PositionFromOtherFactories;
    }

    public boolean isOwnOutput() {
        return ownOutput;
    }

    public int getOwnPosition() {
        return ownPosition;
    }

    public int getNeeded() {
        return needed;
    }

    public List<Double> getUsage() {
        return usage;
    }

    public List<Integer> getPosition() {
        return position;
    }
    /** End **/

    //recursive function to calculate the output needed for the industries
    public void recCalcNeed(Integer FromIndustry, Double Need, List<Factory> factories) {
        //inits the save system for data saving
        if (FromIndustry >= 0) {
            if (!PositionFromOtherFactories.contains(FromIndustry)) {
                PositionFromOtherFactories.add(FromIndustry);
                NeedFromOtherFactories.add(0.0);
            }
            for (int i = 0; i < PositionFromOtherFactories.size(); i++) {
                if (PositionFromOtherFactories.get(i) == FromIndustry) {
                    Double tempNeed = NeedFromOtherFactories.get(i);
                    tempNeed += Need;
                    NeedFromOtherFactories.set(i, tempNeed);
                }
            }
        }
        //calculates the needed units
        for (int i = 0; i < position.size(); i++) {
            Double toUse = Need * usage.get(i);
            if (toUse >= 0.2) factories.get(position.get(i)).recCalcNeed(ownPosition, toUse, factories);
        }
    }

    //calculates the total Work costs in h per Unit via the recursive function Work()
    public void recCalcWork(Double WorkPerUnit, List<Factory> factories) {
        totalWorkCost = Work(WorkPerUnit, factories);
    }

    //recursive function for the calculation of the total Work costs
    public Double Work(Double WorkPerUnit,List<Factory> factories) {
        Double work = WorkPerUnit;
        for (int i = 0; i < position.size(); i++) {
            if (work < 0.05) return work;
            Double tempWork = factories.get(position.get(i)).Work(usage.get(i)*WorkPerUnit,factories);
            work += tempWork;
        }
        return work;
    }

    //returns the combined count of units needed to satisfy all industries
    public int returnFullNeed() {
        int FullNeed = needed;
        for (Double IndustryNeed : NeedFromOtherFactories) {
            FullNeed += Math.ceil(IndustryNeed);
        }
        return FullNeed;
    }
}
