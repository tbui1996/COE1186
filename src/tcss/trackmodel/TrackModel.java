package tcss.trackmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TrackModel {

    private Track redLine;
    private Track greenLine;
    private File trackFile;

    private final int TRACK_FILE_NUM_COLS = 10;

    public TrackModel() throws IOException {
        init();
    }

    public void init() throws IOException {
        trackFile = new File("resources/Sample_Track_File1.xlsx");;
        buildTrack(trackFile);
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

    public boolean buildTrack(File trackFile) throws IOException {

        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(trackFile));

        redLine = new Track();
        greenLine = new Track();

        XSSFSheet redLineSheet = myExcelBook.getSheet("Red Line");
        XSSFSheet greenLineSheet = myExcelBook.getSheet("Green Line");
        Row currRow;

        Row firstRow = redLineSheet.getRow(0);
        //Row firstRow = redLineSheet.getRow(redLineSheet.getFirstRowNum());

        for(int r=1;r<=redLineSheet.getLastRowNum();r++){
            currRow = redLineSheet.getRow(r);
            Block currBlock = new Block();
            for(int c=0;c<TRACK_FILE_NUM_COLS;c++){

                if(c == 7) {
                    continue;
                }

                String colTitle = firstRow.getCell(c).getStringCellValue();
                Cell cell = currRow.getCell(c);

                if(colTitle.equals("Line")){
                    if(!lineParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("Section")){
                    if(!sectionParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("Block Number")){
                    if(!blockNumParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("Block Length (m)")){
                    if(!blockLengthParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("Block Grade (%)")) {
                    if(!blockGradeParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("Speed Limit (Km/Hr)")){
                    if(!speedLimitParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("Infrastructure")){
                    if(!infrastructureParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("ELEVATION (M)")){
                    if(!elevationParse(currBlock, cell)){
                        return false;
                    }
                }else if(colTitle.equals("CUMULATIVE ELEVATION (M)")){
                    if(!cumulativeElevationParse(currBlock, cell)){
                        return false;
                    }
                }else{
                    //invalid track file column title
                    System.out.println("Track Build Error: Invalid track file column title");
                    return false;
                }
            }
            redLine.getBlockList().add(currBlock);
        }
        myExcelBook.close();
        return true;
    }

    public boolean lineParse(Block b, Cell cell){
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

    public boolean sectionParse(Block b, Cell cell){
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

    public boolean blockNumParse(Block b, Cell cell){
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

    public boolean blockLengthParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setLength((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Block Length Cell Type");
            return false;
        }
        return true;
    }

    public boolean blockGradeParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setGrade((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Block Grade Cell Type");
            return false;
        }
        return true;
    }

    public boolean speedLimitParse(Block b, Cell cell){
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

    public boolean infrastructureParse(Block b, Cell cell){
        return true;
    }

    public boolean elevationParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setElevation((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Elevation Cell Type");
            return false;
        }
        return true;
    }

    public boolean cumulativeElevationParse(Block b, Cell cell){
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
            b.setCumulativeElevation((float) cell.getNumericCellValue());
        }else{
            System.out.println("Track Build Error: Invalid Cumulative Elevation Cell Type");
            return false;
        }
        return true;
    }
}
