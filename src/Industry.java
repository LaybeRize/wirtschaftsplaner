import java.util.ArrayList;
import java.util.List;

public class Industry {

    public static class SingleIndustry {
        List<Integer> position;
        List<Double> usage;
        int ownPosition;
        int needed = 0;
        boolean ownOutput = false;

        public SingleIndustry(Integer ownPosition, List<Double> usage, int needed) {
            List<Integer> positions = new ArrayList<>();
            List<Double> usages = new ArrayList<>();

            for (int i = 0; i < usage.size(); i++) {
                if (usage.get(i) != 0) {
                    usages.add(usage.get(i));
                    positions.add(i);
                }
            }

            this.ownPosition = ownPosition;
            this.position = positions;
            this.usage = usages;
            if (needed != 0) {
                this.needed = needed;
                ownOutput = true;
            }
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
        
        /** Pseudo Code for later integeration **/
        
        List<Double> NeedFromOtherIndustries = new ArrayList<>();
        List<Integer> PositionFromOtherIndustries = new ArrayList<>();
        
        public static void recCalc(Integer FromIndustry, Double Need, List<SingleIndustry> singleIndustries) {
            if (FromIndustry >= 0) {
                if (!PositionFromOtherIndustries.contains(FromIndustry) {
                    PositionFromOtherIndustries.add(FromIndustry);
                    NeedFromOtherIndustries.add(0);
                }
                for (int i = 0; i < PositionFromOtherIndustries; i++) {
                    if (PositionFromOtherIndustries.get(i) == FromIndustry) {
                        Double tempNeed = NeedFromOtherIndustries.get(i);
                        tempNeed += Need;
                        NeedFromOtherIndustries.set(i, tempNeed);
                    }
                }
            }
            for (int i = 0; i < position.size(); i++) {
                Double toUse = Need*usage.get(i);
                if (toUse >= 0.5) singleIndustries.get(position.get(i)).recCalc(ownPosition,toUse,singleIndustries);
            }
        }
        
        public int returnFullNeed() {
            int FullNeed = needed;
            for (Double IndustryNeed : NeedFromOtherIndustries) {
                FullNeed += Math.ceil(IndustryNeed);
            }
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
    
    /** Pseudo Code for later integeration **/

    public static void calcAllIndustries(List<SingleIndustry> singleIndustries) {
        for (SingleIndustry singleIndustry : singleIndustries) {
            if (singleIndustry.isOwnOutput) singleIndustry.recCalc(-1,(Double) singleIndustry.getNeeded(),singleIndustries);
        }
    }
    
}
