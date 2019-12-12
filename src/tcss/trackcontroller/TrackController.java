package tcss.trackcontroller;
import tcss.ctc.CTC;
import tcss.trackmodel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TrackController {
    TrackModel TrackModel;
    private int trackControllerID;
    CTC CTC;
    float ss;
    int trackId;
    int auth;
    boolean railroadcrossing;
    boolean switches;
    TrackController TC;
    int line;
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
    public HashMap<Integer, Block> block;
    public HashMap<Integer, Block> switchHashMap;
    public HashMap<Integer, Block> RXR;
    String switcheslogic = null;
    String RXRlogic = null;
    String maintenancelogic = null;
    String proceedlogic = null;
    String lightlogic = null;
    public TrackController waysideControllers[] = new TrackController[7];


    public TrackController(int id, int line, HashMap<Integer, Block> blocks, HashMap<Integer, Block> switches, HashMap<Integer,Block>rxr, Track track){

        this.trackId = id;
        this.switchHashMap = switches;
        this.RXR = rxr;
        this.block = blocks;
        this.line = line;
        this.track = track;

    }




    //GETTERS
    public int getLine(){
        return this.line;
    }
    public int getTCID(){
        return trackControllerID;
    }
    public Block getBlock(int blockId){
        return track.getBlock(blockId);
    }

    public Block getRXR(int blockId){
        return block.get(blockId);
    }

    //updaters
    public void transmitLightState(int blockId, boolean status){
        track.getBlock(blockId).getSwitch().setStraight(status);
    }
    public void trasmitAuthority(float ss, int blockId, int authority){
        track.getBlock(blockId).setSuggSpeedAndAuth(ss, authority);
    }

    public boolean getOccupancy(int blockId){
        return this.occupancy;
    }

    public Block getSwitch(int blockId){
        return switchHashMap.get(blockId);
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
    //flag allows us to know whether or not to open or close block
    public boolean maintenanceRequest(int line, int blockId, boolean flag){
        Block maintenanceBlock = track.getBlock(blockId);
        Block previousBlock = track.getBlock(maintenanceBlock.getPreviousBlock().getBlockNum());
        Block nextBlock = getBlock(maintenanceBlock.getNextBlock().getBlockNum());

        boolean maintenancemode = plc.verifyMaintenance(previousBlock, maintenanceBlock, nextBlock);
        //close
        if(maintenancemode && flag){
            maintenanceBlock.setSuggSpeedAndAuth(-2,0);
            maintenanceBlock.setOccupied(true);
            return true;
        }
        //open
        if(flag == false && maintenancemode) {
            maintenanceBlock.setSuggSpeedAndAuth(-2, 1);
            maintenanceBlock.setOccupied(false);
        }
        return false;
    }


    public boolean railroadCrossingRequest(int line, int blockId){
        Block crossingBlock = track.getBlock(blockId);
        Block previousBlock = getBlock(crossingBlock.getPreviousBlock().getBlockNum());
        Block nextBlock = getBlock(crossingBlock.getNextBlock().getBlockNum());

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
    //blockId: currentblock
    //nexBlock: block after currentblock that train will proceed to
    //destBLock: block after nexBlock
    public boolean proceed(int line, int blockId, int nexBlock, int destBlock, int authority, float suggestedspeed) {
        Block currentBlock = this.track.getBlock(blockId);
        Block nextBlock;
        Block destinationBlock;
        if(currentBlock.isOccupied()){
            nextBlock = this.track.getBlock(currentBlock.getNextBlock().getBlockNum());
            destinationBlock = this.track.getBlock(currentBlock.getBlockAhead(2).getBlockNum());

        } else{
            nextBlock = this.track.getBlock(currentBlock.getNextBlock().getBlockNum());
            destinationBlock = this.track.getBlock(nextBlock.getNextBlock().getBlockNum());
        }

        boolean reverse = currentBlock.getPreviousBlock().getBlockNum() == nexBlock;
        int prev = reverse ? currentBlock.getNextBlock().getBlockNum() : currentBlock.getBlockNum();
        currentBlock.getPreviousBlock();
        Block previousBlock = getBlock(prev);

        //if next block is crossing enter RXR
        //need to know how next block is a light block
        if (nextBlock.getRXR() != null) {
            nextBlock.setSuggSpeedAndAuth(-3, 0);
            if (!(plc.verifyRXR(currentBlock.getPreviousBlock(),currentBlock,nextBlock))) {
                currentBlock.setSuggSpeedAndAuth(0, 0);
                return false;
            }
        }


        //if next block is a switch
        //need to know how next block is a switch block
       // boolean canswitch = plc.verifySwitch(nextBlock, destinationBlock);
       // boolean canswitch = plc.verifySwitch(nextBlock, destinationBlock);
        if (nextBlock.getSwitch() != null) {
            boolean reversed = false;
            boolean switchposition = nextBlock.getSwitch().getStraight();
            boolean isSwitch = blockId == nextBlock.getBlockNum();
            int nextBlockID = switchposition ? nextBlock.getTail().getBlockNum() : nextBlock.getBranch().getBlockNum();
            if ((destinationBlock.getBlockNum() != nextBlockID) || (isSwitch && (blockId != nextBlockID))) {
                if (destinationBlock.getBlockNum() == nextBlock.getPreviousBlock().getBlockNum()) {
                    //toggle switch
                    reversed = true;
                    if (currentBlock.getBlockNum() != nextBlockID) {
                        if (!switchRequest(line, nextBlock.getBlockNum(), destBlock)) {
                            currentBlock.setSuggSpeedAndAuth(0, 0);
                            return false;
                        }
                    }
                } else if (!switchRequest(line, nextBlock.getBlockNum(), nexBlock)) {
                    destinationBlock = getBlock(nextBlockID);
                }

            }
            if(!plc.verifySwitch(nextBlock,destinationBlock)){
                if(!reverse && switchRequest(line, nextBlock.getBlockNum(),destBlock)){
                    switchposition = nextBlock.getSwitch().getStraight();
                    nexBlock = switchposition ? nextBlock.getSwitch().getStraightDest() : nextBlock.getSwitch().getStraightDest();
                    nextBlock.getNextBlock();
                    destinationBlock = this.track.getBlock(nexBlock);
                    if(!plc.verifySwitch(nextBlock,destinationBlock)){
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
        }


        //if curr block is a switch
        if (currentBlock.getSwitch()!=null) {
            boolean switchposition = currentBlock.getSwitch().getStraight();
            int nextBlockId = switchposition ? currentBlock.getBlockNum() : currentBlock.getNextBlock().getBlockNum();
            //switch changed
            if (destinationBlock.getBlockNum() != nextBlockId) {
                nextBlock = getBlock(nextBlockId);
                destBlock = reverse ? nextBlock.getPreviousBlock().getBlockNum() : nextBlock.getNextBlock().getBlockNum();
                nextBlock.getNextBlock();
                destinationBlock = getBlock(destBlock);
            }
        }

            boolean canProceed = plc.verifyProceed(nextBlock, destinationBlock);
            if (!canProceed) {
                currentBlock.setSuggSpeedAndAuth(0, 0);
                return false;
            }


        currentBlock.setSuggSpeedAndAuth(suggestedspeed, authority);
        return true;
    }

    public boolean waysideLights(int line, int blockId, boolean occupancy){
        Block currentBlock = track.getBlock(blockId);
        Block nextBlock = getBlock(currentBlock.getNextBlock().getBlockNum());
        if(currentBlock.getSwitch()!= null){
            boolean lightState = track.getBlock(blockId).getSwitch().getStraight();
            boolean vitalLightState = plc.verifyLightBlock(currentBlock, nextBlock);

            if(vitalLightState != lightState){
                transmitLightState(blockId, vitalLightState);
            }
        }
        return false;
    }

    public boolean switchRequest(int line, int blockId, int destinationId){
        Block currentswitchblock = getBlock(blockId);
        Block nextBlock = getBlock(currentswitchblock.getNextBlock().getBlockNum());
        boolean switchposition = currentswitchblock.getSwitch().getStraight();


        boolean result = plc.vitalSwitch(currentswitchblock, nextBlock);

        if(result){
            currentswitchblock.setSuggSpeedAndAuth(-4,1);
            return true;
        } else{
            System.out.println("Cannot switch.");
            currentswitchblock.setSuggSpeedAndAuth(-4,0);
        }
        return false;

    }

    public String switchStatus(int line, int blockId){
        Block calledBlock = getSwitch(blockId);
        if(calledBlock !=null && calledBlock.getSwitch().getStraight()) {
            int x = calledBlock.getSwitch().getStraight() ? calledBlock.getBlockNum() : calledBlock.getNextBlock().getBlockNum();
            String s = String.valueOf(x);
            return s;
        }
        return "";
    }
    public String blockRequest(int line, int blockId) {
        Block cur = block.get(blockId);

        if(cur==null)
            return null;

        boolean isClosed = !cur.isClosed();
        boolean isOccupied = !cur.isOccupied();

        if(isClosed)
            return "closed";
        if(isOccupied)
            return "occupied";
        return "open";
    }


    public int getAuthority(){
        return this.auth;
    }

}