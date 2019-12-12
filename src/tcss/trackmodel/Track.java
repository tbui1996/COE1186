package tcss.trackmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Track {

    Map<Integer, Block> blockHashMap;
    Map<Integer, Branch> branchMap;

    private LinkedList<Block> blockList;

    ArrayList<Block> stationBlocks;
    ArrayList<Block> rxrBlocks;
    ArrayList<Block> switchBlocks;

    Block startBlock;

    public Track(){
        blockHashMap = new HashMap<>();
        branchMap = new HashMap<>();
        startBlock = null;
        blockList = new LinkedList<Block>();
        stationBlocks = new ArrayList<Block>();
    }

    public double distanceBetweenTwoBlocks(Block start, Block end, int unit){

        double fromTailDistance = distanceHelper(start, end, Direction.FROM_TAIL, unit);
        double fromHeadDistance = distanceHelper(start, end, Direction.FROM_HEAD, unit);

        System.out.println("From Tail Distance: " + fromTailDistance + ", From Head Distance: " + fromHeadDistance);

        if(start.isOccupied()){
            if(start.getDirection() == Direction.FROM_TAIL){
                return fromTailDistance;
            }else{
                return fromHeadDistance;
            }
        }else{
            if(start.getBlockNum() == 7 || start.getBlockNum() == 9){
                return fromHeadDistance;
            }
            return fromTailDistance;
        }
    }

    private double distanceHelper(Block start, Block end, Direction initialDir, int unit){

        double currDistance = 0.0;
        boolean passFirst = false;
        Branch currBranch = getBranch(start.getBlockNum());
        Branch endBranch = getBranch(end.getBlockNum());

        if(unit == 0){
            //distance in meters
            if(currBranch == endBranch){
                if(currBranch.getEnd() > currBranch.getStart()){
                    if((initialDir == Direction.FROM_TAIL && start.getBlockNum() < end.getBlockNum()) ||
                            (initialDir == Direction.FROM_HEAD && end.getBlockNum() < start.getBlockNum())){

                        return currBranch.getDistance(start.getBlockNum(), end.getBlockNum());
                    }
                }else if((initialDir == Direction.FROM_HEAD && start.getBlockNum() < end.getBlockNum()) ||
                        (initialDir == Direction.FROM_TAIL && end.getBlockNum() < start.getBlockNum())){

                    return currBranch.getDistance(start.getBlockNum(), end.getBlockNum());
                }
                passFirst = true;

            }

            if(initialDir == Direction.FROM_TAIL){
                currDistance = currBranch.getDistance(start.getBlockNum(), currBranch.getEnd());
            }else{
                currDistance = currBranch.getDistance(start.getBlockNum(), currBranch.getStart());
            }
        }else{
            //distance in blocks
            if(currBranch == endBranch){
                //if (from tail && )
                if(currBranch.getEnd() > currBranch.getStart()){
                    if((initialDir == Direction.FROM_TAIL && start.getBlockNum() < end.getBlockNum()) ||
                            (initialDir == Direction.FROM_HEAD && end.getBlockNum() < start.getBlockNum())){

                        return Math.abs(start.getBlockNum() - end.getBlockNum());
                    }
                }else if((initialDir == Direction.FROM_HEAD && start.getBlockNum() < end.getBlockNum()) ||
                        (initialDir == Direction.FROM_TAIL && end.getBlockNum() < start.getBlockNum())){

                    return Math.abs(start.getBlockNum() - end.getBlockNum());
                }
                passFirst = true;
            }

            if(initialDir == Direction.FROM_TAIL){
                currDistance = Math.abs(start.getBlockNum() - currBranch.getEnd());
            }else{
                currDistance = Math.abs(start.getBlockNum() - currBranch.getStart());
            }
        }


        Direction dir = initialDir;
        while(currBranch != endBranch || passFirst){
            passFirst = false;
            ArrayList<Branch> next;
            if (dir == Direction.FROM_TAIL){
                next = currBranch.getHead();
            } else {
                next = currBranch.getTail();
            }

            double nextDist = 0.0;
            Branch nextBranch = null;

            if(unit == 0) {
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
            }else{
                //distance calculated in blocks
                if(next.contains(endBranch)){
                    nextBranch = endBranch;
                    nextDist = 0.0;
                }else if(next.size() == 1){
                    nextBranch = next.get(0);
                    nextDist = next.get(0).getNumBlocks();
                }else if(next.get(0).getTotalLength() < next.get(1).getTotalLength()){
                    nextBranch = next.get(0);
                    nextDist = next.get(0).getNumBlocks();
                }else{
                    nextBranch = next.get(1);
                    nextDist = next.get(1).getNumBlocks();
                }
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
            System.out.println("currDistance = " + currDistance);

            if(currBranch == endBranch){
                if(unit == 0){
                    if(dir == Direction.FROM_TAIL){
                        currDistance += currBranch.getDistance(currBranch.getStart(), end.getBlockNum());
                    }else {
                        currDistance += currBranch.getDistance(currBranch.getEnd(), end.getBlockNum());
                    }
                    break;
                }else{
                    if(dir == Direction.FROM_TAIL){
                        currDistance += Math.abs(currBranch.getStart() - end.getBlockNum());
                    }else {
                        currDistance += Math.abs(currBranch.getEnd() - end.getBlockNum());
                    }
                    break;
                }

            }
        }

        return currDistance;
    }

    public double distanceToYard(Block start, int unit){

        return distanceBetweenTwoBlocks(start, getStartBlock(), unit);
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
        System.out.println("Block " + startBlock.getBlockNum() + " is a start block");
        startBlock.setStartBlock(true);
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

    public ArrayList<Block> getStationBlocks(){
        return stationBlocks;
    }
}