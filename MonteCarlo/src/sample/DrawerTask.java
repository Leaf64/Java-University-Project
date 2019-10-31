package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.Random;

public class DrawerTask extends Task {

    private Random random;
    private Equation equation;

    final double RANGE_MIN = -8;
    final double RANGE_MAX = 8;

    double area = 0;
    int n = 10000000;
    private int delay = 0;

    private NewPointListener pointListener;

    public void setPointListener(NewPointListener pointListener) {
        this.pointListener = pointListener;
    }


    public DrawerTask(int n) {
        random = new Random();
        equation = new Equation();
        this.n = n;
    }


    @Override
    protected Object call() throws Exception {

        int k = 0;
        int i = 0;
        //for (int i = 0; i < n; ++i) {
        while (i < n) {
            if (isCancelled()) {
                break;
            }
            double x = RANGE_MIN + (RANGE_MAX - RANGE_MIN) * random.nextDouble();
            double y = RANGE_MIN + (RANGE_MAX - RANGE_MIN) * random.nextDouble();
            if (equation.calc(x, y)) {

                //Platform.runLater(new Runnable() {
                //   @Override
                //  public void run() {
                //if (pointListener != null) {
                pointListener.onPointCalculated(new NewPointEvent(this, x, y, true));
                //}
                //  }

                // });

                updateProgress(i, n);
                k++;
            } else {
                // Platform.runLater(new Runnable() {
                //   @Override
                //   public void run() {
                // if (pointListener != null) {
                pointListener.onPointCalculated(new NewPointEvent(this, x, y, false));
                //}
                // }
                // });
                updateProgress(i, n);
            }
            i++;
        }
        area = 16.0 * 16.0 * (double) k / (double) n;       // 16x16 to oryginalna przestrzen
        System.out.println(area);
        return area;
    }
}
