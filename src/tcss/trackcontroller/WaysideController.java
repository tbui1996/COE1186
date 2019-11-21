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
    private ArrayList<TrackController> redTrack;
    private Track greenTrack;
    public float ss;
    public int auth;
    public int blockId;

    public WaysideController(Track track) throws IOException {
        this.track = track;
        this.line = track.getBlock(blockId).getLine();

        if (this.line == 1) {
            redtrackcontrollerConstructor(this.track, this.line, this.blockId);
        }

        if (this.line == 0) {
            greentrackcontrollerConstructor(this.track, this.line, this.blockId);
        }


    }
    public void getNextStop(float SS, int auth, int ID) {
        this.ss = SS;
        this.blockId = ID;
        this.auth = auth;
    }

    public TrackController redtrackcontrollerConstructor(Track track, int line, int blockId) throws IOException {
        listofredblocks = track.getBlockList();
        listofredRXR = new LinkedList<>();
        listofredswitches = new LinkedList<>();
        TrackController redTrC;

        for (int i = 0; i < listofredblocks.size(); i++) {
            Block currentBlock = listofredblocks.get(i);
            redblocks[i] = currentBlock.getBlockNum();
            if (currentBlock.getCrossing()) {
                listofredRXR.add(currentBlock);
            } else if (currentBlock.getSwitch().getStraight()) {
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
                if (calculate < 0)
                    calculate += listofredblocks.size();
                Block curBlock = listofredblocks.get(calculate);
                blocks.put(curBlock.getBlockNum(), curBlock);
                if (curBlock.getCrossing()) {
                    RXR.put(curBlock.getBlockNum(), curBlock);
                } else calculateHashMaps(blocks, switching, curBlock, listofredblocks);

            }
            redTrC = new TrackController(i, 1, blocks, switching, RXR);
            redTC.add(redTrC);
            redTrC.loadPLC("/Users/thomasbui/Desktop/COE1186_Code1/src/tcss/trackcontroller/plc.plc");
            return redTrC;

        }
        return null;
    }

    public TrackController greentrackcontrollerConstructor(Track track, int line, int blockId) throws IOException {
        listofgreenblocks = track.getBlockList();
        listofgreenRXR = new LinkedList<>();
        listofgreenswitches = new LinkedList<>();
        TrackController greenTrC;

        for (int i = 0; i < listofgreenblocks.size(); i++) {
            Block currentBlock = listofgreenblocks.get(i);
            greenblocks[i] = currentBlock.getBlockNum();
            if (currentBlock.getCrossing()) {
                listofgreenRXR.add(currentBlock);
            } else if (currentBlock.getSwitch().getStraight()) {
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
                if (curBlock.getCrossing()) {
                    RXR1.put(curBlock.getBlockNum(), curBlock);
                } else {
                    calculateHashMaps(blocks1, switching1, curBlock, listofgreenblocks);
                }

            }
            greenTrC = new TrackController(i, 1, blocks1, switching1, RXR1);
            greenTC.add(greenTrC);
            greenTrC.loadPLC("/Users/thomasbui/Desktop/COE1186_Code1/src/tcss/trackcontroller/plc.plc");
            return greenTrC;

        }
        return null;
    }

    public void calculateHashMaps(HashMap<Integer, Block> blocks1, HashMap<Integer, Block> switching1, Block curBlock, LinkedList<Block> listofgreenblocks) {
        if (curBlock.getSwitch().getStraight()) {
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
    }

    public boolean proceed(int line, int blockId, int nexBlock, int destBlock, int authority, float suggestedspeed) {
        TrackController curTC;
        ArrayList<Integer> blocklist = new ArrayList<>();
        this.line = track.getBlockList().get(blockId).getLine();
        Block curBlock;
        curBlock = track.getBlockList().get(blockId);
            int next = curBlock.getNextBlock().getBlockNum();
            int prev = curBlock.getPreviousBlock().getBlockNum();
            if (next >= 0)
                blocklist.add(next);
            if (prev >= 0)
                blocklist.add(prev);

            blocklist.add(blockId);
            curTC = getTC(this.line, blocklist);

            if(curTC!=null){
                return curTC.proceed(line, blockId,nexBlock,destBlock,authority,suggestedspeed);
            }

        return false;
    }
    public boolean maintenanceRequest(int line, int blockId){
        TrackController tc;
        ArrayList<Integer> blocklist = new ArrayList<>();
        this.line = track.getBlockList().get(blockId).getLine();
        Block curBlock;
        curBlock = track.getBlockList().get(blockId);
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

    public boolean railroadCrossingRequest(int line, int blockId){
        TrackController tc;
        ArrayList<Integer> blocklist = new ArrayList<>();
        blocklist.add(blockId);
        tc = getTC(line,blocklist);
        if(tc!=null)
            return tc.railroadCrossingRequest(line,blockId);
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
    public boolean waysideLights(int line, int blockId, boolean occupancy) {
        TrackController tc;
        ArrayList<Integer> blocklist = new ArrayList<>();
        int next = track.getBlockList().get(blockId).getNextBlock().getBlockNum();
        int light = track.getBlockList().get(blockId).getBlockNum();

        blocklist.add(next);
        blocklist.add(light);
        blocklist.add(blockId);

        tc = getTC(line, blocklist);
        if(tc!= null)
            tc.waysideLights(line, blockId, occupancy);
        return false;
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
