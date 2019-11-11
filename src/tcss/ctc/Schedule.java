package tcss.ctc;

import java.util.*;

public class Schedule {
    private int line;   //Red == 1, Green == 2
    private ArrayList<String> stopList;
    private ArrayList<Float> dwellList;

    public Schedule(int line) {
        this.line = line;
        this.stopList = new ArrayList<String>();
        this.dwellList = new ArrayList<Float>();
    }

    public void addStop(String stopName, float minutes) {
        this.stopList.add(stopName);
        this.dwellList.add(minutes);
    }

    public String getLine() {
        if (line == 1)
            return "RED";
        else
            return "GREEN";
    }

    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append("line: " + this.getLine());
        for (int i = 0; i < this.stopList.size(); i++)
            temp.append("\nStop Name: " + this.stopList.get(i) + "\nDwell: " + this.dwellList.get(i));

        return temp.toString();
    }
}

