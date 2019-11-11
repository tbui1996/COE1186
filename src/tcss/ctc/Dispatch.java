package tcss.ctc;

import tcss.trainmodel.TrainModel;

public class Dispatch {
    private float SS;
    private int auth;
    private Schedule schedule;
    public String trainName;
    private int mode;
    private TrainModel train;

    public Dispatch() {
        this.SS = 0;
        this.auth = 0;
    }

    public Dispatch(float SS, int auth, TrainModel train) {
        this.SS = SS;
        this.auth = auth;
        this.train = train;
    }

    public void createSchedule() {
        this.schedule = new Schedule(1);
        this.schedule.addStop("Shadyside", (float) 3.7);
        this.schedule.addStop("Herron Ave", (float) 2.3);
        this.schedule.addStop("Swissvale", (float) 1.5);
        this.schedule.addStop("Penn Station", (float) 1.8);
        this.schedule.addStop("Steel Plaza", (float) 2.1);
        this.schedule.addStop("First Ave", (float) 2.1);
        this.schedule.addStop("Station Square", (float) 1.7);
        this.schedule.addStop("South Hills Junction", (float) 2.3);
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


    public String toString() {
        return "ID: " + this.train.getID() + "\nSuggested Speed: " + this.train.getSSpeed() + "\nAuthority: " + this.train.getAuthority() +
        "Schedule: " + this.schedule;
    }
}
