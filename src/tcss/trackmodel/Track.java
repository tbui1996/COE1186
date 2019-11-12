package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;

public class Track {

    private LinkedList<Block> trackList;
    Map<Integer, Block> blockHashMap = new HashMap<>();

    public Track(){
        trackList = new LinkedList<Block>();
    }

    public boolean initTrain(float ss, int auth, int id){
        System.out.println("Init Train");
        return trackList.get(0).setSuggSpeedAndAuth(ss, auth);
    }

    public Block getBlock(int blockNum){
        return blockHashMap.get(blockNum);
    }

    //distance between two blocks
    public double distanceBetween(){
        return 0.0;
    }

    public boolean addToHashMap(Block b){

        //check for collision, unsuccessful add
        if(blockHashMap.containsKey(b.getBlockNum())){
            System.out.println("Collision of block #!");
            return false;
        }else{
            blockHashMap.put(b.getBlockNum(), b);
        }

        return true;
    }

    public LinkedList<Block> getBlockList(){
        return trackList;
    }
}
