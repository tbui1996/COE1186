package tcss.ctc;

import tcss.main.Main;
import tcss.trainmodel.TrainModel;


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
    private int dwell;
    //private ArrayList<String> stations;

    public Dispatch(String n) {
        this.line = 0;
        this.train = new Train(n);
        this.dispatched = false;
        this.dwell = 0;
    }

    public Dispatch(String l, String n) {
        this.line = this.lineStringToInt(l);
        this.train = new Train(n);
        this.dispatched = false;
        this.dwell = 0;
        //this.SS = 0;
        //this.auth = 0;
    }

    public Dispatch(float SS, int auth, TrainModel train) {
        this.SS = SS;
        this.auth = auth;
        this.dispatched = false;
        this.dwell = 0;
        //this.train = train;
    }

    /**
     * Calls the schedule constructor to create the object to be able to add stop to it
     * @param l
     */
    public void createSchedule(int l) {
        this.schedule = new Schedule(l);
        this.dHr = -1;
        this.dMin = -1;
    }

    /**
     * Creates the SS and Auth to be sent for each stop on the schedule.  Stores them in an array to be sent at the proper time later
     */
    public void setRequests() {
        //Set Departure Time
        //int fMin = this.schedule.getStopMin(0);
        double distance = Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(0))), 0); // Returns the distance in meters
        int travelTime = ((int) distance) / 11; //Distance / slowest speed on track returns travel time in seconds.  Divide my 60 again for minutes
        if (travelTime >= 60) {
            travelTime = travelTime / 60;

            this.dHr = this.schedule.getStopHour(0) - (travelTime / 60);
            if ((this.schedule.getStopMin(0) - (travelTime % 60)) == 0) {
                this.dMin = 0;
            } else if (this.dHr == 0) {
                this.dMin = (this.schedule.getStopMin(0) - (travelTime % 60));
            } else {
                this.dMin = 60 - (this.schedule.getStopMin(0) - (travelTime % 60));
            }
        }
        else {
            if (this.schedule.getStopMin(0) != 0) {
                this.dMin = this.schedule.getStopMin(0) - 1;
            } else if (this.schedule.getStopHour(0) != 0) {
                this.dHr = this.schedule.getStopHour(0) - 1;
                this.dMin = 59;
            }
        }

        if (dHr < 0) {
            dHr = 0;
        }

        if (dMin < 0) {
            dMin = 0;
        }

        if (dMin >= 60) {
            dHr = dHr + dMin / 60;
            dMin = dMin % 60;
        }

        speedList = new float[schedule.getStopNums() + 1];
        authList = new int[schedule.getStopNums() + 1];

        //Red Line
        if (this.line == 1) {
            for (int i = 0; i < this.schedule.getStopNums(); i++) {
                //Calculates speed and authority for each stop
                //(distance between blocks) / ((station w/ dwell) - dwell), unit is blocks/sec
                if (i == 0) {
                    //Distance / Time
                    //(Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.blockReturner(this.line, this.schedule.getStopName(i))), 0)) / //time;
                    speedList[i] = (float) 11;
                    authList[i] = (int) Main.ctc.redLayout.distanceBetweenTwoBlocks(Main.ctc.redLine.get(9), Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i))), 1);
                } else {
                    int time = ((this.schedule.getStopHour(i)*60*60) + (this.schedule.getStopMin(i)*60)) - ((this.schedule.getStopHour(i-1)*60*60) + this.schedule.getStopMin(i-1)*60) - 35;
                    speedList[i] = (float) (Main.ctc.redLayout.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i-1))), Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i))), 0) / time);
                    authList[i] = (int) (Main.ctc.redLayout.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i-1))), Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i))), 1));
                }
            }

            //Send back to yard when done
            speedList[speedList.length-1] = 10; //Min Speed
            authList[authList.length - 1] = (int) Main.ctc.redLayout.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(schedule.getStopNums() - 1))), Main.ctc.redLine.get(9), 1);//YARD

        } else {
            for (int i = 0; i < this.schedule.getStopNums(); i++) {
                //Calculates speed and authority for each stop
                //(distance between blocks) / ((station w/ dwell) - dwell), unit is blocks/sec
                if (i == 0) {
                    //Distance / Time
                    //(Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.blockReturner(this.line, this.schedule.getStopName(i))), 0)) / //time;
                    speedList[i] = (float) 40;
                    authList[i] = (int) Main.ctc.greenLayout.distanceBetweenTwoBlocks(Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i))), Main.ctc.redLine.get(57),1);
                } else {
                    int time = ((this.schedule.getStopHour(i)*60*60) + (this.schedule.getStopMin(i)*60)) - ((this.schedule.getStopHour(i-1)*60*60) + this.schedule.getStopMin(i-1)*60) - 35;
                    speedList[i] = (float) (Main.ctc.greenLayout.distanceBetweenTwoBlocks(Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i-1))), Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i))), 0) / time);
                    authList[i] = (int) (Main.ctc.greenLayout.distanceBetweenTwoBlocks(Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i-1))), Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(i))), 1));
                }
            }

            //Send back to yard when done
            speedList[speedList.length-1] = 10; //Min Speed
            authList[authList.length - 1] = (int) Main.ctc.greenLayout.distanceBetweenTwoBlocks(Main.ctc.greenLine.get(57), Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(schedule.getStopNums() - 1))), 1);//YARD
        }
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

    /*public void setSpeedList(float s, int a) {

        speedList = new float[2];
        speedList[0] = s;
        speedList[1] = 10;
        authList = new int[2];
        authList[0] = a;
        if (this.line == 1) {
            authList[1] = (int) Main.ctc.redLayout.distanceBetweenTwoBlocks(Main.ctc.redLine.get(57), Main.ctc.redLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(schedule.getStopNums() - 1))), 1);//YARD
        } else {
            authList[1] = (int) Main.ctc.greenLayout.distanceBetweenTwoBlocks(Main.ctc.greenLine.get(57), Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(schedule.getStopNums() - 1))), 1);//YARD
        }
    }*/

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
        return this.schedule.toString();
    }

    /**
     * Converts the string representation of the line to the int version
     * @param line
     * @return
     */
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

    public int getDwell() {
        return this.dwell;
    }

    public void setDwell(int i) {
        this.dwell = i;
    }

    /**
     * Converts line to proper int for the Track Controller
     * @return
     */
    public int lineToTc() {
        if (this.line == 1)
            return this.line;
        else
            return 0;
    }
}