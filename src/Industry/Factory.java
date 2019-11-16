package Industry;

import java.util.ArrayList;
import java.util.List;

public class Factory {
    //position of all the usages for all needed Industry
    private List<List<Integer>> position;
    //needed Industry Outputs from other Industries
    private List<Double> usage;
    //own Position in the Industry List
    private List<Integer> ownPosition;
    //count of needed Output for this Industry
    private int needed = 0;
    //true if it produces for the consumer false if it produces only for the industry
    private boolean ownOutput = false;
    //looks if a change has been made, since the last start up of the program
    private boolean hasNewValues = false;
    //saves all the requested Output for this Industry from other Industries
    private List<Double> NeedFromOtherFactories = new ArrayList<>();
    //saves the position of the requesting Industries
    private List<List<Integer>> PositionFromOtherFactories = new ArrayList<>();
    //cost of Work in h per unit
    private Double workPerUnit;
    //the total cost in h for every unit
    private Double totalWorkCost = 0.0;
    //Name of the factory
    private String name;

    //Constructor
    public Factory(List<Integer> ownPosition, List<Double> usage,List<List<Integer>> location, int needed, Double workPerUnit, String name) {
        this.name = name;
        this.workPerUnit = workPerUnit;
        this.ownPosition = ownPosition;
        this.position = location;
        this.usage = usage;
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

    public List<List<Integer>> getPositionFromOtherFactories() {
        return PositionFromOtherFactories;
    }

    public boolean isOwnOutput() {
        return ownOutput;
    }

    public List<Integer> getOwnPosition() {
        return ownPosition;
    }

    public int getNeeded() {
        return needed;
    }

    public List<Double> getUsage() {
        return usage;
    }

    public List<List<Integer>> getPosition() {
        return position;
    }
    /** End **/

    //recursive function to calculate the output needed for the industries
    public void recCalcNeed(List<Integer> FromIndustry, Double Need, List<Industry> industries) {
        //inits the save system for data saving
        if (FromIndustry.get(0) >= 0) {
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
            if (toUse >= 0.2) industries.get(IndustryPosition(i)).factories.get(FactoryPosition(i)).recCalcNeed(ownPosition, toUse, industries);
        }
    }

    //calculates the total Work costs in h per Unit via the recursive function Work()
    public void recCalcWork(Double WorkPerUnit, List<Industry> industries) {
        totalWorkCost = Work(WorkPerUnit, industries);
    }

    //recursive function for the calculation of the total Work costs
    public Double Work(Double WorkPerUnit,List<Industry> industries) {
        Double work = WorkPerUnit;
        for (int i = 0; i < usage.size(); i++) {
            if (work < 0.05) return work;
            Double tempWork = industries.get(IndustryPosition(i)).factories.get(FactoryPosition(i)).Work(usage.get(i)*WorkPerUnit,industries);
            work += tempWork;
        }
        return work;
    }

    public int IndustryPosition(int position) {
        return this.position.get(position).get(0);
    }

    public int FactoryPosition(int position) {
        return this.position.get(position).get(1);
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
