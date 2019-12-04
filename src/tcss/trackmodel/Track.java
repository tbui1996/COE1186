package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Track {

    Map<Integer, Block> blockHashMap;
    Map<Integer, Branch> branchMap;

    LinkedList<Block> blockList;

    ArrayList<Station> stations;
    ArrayList<Block> rxrBlocks;
    ArrayList<Block> switchBlocks;

    Block startBlock;

    public Track(){
        blockHashMap = new HashMap<>();
        branchMap = new HashMap<>();
        startBlock = null;
        blockList = new LinkedList<Block>();
    }

    public boolean initTrain(float ss, int auth, int id){
        System.out.println("Init Train");
        return startBlock.setSuggSpeedAndAuth(ss, auth);
    }

    //distance between two blocks
    public double distanceBetweenTwoBlocks(Block start, Block end){

        double headDistance = distanceHelper(start, end, Direction.FROM_TAIL);
        double tailDistance = distanceHelper(start, end, Direction.FROM_HEAD);

        if(headDistance <= tailDistance){
            return headDistance;
        }else{
            return tailDistance;
        }
    }

    private double distanceHelper(Block start, Block end, Direction initialDir){

        double currDistance = 0.0;
        Branch currBranch = getBranch(start.getBlockNum());
        Branch endBranch = getBranch(end.getBlockNum());

        if(currBranch == endBranch){
            return currBranch.getDistance(start.getBlockNum(), end.getBlockNum());
        }

        if(initialDir == Direction.FROM_TAIL){
            currDistance = currBranch.getDistance(start.getBlockNum(), currBranch.getEnd());
        }else{
            currDistance = currBranch.getDistance(start.getBlockNum(), currBranch.getStart());
        }

        Direction dir = initialDir;
        while(currBranch != endBranch){
            ArrayList<Branch> next;
            if(dir == Direction.FROM_TAIL) {
                next = currBranch.getHead();
            }else {
                next = currBranch.getTail();
            }

            double nextDist = 0.0;
            Branch nextBranch = null;
            if(next.contains(endBranch)){
                nextBranch = endBranch;
                nextDist = 0.0;
            }else if(next.size() == 1){
                nextBranch = next.get(0);
                nextDist = next.get(0).getTotalLength();
            }else if(next.get(0).getTotalLength() < next.get(1).getTotalLength()){
                nextBranch = next.get(0);
                nextDist = next.get(0).getTotalLength();
            }else{
                nextBranch = next.get(1);
                nextDist = next.get(1).getTotalLength();
            }

            if(nextBranch.getTail().contains(currBranch)){
                dir = Direction.FROM_TAIL;
                System.out.println("Branch " +currBranch.getStart()+" "+currBranch.getEnd() +
                        " => Tail of Branch "+nextBranch.getStart()+" "+nextBranch.getEnd());
            }else{
                dir = Direction.FROM_HEAD;
                System.out.println("Branch " +currBranch.getStart()+" "+currBranch.getEnd() +
                        " => Head of Branch "+nextBranch.getStart()+" "+nextBranch.getEnd());
            }

            currBranch = nextBranch;
            currDistance += nextDist;

            if(currBranch == endBranch){
                if(dir == Direction.FROM_TAIL){
                    currDistance += currBranch.getDistance(currBranch.getStart(), end.getBlockNum());
                }else {
                    currDistance += currBranch.getDistance(currBranch.getEnd(), end.getBlockNum());
                }
                break;
            }
        }

        return currDistance;
    }

    public double distanceToYard(Block start){

        return distanceBetweenTwoBlocks(start, getStartBlock());
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

    public Branch getBranch(int blockNum){
        return branchMap.get(blockNum);
    }

    public Map<Integer, Block> getBlockHashMap(){
        return blockHashMap;
    }

    public Map<Integer, Branch> getBranchMap(){
        return branchMap;
    }

    public ArrayList<Station> getStations(){
        return stations;
    }
}
