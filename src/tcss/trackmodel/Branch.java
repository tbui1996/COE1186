package tcss.trackmodel;

import java.util.ArrayList;

public class Branch {

    private double totalLength;
    private int start;
    private int end;
    private ArrayList<Branch> head;
    private ArrayList<Branch> tail;
    private Track track;

    public Branch(int start, int end, Track track){

        setStart(start);
        setEnd(end);

        double sum = 0;

        for(int i=start;i<=end;i++){
            sum += track.getBlock(i).getLength();
        }

        setTotalLength(sum);

        setHead(new ArrayList<Branch>());
        setTail(new ArrayList<Branch>());

        setTrack(track);
    }

    public double getDistance(int start, int end){

        double dist = 0.0;
        if(end > start){
            for(int i=start;i<end;i++){
                dist += track.getBlock(i).getLength();
            }
        }else{
            for(int i=start;i>end;i--){
                dist += track.getBlock(i).getLength();
            }
        }

        return dist;
    }

    //****************** GETTERS **********************************

    public double getTotalLength(){
        return totalLength;
    }

    public int getStart(){
        return start;
    }

    public int getEnd() {
        return end;
    }

    public ArrayList<Branch> getHead(){
        return head;
    }

    public ArrayList<Branch> getTail(){
        return tail;
    }

    public Track getTrack(){
        return track;
    }

    //****************** SETTERS **********************************

    public void setTotalLength(double totalLength){
        this.totalLength = totalLength;
    }

    public void setStart(int start){
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setHead(ArrayList<Branch> head) {
        this.head = head;
    }

    public void setTail(ArrayList<Branch> tail) {
        this.tail = tail;
    }

    public void setTrack(Track track){
        this.track = track;
    }
}
