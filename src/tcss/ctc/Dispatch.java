package tcss.ctc;

public class Dispatch {
    private double SS;
    private int auth;
    private Train train;

    public Dispatch() {
        this.SS = 0;
        this.auth = 0;
    }

    public Dispatch(float SS, int auth, String trainName) {
        this.SS = SS;
        this.auth = auth;
        train.name = trainName;
    }

    public void setSS(float SS) {
        this.SS = SS;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public void sendNextStop() {

    }
}
