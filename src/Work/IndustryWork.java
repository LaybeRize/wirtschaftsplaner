package Work;

import java.util.List;

public class IndustryWork {
    private String name;
    private int position;
    private List<Double> work;

    public IndustryWork(String name, int position, List<Double> work) {
        this.name = name;
        this.position = position;
        this.work = work;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public List<Double> getWork() {
        return work;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setWork(List<Double> work) {
        this.work = work;
    }

}
