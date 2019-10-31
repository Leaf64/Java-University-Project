package sample;

import java.util.EventObject;


public class NewPointEvent extends EventObject {
    private double x;
    private double y;
    private boolean isInside;


    public NewPointEvent(Object source, double x, double y, boolean inside) {
        super(source);
        this.x = x;
        this.y = y;
        this.isInside = inside;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isInside() {
        return isInside;
    }
}
