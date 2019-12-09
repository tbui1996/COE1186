package tcss.ctc;

import tcss.main.Main;
import tcss.trackmodel.Track;
import tcss.trainmodel.TrainModel;

import java.util.ArrayList;


public class Dispatch {
    private float SS = 0;
    private int auth = 0;
    private int line; //Red == 1, Green == 2
    public Schedule schedule;
    private int currStop = -1;
    private float [] speedList;
    private int [] authList;
    private int dHr;
    private int dMin;
    private Train train;
    private boolean dispatched;
    //private ArrayList<String> stations;

    public Dispatch(String l, String n) {
        this.line = this.lineStringToInt(l);
        this.train = new Train(n);
        this.dispatched = false;
        //this.SS = 0;
        //this.auth = 0;
    }

    public Dispatch(float SS, int auth, TrainModel train) {
        this.SS = SS;
        this.auth = auth;
        this.dispatched = false;
        //this.train = train;
    }

    public void createSchedule(int l) {
        this.schedule = new Schedule(l);
        this.dHr = 14;
        this.dMin = 0;
    }

    public void setRequests() {
        speedList = new float[schedule.getStopNums() + 1];
        authList = new int[schedule.getStopNums() + 1];


        if (this.line == 1) {
            for (int i = 0; i < schedule.getStopNums(); i++) {
                //Calculates speed and authority for each stop
                //(distance between blocks) / ((station w/ dwell) - dwell), unit is blocks/sec
                if (i == 0) {
                    speedList[i] = (float) (Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))),0) / (this.schedule.getStopDwell(i) - 35)); //stationToYard((schedule.getStopName(i)) / schedule.getStopDwell(i) - 35
                    authList[i] = (int) Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))),1); //stationToYard(schedule.getStopName(i))
                } else {
                    //speedList[i] = stationToStation((schedule.getStopName(i-1),schedule.getStopName(i)) / schedule.getStopDwell(i)*60 - 35;
                    //authList = stationToStation(schedule.getStopName(i-1), schedule.getStopName(i));
                    speedList[i] = (float) (Main.redLine.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))),0) / (float) (this.schedule.getStopDwell(i) - 35));;
                    authList[i] = (int) Main.redLine.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))),1);
                }
            }
            //Send back to yard when done
            speedList[speedList.length-1] = 10/*Min Speed*/;
            authList[authList.length-1] = (int) Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(schedule.getStopNums()-1))),1)/*YARD*/;
        }
        else {

        }

        speedList[speedList.length-1] = 10/*Min Speed*/;
        authList[authList.length-1] = 0/*YARD*/;
    }

    public void setSS(float SS) {
        this.SS = SS;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public float getSS(){
        return this.SS;
    }

    public int getAuth() {
        return this.auth;
    }

    public int getCurrStop() {
        return this.currStop;
    }

    public void setCurrStop(int i) {
        this.currStop = i;
        /**/
    }

    public int getDepartureHour() {
        return this.dHr;
    }

    public int getDepartureMin() {
        return this.dMin;
    }

    public void setDepartureTime(int h, int m) {
        this.dHr = h;
        this.dMin = m;
    }

    public String getStopName(int i) {
        return this.schedule.getStopName(i);
    }

    public String departureTimeString() {
        if (this.dHr > 12) {
            return (this.dHr % 12) + " : " + this.dMin + " PM";
        }
        else {
            return (this.dHr) + " : " +  this.dMin + " AM";
        }
    }

    public int getLine() {
        return line;
    }

    //Set name of Dispatch for display purposes
    public void setName(String n) {
        this.train.setName(n);
    }

    public float getSpeed(int index) {
        return speedList[index];
    }

    public int getAuth(int index) {
        return authList[index];
    }

    public String getName() {
        return this.train.getName();
    }


    public String toString() {
        if (currStop < this.schedule.getStopNums()-1) {
            return "Train: " + this.train.getName() + "\nDeparture Time: " + this.departureTimeString() +
                    "\n" + this.schedule + "\nNext Stop: " + this.schedule.getStopName(currStop + 1);
        }
        else {
            return "Train: " + this.train.getName() + "\nDeparture Time: " + this.departureTimeString() +
                    "\n" + this.schedule + "\nNext Stop: Yard";
        }
    }

    private int lineStringToInt(String line) {
        if (line.equals("RED"))
            return 1;
        else if (line.equals("GREEN"))
            return 2;
        else
            return 0;
    }

    public Train getTrain() {
        return train;
    }

    public void setDispatched() {
        this.dispatched = true;
    }

    public boolean isDispatched() {
        return this.dispatched;
    }

    public int lineToTc() {
        if (this.line == 1)
            return this.line;
        else
            return 0;
    }
}