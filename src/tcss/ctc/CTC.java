package tcss.ctc;

import tcss.trackcontroller.TrackController;
import java.util.ArrayList;

public class CTC {
    TrackController TC1;
    ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    ArrayList<Train> trainList = new ArrayList<Train>(); /*Number of Trains*/

    public CTC() {
        this.TC1 = new TrackController();
    }

    public void createDispatch(String name) {
        this.trainList.add(new Train(name, trainList.size()));
        this.dispatchList.add(new Dispatch(32, 5, this.trainList.get(this.trainList.size()-1)));
        //sendNextStop(32, 5, this.trainList.get(this.trainList.size()-1).getID());
    }

    public void sendNextStop(float SS, int auth, int ID) {
        this.TC1.getNextStop(SS, auth, ID);
    }
}
