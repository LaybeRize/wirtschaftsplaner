import java.util.ArrayList;
import java.util.List;

public class Industry {

    public static class SingleIndustry {
        private List<Integer> position;
        private List<Double> usage;
        private int ownPosition;
        private int needed = 0;
        private boolean ownOutput = false;
        private List<Double> NeedFromOtherIndustries = new ArrayList<>();
        private List<Integer> PositionFromOtherIndustries = new ArrayList<>();
        private Double workPerUnit;
        private Double totalWorkCost = 0.0;

        public SingleIndustry(Integer ownPosition, List<Double> usage, int needed, Double workPerUnit) {
            List<Integer> positions = new ArrayList<>();
            List<Double> usages = new ArrayList<>();

            for (int i = 0; i < usage.size(); i++) {
                if (usage.get(i) != 0) {
                    usages.add(usage.get(i));
                    positions.add(i);
                }
            }

            this.workPerUnit = workPerUnit;
            this.ownPosition = ownPosition;
            this.position = positions;
            this.usage = usages;
            if (needed != 0) {
                this.needed = needed;
                ownOutput = true;
            }
        }

        public Double getWorkCostPerUnit() {
            return totalWorkCost;
        }

        public Double getWorkPerUnit() {
            return workPerUnit;
        }

        public List<Double> getNeedFromOtherIndustries() {
            return NeedFromOtherIndustries;
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
        
        public void recCalcNeed(Integer FromIndustry, Double Need, List<SingleIndustry> singleIndustries) {
            if (FromIndustry >= 0) {
                if (!PositionFromOtherIndustries.contains(FromIndustry)) {
                    PositionFromOtherIndustries.add(FromIndustry);
                    NeedFromOtherIndustries.add(0.0);
                }
                for (int i = 0; i < PositionFromOtherIndustries.size(); i++) {
                    if (PositionFromOtherIndustries.get(i) == FromIndustry) {
                        Double tempNeed = NeedFromOtherIndustries.get(i);
                        tempNeed += Need;
                        NeedFromOtherIndustries.set(i, tempNeed);
                    }
                }
            }
            for (int i = 0; i < position.size(); i++) {
                Double toUse = Need*usage.get(i);
                if (toUse >= 0.2) singleIndustries.get(position.get(i)).recCalcNeed(ownPosition,toUse,singleIndustries);
            }
        }

        public void recCalcWork(Double WorkPerUnit, List<SingleIndustry> singleIndustries) {
            totalWorkCost = Work(WorkPerUnit, singleIndustries);
        }


        public Double Work(Double WorkPerUnit,List<SingleIndustry> singleIndustries) {
            Double work = WorkPerUnit;
            for (int i = 0; i < position.size(); i++) {
                if (work < 0.05) return 0.05;
                Double tempWork = usage.get(i)*singleIndustries.get(position.get(i)).Work(usage.get(i)*WorkPerUnit,singleIndustries);
                work += tempWork;
            }
            return work;
        }
        public int returnFullNeed() {
            int FullNeed = needed;
            for (Double IndustryNeed : NeedFromOtherIndustries) {
                FullNeed += Math.ceil(IndustryNeed);
            }
            return FullNeed;
        }
        
    }

    public static boolean checkIfIndustryIsFinal (int position, List<SingleIndustry> singleIndustries) {
        for (SingleIndustry singleIndustry : singleIndustries) {
            List<Integer> integers = singleIndustry.getPosition();
            for (Integer specificPosition : integers) {
                if (specificPosition == position) return false;
            }
        }
        return true;
    }
    public static void calcAllIndustries(List<SingleIndustry> singleIndustries) {
        for (SingleIndustry singleIndustry : singleIndustries) {
            if (singleIndustry.isOwnOutput()) singleIndustry.recCalcNeed(-1,(double) singleIndustry.getNeeded(),singleIndustries);
            singleIndustry.recCalcWork(singleIndustry.getWorkPerUnit(),singleIndustries);
        }
    }
    
}
