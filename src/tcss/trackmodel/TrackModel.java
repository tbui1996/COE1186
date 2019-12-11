package tcss.trackmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TrackModel {

    private Track redLine;
    private Track greenLine;

    private final int TRACK_FILE_NUM_COLS = 10;

    public TrackModel() throws Exception {
        init();

        initRedLineBranches();
        initGreenLineBranches();

        //TODO: get rid of hard-coded startBlock if possible
        getRedLine().setStartBlock(getRedLine().getBlock(9));
        getGreenLine().setStartBlock(getGreenLine().getBlock(63));

        addBeacons(getRedLine());
        addBeacons(getGreenLine());

        System.out.println("Verifying Red Line...");
        if(!verify(getRedLine())){
            System.out.println("Red Line Verification Failed!");
        }else {
            System.out.println("Red Line Verification Passed");
        }

        System.out.println("Verifying Green Line");
        if(!verify(getGreenLine())){
            System.out.println("Green Line Verification Failed!");
        }else {
            System.out.println("Green Line Verification Passed");
        }
    }

    private void init() throws IOException {
        File trackFile = new File("resources/Sample_Track_File2.xlsx");
        //init red and green line
        redLine = new Track();
        greenLine = new Track();
        if (!buildTrack(trackFile, redLine)) {
            System.out.println("Error Constructing Red Line!");
        }

        if (!buildTrack(trackFile, greenLine)) {
            System.out.println("Error Constructing Green Line!");
        }
    }

    public Track getRedLine(){
        return redLine;
    }

    public Track getTrack(){
        return redLine;
    }

    public Track getGreenLine(){
        return greenLine;
    }

    //TODO: implement yard switches
    private boolean buildTrack(File trackFile, Track track) throws IOException {

        ArrayList<Switch> builtSwitches = new ArrayList<Switch>();
        ArrayList<Integer> branchEnds = new ArrayList<Integer>();
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(trackFile));

        XSSFSheet trackSheet;
        if(track == getRedLine()){
            trackSheet = myExcelBook.getSheet("Red Line");
        }else{
            trackSheet = myExcelBook.getSheet("Green Line");
        }

        Row currRow;
        Row firstRow = trackSheet.getRow(0);
        System.out.println(trackSheet.getLastRowNum());
        //iterate through sheet
        for(int r=1;!isRowEmpty(trackSheet.getRow(r));r++){

            //each row corresponds to a block
            currRow = trackSheet.getRow(r);
            Block currBlock = new Block();
            for(int c=0;c<TRACK_FILE_NUM_COLS;c++){
                //each column has a block property

                //col 7 is empty in the sample
                if(c == 7) {
                    continue;
                }

                String colTitle = firstRow.getCell(c).getStringCellValue();
                Cell cell = currRow.getCell(c);
                //parse cell differently based on which column it is
                switch(colTitle) {
                    case "Line":
                        if(!lineParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    case "Section":
                        if(!sectionParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    case "Block Number":
                        if(!blockNumParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    case "Block Length (m)":
                        if(!blockLengthParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    case "Block Grade (%)":
                        if(!blockGradeParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    case "Speed Limit (Km/Hr)":
                        if(!speedLimitParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    case "Infrastructure":
                        if(!infrastructureParse(currBlock, cell, builtSwitches, branchEnds)){
                            return false;
                        }
                        break;
                    case "ELEVATION (M)":
                        if(!elevationParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    case "CUMULATIVE ELEVATION (M)":
                        if(!cumulativeElevationParse(currBlock, cell)){
                            return false;
                        }
                        break;
                    default:
                        //invalid track file column title
                        System.out.println("Track Build Error: Invalid track file column title");
                        return false;
                }
            }

            if(currBlock.getBlockNum() > 1){
                //set tail of current block to be the previous block
                currBlock.setTail(track.getBlock(currBlock.getBlockNum() - 1));

                //set head of previous block to be current block
                currBlock.getPreviousBlock().setHead(currBlock);
            }

            //populate lists that are referenced later
            track.getBlockList().add(currBlock);
            track.addToHashMap(currBlock);
            if(currBlock.getStation() != null){
                track.getStationBlocks().add(currBlock);
            }
            if(r == trackSheet.getLastRowNum()){
                break;
            }
        }

        System.out.println("Placing switches...");
        for(Switch sw: builtSwitches){
            track.getBlock(sw.getRoot()).setSwitch(sw);
        }

        if(!connectBranches(track, branchEnds, builtSwitches)){
            System.out.println("Track Build Error: Connecting Branches");
            return false;
        }

        myExcelBook.close();

        System.out.println("Finished Building Track!");
        return true;
    }

    //parse line name into block
    private boolean lineParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_STRING || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING){
            if(cell.getStringCellValue().equals("Red")){
                b.setLine(0);
            }else if(cell.getStringCellValue().equals("Green")){
                b.setLine(1);
            }else{
                System.out.println("Track Build Error: Line naming");
                return false;
            }
        }else{
            System.out.println("Track Build Error: Invalid Line Cell Type");
            return false;
        }
        return true;
    }

    //parse section marker into block
    private boolean sectionParse(Block b, Cell cell){
        if(cell.getCellType() != Cell.CELL_TYPE_STRING){
            System.out.println("Track Build Error: Invalid Section Cell Type");
            return false;
        }else if(cell.getStringCellValue().length() != 1){
            System.out.println("Track Build Error: Invalid Section Cell Length");
            return false;
        }else{
            b.setSection(cell.getStringCellValue().charAt(0));
        }
        return true;
    }

    //parse block num into block
    private boolean blockNumParse(Block b, Cell cell){
        if(cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
            System.out.println("Track Build Error: Invalid BlockNum Cell Type");
            return false;
        }else if(cell.getNumericCellValue() != Math.floor(cell.getNumericCellValue())){
            //cell value is not integer
            System.out.println("Track Build Error: Block Num Cannot be double");
            return false;
        }else{
            b.setBlockNum((int) cell.getNumericCellValue());
        }
        return true;
    }

    //parse block length into block
    private boolean blockLengthParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setLength((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Block Length Cell Type");
            return false;
        }
        return true;
    }

    //parse block grade into block
    private boolean blockGradeParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setGrade((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Block Grade Cell Type");
            return false;
        }
        return true;
    }

    //parse speed limit into block
    private boolean speedLimitParse(Block b, Cell cell){
        if(cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
            System.out.println("Track Build Error: Invalid Block Length Cell Type");
            return false;
        }else if(cell.getNumericCellValue() != Math.floor(cell.getNumericCellValue())){
            //cell value is not integer
            System.out.println("Track Build Error: Speed Limit Cannot be double");
            return false;
        }else{
            b.setSpeedLimit((int) cell.getNumericCellValue());
        }
        return true;
    }

    //parse infrastructure features into block, if applicable
    private boolean infrastructureParse(Block b, Cell cell, ArrayList<Switch> builtSwitches, ArrayList<Integer> branchEnds){

        if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK){
            //System.out.println("Null/Blank Infrastructure Cell");
        }else if(cell.getCellType() == Cell.CELL_TYPE_STRING || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING){

            String[] infraSects = cell.getStringCellValue().split(";",0);

            for(int i=0;i<infraSects.length;i++){
                if(infraSects[i].equals("UNDERGROUND")){
                    //underground
                    b.setUnderground(true);
                }else if(infraSects[i].equals("RAILWAY CROSSING")){
                    b.setRXR(new RXR());
                }else if(infraSects[i].startsWith("STATION")){
                    //stations
                    String stationName = infraSects[i+1];
                    stationName = stationName.trim();
                    Station newStation = new Station(stationName);
                    b.setStation(newStation);
                }else if(infraSects[i].startsWith("SWITCH") && infraSects.length > 1){
                    //switches
                    String switchString = infraSects[i] + infraSects[i+1];

                    String[] switchSplit = switchString.split("S|W|I|T|C|H|\\s|\\(|-|\\)");
                    HashSet<Integer> tempSet = new HashSet<Integer>();
                    int root = 0, blockNum;

                    //parse switch entry for values
                    for(String s: switchSplit){
                        try{
                            blockNum = Integer.parseInt(s);
                            if(!tempSet.add(blockNum)){
                                root = blockNum;
                            }
                        }catch(Exception e){
                            //do nothing
                        }
                    }

                    //build switch from parsed block numbers
                    Switch sw = new Switch();
                    Iterator<Integer> it = tempSet.iterator();
                    while (it.hasNext()){
                        int temp = it.next();
                        if(!branchEnds.contains(temp)){
                            branchEnds.add(temp);
                        }
                        if(temp != root){
                            if(temp - root == -1 || temp - root == 1){
                                sw.setStraightDest(temp);
                            }else{
                                sw.setBranchDest(temp);
                            }
                        }
                    }

                    sw.setRoot(root);

                    builtSwitches.add(sw);
                }
            }
        }else{
            System.out.println("Track Build Error: Invalid Infrastructure Cell Type");
            return false;
        }

        return true;
    }

    //parse elevation data into block
    private boolean elevationParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setElevation((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Elevation Cell Type");
            return false;
        }
        return true;
    }

    //parse cumulative elevation data into block
    private boolean cumulativeElevationParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setCumulativeElevation((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Cumulative Elevation Cell Type");
            return false;
        }
        return true;
    }

    private boolean connectBranches(Track track, ArrayList<Integer> branchEnds, ArrayList<Switch> builtSwitches){
        Collections.sort(branchEnds);

        for(Switch sw: builtSwitches){

            if(branchEnds.indexOf(sw.getRoot()) % 2 == 0){
                track.getBlock(sw.getRoot()).setTail(track.getBlock(sw.getStraightDest()));
            }else if(branchEnds.indexOf(sw.getRoot()) % 2 == 1){
                track.getBlock(sw.getRoot()).setHead(track.getBlock(sw.getStraightDest()));
            }else{
                return false;
            }

            if(branchEnds.indexOf(sw.getStraightDest()) % 2 == 0){
                track.getBlock(sw.getStraightDest()).setTail(track.getBlock(sw.getRoot()));
            }else if(branchEnds.indexOf(sw.getStraightDest()) % 2 == 1){
                track.getBlock(sw.getStraightDest()).setHead(track.getBlock(sw.getRoot()));
            }else{
                return false;
            }

            track.getBlock(sw.getRoot()).setBranch(track.getBlock(sw.getBranchDest()));

            if(branchEnds.indexOf(sw.getBranchDest()) % 2 == 0){
                track.getBlock(sw.getBranchDest()).setTail(track.getBlock(sw.getRoot()));
            }else if(branchEnds.indexOf(sw.getBranchDest()) % 2 == 1){
                track.getBlock(sw.getBranchDest()).setHead(track.getBlock(sw.getRoot()));
            }else{
                return false;
            }

            System.out.println("Straight " + track.getBlock(sw.getRoot()).getTail().getBlockNum() +
                    " => " + track.getBlock(sw.getRoot()).getBlockNum() +
                    " => " + track.getBlock(sw.getRoot()).getHead().getBlockNum());
            System.out.println("Branch " + track.getBlock(sw.getRoot()).getTail().getBlockNum() +
                    " => " + track.getBlock(sw.getRoot()).getBlockNum() +
                    " => " + track.getBlock(sw.getRoot()).getBranch().getBlockNum());
        }
        return true;
    }

    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                return false;
        }
        return true;
    }

    public boolean verify(Track track) throws Exception {

        int travelCount = 0;
        Block currBlock = track.getBlock(1);

        while(travelCount < 200){
            //test basic head tail connections
            if(currBlock.getHead().getBlockNum() != currBlock.getBlockNum() + 1){
                if(currBlock.getBlockNum() >= track.getBlockHashMap().size()){
                    currBlock = track.getBlock(1);
                    System.out.println("Jumping to beginning of track...");
                }else{
                    currBlock = track.getBlock(currBlock.getBlockNum() + 1);
                    System.out.println("Jumping to next branch...");
                }
            }else{
                currBlock = currBlock.getHead();
            }

            travelCount++;
        }

        Block testBlock = track.getBlock((int) (Math.random() * track.getBlockHashMap().size()) + 1);
        currBlock.setDirection(Direction.FROM_TAIL);
        System.out.println("From Tail: " + testBlock.getBlockNum());
        testBlock.initTrain(0, 0, 0);
        for(int i=0;i<30;i++){
            testBlock = testBlock.trainGetNextBlock();
            System.out.println("=> " + testBlock.getBlockNum());
        }

        //clear test train
        testBlock.setTrain(null);

        if(track == getRedLine()){
            Block b1 = track.getBlock(1);
            Block b2 = track.getBlock(66);
            System.out.println("Distance (meters) between " + b1.getBlockNum() + " and " + b2.getBlockNum() + " = " + track.distanceBetweenTwoBlocks(b1,b2, 0));
            System.out.println("Distance (meters) between " + b1.getBlockNum() + " and yard = " + track.distanceToYard(b1, 0));
            System.out.println("Distance (blocks) between " + b1.getBlockNum() + " and " + b2.getBlockNum() + " = " + track.distanceBetweenTwoBlocks(b1,b2, 1));
            System.out.println("Distance (blocks) between " + b1.getBlockNum() + " and yard = " + track.distanceToYard(b1, 1));

        }else{
            Block b1 = track.getBlock(1);
            Block b2 = track.getBlock(150);
            System.out.println("Distance (meters) between " + b1.getBlockNum() + " and " + b2.getBlockNum() + " = " + track.distanceBetweenTwoBlocks(b1,b2, 0));
            System.out.println("Distance (meters) between " + b1.getBlockNum() + " and yard = " + track.distanceToYard(b1, 0));
            System.out.println("Distance (blocks) between " + b1.getBlockNum() + " and " + b2.getBlockNum() + " = " + track.distanceBetweenTwoBlocks(b1,b2, 1));
            System.out.println("Distance (blocks) between " + b1.getBlockNum() + " and yard = " + track.distanceToYard(b1, 1));
        }

        return true;
    }

    public void initRedLineBranches(){

        ArrayList<Branch> branchList = new ArrayList<Branch>();
        Branch r0 = new Branch(1,15, getRedLine());
        Branch r1 = new Branch(16, 27, getRedLine());
        Branch r2 = new Branch(28, 32, getRedLine());
        Branch r3 = new Branch(33, 38, getRedLine());
        Branch r4 = new Branch(39, 43, getRedLine());
        Branch r5 = new Branch(44, 52, getRedLine());
        Branch r6 = new Branch(53, 66, getRedLine());
        Branch r7 = new Branch(67, 71, getRedLine());
        Branch r8 = new Branch(72, 76, getRedLine());

        branchList.add(r0);
        branchList.add(r1);
        branchList.add(r2);
        branchList.add(r3);
        branchList.add(r4);
        branchList.add(r5);
        branchList.add(r6);
        branchList.add(r7);
        branchList.add(r8);

        for(Branch b: branchList){
            for(int i=b.getStart();i<= b.getEnd();i++){
                redLine.getBranchMap().put(i, b);
            }
        }

        r0.getHead().add(r1);
        r0.getTail().add(r1);

        r1.getHead().add(r2);
        r1.getHead().add(r8);
        r1.getTail().add(r0);

        r2.getHead().add(r3);
        r2.getTail().add(r1);

        r3.getHead().add(r4);
        r3.getHead().add(r7);
        r3.getTail().add(r2);
        r3.getTail().add(r8);

        r4.getHead().add(r5);
        r4.getTail().add(r3);

        r5.getHead().add(r6);
        r5.getTail().add(r4);
        r5.getTail().add(r7);

        r6.getHead().add(r5);
        r6.getTail().add(r5);

        r7.getHead().add(r3);
        r7.getTail().add(r5);

        r8.getHead().add(r1);
        r8.getTail().add(r3);
    }

    public void initGreenLineBranches(){

        Track tempTrack = getGreenLine();
        ArrayList<Branch> branchList = new ArrayList<Branch>();
        Branch r0 = new Branch(12,1, tempTrack);
        Branch r1 = new Branch(13, 28, tempTrack);
        Branch r2 = new Branch(29, 76, tempTrack);
        Branch r3 = new Branch(77, 85, tempTrack);
        Branch r4 = new Branch(86, 100, tempTrack);
        Branch r5 = new Branch(101, 150, tempTrack);

        branchList.add(r0);
        branchList.add(r1);
        branchList.add(r2);
        branchList.add(r3);
        branchList.add(r4);
        branchList.add(r5);

        for(Branch b: branchList){
            if(b.getStart() > b.getEnd()){
                for(int i=b.getStart();i>= b.getEnd();i--){
                    tempTrack.getBranchMap().put(i, b);
                }
            }else {
                for (int i = b.getStart(); i <= b.getEnd(); i++) {
                    tempTrack.getBranchMap().put(i, b);
                }
            }
        }

        r0.getHead().add(r1);
        r0.getTail().add(r1);

        r1.getHead().add(r2);
        r1.getTail().add(r0);

        r2.getHead().add(r3);
        r2.getTail().add(r1);

        r3.getHead().add(r4);
        r3.getTail().add(r2);
        r3.getTail().add(r5);

        r4.getHead().add(r3);
        r4.getTail().add(r3);

        r5.getHead().add(r1);
        r5.getTail().add(r3);
    }

    public void addBeacons(Track currTrack){

        ArrayList<Block> stationBlocks = currTrack.getStationBlocks();
        for(Block b: stationBlocks){
            b.getHead().setBeacon(new Beacon(b.getStation().getName()));
            b.getTail().setBeacon(new Beacon(b.getStation().getName()));
        }
    }

    public int updateThroughput(int line){

        Track currTrack = null;

        //set line based on passed int
        if(line == 0){
            currTrack = getRedLine();
        }else{
            currTrack = getGreenLine();
        }

        //init running total
        int totalPassengers = 0;

        //for each station on line, su
        for(Block b: currTrack.getStationBlocks()){
           totalPassengers += b.getStation().generatePassengers();
        }

        //return total
        return totalPassengers;
    }

    public void updatePassengers(){
        updatePassengersHelper(getRedLine());
        updatePassengersHelper(getGreenLine());
    }

    public void updatePassengersHelper(Track currTrack){

        ArrayList<Block> stationBlockList= currTrack.getStationBlocks();

        for(Block b: stationBlockList){
            if(b.getTrain() != null && b.getTrain().getCurV() == 0.0 && !b.isPassengerUpdateDone()){

                int availableSpace = b.getTrain().removePassengers();
                int addedPassengers = b.getStation().getPassengers();

                if(addedPassengers >= availableSpace){
                    addedPassengers = availableSpace;
                }
                b.getTrain().addPassengers(addedPassengers);
                b.setPassengerUpdateDone(true);
            }
        }
    }
}