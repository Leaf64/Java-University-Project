package sample;


import java.util.EventListener;


public interface NewPointListener extends EventListener {
    void onPointCalculated(NewPointEvent event);
}
