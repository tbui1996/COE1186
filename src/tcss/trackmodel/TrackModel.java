package tcss.trackmodel;

import java.io.File;

public class TrackModel {

    private Track track;
    private File trackFile;

    public TrackModel(){
        init();
    }

    public void init(){
        trackFile = null;
        buildTrack(trackFile);
    }

    public Track getTrack(){
        return track;
    }

    public void buildTrack(File trackFile){
        track = new Track();
    }
}
