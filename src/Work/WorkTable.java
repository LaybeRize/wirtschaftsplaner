package Work;

import java.util.ArrayList;
import java.util.List;

public class WorkTable {

    private List<IndustryWork> industryWorkList = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    public WorkTable() {}

    public List<IndustryWork> getIndustryWorkList() {
        return industryWorkList;
    }

    public List<String> getNames() {
        return names;
    }

    public void addIndustryWork(String name, IndustryWork industryWork) {
        names.add(name);
        industryWorkList.add(industryWork);
    }
}
