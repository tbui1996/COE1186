package tcss.ctc;

public class Maintenance {

    int hour;
    int min;
    int line;
    int block;
    boolean active;


    public Maintenance() {
        this.hour = 0;
        this.min = 0;
        this.line = 0;
        this.block = 0;
        this.active = false;
    }

    public Maintenance(int h, int m, int l, int b) {
        this.hour = h;
        this.min = m;
        this.line = l;
        this.block = b;
        this.active = false;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMin() {
        return this.min;
    }

    public int getLine() {
        return this.line;
    }

    public int getBlock() {
        return this.block;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean a) {
        this.active = a;
    }
}
