package tcss.main;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.floor;

public class SimTime {
    private Timer T;
    private int rate;
    private boolean running;
    static private SimClock clock;

    public SimTime() {
        clock = new SimClock();
    }

    public boolean isRunning() {
        return running;
    }

    public int getRate() {
        return rate;
    }

    public void changeRate(int rate) {
        if(T != null) {
            this.rate = rate;
            T.cancel();
            T = null;
            T = new Timer();
            T.schedule(new TimerTask() {
                @Override
                public void run() {
                    clock.addSec(.2);
                }
            }, 0, 200/this.rate);
        }
    }

    public void pause() {
        T.cancel();
        T = null;
    }

    public void play(int rate) {
        T = new Timer();
        T.schedule(new TimerTask() {
            @Override
            public void run() {
                clock.addSec(.2);
            }
        }, 0, 200 / rate);
    }

    public String getLongTime() {
        return clock.getLongTime();
    }

    public String getShortTime() {
        return clock.getShortTime();
    }
}

class SimClock {
    private int hour;
    private int min;
    private double sec;

    public SimClock() {
        hour = 0;
        min = 0;
        sec = 0;
    }

    public String getLongTime() {
        String s = hour < 10 ? "0" + hour : "" + hour;
        s = min < 10 ? s + ":0" + min : s + ":" + min;
        s = (int)floor(sec) < 10 ? s + ":0" + (int)floor(sec) : s + ":" + (int)floor(sec);
        return s;
    }

    public String getShortTime() {
        String s = hour < 10 ? "0" + hour : "" + hour;
        s = min < 10 ? s + ":0" + min : s + ":" + min;
        return s;
    }

    public double getSec() {
        return sec;
    }

    public int getMin() {
        return min;
    }

    public int getHour() {
        return hour;
    }

    void addSec(double sec) {
        this.sec += sec;
        if(this.sec >= 60) {
            min += this.sec / 60;
            this.sec %= 60;
            if(min >= 60) {
                hour += min/60;
                min %= 60;
                if(hour >= 24) {
                    hour = 0;
                }
            }
        }
        Main.update();
    }
}
