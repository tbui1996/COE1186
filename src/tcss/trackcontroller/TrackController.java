package tcss.trackcontroller;
import tcss.ctc.*;
import tcss.trackmodel.*;
import java.util.*;
import tcss.trackmodel.RXR;

public class TrackController {
    TrackModel TrackModel;
    RXR RXR;
    CTC CTC;
    float ss;
    int id;
    int auth;
    boolean railroadcrossing;
    boolean switches;
    Block block;
    TrackController TC;
    int line;
    int blockId;
    boolean iScrossing;
    boolean status;
    boolean lights;
    boolean occupancy;
    boolean heat;
    Switch aSwitch;
    Station station;

    public TrackController(){
        this.TrackModel = TrackModel;
        this.RXR = RXR;
    }

    public void getNextStop(float SS, int auth, int ID) {
        this.ss = ss;
        this.id = ID;
        this.auth = auth;
    }

    //SETTERS
    public void isRailroadcrossingUpOrDown(RXR rxr){
        rxr.setDown(railroadcrossing);
    }


    //GETTERS
    public void getLine(){
        this.line = this.block.getLine();
    }
    public void getSection(){
        this.block.getSection();
    }
    public void getBlockNum(){
        this.blockId = this.block.getBlockNum();
    }

    public void isOccupied(){
        this.occupancy = this.block.isOccupied();
    }

    public void getSwitch(){
        this.aSwitch = this.block.getSwitch();
    }

    public void getRXR() {
        this.RXR = this.block.getRXR();
    }

    public void getStation() {
        this.station = this.block.getStation();
    }

    public boolean turnOnLight() {
        return lights;
    }

    //how to send track model data
    //getTrack() which returns track
    //then call inittrain()


}
