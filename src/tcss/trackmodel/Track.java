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
        double[] dProperties = {0.5, 40.0, 0.0, 0.0};

        track.add(newBlock(iProperties, dProperties));
        track.add(newBlock(iProperties, dProperties));
        track.get(1).setStation(new Station("Dormont"));
    }

    private Block newBlock(int[] iProps, double[] dProps){
        Block b = new Block();
        b.setLine(iProps[0]);
        b.setSection(iProps[1]);
        b.setBlockNum(iProps[2]);
        b.setLength(iProps[3]);

        b.setGrade(dProps[0]);
        b.setSpeedLimit(dProps[1]);
        b.setElevation(dProps[2]);
        b.setCumulativeElevation(dProps[3]);

        return b;
    }

    private boolean initTrain(double suggSpeed, int auth, int id){
        TrainModel train = new TrainModel();
        track.get(0).setTrain(new TrainModel());
        return true;
    }

    private Block getNextBlock(Block b){
        return b;
    }
}
