package tcss.trackmodel;

import javafx.util.Pair;
import tcss.trainmodel.TrainModel;

enum Failure{
    BROKEN_RAIL, CIRCUIT_FAILURE, POWER_FAILURE, NONE;
}

enum Direction{
    FROM_HEAD, FROM_TAIL, FROM_BRANCH, NONE;
}

public class Block{

    private int line;
    private char section;
    private int blockNum;
    private int authority;

    private float suggestedSpeed;
    private float length;
    private float grade;
    private float speedLimit;
    private float elevation;
    private float cumulativeElevation;

    private boolean underground;
    private boolean occupied;
    private boolean closed;
    private boolean startBlock;
    private boolean yardBlock;
    private boolean passengerUpdateDone;

    private Failure failure;
    private Direction direction;

    private Block head;
    private Block tail;
    private Block branch;

    private Switch sw;
    private Station station;
    private RXR rxr;
    private Beacon beacon;
    private TrainModel train;

    private int trainID;

    public Block(){
        setLine(-1);
        setSection('\u0000');
        setBlockNum(-1);
        setLength(-1);
        setAuthority(0);

        setSuggestedSpeed(0);
        setGrade(-1);
        setSpeedLimit(-1);
        setElevation(-1);
        setCumulativeElevation(-1);

        setUnderground(false);
        setOccupied(false);
        setStartBlock(false);
        setYardBlock(false);
        setPassengerUpdateDone(false);

        setFailure(Failure.NONE);
        setDirection(Direction.NONE);

        setHead(null);
        setTail(null);
        setBranch(null);

        setSwitch(null);
        setStation(null);
        setRXR(null);
        setBeacon(null);
        setTrain(null);

        trainID = 0;
    }

    public Block(Block b){
        setLine(b.getLine());
        setSection(b.getSection());
        setBlockNum(b.getBlockNum());
        setLength(b.getLength());
        setAuthority(b.getAuthority());

        setSuggestedSpeed(b.getSuggestedSpeed());
        setGrade(b.getGrade());
        setSpeedLimit(b.getSpeedLimit());
        setElevation(b.getElevation());
        setCumulativeElevation(b.getCumulativeElevation());

        setUnderground(b.isUnderground());
        setOccupied(b.isOccupied());

        setFailure(b.getFailure());
        setDirection(b.getDirection());

        setHead(b.getHead());
        setTail(b.getTail());
        setBranch(b.getBranch());

        Switch sw = new Switch();
        if (b.getSwitch() != null) {
            sw.setRoot(b.getSwitch().getRoot());
            sw.setBranchDest(b.getSwitch().getBranchDest());
            sw.setStraightDest(b.getSwitch().getStraightDest());
            sw.setStraight(b.getSwitch().getStraight());
            sw.setLights(b.getSwitch().lightsOn());
            setSwitch(sw);
        }
        else {
            setSwitch(null);
        }

        setStation(b.getStation());
        setRXR(b.getRXR());
        setBeacon(b.getBeacon());
        setTrain(null);
    }

    public Block trainGetNextBlock(){

        Block retBlock = getBlockAhead(1);
        if(!moveTrain(retBlock)){
            System.out.println("failed to move train");
            return null;
        }

        return retBlock;

        /*
        if(getBranch() == null){

            if(getDirection() == Direction.FROM_TAIL){
                retBlock = getHead();
            }else if(getDirection() == Direction.FROM_HEAD){
                retBlock = getTail();
            }else{
                System.out.println("trainGetNextBlock(): current block has no valid direction specified");
                return null;
            }
        }else{
            if(this == getBranch().getHead()){

                //branching into a head
                if(getDirection() == Direction.FROM_BRANCH || getDirection() == Direction.FROM_HEAD){
                    //if from branch or from head, to tail
                    retBlock = getTail();
                }else if(getDirection() == Direction.FROM_TAIL){
                    //if from tail

                    if(!getSwitch().getStraight()){
                        //if switch is branched, to branch
                        retBlock = getBranch();
                    }else{
                        //else, to head
                        retBlock = getHead();
                    }
                }else{
                    System.out.println("trainGetNextBlock(): current block has no valid direction specified");
                    return null;
                }

            }else{
                //branching into a tail
                if(getDirection() == Direction.FROM_BRANCH || getDirection() == Direction.FROM_TAIL){
                    //if from branch or from tail, to head
                    retBlock = getHead();
                }else if(getDirection() == Direction.FROM_HEAD){

                    //if from head
                    if(!getSwitch().getStraight()){
                        //if switch is branched, to branch
                        retBlock = getBranch();
                    }else{
                        //else, to tail
                        retBlock = getTail();
                    }
                }else{
                    System.out.println("trainGetNextBlock(): current block has no direction specified");
                    return null;
                }
            }
        }

        //set train of next block to that of current block
        retBlock.setTrain(getTrain());
        retBlock.setOccupied(true);
        setTrain(null);
        setOccupied(false);
        setDirection(Direction.NONE);
        setPassengerUpdateDone(false);

        //if next block has beacon, set train beacon data
        if(retBlock.getBeacon() != null){
            retBlock.getTrain().setBeacon(retBlock.getBeacon().getData().toCharArray());
        }

        //set direction of next block
        if(this == retBlock.getHead()){
            retBlock.setDirection(Direction.FROM_HEAD);
        }else if(this == retBlock.getTail()){
            retBlock.setDirection(Direction.FROM_TAIL);
            retBlock.getTrain().setGrade(retBlock.getGrade() * -1);
        }else if(retBlock.getBranch() != null){
            retBlock.setDirection(Direction.FROM_BRANCH);
        }else{
            System.out.println("trainGetNextBlock(): no references on returned block point to current block");
            return null;
        }*/
    }

    public Block getNextBlock(){
        if(getDirection() == Direction.NONE){
            return getHead();
        }
        return getBlockAhead(1);
    }

    public Block getPreviousBlock(){
        if(getDirection() == Direction.NONE){
            return getTail();
        }
        return getBlockBehind(1);
    }

    public Block getBlockAhead(int numAhead){

        Block currBlock = this;

        if(numAhead <= 0){
            return currBlock;
        }

        if(!isOccupied()){
            System.out.println("Train Direction == " + getDirection());
            System.out.println("No train present on block " + getBlockNum());
            return null;
        }

        Direction dir = getDirection();

        for(int i=0;i<numAhead;i++){
            Pair<Block, Direction> currPair = getBlockAheadHelper(currBlock, dir);
            currBlock = currPair.getKey();
            dir = currPair.getValue();
            System.out.println("=> " + currBlock.getBlockNum());
        }

        return currBlock;
    }

    public Pair<Block, Direction> getBlockAheadHelper(Block currBlock, Direction dir){

        Block retBlock;

        if(currBlock.getBranch() == null){

            if(dir == Direction.FROM_TAIL){
                retBlock = currBlock.getHead();
            }else if(dir == Direction.FROM_HEAD){
                retBlock = currBlock.getTail();
            }else{
                System.out.println("getBlockAheadHelper(): current block has no valid direction specified 1");
                return null;
            }
        }else{
            if(currBlock == currBlock.getBranch().getHead()){

                //branching into a head
                if(dir == Direction.FROM_BRANCH || dir == Direction.FROM_HEAD){
                    //if from branch or from head, to tail
                    retBlock = currBlock.getTail();
                }else if(dir == Direction.FROM_TAIL){
                    //if from tail

                    if(!currBlock.getSwitch().getStraight()){
                        //if switch is branched, to branch
                        retBlock = currBlock.getBranch();
                    }else{
                        //else, to head
                        retBlock = currBlock.getHead();
                    }
                }else{
                    System.out.println("getBlockAheadHelper(): current block has no valid direction specified 2");
                    return null;
                }

            }else{
                //branching into a tail
                if(dir == Direction.FROM_BRANCH || dir == Direction.FROM_TAIL){
                    //if from branch or from tail, to head
                    retBlock = currBlock.getHead();
                }else if(dir == Direction.FROM_HEAD){

                    //if from head
                    if(!currBlock.getSwitch().getStraight()){
                        //if switch is branched, to branch
                        retBlock = currBlock.getBranch();
                    }else{
                        //else, to tail
                        retBlock = currBlock.getTail();
                    }
                }else{
                    System.out.println("getBlockAheadHelper(): current block has no direction specified 3");
                    return null;
                }
            }
        }

        //update current direction of next block
        Direction retDir;
        if(currBlock == retBlock.getHead()){
            retDir = Direction.FROM_HEAD;
        }else if(currBlock == retBlock.getTail()){
            retDir = Direction.FROM_TAIL;
        }else if(retBlock.getBranch() != null){
            retDir = Direction.FROM_BRANCH;
        }else{
            System.out.println("getBlockAheadHelper(): no references on returned block point to current block");
            return null;
        }

        Pair<Block, Direction> retPair = new Pair<Block, Direction>(retBlock, retDir);
        return retPair;
    }

    public Block getBlockBehind(int numBehind) {
        Block currBlock = this;

        if(numBehind <= 0){
            return currBlock;
        }

        Direction dir = getOppositeDirection();

        for(int i=0;i<numBehind;i++){
            Pair<Block, Direction> currPair = getBlockAheadHelper(currBlock, dir);
            currBlock = currPair.getKey();
            dir = currPair.getValue();
            System.out.println("=> " + currBlock.getBlockNum());
        }

        return currBlock;
    }

    public Direction getOppositeDirection(){

        if(getBranch() == null){
            //no branch, only head and tail, direct opposites
            if(getDirection() == Direction.FROM_TAIL){
                return Direction.FROM_HEAD;
            }else if(getDirection() == Direction.FROM_HEAD){
                return Direction.FROM_HEAD;
            }else{
                System.out.println("getOppositeDirection(): current block has no valid direction specified 1");
                return Direction.NONE;
            }
        }else{
            if(this == getBranch().getHead()){

                //branching into a head
                if(getDirection() == Direction.FROM_BRANCH || getDirection() == Direction.FROM_HEAD){
                    //if from branch or from head, from tail
                    return Direction.FROM_TAIL;
                }else if(getDirection() == Direction.FROM_TAIL){
                    //if from tail

                    if(!getSwitch().getStraight()){
                        //if switch is branched, from branch
                        return Direction.FROM_BRANCH;
                    }else{
                        //else, from head
                        return Direction.FROM_HEAD;
                    }
                }else{
                    System.out.println("getOppositeDirection(): current block has no valid direction specified 2");
                    return Direction.NONE;
                }

            }else{
                //branching into a tail
                if(getDirection() == Direction.FROM_BRANCH || getDirection() == Direction.FROM_TAIL){
                    //if from branch or from tail, from head
                    return Direction.FROM_HEAD;
                }else if(getDirection() == Direction.FROM_HEAD){

                    //if from head
                    if(!getSwitch().getStraight()){
                        //if switch is branched, from branch
                        return Direction.FROM_BRANCH;
                    }else{
                        //else, from tail
                        return Direction.FROM_TAIL;
                    }
                }else{
                    System.out.println("getOppositeDirection(): current block has no direction specified 3");
                    return Direction.NONE;
                }
            }
        }
    }


    public boolean moveTrain(Block nextBlock){

        if(!isOccupied()){
            System.out.println("Train Direction == " + getDirection());
            System.out.println("No train present on block " + getBlockNum());
            return false;
        }else if(nextBlock.isOccupied()){
            System.out.println("Trains crashed on block" + getBlockNum());
            return false;
        }else if(nextBlock.isYardBlock()){
            getTrain().toYard();
            setTrain(null);
            setOccupied(false);
            setDirection(Direction.NONE);
            setPassengerUpdateDone(false);
            return true;
        }

        //set train of next block to that of current block
        nextBlock.setTrain(getTrain());
        nextBlock.setOccupied(true);
        setTrain(null);
        setOccupied(false);
        setDirection(Direction.NONE);
        setPassengerUpdateDone(false);

        //if next block has beacon, set train beacon data
        if(nextBlock.getBeacon() != null){
            nextBlock.getTrain().setBeacon(nextBlock.getBeacon().getData().toCharArray());
        }

        //set direction of next block
        if(this == nextBlock.getHead()){
            nextBlock.setDirection(Direction.FROM_HEAD);
        }else if(this == nextBlock.getTail()){
            nextBlock.setDirection(Direction.FROM_TAIL);
            nextBlock.getTrain().setGrade(nextBlock.getGrade() * -1);
        }else if(nextBlock.getBranch() != null){
            nextBlock.setDirection(Direction.FROM_BRANCH);
        }else{
            System.out.println("moveTrain(): no references on next block point to current block");
            return false;
        }

        return true;
    }

    public boolean setSuggSpeedAndAuth(float ss, int a){

        System.out.println("SS: " + ss + ", Auth:" + a);

        if(ss == -2.0 && a == 0){

            //close block for maintenance
            setClosed(true);
        }else if(ss == -2.0 && a == 1){

            //open block
            setClosed(false);
        }else if(ss == -3.0 && a == 0){

            //rxr set up
            if(getRXR() == null){
                return false;
            }else{
                getRXR().setDown(false);
            }
        }else if(ss == -3.0 && a == 1){

            //rxr set down
            if(getRXR() == null){
                return false;
            }else{
                getRXR().setDown(true);
            }
        }else if(ss == -4.0 && a == 0){

            //set switch curved
            if(getSwitch() == null){
                return false;
            }else{
                getSwitch().setStraight(false);
            }
        }else if(ss == -4.0 && a == 1){

            //set switch straight
            if(getSwitch() == null){
                return false;
            }else{
                getSwitch().setStraight(true);
            }
        }else{
            System.out.println(getTrain() + " " + isStartBlock() + " " + getBlockNum());
            if(!isOccupied() && isStartBlock()){
                System.out.println("Initializing train on block " + getBlockNum());
                return initTrain(ss, a, trainID++);
            }else{
                //pass values to train on block
                getTrain().passCommands(ss, a);
            }
        }

        return true;
    }

    public boolean initTrain(float suggSpeed, int auth, int id){
        if(isOccupied()){
            System.out.println("Failed to init, block is occupied");
            return false;
        }
        TrainModel train = new TrainModel(suggSpeed, auth, id, this);
        setTrain(train);
        setOccupied(true);
        setDirection(Direction.FROM_HEAD);
        setSuggestedSpeed(suggSpeed);
        setAuthority(auth);
        return true;
    }

    //****************** ACCESSOR METHODS **********************************

    public int getLine(){
        return line;
    }

    public char getSection(){
        return section;
    }

    public int getBlockNum(){
        return blockNum;
    }

    public int getAuthority(){
        return authority;
    }

    public float getSuggestedSpeed(){
        return suggestedSpeed;
    }

    public float getLength(){
        return length;
    }

    public float getGrade(){
        return grade;
    }

    public float getSpeedLimit(){
        return speedLimit;
    }

    public float getElevation(){
        return elevation;
    }

    public float getCumulativeElevation(){
        return cumulativeElevation;
    }

    public boolean isUnderground(){
        return underground;
    }

    public boolean isOccupied(){
        return occupied;
    }

    public boolean isClosed(){
        return closed;
    }

    public boolean isStartBlock(){
        return startBlock;
    }

    public boolean isYardBlock(){
        return yardBlock;
    }

    public boolean isPassengerUpdateDone(){
        return passengerUpdateDone;
    }

    public Failure getFailure(){
        return failure;
    }

    public Direction getDirection(){
        return direction;
    }

    public Block getHead() {
        return head;
    }

    public Block getTail(){
        return tail;
    }

    public Block getBranch() {
        return branch;
    }

    public Switch getSwitch(){
        return sw;
    }

    public Station getStation(){
        return station;
    }

    public RXR getRXR(){
        return rxr;
    }

    public Beacon getBeacon(){
        return beacon;
    }

    public TrainModel getTrain(){
        return train;
    }

    //****************** MUTATOR METHODS **********************************

    public void setLine(int line){
        this.line = line;
    }

    public void setSection(char section){
        this.section = section;
    }

    public void setBlockNum(int blockNum){
        this.blockNum = blockNum;
    }

    public void setAuthority(int authority){
        this.authority = authority;
    }

    public void setSuggestedSpeed(float suggestedSpeed){
        this.suggestedSpeed = suggestedSpeed;
    }

    public void setLength(float length){
        this.length = length;
    }

    public void setGrade(float grade){
        this.grade = grade;
    }

    public void setSpeedLimit(float speedLimit){
        this.speedLimit = speedLimit;
    }

    public void setElevation(float elevation){
        this.elevation = elevation;
    }

    public void setCumulativeElevation(float ce){
        cumulativeElevation = ce;
    }

    public void setUnderground(boolean underground){
        this.underground = underground;
    }

    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }

    public void setClosed(boolean closed){
        this.closed = closed;
    }

    public void setStartBlock(boolean startBlock){
        this.startBlock = startBlock;
    }

    public void setYardBlock(boolean yardBlock){
        this.yardBlock = yardBlock;
    }

    public void setPassengerUpdateDone(boolean passengerUpdateDone) {
        this.passengerUpdateDone = passengerUpdateDone;
    }

    public void setFailure(Failure failure){
        this.failure = failure;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setHead(Block head) {
        this.head = head;
    }

    public void setTail(Block tail) {
        this.tail = tail;
    }

    public void setBranch(Block branch) {
        this.branch = branch;
    }

    public void setSwitch(Switch sw) {
        this.sw = sw;
    }

    public void setStation(Station station){
        this.station = station;
    }

    public void setRXR(RXR rxr){
        this.rxr = rxr;
    }

    public void setBeacon(Beacon beacon){
        this.beacon = beacon;
    }

    public void setTrain(TrainModel train){
        this.train = train;
    }

    //****************** EXCEPTIONS **********************************
}