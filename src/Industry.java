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
}
