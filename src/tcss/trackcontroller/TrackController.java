package tcss.trackcontroller;
import tcss.ctc.CTC;
import tcss.trackmodel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TrackController {
    TrackModel TrackModel;
    private int trackControllerID;
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
    public PLC plc;
    public String plcFile;
    boolean plcLoaded;

    public TrackController(){
        this.TrackModel = TrackModel;
        this.RXR = RXR;
        this.trackControllerID =trackControllerID;
        this.railroadcrossing = railroadcrossing;
        this.line = line;
    }

    public void getNextStop(float SS, int auth, int ID) {
        this.ss = ss;
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
    public float setSS()
    {
        return this.ss;
    }

    public boolean setOccupancy(){
        return this.occupancy;
    }



    //GETTERS
    public void getLine(){
        this.line = this.block.getLine();
    }
    public void getSection(){
        this.block.getSection();
    }
    public int getBlock(){
        return block.getBlockNum();
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

    public Track getTrack(){
        this.track = this.TrackModel.getTrack();
        return track;
    }

    public void initTrain(){
        Track t = new Track();
        Method m = null;
        try {
             m = Track.class.getDeclaredMethod("initTrain", float.class, int.class, int.class);
        } catch (NoSuchMethodException e){
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        }
        m.setAccessible(true);
        Object b;
        try {
            b = m.invoke(this.ss, this.auth, this.id);
        } catch (IllegalAccessException e){
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (InvocationTargetException e){
            e.printStackTrace();
        }
    }

    public int getID() {
        return this.trackControllerID;
    }


    public boolean loadPLC(String destination) throws IOException {
        String cur;

        String switches = null;
        String RXR = null;
        String maintenance = null;
        String proceed = null;

        FileReader file = new FileReader(destination);
        try(BufferedReader bf = new BufferedReader(file)) {


        while((cur = bf.readLine()) != null){
            //control ends before :
            String[] lines = cur.split(":");
            if (lines[0].equals("switch")) {
                switches = lines[1];
            }
            if (lines[0].equals("proceed")) {
                proceed = lines[1];
            }
            if (lines[0].equals("maintenance")) {
                maintenance = lines[1];
            }
            if (lines[0].equals("railroadcrossing")) {
                RXR = lines[1];
            }
        }
    } catch (IOException e){
        e.printStackTrace();
        return false;
    }
        this.plc = new PLC(switches, proceed, maintenance, RXR);
        return true;
    }


}