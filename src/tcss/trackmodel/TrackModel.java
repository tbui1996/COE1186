package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

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

    public void buildTrack(File trackFile){
        track = new Track();
    }
}
