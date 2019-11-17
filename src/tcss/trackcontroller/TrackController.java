package tcss.trackcontroller;
import tcss.ctc.*;
import tcss.trackmodel.*;
import java.util.*;
import tcss.trackmodel.*;
import java.lang.reflect.*;
 import java.lang.NoSuchMethodError;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TrackController {
    TrackModel TrackModel;
    private int trackControllerID;
    CTC CTC;
    float ss;
    int id;
    int auth;
    boolean railroadcrossing;
    boolean switches;
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
    private final int numGreenTrackControllers = 4;
    private final int numRedTrackControllers = 4;
    private final ArrayList<Block> redBlocks;
    private final ArrayList<Block> redSwitches;
    private final ArrayList<Block> redCrossings;
    private final ArrayList<Block> greenBlocks;
    private final ArrayList<Block> greenSwitches;
    private final ArrayList<Block> greenCrossings;
    public HashMap<Integer, Block> block;
    public HashMap<Integer, Block> switchHashMap;
    public HashMap<Integer, Block> RXR;


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
    public void setRXR(boolean railroad){
        this.railroadcrossing = railroad;
    }
    public int setID(){
        return this.id;
    }

    //set
    public boolean setRailRoadCrossing(boolean railroad){
        RXR.setDown(railroad);
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

    public boolean maintenanceRequest(int line, int blockId, int authority, float suggestedspeed){
        Block maintenanceBlock = track.getBlock(blockId);
        Block previousBlock = maintenanceBlock.getPreviousBlock();
        Block nextBlock = maintenanceBlock.getNextBlock();

        boolean maintenancemode = plc.verifyMaintenance(previousBlock, maintenanceBlock, nextBlock);
        //close
        if(maintenancemode){
            maintenanceBlock.setSuggSpeedAndAuth(-2,0);
            return true;
        }
        //open
        maintenanceBlock.setSuggSpeedAndAuth(-2, 1);
        return false;
    }

    public boolean checkSpeed(double speed, double speedLimit){
        if(speed <= speedLimit)
            return true;
        else
            return false;
    }

    public boolean railroadCrossingRequest(int line, int blockId, int authority, float suggestedspeed){
        Block crossingBlock = track.getBlock(blockId);
        Block previousBlock = crossingBlock.getPreviousBlock();
        Block nextBlock = crossingBlock.getNextBlock();

        boolean crossingMode = plc.verifyRXR(previousBlock, crossingBlock, nextBlock);
        //put it down
        if(crossingMode){
            crossingBlock.setSuggSpeedAndAuth(-3,0);
            return true;
        }
        //put it up
        crossingBlock.setSuggSpeedAndAuth(-3, 1);
        return false;

    }


}