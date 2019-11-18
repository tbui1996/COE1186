package tcss.trackcontroller;
import tcss.ctc.CTC;
import tcss.trackmodel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

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
    //private final ArrayList<Block> redBlocks;
    //private final ArrayList<Block> redSwitches;
    //private final ArrayList<Block> redCrossings;
    //private final ArrayList<Block> greenBlocks;
    //private final ArrayList<Block> greenSwitches;
    //private final ArrayList<Block> greenCrossings;
    public HashMap<Integer, Block> block;
    public HashMap<Integer, Block> switchHashMap;
    public HashMap<Integer, Block> RXR;
    String switcheslogic = null;
    String RXRlogic = null;
    String maintenancelogic = null;
    String proceedlogic = null;
    String lightlogic = null;


    public TrackController(){

        this.TrackModel = TrackModel;
        this.RXR = RXR;
    }


    public void getNextStop(float SS, int auth, int ID) {
        this.ss = SS;
        this.id = ID;
        this.auth = auth;
    }
    private void transmitLightState(int blockId, boolean status){
        track.getBlock(blockId).getLight().setState(status);
    }

    //GETTERS
    public int getLine(){
        return this.line;
    }

    public Block getBlock(int blockId){
        return block.get(blockId);
    }

    public Block getCrossing(int blockId){
        return block.get(blockId);
    }

    public boolean loadPLC(String destination) throws IOException {
        String cur;



        FileReader file = new FileReader(destination);
        try(BufferedReader bf = new BufferedReader(file)) {


        while((cur = bf.readLine()) != null){
            //control ends before :
            String[] lines = cur.split(":");
            if (lines[0].equals("switch")) {
                switcheslogic = lines[1];
            }
            if (lines[0].equals("proceed")) {
                proceedlogic = lines[1];
            }
            if (lines[0].equals("maintenance")) {
                maintenancelogic = lines[1];
            }
            if (lines[0].equals("railroadcrossing")) {
                RXRlogic = lines[1];
            }
            if(lines[0].equals("lights")){
                lightlogic = lines[1];
            }
        }
    } catch (IOException e){
        e.printStackTrace();
        return false;
    }
        this.plc = new PLC(switcheslogic, proceedlogic, maintenancelogic, RXRlogic, lightlogic);
        return true;
    }

    public boolean maintenanceRequest(int line, int blockId){
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


    public boolean railroadCrossingRequest(int line, int blockId){
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

    public boolean proceed(int line, int blockId, int authority, float suggestedspeed){
        Block currentBlock = track.getBlock(blockId);
        Block nextBlock = currentBlock.getNextBlock();
        Block preiousBlock = currentBlock.getPreviousBlock();

        return false;
    }

    public boolean waysideLights(int line, int blockId, boolean occupancy){
        Block currentBlock = track.getBlock(blockId);
        Block nextBlock = currentBlock.getNextBlock();
        if(currentBlock.getLight()!= null){
            boolean lightState = track.getBlock(blockId).getLight().getState();
            boolean vitalLightState = plc.verifyLightBlock(currentBlock, nextBlock);

            if(vitalLightState != lightState){
                transmitLightState(blockId, vitalLightState);
            }
        }

    }




}