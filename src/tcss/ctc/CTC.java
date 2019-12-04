package tcss.ctc;

import tcss.trackcontroller.TrackController;
import tcss.trackmodel.Block;
import tcss.trackmodel.Station;
import tcss.trackmodel.Track;
import tcss.trackmodel.TrackModel;
import tcss.trainmodel.TrainModel;

import java.util.*;


public class CTC {
    private ArrayList<Dispatch> dispatchList = new ArrayList<Dispatch>(); /*Number of Trains*/
    public ArrayList<TrainModel> trainList = new ArrayList<TrainModel>(); /*Number of Trains*/

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





    public CTC() throws Exception {

        redLine = new HashMap<>();
        greenLine = new HashMap<>();

        //Temporary Red and Green Line setup for creating a Dispatch
        redLine = tcss.main.Main.tm.getRedLine().getBlockHashMap();     //Red Line Hash Map
        greenLine = tcss.main.Main.tm.getGreenLine().getBlockHashMap();                   //Green Line Hash Map
        System.out.println(redLine);

        //Private Track Model
        privateTrack = new TrackModel();
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
                if (!greenStations.contains(greenLine.get(mapElement.getKey()).getStation().getName())){
                    stationToBlockNumGreen.put(greenLine.get(mapElement.getKey()).getStation().getName(), mapElement.getKey());
                    greenStations.add(greenLine.get(mapElement.getKey()).getStation().getName());
                }
            }
        }

        System.out.println("Red Stations: \n" + redStations.size());
        System.out.println("Green Stations: \n" + greenStations.size());
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
                if (tcss.main.Main.getSimTime().getHour() >= temp.getDepartureHour()) {
                    //If the current hour is passed the departure hour or the current hour is the departure hour and the current minute is greater than or equal to the departure minute
                    if (tcss.main.Main.getSimTime().getHour() > temp.getDepartureHour() || tcss.main.Main.getSimTime().getMin() >= temp.getDepartureMin()) {
                        temp.setSS(temp.getSpeed(temp.getCurrStop() + 1));
                        temp.setAuth(temp.getAuth(temp.getCurrStop() + 1));
                        System.out.println("Train sent");
                        //Sends SS and Auth to new
                        if (temp.getLine() == 1)
                            tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop()+1),temp.getAuth(temp.getCurrStop()+1),1,9);
                        else
                            tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop()+1),temp.getAuth(temp.getCurrStop()+1),0,63);
                    }
                }
                /*
                if (11 >= temp.getDepartureHour()) { //This should be if departure time == current global time
                    temp.setSS(temp.getSpeed(temp.getCurrStop()+1));
                    temp.setAuth(temp.getAuth(temp.getCurrStop()+1));
                    System.out.println("Train sent");
                    //Sends SS and Auth to new
                    tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop()+1),temp.getAuth(temp.getCurrStop()+1),YARD)
                }*/
            }
            //If train is already dispatched
            else {
                //Check block occupancy list to see if next stop block is currently occupied.  If so, a new request must be sent to keep train moving
                if (temp.getLine() == 1) {
                    if (redLine.get(stationToBlockNumRed.get(temp.schedule.getStopName(temp.getCurrStop() + 1))).isOccupied()) {
                        temp.setCurrStop(temp.getCurrStop() + 1);
                        tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop() + 1), temp.getAuth(temp.getCurrStop() + 1), temp.lineToTc(), stationToBlockNumRed.get(redStations.get(temp.getCurrStop() + 1)));
                    }
                } else {
                        if (greenLine.get(stationToBlockNumGreen.get(temp.schedule.getStopName(temp.getCurrStop() + 1))).isOccupied()) {
                            temp.setCurrStop(temp.getCurrStop() + 1);
                            tcss.main.Main.tc.getNextStop(temp.getSpeed(temp.getCurrStop()+1),temp.getAuth(temp.getCurrStop()+1), temp.lineToTc(), stationToBlockNumGreen.get(greenStations.get(temp.getCurrStop()+1)));
                        }
                }
                    //stationToBlock.get(temp.schedule.stopList.get(temp.getCurrStop()+1)
            }
        }
    }

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
    }

    public void updateThroughput() {
        if (tcss.main.Main.getSimTime().getSec() % 3 == 0) {
            redTicketTotal += tcss.main.Main.tm.updateThroughput(0);
            greenTicketTotal += tcss.main.Main.tm.updateThroughput(1);
            System.out.println(redTicketTotal);
            System.out.println(greenTicketTotal);
        }
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