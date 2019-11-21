package tcss.trackmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TrackModel {

    private Track redLine;
    private Track greenLine;

    private final int TRACK_FILE_NUM_COLS = 10;

    public TrackModel() throws IOException {
        init();
    }

    private void init() throws IOException {
        File trackFile = new File("resources/Sample_Track_File2.xlsx");
        //init red and green line
        redLine = new Track();
        greenLine = new Track();
        if (!buildTrack(trackFile, redLine)) {
            System.out.println("Error Constructing Track!");
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

            if(currBlock.getBlockNum() > 2){
                System.out.println(currBlock.getPreviousBlock().getTail().getBlockNum() + " => "
                        + currBlock.getPreviousBlock().getBlockNum() + " => "
                        + currBlock.getPreviousBlock().getHead().getBlockNum());
            }

            track.addToHashMap(currBlock);
            track.getBlockList().add(currBlock);
            if(r == trackSheet.getLastRowNum()){
                break;
            }
        }

        System.out.println("Placing switches...");
        for(Switch sw: builtSwitches){
            System.out.println("Setting switch on block " + sw.getRoot());
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
            System.out.println("Null/Blank Infrastructure Cell");
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
                    System.out.println(stationName);
                    b.setStation(new Station(stationName));
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
                    System.out.println("New switch on block " + root
                        + ", Straight: " + sw.getStraightDest()
                        + ", Branch: " + sw.getBranchDest());

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
                track.getBlock(sw.getStraightDest()).setHead(track.getBlock(sw.getRoot()));
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
}
