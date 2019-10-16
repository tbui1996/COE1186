package tcss.ctc;

public class Train {

    public String name;
    private double SS;
    private int auth;
    private int ID;

    public Train(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

}
