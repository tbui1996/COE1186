package tcss.trackcontroller;
import tcss.ctc.*;
import tcss.trackmodel.*;
import java.util.*;
import tcss.trackmodel.RXR;
import java.lang.reflect.*;
 import java.lang.NoSuchMethodError;

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
    Track track;
    int trackNum;


    public TrackController(){

        this.TrackModel = TrackModel;
        this.RXR = RXR;
    }

    public void getNextStop(float SS, int auth, int ID) {
        this.ss = SS;
        this.id = ID;
        this.auth = auth;
    }
    //set
    public void setRXR(){
        this.railroadcrossing = isRailroadcrossingUpOrDown(RXR);
    }
    public int setID(){
        return this.id;
    }

    //get
    public boolean isRailroadcrossingUpOrDown(RXR rxr){

        rxr.setDown(railroadcrossing);
        return this.railroadcrossing;
    }
    public void setSS(float SS)
    {
         this.ss = SS;
    }
    public void setTrack(Track t){
        this.track = t;
    }

    public void setOccupancy(boolean occupancy){
        this.occupancy = occupancy;
    }
    public boolean getOccupancy(){
        return this.track.getBlockList().get(0).isOccupied();
    }

    public float getSs(){
        return this.ss;
    }
    public int getTrackNum() {
        return 1;
    }

    public int getAuth() {
        return this.auth;
    }

    public void setAuth(int auht) {
        this.auth = auht;
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


    public Track getTrack(){
        this.track = this.TrackModel.getTrack();
        return this.track;
    }

    public void initTrain(){
        track.initTrain(this.ss, this.auth, this.id);
    }


}