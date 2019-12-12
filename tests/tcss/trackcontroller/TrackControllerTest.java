package tcss.trackcontroller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tcss.main.TrackControllerController;
import tcss.trackmodel.Block;
import tcss.trackmodel.Track;
import tcss.trackmodel.TrackModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTrue;


class TrackControllerTest {
    private PLC plc;
    private static TrackController redTrC;
    private static Track redLine;
    private static Track greenLine;
    private static TrackModel tm;
    private static WaysideController wc;
    private static TrackControllerController tc1;
    private final int green_line = 0;
    private final int red_line = 1;
    private static LinkedList<Block> listofredblocks;
    private static ArrayList<TrackController> redTC;
    private static ArrayList<TrackController> greenTC;
    private static LinkedList<Block> listofgreenblocks;
    private static LinkedList<Block> listofredswitches;
    private static LinkedList<Block> listofgreenswitches;
    private static LinkedList<Block> listofredRXR;
    private static LinkedList<Block> listofgreenRXR;
    private static int redblocks[];
    private static int greenblocks[];
    private static int numberofGreenTrackControllers = 4;
    private static int numberofRedTrackControllers = 4;
    private static int overlap = 2;
    private static int beforelap = 2;
    private static Track redTrack;
    private static Track greenTrack;
    private TrackController redtrackController;
    private TrackController greentrackController;
    public float ss;
    private static int auth;
    private static int blockId;
    @BeforeAll
    static void setUp() throws Exception{
        tm = new TrackModel();
        redTrack = tm.getRedLine();
        greenTrack = tm.getGreenLine();

        WaysideController wc = new WaysideController(redTrack,redTrack);
        listofredblocks = redTrack.getBlockList();
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
                    wc.calculateHashMaps(1, blocks, switching, curBlock, listofredblocks);
                }
            }
            TrackController redTrC = new TrackController(i, 1, blocks, switching, RXR, redTrack);
            redTC.add(redTrC);
        }
    }
    @Test
    void verifyUploadPLC() throws IOException {
        boolean atttempt = redTC.get(0).loadPLC("resources/plctest.plc");
        assertTrue(atttempt);
    }

    @Test
    void maintenanceRequest(){
        boolean attempt = redTC.get(0).maintenanceRequest(1,1, true);
        assertTrue(attempt);
    }

    @Test
    void railroadRequestTest(){
        boolean attempt =  redTC.get(0).railroadCrossingRequest(1,1);
        assertTrue(attempt);
    }

    @Test
    void switchRequestTest(){
        boolean switchrequest =  redTC.get(0).switchRequest(1,1,2);
        assertTrue(switchrequest);
    }

}