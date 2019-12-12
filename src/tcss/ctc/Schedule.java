package tcss.ctc;

import java.util.*;

public class Schedule {

    private String name = "";
    private int line;   //Red == 1, Green == 2
    private ArrayList<String> stopList;
    private ArrayList<Float> dwellList;
    private ArrayList<Integer> aHr;
    private ArrayList<Integer> aMin;


    //Manual Mode
    public Schedule(int line) {
        this.line = line;
        this.stopList = new ArrayList<String>();
        this.dwellList = new ArrayList<Float>();
        this.aHr = new ArrayList<>();
        this.aMin = new ArrayList<>();
    }

    /*public void addStop(String stopName, float seconds) {
        this.stopList.add(stopName);
        this.dwellList.add(seconds);
    }*/

    public void addStop(String stopName, int hr, int min) {
        this.stopList.add(stopName);
        this.aHr.add(hr);
        this.aMin.add(min);
    }

    public String getLine() {
        if (line == 1)
            return "RED";
        else
            return "GREEN";
    }

    //Set line as int
    public void setLine(int line) {
        line = line;
    }

    public int getStopNums() {
        return this.stopList.size();
    }

    public String getStopName(int i) {
        return stopList.get(i);
    }

    public int getStopHour(int i) {
        return this.aHr.get(i);
    }

    public int getStopMin(int i) {
        return this.aMin.get(i);
    }

    /**
     * Returns string representation of the arrival time of a given stop, i
     * @param i
     * @return
     */
    private String aTimeString(int i) {
        if (aHr.get(i) == 0) {
            if (aMin.get(i) < 10) {
                return "12:0" + this.aMin.get(i) + " AM";
            } else {
                return "12:" + this.aMin.get(i) + " AM";
            }
        } else if (aHr.get(i) <= 11) {
            if (aMin.get(i) < 10) {
                return this.aHr.get(i) + ":0" + this.aMin.get(i) + " AM";
            } else {
                return this.aHr.get(i) + ":" + this.aMin.get(i) + " AM";
            }
        } else if (aHr.get(i) == 12) {
            if (aMin.get(i) < 10) {
                return "12:0" + this.aMin.get(i) + " PM";
            } else {
                return "12:" + this.aMin.get(i) + " PM";
            }
        } else {
            if (aMin.get(i) < 10) {
                return this.aHr.get(i)%12 + ":0" + this.aMin + " PM";
            } else {
                return this.aHr.get(i)%12 + ":" + this.aMin + " PM";
            }
        }
    }

    /**
     * String representation of the schedule
     * @return
     */
    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append("line: " + this.getLine());
        for (int i = 0; i < this.stopList.size(); i++)
            temp.append("\nStop Name: " + this.stopList.get(i) + "\nArrival Time: " + this.aTimeString(i));

        return temp.toString();
    }

    public void setName(String n) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
