package tcss.ctc;

import java.util.*;

import tcss.trackmodel.TrackModel;
import tcss.trainmodel.*;
import tcss.trackcontroller.*;
import tcss.trackmodel.*;

public class CTC {
    TrackController TC1;
    ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    public ArrayList<TrainModel> trainList = new ArrayList<TrainModel>(); /*Number of Trains*/

    //Temporary Red and Green Line setup for creating a Dispatch
    private LinkedList<Block> redLine;
    private LinkedList<Block> greenLine;
    private String stationNames [];


    public CTC(TrackController track) {

        this.TC1 = track;

        //Temporary Red and Green Line setup for creating a Dispatch
        redLine = new LinkedList<Block>();
        greenLine = new LinkedList<Block>();
        stationNames = new String[5];
        stationNames[0] = "Dormont";
        stationNames[1] = "Station Square";
        stationNames[2] = "First Ave";
        stationNames[3] = "South Hills Junction";
        stationNames[4] = "Swissvale";
        for (int i=0; i < 5; i++) {
            if (i%2 == 0) {
                redLine.add(new Block());
                redLine.get(i).setStation(new Station(stationNames[i]));
            }
            else {
                greenLine.add(new Block());
                greenLine.get(i).setStation(new Station(stationNames[i]));
            }
        }

    }

    public void createDispatch(String name, float SS, int auth, TrainModel train) {
        //TrainModel temp = new TrainModel(SS, auth, trainList.size(), 55);
        //this.trainList.add(new TrainModel(name, trainList.size()));
        System.out.println("Red Line:");
        //Start here and Fix this
        for (int i = 0; i < 10; i++) {

        }
        this.trainList.add(train);
        this.dispatchList.add(new Dispatch(SS, auth, this.trainList.get(this.trainList.size()-1)));
        this.dispatchList.get(this.dispatchList.size()-1).createSchedule();
        System.out.println(this.dispatchList.get(this.dispatchList.size()-1));
        sendNextStop(SS, auth, train.getID());
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


}
