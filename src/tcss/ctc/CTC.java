package tcss.ctc;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tcss.main.Main;
import tcss.trackcontroller.TrackController;
import tcss.trackmodel.Block;
import tcss.trackmodel.Station;
import tcss.trackmodel.Track;
import tcss.trackmodel.TrackModel;
import tcss.trainmodel.TrainModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class CTC {
    private ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    public ArrayList<TrainModel> trainList = new ArrayList<TrainModel>(); /*Number of Trains*/
    private ArrayList<Maintenance> maintenanceList = new ArrayList<>();

    //Temporary Red and Green Line setup for creating a Dispatch
    protected Map<Integer,Block> redLine;
    protected Map<Integer,Block> greenLine;
    protected TrackModel privateTrack;
    protected Track redLayout;
    protected Track greenLayout;
    private String [] stationNames; //This will be deleted

    //What I might need
    //This would be Station name to block number
    protected HashMap<String,Integer> stationToBlockNumRed;
    protected HashMap<String,Integer> stationToBlockNumGreen;

    //String ArrayLists of all of the stations on each line
    private ArrayList<String> redStations;
    private ArrayList<String> greenStations;

    //Line Throughput
    private int redTicketTotal = 0;
    private int greenTicketTotal = 0;

    private int totalDwell = 5 * 35; //This is the total amount of times the system updates in 35 simulation seconds


    /**
     *Initializes the CTC
     * @throws Exception
     */
    public CTC() throws Exception {

        redLine = new HashMap<>();
        greenLine = new HashMap<>();

        //Private Track Model
        privateTrack = new TrackModel();
        redLine = privateTrack.getRedLine().getBlockHashMap();
        greenLine = privateTrack.getGreenLine().getBlockHashMap();
        greenLayout = privateTrack.getGreenLine();
        redLayout = privateTrack.getRedLine();

        stationToBlockNumRed = new HashMap<>();
        stationToBlockNumGreen = new HashMap<>();

        redStations = new ArrayList<>();
        greenStations = new ArrayList<>();



        //Loads in block hashmaps with new instances of blocks with the same values
        Iterator<Map.Entry<Integer,Block>> redIterator = redLine.entrySet().iterator();
        Iterator<Map.Entry<Integer,Block>> greenIterator = greenLine.entrySet().iterator();

        while (redIterator.hasNext()) {
            Map.Entry<Integer,Block> mapElement = redIterator.next();
            redLine.replace(mapElement.getKey(), new Block(mapElement.getValue()));
            //updates station to block number hash map
            if (redLine.get(mapElement.getKey()).getStation() != null) {
                if (!redStations.contains(redLine.get(mapElement.getKey()).getStation().getName())) {
                    stationToBlockNumRed.put(redLine.get(mapElement.getKey()).getStation().getName(), mapElement.getKey());
                    redStations.add(redLine.get(mapElement.getKey()).getStation().getName());
                }
            }
        }

        while (greenIterator.hasNext()) {
           Map.Entry<Integer,Block> mapElement = greenIterator.next();
           greenLine.replace(mapElement.getKey(), new Block(mapElement.getValue()));
            //updates station to block number hash map
            if (greenLine.get(mapElement.getKey()).getStation() != null) {
                if (!greenStations.contains(greenLine.get(mapElement.getKey()).getStation().getName())) {
                    stationToBlockNumGreen.put(greenLine.get(mapElement.getKey()).getStation().getName(), mapElement.getKey());
                    greenStations.add(greenLine.get(mapElement.getKey()).getStation().getName());
                }
            }
        }
    }



    /**
     * Creates the dispatches for the CTC if a file is uploaded for scheduling
     * @param schedules
     * @throws IOException
     */
    public void automaticDispatch(File schedules) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(schedules));
        ArrayList<Dispatch> autoDispatch = new ArrayList<>();

        XSSFSheet sched;
        sched = myExcelBook.getSheet("Schedules");

        Row firstRow = sched.getRow(0);
        Row currRow;

        //System.out.println(firstRow.getLastCellNum());
        //Initialize trains
        int trainCount = 0;
        //i is the block number
        for (int i = 31; i < firstRow.getLastCellNum(); i++) {
            if (firstRow.getCell(i) == null) {
                break;
            }
            String name = firstRow.getCell(i).toString();
            if (!name.contains("Arrival Time at Station")) {
                break;
            }
            name = name.replace(" Arrival Time at Station", "");
            Dispatch temp = new Dispatch("GREEN", name);
            //System.out.println(name);
            temp.createSchedule(temp.getLine());
            autoDispatch.add(temp);
            trainCount++;
        }
        //System.out.println(trainCount);


        //iterate through sheet, adding stops
        for (int r=1; !isRowEmpty(sched.getRow(r)); r++) {
            currRow = sched.getRow(r);
            //System.out.println("Here");

            for (int i = 31; i < trainCount+31; i++) {
                //If there is an arrival time
                if (currRow.getCell(i) != null) {
                    if (currRow.getCell(i).getDateCellValue() != null) {
                        System.out.println((currRow.getCell(i).getDateCellValue()));
                        Date temp = currRow.getCell(i).getDateCellValue();
                        String[] time = currRow.getCell(i).getDateCellValue().toString().split(":");
                        //if (i == 32)
                        //System.out.println(currRow.getCell(i).getDateCellValue());
                        String hour = new StringBuilder(time[0].charAt(time[0].length() - 2)).append(time[0].charAt(time[0].length() - 1)).toString();

                        //System.out.println(hour);
                        //System.out.println(Integer.parseInt(time[1]));
                        autoDispatch.get(i - 31).schedule.addStop(Integer.toString(r), Integer.parseInt(hour), Integer.parseInt(time[1]));
                    }
                }
            }
        }
        System.out.println(autoDispatch.get(0).schedule.toString());

        for (int i = 0; i < autoDispatch.size(); i++) {
            autoDispatch.get(i).setRequests();
            autoDispatch.get(i).getTrain().setStation(autoDispatch.get(i).schedule.getStopName(0));
            autoDispatch.get(i).getTrain().setDTime(autoDispatch.get(i).getDepartureHour(), autoDispatch.get(i).getDepartureMin());
        }

        System.out.println("Dispatch: \n" + autoDispatch.get(7));

        dispatchList.addAll(autoDispatch);
    }


    /**
     * Add a dispatch to the list in the CTC
     * @param d
     */
    public void addDispatch(Dispatch d) {
        this.dispatchList.add(d);
    }

    /**
     * Add a maintenance request to the list in the CTC
     * @param hr
     * @param min
     * @param line
     * @param block
     * @param length
     */
    public void addMaintenance(int hr, int min, int line, int block, int length) {
        maintenanceList.add(new Maintenance(hr, min, line, block, length));
    }

    /**
     * Called every cycle.  Checks when a new SS and Authority needs to be sent for each Train
     */
    public void checkDispatchList () {
        for (Dispatch temp : dispatchList) {
            //if train is not dispatched yet
            if (temp.getCurrStop() == -1 && temp.getSS() == 0) {
                if (tcss.main.Main.getSimTime().getHour() >= temp.getDepartureHour()) {
                    //If the current hour is passed the departure hour or the current hour is the departure hour and the current minute is greater than or equal to the departure minute
                    if (tcss.main.Main.getSimTime().getHour() > temp.getDepartureHour() || tcss.main.Main.getSimTime().getMin() >= temp.getDepartureMin()) {
                        temp.setSS(temp.getSpeed(temp.getCurrStop() + 1));
                        temp.setAuth(temp.getAuth(temp.getCurrStop() + 1));
                        System.out.println("Train sent");
                        //Sends SS and Auth to new
                        if (temp.getLine() == 1) {
                            tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop() + 1), temp.getAuth(temp.getCurrStop() + 1), 1, 9);
                            temp.setDispatched();
                        }
                        else {
                            tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop() + 1), temp.getAuth(temp.getCurrStop() + 1), 0, 63);
                            temp.setDispatched();
                        }
                    }
                }
            }
            //If train is already dispatched
            else {
                //Check block occupancy list to see if next stop block is currently occupied.  If so, a new request must be sent to keep train moving
                if (temp.getLine() == 1) {
                    if (Integer.parseInt(temp.getTrain().getBlock()) == 9 && temp.getCurrStop() == temp.schedule.getStopNums()) {
                        dispatchList.remove(temp);
                        continue;
                    }
                    if (temp.getCurrStop() < temp.schedule.getStopNums()-1 && redLine.get(blockReturner(temp.getLine(), temp.schedule.getStopName(temp.getCurrStop() + 1))).isOccupied()) {
                        if (temp.getDwell() == totalDwell) {
                            temp.setDwell(0);
                            temp.setCurrStop(temp.getCurrStop() + 1);
                            tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop() + 1), temp.getAuth(temp.getCurrStop() + 1), temp.lineToTc(), blockReturner(temp.getLine(), temp.getStopName(temp.getCurrStop())));
                        }
                        else {
                            temp.setDwell(temp.getDwell() + 1);
                        }
                    }
                } else {
                    if (Integer.parseInt(temp.getTrain().getBlock()) == 63 && temp.getCurrStop() == temp.schedule.getStopNums()) {
                        dispatchList.remove(temp);
                        continue;
                    }
                    if (temp.getCurrStop() < temp.schedule.getStopNums()-1 && greenLine.get(blockReturner(temp.getLine(), temp.schedule.getStopName(temp.getCurrStop() + 1))).isOccupied()) {
                        if (temp.getDwell() == totalDwell) {
                            temp.setDwell(0);
                            temp.setCurrStop(temp.getCurrStop() + 1);
                            tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop() + 1), temp.getAuth(temp.getCurrStop() + 1), temp.lineToTc(), blockReturner(temp.getLine(), temp.getStopName(temp.getCurrStop())));
                        }
                        else {
                           temp.setDwell(temp.getDwell() + 1);
                        }
                    }
                }
                //stationToBlock.get(temp.schedule.stopList.get(temp.getCurrStop()+1)
            }
        }
    }

    /**
     * Called every cycle.  Checks when a new block needs to be opened/closed from Maintenance request
     */
    public void checkMaintenanceList() {
        for (Maintenance temp : maintenanceList) {
            //Check if request is done, if active already
            if (temp.isActive()) {
                //If it's been closed long enough
                if (temp.getTimePassed() == temp.getLength() * 5 * 60) {
                    //Send request to reopen block
                    Main.tc.maintenanceRequest(temp.getLine(), temp.getBlock());
                }
                //Update wait time
                else {
                    temp.setTimePassed(temp.getTimePassed() + 1);
                }
            }
            //Check if need to send out request
            else {
                if (Main.getSimTime().getHour() >= temp.getHour()) {
                    //If the time is now or has passed
                    if (Main.getSimTime().getHour() > temp.getHour() || Main.getSimTime().getMin() >= temp.getMin()) {
                        //Send request if block is not occupied
                        if (!getBlock(temp.getLine(), temp.getBlock()).isOccupied()) {
                            Main.tc.maintenanceRequest(temp.getLine(), temp.getBlock());
                            temp.setActive(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the private instance of the track in the CTC to use for calculations later.  Updated every cycle
     */
    public void updateTrackState() {
        for (int j = 1; j < (redLine.size()); j++) {
            //updating red line
            redLine.get(j).setOccupied(tcss.main.Main.tc.getOccupied(1,j));
            if (redLine.get(j).getSwitch() != null) {
                redLine.get(j).getSwitch().setStraight(tcss.main.Main.tc.getSwitchStraight(1, j));
                redLine.get(j).getSwitch().setLights(tcss.main.Main.tc.getLightState(1, j));
            }
        }

        //updating green line
        for (int j = 1; j < (greenLine.size()); j++) {
            greenLine.get(j).setOccupied(tcss.main.Main.tc.getOccupied(0,j));
            if (greenLine.get(j).getSwitch() != null) {
                greenLine.get(j).getSwitch().setStraight(tcss.main.Main.tc.getSwitchStraight(0,j));
                greenLine.get(j).getSwitch().setLights(tcss.main.Main.tc.getLightState(0,j));
            }
        }

        //Updating block position of trains
        for (Dispatch temp : dispatchList) {
            //Red Line
            if (temp.getLine() == 1) {
                //If dispatched, need to update the location
                if (temp.isDispatched()) {
                    //If haven't left Yard yet, keep checking for starting block to be occupancy
                    if (temp.getTrain().getBlock().equals("N/A") && redLine.get(9).isOccupied()) {
                        temp.getTrain().setBlock(9);
                    }
                    //Has left yard, so check block next to it
                    else {
                        //If head block is occupied, move train to head block
                        if (redLine.get(redLine.get(temp.getTrain().getBlockInt()).getHead().getBlockNum()).isOccupied())
                            temp.getTrain().setBlock(redLine.get(temp.getTrain().getBlockInt()).getHead().getBlockNum());
                        //If tail block is occupied, move train to tail block
                        else if (redLine.get(redLine.get(temp.getTrain().getBlockInt()).getTail().getBlockNum()).isOccupied())
                            temp.getTrain().setBlock(redLine.get(temp.getTrain().getBlockInt()).getTail().getBlockNum());
                            //If branch block is occupied, move train to tail block
                        else if (redLine.get(temp.getTrain().getBlockInt()).getBranch() != null && redLine.get(redLine.get(temp.getTrain().getBlockInt()).getBranch().getBlockNum()).isOccupied())
                            temp.getTrain().setBlock(redLine.get(temp.getTrain().getBlockInt()).getBranch().getBlockNum());
                    }
                }
            }
            //Green Line
            else {
                //If dispatched, need to update the location
                if (temp.isDispatched()) {
                    //If haven't left Yard yet, keep checking for starting block to be occupancy
                    if (temp.getTrain().getBlock().equals("N/A") && greenLine.get(63).isOccupied()) {
                        temp.getTrain().setBlock(63);
                    }
                    //Has left yard, so check block next to it
                    else {
                        //If head block is occupied, move train to head block
                        if (greenLine.get(temp.getTrain().getBlockInt()).getHead().isOccupied())
                            temp.getTrain().setBlock(greenLine.get(temp.getTrain().getBlockInt()).getHead().getBlockNum());
                            //If tail block is occupied, move train to tail block
                        else if (greenLine.get(temp.getTrain().getBlockInt()).getTail().isOccupied())
                            temp.getTrain().setBlock(greenLine.get(temp.getTrain().getBlockInt()).getTail().getBlockNum());
                            //If branch block is occupied, move train to tail block
                        else if (greenLine.get(temp.getTrain().getBlockInt()).getBranch() != null && greenLine.get(temp.getTrain().getBlockInt()).getBranch().isOccupied())
                            temp.getTrain().setBlock(greenLine.get(temp.getTrain().getBlockInt()).getBranch().getBlockNum());
                    }
                }
            }
        }
    }

    /**
     * Updates throughput every 3 seconds and adds it to the total numbers
     */
    public void updateThroughput() {
        DecimalFormat df = new DecimalFormat("#.#");
        if (Double.parseDouble(df.format(tcss.main.Main.getSimTime().getSec())) % 3 == 0) {
            redTicketTotal += tcss.main.Main.tm.updateThroughput(0);
            greenTicketTotal += tcss.main.Main.tm.updateThroughput(1);
            //System.out.println(redTicketTotal);
            //System.out.println(greenTicketTotal);
        }
    }

    /**
     * Used privately to determine if a new row needs to be read in from the Excel file.  Used for automatic dispatching
     * @param row
     * @return
     */
    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * Used to return an integer representation of the block, given the block number or station name on the block
     * @param l
     * @param b
     * @return
     */
    public int blockReturner(int l, String b) {
        if (b.length() > 3) {
            if (l == 1) {
                return stationToBlockNumRed.get(b);
            } else {
                return stationToBlockNumGreen.get(b);
            }
        } else {
            return Integer.parseInt(b);
        }
    }

    public Dispatch getDispatch(int i) {
        return dispatchList.get(i);
    }

    public int numDispatches() {
        return this.dispatchList.size();
    }

    //Return a String array of all stops in a line

    /**
     * Returns a String array of every block on the line, and adds the station name if applicable
     * @param l
     * @return
     */
    public String [] getAllStops(int l) {
        if (l == 1) {
            String [] temp = new String[redLine.size()];
            for (int i = 1; i < redLine.size(); i++) {
                Block b = redLine.get(i);
                if (b.getStation() != null)
                    temp[i] = b.getStation().getName();
                else
                    temp[i] = Integer.toString(i);
            }
            return temp;
        }
        else {
            String [] temp = new String[greenLine.size()];
            for (int i = 1; i < greenLine.size(); i++) {
                Block b = greenLine.get(i);
                if (b.getStation() != null)
                    temp[i] = b.getStation().getName();
                else
                    temp[i] = Integer.toString(i);

            }
            return temp;
        }
    }

    public int getRedTicketTotal() {
        return this.redTicketTotal;
    }

    public int getGreenTicketTotal() {
        return this.greenTicketTotal;
    }

    /**
     * Returns an int representation of the line, given the uppercase String
     * @param line Upper case String name of the line
     * @return integer representation of selected line
     */
    public int lineStringToInt(String line) {
        if (line.equals("RED"))
            return 1;
        else if (line.equals("GREEN"))
            return 2;
        else
            return 0;
    }

    //returns total block number in the line

    /**
     * Returns the length of a given line
     * @param l int representaion of line.  Red == 1, Green == 2
     * @return int value of length in block number
     */
    public int lineLength(int l) {
        if (l == 1) {
            return redLine.size();
        }
        else if (l == 2) {
            return greenLine.size();
        }
        else {
            return -1;
        }
    }

    /**
     * Returns a block object given a line and block number
     * @param l line the block is located on
     * @param b Index of block number for the line
     * @return Block object from private track layout
     */
    public Block getBlock(int l, int b) {
        if (l == 1) {
            return redLine.get(b);
        }
        else if (l == 2) {
            return greenLine.get(b);
        }
        else {
            return null;
        }
    }

}