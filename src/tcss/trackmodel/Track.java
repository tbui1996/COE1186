package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Track {

    Map<Integer, Block> blockHashMap;
    LinkedList<Block> blockList;

    ArrayList<Block> rxrBlocks;
    ArrayList<Block> switchBlocks;

    Block startBlock;

    public Track(){
        blockHashMap = new HashMap<>();
        startBlock = null;
        blockList = new LinkedList<Block>();
    }

    public boolean initTrain(float ss, int auth, int id){
        System.out.println("Init Train");
        return startBlock.setSuggSpeedAndAuth(ss, auth);
    }

    //distance between two blocks
    public double distanceBetweenTwoBlocks(Block start, Block end){
        return 0.0;
    }

    public double distanceToYard(Block start){
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

    public Block getStartBlock(){
        return startBlock;
    }

    public void setStartBlock(Block sb){
        startBlock = sb;
    }

    public LinkedList<Block> getBlockList(){
        return this.blockList;
    }

    public Block getBlock(int blockNum){
        return blockHashMap.get(blockNum);
    }
}
