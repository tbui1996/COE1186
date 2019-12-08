package tcss.ctc;

public class Train {

    public String name;
    private double SS;
    private int auth;
    protected int block = -1;

    public Train(String n) {
        this.name = n;
    }

    public Train(String n, int b) {
        this.name = n;
        this.block = b;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }

    public void setBlock(int b) {
        this.block = b;
    }

    public String getBlock() {
        if (block == -1)
            return "N/A";
        else
            return Integer.toString(this.block);
    }

    public int getBlockInt() {
        return this.block;
    }



}
