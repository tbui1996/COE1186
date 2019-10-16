package tcss.TrackController;

public class TrackController {
    TrackModel TrackModel();
    CTC CTC();
    ArrayList<WaysideController> waysideController = new ArrayList<WaysideController>();
    float ss;
    int id;
    int auth;


    public TrackController(TrackController m){

    }
    public void getNextStop(float SS, int auth, int ID) {

        this.ss = ss;
        this.id = id;
        this.auth = auth;
    }
    
}
