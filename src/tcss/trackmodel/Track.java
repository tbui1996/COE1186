package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

import java.util.LinkedList;
import java.util.ArrayList;

public class Track {

    private LinkedList<Block> trackList;
    private float suggestedSpeed;
    private int authority;

    public Track(){
        trackList = new LinkedList<Block>();

        /*
        int[] iProperties = {0, 0, 1, 50};
        float[] fProperties = {0.5f, 40.0f, 0.0f, 0.0f};

        trackList.add(newBlock(iProperties, fProperties, 1));
        trackList.add(newBlock(iProperties, fProperties, 2));
        trackList.get(1).setStation(new Station("Dormont"));
        */
    }

    private Block newBlock(int[] iProps, float[] fProps, int blockNum){
        Block b = new Block();
        b.setLine(iProps[0]);
        b.setSection('0');
        b.setBlockNum(blockNum);
        b.setLength(iProps[3]);

        b.setGrade(fProps[0]);
        b.setSpeedLimit(fProps[1]);
        b.setElevation(fProps[2]);
        b.setCumulativeElevation(fProps[3]);

        return b;
    }

    public boolean initTrain(float ss, int auth, int id){

        Block startBlock = trackList.get(0);
        TrainModel train = new TrainModel(ss, auth, id, startBlock);
        startBlock.setTrain(train);
        startBlock.setOccupied(true);
        System.out.println(ss + auth);
        setSuggestedSpeed(ss);
        setAuthority(auth);
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

    public void setSuggestedSpeed(float ss){
        suggestedSpeed = ss;
    }

    public void setAuthority(int a){
        authority = a;
    }

    private Block getNextBlock(Block b){
        return b;
    }

    public LinkedList<Block> getBlockList(){
        return trackList;
    }
}
