package tcss.ctc;

import tcss.trainmodel.TrainModel;


public class Dispatch {
    private String name;
    private float SS = 0;
    private int auth = 0;
    private int line; //Red == 1, Green == 2
    public Schedule schedule;
    //public String trainName;
    private int mode;
    //private TrainModel train;
    private int currStop = -1;
    private float [] speedList;
    private int [] authList;
    private int aHr;
    private int aMin;
    private int dHr;
    private int dMin;

    public Dispatch(String l, String n) {
        this.line = this.lineStringToInt(l);
        this.name = n;
        //this.SS = 0;
        //this.auth = 0;
    }

    public Dispatch(float SS, int auth, TrainModel train) {
        this.SS = SS;
        this.auth = auth;
        //this.train = train;
    }

    public void createSchedule(int l) {
        this.schedule = new Schedule(l);
        //this.schedule.addStop("Shadyside", (float) 3.7);
        //this.schedule.addStop("Herron Ave", (float) 2.3);
        //this.schedule.addStop("Swissvale", (float) 1.5);
        //this.schedule.addStop("Penn Station", (float) 1.8);
        //this.schedule.addStop("Steel Plaza", (float) 2.1);
        //this.schedule.addStop("First Ave", (float) 2.1);
        //this.schedule.addStop("Station Square", (float) 1.7);
        //this.schedule.addStop("South Hills Junction", (float) 2.3);
        this.aHr = 11;
        this.aMin = 0;
        this.dHr = 14;
        this.dMin = 0;
    }

    public void setRequests() {
        speedList = new float[schedule.getStopNums() + 1];
        authList = new int[schedule.getStopNums() + 1];


        for (int i = 0; i < schedule.getStopNums(); i++) {
            //Calculates speed and authority for each stop
            //(distance between blocks) / ((station w/ dwell) - dwell), unit is blocks/sec
            if (i == 0) {
                speedList[i] = (1 / (float) (this.schedule.getStopDwell(i) - 35)); //stationToYard((schedule.getStopName(i)) / schedule.getStopDwell(i) - 35
                authList[i] = 1; //stationToYard(schedule.getStopName(i))
            }
            else {
                //speedList[i] = stationToStation((schedule.getStopName(i-1),schedule.getStopName(i)) / schedule.getStopDwell(i)*60 - 35;
                //authList = stationToStation(schedule.getStopName(i-1), schedule.getStopName(i));
                speedList[i] = (3 / (float) (1.5 * 60 - 35));
                authList[i] = 3;
            }
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

    public void sendNextStop() {

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

    public int getArrivalHour() {
        return this.aHr;
    }

    public int getArrivalMin() {
        return this.aMin;
    }

    public void setArrivalTime(int h, int m) {
        this.aHr = h;
        this.aMin = m;
    }

    public String arrivalTimeString() {
        if (this.aHr > 12) {
            return (this.aHr % 12) + " : " + this.aMin + " PM";
        }
        else {
            return (this.aHr) + " : " + this.aMin + " AM";
        }
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
        this.name = n;
    }

    public float getSpeed(int index) {
        return speedList[index];
    }

    public int getAuth(int index) {
        return authList[index];
    }

    public String getName() {
        return this.name;
    }


    public String toString() {
        return /*"ID: " + this.train.getID() + "\nSuggested Speed: " + this.train.getSSpeed() + "\nAuthority: " + this.train.getAuthority() +
                */"Schedule: \n" + "Departure Time: " + this.departureTimeString() + "\n" + this.schedule + "\nFinal Arrival Time: " +
                this.arrivalTimeString() + "\nNext Stop: " + this.schedule.getStopName(currStop+1);
    }

    private int lineStringToInt(String line) {
        if (line.equals("RED"))
            return 1;
        else if (line.equals("GREEN"))
            return 2;
        else
            return 0;
    }
}