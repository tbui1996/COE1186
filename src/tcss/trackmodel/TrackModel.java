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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class TrackModel {

    private Track redLine;
    private Track greenLine;
    private File trackFile;

    private final int TRACK_FILE_NUM_COLS = 10;

    public TrackModel() throws IOException {
        init();
    }

    public void init() throws IOException {
        trackFile = null;
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

    public void buildTrack(File trackFile) throws IOException {
        //File file = new File("../../../resources/Sample_Track_File1.xlsx");
        File file = new File("Sample_Track_File1.xlsx");
        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
        redLine = new Track();

        HSSFSheet redLineSheet = myExcelBook.getSheet("Red Line");
        HSSFSheet greenLineSheet = myExcelBook.getSheet("Green Line");
        HSSFRow currRow;
        HSSFRow firstRow = redLineSheet.getRow(redLineSheet.getFirstRowNum());

        for(int i=0;i<TRACK_FILE_NUM_COLS;i++){
            System.out.println(firstRow.getCell(i).getStringCellValue());
        }

        for(int r=1;r<redLineSheet.getLastRowNum();r++){
            currRow = redLineSheet.getRow(r);
            for(int c=0;c<TRACK_FILE_NUM_COLS;c++){

            }
        }





        /*
        if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING){
            String name = row.getCell(0).getStringCellValue();
            System.out.println("name : " + name);
        }

        if(row.getCell(1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
            Date birthdate = row.getCell(1).getDateCellValue();
            System.out.println("birthdate :" + birthdate);
        }*/

        myExcelBook.close();
    }
}
