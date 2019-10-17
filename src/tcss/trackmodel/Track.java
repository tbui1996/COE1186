package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

import java.util.LinkedList;
import java.util.ArrayList;

public class Track {

    private LinkedList<Block> trackList;
    private ArrayList<TrainModel> trains;
    private float suggestedSpeed;
    private int authority;

    public Track(){
        trackList = new LinkedList<Block>();
        int[] iProperties = {0, 0, 1, 50};
        float[] fProperties = {0.5f, 40.0f, 0.0f, 0.0f};

        trackList.add(newBlock(iProperties, fProperties, 1));
        trackList.add(newBlock(iProperties, fProperties, 2));
        trackList.get(1).setStation(new Station("Dormont"));
    }

    private Block newBlock(int[] iProps, float[] fProps, int blockNum){
        Block b = new Block();
        b.setLine(iProps[0]);
        b.setSection(iProps[1]);
        b.setBlockNum(blockNum);
        b.setLength(iProps[3]);

        b.setGrade(fProps[0]);
        b.setSpeedLimit(fProps[1]);
        b.setElevation(fProps[2]);
        b.setCumulativeElevation(fProps[3]);

        return b;
    }

    private boolean initTrain(float suggestedSpeed, int auth, int id){
        Block startBlock = trackList.get(0);
        TrainModel train = new TrainModel(suggestedSpeed, auth, id, startBlock.getSpeedLimit());
        startBlock.setTrain(train);
        startBlock.setOccupied(true);
        trains.add(train);
        return true;
    }

    public Block getBlock(int blockNum){
        return trackList.get(blockNum-1);
    }

    public float getSuggestedSpeed(){
        return suggestedSpeed;
    }

    public int getAuthority(){
        return authority;
    }

    private Block getNextBlock(Block b){
        return b;
    }
}
