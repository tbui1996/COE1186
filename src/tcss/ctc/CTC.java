package tcss.ctc;

import java.util.*;

import tcss.trackmodel.TrackModel;
import tcss.trainmodel.*;
import tcss.trackcontroller.*;

public class CTC {
    TrackController TC1;
    ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    public ArrayList<TrainModel> trainList = new ArrayList<TrainModel>(); /*Number of Trains*/

    public CTC(TrackController track) {

        this.TC1 = track;

    }

    public void createDispatch(String name, float SS, int auth, TrainModel train) {
        //TrainModel temp = new TrainModel(SS, auth, trainList.size(), 55);
        //this.trainList.add(new TrainModel(name, trainList.size()));
        this.trainList.add(train);
        this.dispatchList.add(new Dispatch(SS, auth, this.trainList.get(this.trainList.size()-1)));
        sendNextStop(SS, auth, train.getID());
    }
    public TrackController getTrackController(int index){
        return this.TC1;
    }

    public void sendNextStop(float SS, int auth, int ID) {
        //this.TC1.getNextStop(SS, auth, ID);
    }

    public Dispatch getFirstDispatch() {
        return dispatchList.get(0);
    }


}
