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

    public void createSchedule(int l) {
        this.schedule = new Schedule(l);
        this.dHr = -1;
        this.dMin = -1;
    }

    /*public void setRequests() {
        speedList = new float[schedule.getStopNums() + 1];
        authList = new int[schedule.getStopNums() + 1];

        if (this.line == 1) {
            for (int i = 0; i < schedule.getStopNums(); i++) {
                //Calculates speed and authority for each stop
                //(distance between blocks) / ((station w/ dwell) - dwell), unit is blocks/sec
                if (i == 0) {
                    if (this.schedule.getStopName(i).length() > 3) {
                        speedList[i] = (float) (Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), 0) / (this.schedule.getStopDwell(i) - 35)); //stationToYard((schedule.getStopName(i)) / schedule.getStopDwell(i) - 35
                        authList[i] = (int) Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), 1); //stationToYard(schedule.getStopName(i))
                    } else {
                        speedList[i] = (float) (Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Integer.parseInt(this.schedule.getStopName(i))), 0) / (this.schedule.getStopDwell(i) - 35)); //stationToYard((schedule.getStopName(i)) / schedule.getStopDwell(i) - 35
                        authList[i] = (int) Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Integer.parseInt(this.schedule.getStopName(i))), 1); //stationToYard(schedule.getStopName(i))
                    }
                } else {
                    //speedList[i] = stationToStation((schedule.getStopName(i-1),schedule.getStopName(i)) / schedule.getStopDwell(i)*60 - 35;
                    //authList = stationToStation(schedule.getStopName(i-1), schedule.getStopName(i));
                    if (this.schedule.getStopName(i).length() > 3) {
                        speedList[i] = (float) (Main.redLine.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), 0) / (float) (this.schedule.getStopDwell(i) - 35));
                        authList[i] = (int) Main.redLine.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(i))), 1);
                    } else {
                        speedList[i] = (float) (Main.redLine.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Integer.parseInt(this.schedule.getStopName(i))), Main.ctc.redLine.get(Integer.parseInt(this.schedule.getStopName(i))), 0) / (float) (this.schedule.getStopDwell(i) - 35));
                        authList[i] = (int) Main.redLine.distanceBetweenTwoBlocks(Main.ctc.redLine.get(Integer.parseInt(this.schedule.getStopName(i))), Main.ctc.redLine.get(Integer.parseInt(this.schedule.getStopName(i))), 1);
                    }
                }
            }
            //Send back to yard when done
            speedList[speedList.length-1] = 10; //Min Speed
            if (this.schedule.getStopName(schedule.getStopNums()-1).length() > 3) {
                authList[authList.length - 1] = (int) Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Main.ctc.stationToBlockNumRed.get(this.schedule.getStopName(schedule.getStopNums() - 1))), 1);//YARD
            } else {
                authList[authList.length - 1] = (int) Main.ctc.redLayout.distanceToYard(Main.ctc.redLine.get(Integer.parseInt(this.schedule.getStopName(schedule.getStopNums() - 1))), 1);//YARD
            }
        }
        else {

        }
    }*/

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
            this.dHr = this.schedule.getStopHour(0);
            this.dMin = this.schedule.getStopMin(0);
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
                    speedList[i] = (float) 40;
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
            authList[authList.length - 1] = (int) Main.ctc.greenLayout.distanceBetweenTwoBlocks(Main.ctc.redLine.get(57), Main.ctc.greenLine.get(Main.ctc.blockReturner(this.getLine(), this.schedule.getStopName(schedule.getStopNums() - 1))), 1);//YARD
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

    public int getDwell() {
        return this.dwell;
    }

    public void setDwell(int i) {
        this.dwell = i;
    }

    public int lineToTc() {
        if (this.line == 1)
            return this.line;
        else
            return 0;
    }
}