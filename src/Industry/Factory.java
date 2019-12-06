package Industry;

import java.awt.image.AreaAveragingScaleFilter;
import java.text.DecimalFormat;
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
    private boolean newValues = true;
    //saves all the requested Output for this Industry from other Industries
    private List<Double> needFromOtherFactories = new ArrayList<>();
    //saves the position of the requesting Industries
    private List<List<Integer>> positionFromOtherFactories = new ArrayList<>();
    //saves all the requested Work for other Industries from this Industry
    private String FactoryFrom = "";
    private String FactoryTo = "";
    private String WorkString = "";
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

    public void setNewValues(boolean NewValues) {
        this.newValues = NewValues;
    }

    public boolean hasNewValues() {
        return newValues;
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
        return needFromOtherFactories;
    }

    public List<List<Integer>> getPositionFromOtherFactories() {
        return positionFromOtherFactories;
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
            if (!positionFromOtherFactories.contains(FromIndustry)) {
                positionFromOtherFactories.add(FromIndustry);
                needFromOtherFactories.add(0.0);
            }
            for (int i = 0; i < positionFromOtherFactories.size(); i++) {
                if (positionFromOtherFactories.get(i) == FromIndustry) {
                    Double tempNeed = needFromOtherFactories.get(i);
                    tempNeed += Need;
                    needFromOtherFactories.set(i, tempNeed);
                }
            }
        }
        //calculates the needed units
        for (int i = 0; i < position.size(); i++) {
            Double toUse = Need * usage.get(i);
            if (toUse >= 1) industries.get(IndustryPosition(i)).factories.get(FactoryPosition(i)).recCalcNeed(ownPosition, toUse, industries);
        }
    }

    //calculates the total Work costs in h per Unit via the recursive function Work()
    public void recCalcWork(Double WorkPerUnit, List<Industry> industries) {
        StringDouble work = Work(WorkPerUnit, industries, name);
        totalWorkCost = work.work;
        work.correct(WorkPerUnit);
        if (FactoryTo.equals("")) {
            work.checkDoubles(name);
            for (String string : work.NameFrom) {
                FactoryFrom = FactoryFrom + '"' + string + '"' + ",";
            }
            for (String string : work.NameTo) {
                FactoryTo = FactoryTo + '"' + string + '"' + ",";
            }
            for (Integer integer : work.Work) {
                WorkString = WorkString + integer.toString() + ",";
            }
            FactoryFrom = FactoryFrom.substring(0, FactoryFrom.length() - 1);
            FactoryTo = FactoryTo.substring(0, FactoryTo.length() - 1);
            WorkString = WorkString.substring(0, WorkString.length() - 1);
        }
    }

    public void printAllStrings() {
        System.out.println("  source=c(" + FactoryFrom + "),");
        System.out.println("  target=c(" + FactoryTo + "),");
        System.out.println("  value=c(" + WorkString + ")");
    }

    //recursive function for the calculation of the total Work costs
    public StringDouble Work(Double WorkPerUnit,List<Industry> industries, String nameTo) {
        StringDouble work = new StringDouble();
        work.work = WorkPerUnit;
        for (int i = 0; i < usage.size(); i++) {
            if (work.work < 0.05) return work;
            StringDouble tempWork = industries.get(IndustryPosition(i)).factories.get(FactoryPosition(i)).Work(usage.get(i)*WorkPerUnit,industries, name);
            work.addAll(tempWork);
        }
        work.add(name, nameTo, work.work);
        return work;
    }

    public class StringDouble {
        public Double work;
        List<String> NameFrom = new ArrayList<>();
        List<String> NameTo = new ArrayList<>();
        List<Integer> Work = new ArrayList<>();

        public StringDouble() {

        }

        public void checkDoubles(String Name) {
            for (int i = 0; i < NameFrom.size(); i++) {
                if (NameFrom.get(i).equals(Name)) NameFrom.set(i, "own");
            }
            List<String> FutureFrom = new ArrayList<>();
            List<String> FutureTo = new ArrayList<>();
            List<Integer> FutureWork = new ArrayList<>();
            for (int i = 0; i < NameFrom.size(); i++) {
                boolean exist = false;
                int pos = 0;
                for (int a = 0; a < FutureFrom.size(); a++) {
                    if (FutureFrom.get(a).equals(NameFrom.get(i)) && FutureTo.get(a).equals(NameTo.get(i))) {
                        exist = true;
                        pos = a;
                    }
                }
                if (exist) {
                    FutureWork.set(pos, FutureWork.get(pos) + Work.get(i));
                } else {
                    FutureFrom.add(NameFrom.get(i));
                    FutureTo.add(NameTo.get(i));
                    FutureWork.add(Work.get(i));
                }
            }
            NameFrom = FutureFrom;
            NameTo = FutureTo;
            Work = FutureWork;
        }

        public void addAll(StringDouble stringDouble) {
            work += stringDouble.work;
            NameFrom.addAll(stringDouble.NameFrom);
            NameTo.addAll(stringDouble.NameTo);
            Work.addAll(stringDouble.Work);
        }

        public void correct(Double work) {
            work *= 100;
            Long workR = Math.round(work);
            Work.set(Work.size()-1, Math.toIntExact(workR));
        }

        public void add(String nameFrom, String nameTo, Double work) {
            NameFrom.add(nameFrom);
            NameTo.add(nameTo);
            work *= 100;
            Long workR = Math.round(work);
            Work.add(Math.toIntExact(workR));
        }
    }

    public int IndustryPosition(int position) {
        return this.position.get(position).get(0) - 1;
    }

    public int FactoryPosition(int position) {
        return this.position.get(position).get(1) - 1;
    }

    //returns the combined count of units needed to satisfy all industries
    public int returnFullNeed() {
        int FullNeed = needed;
        for (Double IndustryNeed : needFromOtherFactories) {
            FullNeed += Math.ceil(IndustryNeed);
        }
        return FullNeed;
    }
}
