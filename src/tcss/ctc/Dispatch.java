package tcss.ctc;

import tcss.trainmodel.TrainModel;


public class Dispatch {
    private String name;
    private float SS;
    private int auth;
    private int line; //Red == 1, Green == 2
    public Schedule schedule;
    public String trainName;
    private int mode;
    private TrainModel train;
    private int currStop = -1;
    private float speedList [];
    private int authList [];
    private int arrivalTime;
    private int departureTime;

    public Dispatch(String l, String n) {
        this.line = this.lineStringToInt(l);
        this.name = n;
        //this.SS = 0;
        //this.auth = 0;
    }

    public Dispatch(float SS, int auth, TrainModel train) {
        this.SS = SS;
        this.auth = auth;
        this.train = train;
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
        this.arrivalTime = 500;
        this.departureTime = 100;
    }

    public void setRequests() {
        speedList = new float[schedule.getStopNums()];
        authList = new int[schedule.getStopNums()];


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

    public int getArrivalTime() {
        return arrivalTime % 12;
    }

    public void setArrivalTime(int t) {
        this.arrivalTime = t;
    }

    public String arrivalTimeString() {
        if (this.arrivalTime > 12) {
            return (this.arrivalTime % 12) + "PM";
        }
        else {
            return (this.arrivalTime) + "AM";
        }
    }

    public int getDepartureTime() {
        return departureTime % 12;
    }

    public void setDepartureTime(int t) {
        this.departureTime = t;
    }

    public String departureTimeString() {
        if (this.departureTime > 12) {
            return (this.departureTime % 12) + " PM";
        }
        else {
            return (this.departureTime) + " AM";
        }
    }

    public int getLine() {
        return line;
    }

    //Set name of Dispatch for display purposes
    public void setName(String n) {
        this.name = n;
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