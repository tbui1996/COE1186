package tcss.trackcontroller;

import tcss.trackmodel.Block;
import tcss.trackmodel.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
public class WaysideController {
    Track track;
    int line;
    Block block;
    boolean iSswitch;
    boolean iScrossing;
    String status;
    boolean lights;
    boolean occupancy;
    boolean heat;
    private final int green_line = 0;
    private final int red_line = 1;
    private LinkedList<Block> listofredblocks;
    public ArrayList<TrackController> redTC;
    public ArrayList<TrackController> greenTC;
    private LinkedList<Block> listofgreenblocks;
    private LinkedList<Block> listofredswitches;
    private LinkedList<Block> listofgreenswitches;
    private LinkedList<Block> listofredRXR;
    private LinkedList<Block> listofgreenRXR;
    private int redblocks[];
    private int greenblocks[];
    private final int numberofGreenTrackControllers = 4;
    private final int numberofRedTrackControllers = 4;
    private final int overlap = 2;
    private final int beforelap = 2;
    private Track redTrack;
    private Track greenTrack;
    private TrackController redtrackController;
    private TrackController greentrackController;
    public float ss;
    public int auth;
    public int blockId;
    Block currentBlock;

    public WaysideController(Track redtrack, Track greenTrack) throws IOException {
        this.redTrack = redtrack;
        //this.line = track.getBlock(blockId).getLine();
        this.greenTrack = greenTrack;
        if (this.redTrack == redtrack) {
            listofredblocks = this.redTrack.getBlockList();
            listofredRXR = new LinkedList<>();
            listofredswitches = new LinkedList<>();
            redblocks = new int[listofredblocks.size()];

            for (int i = 0; i < listofredblocks.size(); i++) {
                Block currentBlock = listofredblocks.get(i);
                redblocks[i] = currentBlock.getBlockNum();
                if (currentBlock.getRXR() != null) {
                    listofredRXR.add(currentBlock);
                } else if (currentBlock.getSwitch() != null) {
                    listofredswitches.add(currentBlock);
                }
            }
            int blocksinRedTC = (listofredblocks.size() / numberofRedTrackControllers) + 4;
            redTC = new ArrayList<>();
            for (int i = 0; i < numberofRedTrackControllers; i++) {
                HashMap<Integer, Block> blocks = new HashMap<>();
                HashMap<Integer, Block> RXR = new HashMap<>();
                HashMap<Integer, Block> switching = new HashMap<>();

                int over = blocksinRedTC - 4;
                for (int x = (i * over - beforelap); x < (blocksinRedTC + (i * over - 2)); x++) {
                    int calculate;
                    calculate = x % listofredblocks.size();
                    if (calculate < 0) {
                        calculate += listofredblocks.size();
                    }
                    Block curBlock = listofredblocks.get(calculate);
                    blocks.put(curBlock.getBlockNum(), curBlock);
                    if (curBlock.getRXR() != null) {
                        RXR.put(curBlock.getBlockNum(), curBlock);
                    } else if(curBlock.getSwitch() != null) {
                        calculateHashMaps(1, blocks, switching, curBlock, listofredblocks);
                    }
                }
                TrackController redTrC = new TrackController(i, 1, blocks, switching, RXR, redTrack);
                redTC.add(redTrC);
                redTrC.loadPLC("resources/plctest.plc");
            }
        }

        if (this.greenTrack == greenTrack) {
            listofgreenblocks = this.greenTrack.getBlockList();
            listofgreenRXR = new LinkedList<>();
            listofgreenswitches = new LinkedList<>();
            greenblocks = new int[listofgreenblocks.size()];

            for (int i = 0; i < listofgreenblocks.size(); i++) {
                Block currentBlock = listofgreenblocks.get(i);
                greenblocks[i] = currentBlock.getBlockNum();
                if (currentBlock.getRXR() != null) {
                    listofgreenRXR.add(currentBlock);
                } else if (currentBlock.getSwitch() != null) {
                    listofgreenswitches.add(currentBlock);
                }
            }
            int blocksinGreenTC = (listofgreenblocks.size() / numberofGreenTrackControllers) + 4;
            greenTC = new ArrayList<>();
            for (int i = 0; i < numberofGreenTrackControllers; i++) {
                HashMap<Integer, Block> blocks1 = new HashMap<>();
                HashMap<Integer, Block> RXR1 = new HashMap<>();
                HashMap<Integer, Block> switching1 = new HashMap<>();

                int over = blocksinGreenTC - 4;
                for (int x = (i * over - beforelap); x < (blocksinGreenTC + (i * over - 2)); x++) {
                    int calculate;
                    calculate = x % listofgreenblocks.size();
                    if (calculate < 0)
                        calculate += listofgreenblocks.size();
                    Block curBlock = listofgreenblocks.get(calculate);
                    blocks1.put(curBlock.getBlockNum(), curBlock);
                    if (curBlock.getRXR()!=null) {
                        RXR1.put(curBlock.getBlockNum(), curBlock);
                    } else if(curBlock.getSwitch() != null){
                        calculateHashMaps(0, blocks1, switching1, curBlock, listofgreenblocks);
                    }

                }
                TrackController greenTrC = new TrackController(i, 1, blocks1, switching1, RXR1, greenTrack);
                greenTC.add(greenTrC);
                greenTrC.loadPLC("resources/plctest.plc");
                }
            }

            //TrackController test = redTC.get(0);
            //System.out.println("proceed " + test.plc.verifyProceed(null, null));
            //System.out.println("maintenance " + test.plc.verifyMaintenance(null, null,null));
            //System.out.println("switch " + test.plc.verifySwitch(null, null));
            //System.out.println("RXR " + test.plc.verifyRXR(null, null,null));


        }
    public void getNextStop(float SS, int auth, int line, int ID) {
        this.line = line;
        boolean canProceed;
        int nextBlock;
        int upcomingBlock;

        if(this.line == 0){
            Block curBlock = greenTrack.getBlock(ID);
            this.blockId = greenTrack.getBlock(ID).getBlockNum();
            nextBlock = greenTrack.getBlock(ID).getNextBlock().getBlockNum();
            if(curBlock.isOccupied()){
                upcomingBlock = greenTrack.getBlock(ID).getBlockAhead(2).getBlockNum();
            } else {
                upcomingBlock = greenTrack.getBlock(ID).getNextBlock().getNextBlock().getBlockNum();
            }
            this.ss = SS;
            this.auth = auth;
            canProceed = proceed(0, this.blockId,nextBlock, upcomingBlock,auth,SS);
            if(canProceed) {
                greenTrack.getBlock(ID).setSuggSpeedAndAuth(ss, auth);
            } else {
                //greenTrack.getBlock(ID).setSuggSpeedAndAuth(0, 0);
            }
        }
        if(this.line==1){
            Block curBlock = greenTrack.getBlock(ID);
            this.blockId = redTrack.getBlock(ID).getBlockNum();
            nextBlock = redTrack.getBlock(ID).getNextBlock().getBlockNum();
            if(curBlock.isOccupied()){
                upcomingBlock = redTrack.getBlock(ID).getBlockAhead(2).getBlockNum();
            } else {
                upcomingBlock = redTrack.getBlock(ID).getNextBlock().getNextBlock().getBlockNum();
            }
            this.ss = SS;
            this.auth = auth;
            canProceed = proceed(1, this.blockId,nextBlock,upcomingBlock,auth,ss);
            if(canProceed){
                redTrack.getBlock(ID).setSuggSpeedAndAuth(this.ss,this.auth);
            } else{
                //redTrack.getBlock(ID).setSuggSpeedAndAuth(0,0);
            }

        }

    }


    public void calculateHashMaps(int line, HashMap<Integer, Block> blocks1, HashMap<Integer, Block> switching1, Block curBlock, LinkedList<Block> listofgreenblocks) {
            switching1.put(curBlock.getBlockNum(), curBlock);
            int switchid1 = curBlock.getBlockNum();

            Block switchblock1 = listofgreenblocks.get(switchid1);
            int blockafterswitchblock1 = switchblock1.getNextBlock().getBlockNum();
            Block destBlock1 = listofgreenblocks.get(blockafterswitchblock1);
            int destid1 = destBlock1.getNextBlock().getBlockNum();
            blocks1.put(switchid1, switchblock1);
            blocks1.put(blockafterswitchblock1, destBlock1);
            blocks1.put(destid1, listofgreenblocks.get(destid1));
    }

    public boolean proceed(int line, int blockId, int nexBlock, int destBlock, int authority, float suggestedspeed) {
        TrackController curTC;
        ArrayList<Integer> blocklist = new ArrayList<>();
        if(line ==0) {
            this.line = greenTrack.getBlockList().get(blockId).getLine();
            this.track = greenTrack;
        } else if (line == 1){
            this.line = redTrack.getBlockList().get(blockId).getLine();
            this.track = redTrack;
        }
        Block curBlock;
        curBlock = this.track.getBlockList().get(blockId);
        boolean isReverse = curBlock.getPreviousBlock().getBlockNum() == nexBlock;
        int prev = isReverse ? curBlock.getNextBlock().getBlockNum() : curBlock.getPreviousBlock().getBlockNum();
        if(prev>= 0)
            blocklist.add(prev);

        blocklist.add(blockId);
        blocklist.add(nexBlock);
        blocklist.add(destBlock);
        curTC = getTC(line, blocklist);

        if(curTC!=null){
                return curTC.proceed(line, blockId,nexBlock,destBlock,authority,suggestedspeed);
        }

        return false;
    }
    public boolean maintenanceRequest(int line, int blockId){
        TrackController tc;
        ArrayList<Integer> blocklist = new ArrayList<>();

        Block curBlock = null;

        if(line == 0){
            this.line = 0;
            curBlock = track.getBlockList().get(blockId);
        }
        if(line == 1){
            this.line = 1;
            curBlock = track.getBlockList().get(blockId);
        }
        int next = curBlock.getNextBlock().getBlockNum();
        int prev = curBlock.getPreviousBlock().getBlockNum();
        if(next>=0)
            blocklist.add(next);
        if(prev>0)
            blocklist.add(prev);

        blocklist.add(blockId);
        tc = getTC(line, blocklist);
        if(tc!=null)
            return tc.maintenanceRequest(line,blockId);

        return false;
    }
    

    public boolean switchRequest(int line, int blockId, int destinationId) {
        TrackController tc;
        ArrayList<Integer> blocklist = new ArrayList<>();
        int next = track.getBlockList().get(blockId).getNextBlock().getBlockNum();
        int switches = track.getBlockList().get(blockId).getSwitch().getBranchDest();

        blocklist.add(next);
        blocklist.add(switches);
        blocklist.add(blockId);
        blocklist.add(destinationId);

        tc = getTC(line, blocklist);
        if (tc != null)
            tc.switchRequest(line, blockId, destinationId);
        return false;

    }

    public boolean getOccupied(int line, int blockId) {
        if (line == 1) {
            this.occupancy = redTrack.getBlock(blockId).isOccupied();
            return redTrack.getBlock(blockId).isOccupied();
        }
        else {
            this.occupancy = greenTrack.getBlock(blockId).isOccupied();
            return greenTrack.getBlock(blockId).isOccupied();
        }
    }

    public boolean getSwitchStraight(int line, int blockId) {
        if (line == 1) {
            return redTrack.getBlock(blockId).getSwitch().getStraight();
        } 
        else {
            return greenTrack.getBlock(blockId).getSwitch().getStraight();
        }
    }

    public boolean getLightState(int line, int blockId) {
        if (line == 1) {
            return redTrack.getBlock(blockId).getSwitch().lightsOn();
        }
        else {
            return greenTrack.getBlock(blockId).getSwitch().lightsOn();
        }
    }

    private TrackController getTC(int line, ArrayList<Integer> blocklist){
        ArrayList<TrackController> tc;

        if(line==0){
            tc = greenTC;
        }
        else
            tc = redTC;
        for(TrackController t: tc){
            boolean istrue = true;
            for(Integer block: blocklist){
                if(t.getBlock(block) == null)
                    istrue = false;
            }
            if (istrue)
                return t;
        }
        return null;
    }

    public String blockRequest(int line, int blockId){
        TrackController tc;
        ArrayList<Integer> blocklist = new ArrayList<>();
        blocklist.add(blockId);
        tc = getTC(line, blocklist);
        if(tc!=null)
            return tc.blockRequest(line, blockId);

        return " ";
    }





}
