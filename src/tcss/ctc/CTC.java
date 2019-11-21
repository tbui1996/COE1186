package tcss.ctc;

import tcss.trackcontroller.TrackController;
import tcss.trackmodel.Block;
import tcss.trackmodel.Station;
import tcss.trackmodel.Track;
import tcss.trainmodel.TrainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CTC {
    TrackController TC1;
    private ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    public ArrayList<TrainModel> trainList = new ArrayList<TrainModel>(); /*Number of Trains*/

    //Temporary Red and Green Line setup for creating a Dispatch
    private HashMap<Integer, Block> redLine;
    private HashMap<Integer, Block> greenLine;
    private String [] stationNames; //This will be deleted

    //What I might need
    //This would be Station name to block number
    private HashMap<String,Integer> stationToBlockNumRed;
    private HashMap<String,Integer> stationToBlockNumGreen;

    //String ArrayLists of all of the stations on each line
    private String [] redStations;
    private String [] greenStations;





    public CTC() {

//        this.TC1 = track;

        //Temporary Red and Green Line setup for creating a Dispatch
        //redLine = tcss.main.Main.TrackModel.getRedLine().getBlockHashMap;     Red Line Hash Map
        //greenLine = tcss.main.Main.TrackModel.getRedLine();                   Green Line Hash Map

        /*

        //Loads in block hashmaps with new instances of blocks with the same values
        Iterator redIterator = redLine.entrySet().iterator();
        Iterator greenIterator = greenLine.entrySet().iterator();

        while (redIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)redIterator.next();
            redLine.setValue(mapElement.getKey()) = new Block(mapElement.getValue());
            updates station to block number hash map
            if (mapElement.getValue().getStation != null) {

            }
        }

        while (greenIterator.hasNext()) {
           Map.Entry mapElement = (Map.Entry)greenIterator.next();
           greenLine.setValue(mapElement.getKey()) = new Block(mapElement.getValue());
        }
        */

        redLine = new HashMap<Integer, Block>();
        greenLine = new HashMap<Integer, Block>();

        stationNames = new String[5];
        stationNames[0] = "Dormont";
        stationNames[1] = "Shadyside";
        stationNames[2] = "First Ave";
        stationNames[3] = "South Hills Junction";
        stationNames[4] = "Swissvale";

        //Initialize Station lists for each line
        redStations = new String[3];
        greenStations = new String[3];

        //Get rid of these.  Used to prevent ArrayIndexOutOfBounds for redStations/greenStations
        int redCount = 0;
        int greenCount = 0;

        for (int i=0; i < 5; i++) {
            Block temp = new Block();
            temp.setStation(new Station(stationNames[i]));
            if (i%2 == 0) {
                redLine.put(i,temp);
                greenLine.put(i,new Block());
                //Populates Station to Block Number hash map
                //stationToBlockNumRed()
                redStations[redCount] = stationNames[i]; //Adding the station name on the block the the Array
                redCount++;
                //stationToBlockNumGreen()
            }
            else {
                greenLine.put(i,temp);
                redLine.put(i,new Block());
                greenStations[greenCount] = stationNames[i];
                greenCount++;
            }
        }

    }

    public void createDispatch(String name, float SS, int auth, TrainModel train) {
        //TrainModel temp = new TrainModel(SS, auth, trainList.size(), 55);
        //this.trainList.add(new TrainModel(name, trainList.size()));
        //Start here and Fix this
        for (int i = 0; i < redLine.size(); i++) {
            System.out.println("Red Block " + i + ": " + redLine.get(i).toString());
            System.out.println("Green Block " + i + ": " + greenLine.get(i).toString());
        }
        this.trainList.add(train);
        this.dispatchList.add(new Dispatch(SS, auth, this.trainList.get(this.trainList.size()-1)));
        //this.dispatchList.get(this.dispatchList.size()-1).createSchedule();
        this.dispatchList.get(this.dispatchList.size()-1).setRequests();
        System.out.println(this.dispatchList.get(this.dispatchList.size()-1));
        sendNextStop(SS, auth, train.getID());
    }

    public void addDispatch(Dispatch d) {
        this.dispatchList.add(d);
    }

    //This checks dispatch list to see if a new suggested speed and authority need to be sent
    public void checkDispatchList () {
        for (int i = 0; i < dispatchList.size(); i++) {
            Dispatch temp = dispatchList.get(i);
            //if train is not dispatched yet
            if (temp.getCurrStop() == -1 && temp.getSS() == 0) {
                //tcss.main.Main.T.getHour()
                //tcss.main.Main.T.getMin()
                /*if (tcss.main.Main.T.getHour() >= temp.getDepartureHour()) {
                    //If the current hour is passed the departure hour or the current hour is the departure hour and the current minute is greater than or equal to the departure minute
                    if (tcss.main.Main.t.getHour() > temp.getDepartureHour() || tcss.main.Main.getMin() >= temp.getDepartureMin()) {
                        temp.setSS(temp.getSpeed(temp.getCurrStop()+1));
                        temp.setAuth(temp.getAuth(temp.getCurrStop()+1));
                        System.out.println("Train sent");
                        //Sends SS and Auth to new
                        tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop()+1),temp.getAuth(temp.getCurrStop()+1),YARD);
                    }
                }*/
                if (11 >= temp.getDepartureHour()) { //This should be if departure time == current global time
                    temp.setSS(temp.getSpeed(temp.getCurrStop()+1));
                    temp.setAuth(temp.getAuth(temp.getCurrStop()+1));
                    System.out.println("Train sent");
                    //Sends SS and Auth to new
                    //tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop()+1),temp.getAuth(temp.getCurrStop()+1),YARD);
                }
            }
            //If train is already dispatched
            else {
                //Check block occupancy list to see if next stop block is currently occupied.  If so, a new request must be sent to keep train moving
                /*if (temp.getLine() == 1) {
                    if (redLineBlocks(stationToBlockNum(temp.schedule.getStopName(temp.getCurrStop()+1))).isOccupied()) {
                        temp.setCurrStop(temp.getCurrStop()+1);
                    }
                else {
                    if (greenLineBlocks(stationToBlockNum(temp.schedule.getStopName(temp.getCurrStop()+1))).isOccupied()) {
                        temp.setCurrStop(temp.getCurrStop()+1);
                    }
                }*/
                //stationToBlock.get(temp.schedule.stopList.get(temp.getCurrStop()+1)
            }
        }
    }

    public void updateTrackState() {
        for (int j = 0; j < (redLine.size() + greenLine.size()); j++) {
            //updating red line
            if (j < redLine.size()) {
                //redLine.get(j).setOccupancy(tcss.main.Main.tc.getOccupancy(j));
                //redLine.get(j).setSwitchPosition(tcss.main.Main.tc.getSwitchPosition(j));
                //redLine.get(j).setLightState(tcss.main.Main.tc.getSwitchPosition(j));
            }

            //updating green line
            else {
                //greenLine.get(j - redLine.size()).setOccupancy(tcss.main.Main.tc.getOccupancy(j));
                //greenLine.get(j - redLine.size()).setSwitchPosition(tcss.main.Main.tc.getSwitchPosition(j));
                //greenLine.get(j - redLine.size()).setLightState(tcss.main.Main.tc.getLightState(j));
            }
        }
    }

    public TrackController getTrackController(int index){
        return this.TC1;
    }

    public void sendNextStop(float SS, int auth, int ID) {
        this.TC1.getNextStop(SS, auth, ID);
    }

    public Dispatch getDispatch(int i) {
        return dispatchList.get(i);
    }

    public String getDispatchString(int i) {
        return dispatchList.get(i).toString();
    }

    public int numDispatches() {
        return this.dispatchList.size();
    }

    //Return a String array of all stops in a line
    public String [] getAllStops(int l) {
        if (l == 1) {
            return redStations;
        }
        else {
            return greenStations;
        }
    }

    public int lineStringToInt(String line) {
        if (line.equals("RED"))
            return 1;
        else if (line.equals("GREEN"))
            return 2;
        else
            return 0;
    }

    //returns total block number in the line
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