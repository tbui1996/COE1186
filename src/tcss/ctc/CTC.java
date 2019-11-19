package tcss.ctc;

import java.util.*;

import tcss.trackmodel.TrackModel;
import tcss.trainmodel.*;
import tcss.trackcontroller.*;
import tcss.trackmodel.*;

public class CTC {
    TrackController TC1;
    private ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    public ArrayList<TrainModel> trainList = new ArrayList<TrainModel>(); /*Number of Trains*/

    //Temporary Red and Green Line setup for creating a Dispatch
    private LinkedList<Block> redLine;
    private LinkedList<Block> greenLine;
    private String [] stationNames; //This will be deleted

    //String ArrayLists of all of the stations on each line
    private String [] redStations;
    private String [] greenStations;





    public CTC(TrackController track) {

        this.TC1 = track;

        //Temporary Red and Green Line setup for creating a Dispatch
        redLine = new LinkedList<Block>();
        greenLine = new LinkedList<Block>();
        stationNames = new String[5];
        stationNames[0] = "Dormont";
        stationNames[1] = "Shadyside";
        stationNames[2] = "First Ave";
        stationNames[3] = "South Hills Junction";
        stationNames[4] = "Swissvale";

        //Initialize Station lists for each line
        redStations = new String[3];
        greenStations = new String[3];

        //Get rid of these.  Used to prevent ArrayIndexOutOfBounds for redStations/greenStations
        int redCount = 0;
        int greenCount = 0;

        for (int i=0; i < 5; i++) {
            Block temp = new Block();
            temp.setStation(new Station(stationNames[i]));
            if (i%2 == 0) {
                redLine.add(temp);
                greenLine.add(new Block());
                redStations[redCount] = stationNames[i]; //Adding the station name on the block the the Array
                redCount++;
            }
            else {
                greenLine.add(temp);
                redLine.add(new Block());
                greenStations[greenCount] = stationNames[i];
                greenCount++;
            }
        }

    }

    public void createDispatch(String name, float SS, int auth, TrainModel train) {
        //TrainModel temp = new TrainModel(SS, auth, trainList.size(), 55);
        //this.trainList.add(new TrainModel(name, trainList.size()));
        //Start here and Fix this
        for (int i = 0; i < redLine.size(); i++) {
            System.out.println("Red Block " + i + ": " + redLine.get(i).toString());
            System.out.println("Green Block " + i + ": " + greenLine.get(i).toString());
        }
        this.trainList.add(train);
        this.dispatchList.add(new Dispatch(SS, auth, this.trainList.get(this.trainList.size()-1)));
        this.dispatchList.get(this.dispatchList.size()-1).createSchedule();
        this.dispatchList.get(this.dispatchList.size()-1).setRequests();
        System.out.println(this.dispatchList.get(this.dispatchList.size()-1));
        sendNextStop(SS, auth, train.getID());
    }

    public void addDispatch(Dispatch d) {
        this.dispatchList.add(d);
    }

    //This checks dispatch list to see if a new suggested speed and authority need to be sent
    public void checkDispatchList () {
        for (int i = 0; i < dispatchList.size(); i++) {
            Dispatch temp = dispatchList.get(i);
            //if train is not dispatched yet
            if (temp.getCurrStop() == -1) {
                if (100 == temp.getDepartureTime()) { //This should be if departure time == current global time
                    //temp.set
                }
            }
            else {

            }
        }
    }

    public TrackController getTrackController(int index){
        return this.TC1;
    }

    public void sendNextStop(float SS, int auth, int ID) {
        this.TC1.getNextStop(SS, auth, ID);
    }

    public Dispatch getFirstDispatch() {
        return dispatchList.get(0);
    }

    //Return a String array of all stops in a line
    public String [] getAllStops(int l) {
        if (l == 1) {
            return redStations;
        }
        else {
            return greenStations;
        }
    }


}