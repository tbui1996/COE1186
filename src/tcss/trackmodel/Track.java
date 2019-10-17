package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

import java.util.LinkedList;
import java.util.ArrayList;

public class Track {

    private LinkedList<Block> track;
    private ArrayList<TrainModel> trains;

    public Track(){
        track = new LinkedList<Block>();
        int[] iProperties = {0, 0, 1, 50};
        float[] fProperties = {0.5f, 40.0f, 0.0f, 0.0f};

        track.add(newBlock(iProperties, fProperties));
        track.add(newBlock(iProperties, fProperties));
        track.get(1).setStation(new Station("Dormont"));
    }

    private Block newBlock(int[] iProps, float[] fProps){
        Block b = new Block();
        b.setLine(iProps[0]);
        b.setSection(iProps[1]);
        b.setBlockNum(iProps[2]);
        b.setLength(iProps[3]);

        b.setGrade(fProps[0]);
        b.setSpeedLimit(fProps[1]);
        b.setElevation(fProps[2]);
        b.setCumulativeElevation(fProps[3]);

        return b;
    }

    private boolean initTrain(float suggestedSpeed, int auth, int id){
        Block startBlock = track.get(0);
        TrainModel train = new TrainModel(suggestedSpeed, auth, id, startBlock);
        track.get(0).setTrain(train);
        trains.add(train);
        return true;
    }

    private Block getNextBlock(Block b){
        return b;
    }
}
