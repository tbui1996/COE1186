package tcss.ctc;

import java.util.*;
//import tcss.trackcontroller.TrackController;

public class CTC {
    //TrackController TC1;
    ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    ArrayList<Train> trainList = new ArrayList<Train>(); /*Number of Trains*/

    public CTC() {

        //this.TC1 = TrackController();

    }

    public void createDispatch(String name, float SS, int auth) {
        this.trainList.add(new Train(name, trainList.size()));
        this.dispatchList.add(new Dispatch(SS, auth, this.trainList.get(this.trainList.size()-1)));
        //sendNextStop(32, 5, this.trainList.get(this.trainList.size()-1).ID);
    }

    public void sendNextStop(float SS, int auth, int ID) {
        //this.TC1.getNextStop(SS, auth, ID);
    }

    public Dispatch getFirstDispatch() {
        return dispatchList.get(0);
    }
}
