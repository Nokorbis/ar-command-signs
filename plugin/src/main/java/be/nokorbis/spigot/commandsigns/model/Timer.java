package be.nokorbis.spigot.commandsigns.model;

public class Timer implements Cloneable{
    private int duration; //in seconds
    private boolean reset = false;
    private boolean cancel = false;

    public Timer() {
    }

    public Timer(int duration, boolean reset, boolean cancel) {
        this.duration = duration;
        this.reset = reset;
        this.cancel = cancel;
    }

    public Timer(Timer t) {
       this(t.duration, t.reset, t.cancel);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public Timer copy() {
        return new Timer(this);
    }

    @Override
    protected Timer clone() throws CloneNotSupportedException {
        return (Timer) super.clone();
    }
}
