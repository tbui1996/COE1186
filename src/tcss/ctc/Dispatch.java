package tcss.ctc;

import tcss.trainmodel.TrainModel;

public class Dispatch {
    private double SS;
    private int auth;
    private TrainModel train;

    public Dispatch() {
        this.SS = 0;
        this.auth = 0;
    }

    public Dispatch(float SS, int auth, TrainModel train) {
        this.SS = SS;
        this.auth = auth;
        this.train = train;
    }

    public void setSS(float SS) {
        this.SS = SS;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public void sendNextStop() {

    }

    public String toString() {
        return "ID: " + this.train.getID() + "\nSuggested Speed: " + this.train.getSSpeed() + "\nAuthority: " + this.train.getAuthority();
    }
}
