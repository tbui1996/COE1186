package tcss.ctc;

public class Dispatch {
    private double SS;
    private int auth;

    public Dispatch() {
        this.SS = 0;
        this.auth = 0;
    }

    public Dispatch(float SS, int auth) {
        this.SS = SS;
        this.auth = auth;
    }

    public void setSS(float SS) {
        this.SS = SS;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public void sendInfo() {

    }
}
