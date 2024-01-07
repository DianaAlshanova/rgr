package com.example.presentation;

import javafx.beans.value.ChangeListener;

import java.util.Random;
import java.util.stream.IntStream;

import static com.example.presentation.ControllerUtils.*;

public class DotController {
    private final long dt = 20;
    private final Dot[] dots;
    private final Dot mainDot;
    private final Thread thread;

    public DotController(int dotsCount, double maxX, double maxY) {
        var random = new Random();
        this.dots = new Dot[dotsCount + 1];
        for (int i = 0; i < dotsCount; i++) {
            var cords = new Dot.Coords(random.nextDouble(maxX), random.nextDouble(maxY));
            dots[i] = new Dot(cords, Dots.DEFAULT_BODY_MASS);
        }
        var cords = new Dot.Coords(random.nextDouble(maxX), random.nextDouble(maxY));
        mainDot = new Dot(cords, Dots.DEFAULT_BODY_MASS);
        dots[dots.length - 1] = mainDot;
        thread.setDaemon(true);
        thread.start();
    }

    {thread = new Thread(() -> {
            long lastUpdate = -1;
            while (true) {
                if (System.currentTimeMillis() - lastUpdate >= dt) {
                    recalcBodiesForces();
                    moveMainBody();
                    lastUpdate = System.currentTimeMillis();
                }
            }
        });
    }

    private void recalcBodiesForces() {
        double distance;
        double magnitude;
        Dot.Coords direction;

        final int n = dots.length;
        for (int k = 0; k < n - 1; k++) {
            for (int l = k + 1; l < n; l++) {
                distance = distance(dots[k], dots[l]);
                magnitude = (distance < 1e2) ? 0.0 : magnitude(dots[k], dots[l], distance);
                direction = direction(dots[k], dots[l]);

                dots[k].setF(new Dot.Coords(
                        dots[k].getF().x() + magnitude * direction.x() / distance,
                        dots[k].getF().y() + magnitude * direction.y() / distance
                ));

                dots[l].setF(new Dot.Coords(
                        dots[l].getF().x() - magnitude * direction.x() / distance,
                        dots[l].getF().y() - magnitude * direction.y() / distance
                ));
            }
        }
    }

    private void moveMainBody() {
        Dot.Coords dv; // dv = f/m * dt
        Dot.Coords dp; // dp = (v + dv/2) * dt

        dv = dv(mainDot, dt);
        dp = dp(mainDot, dt, dv);

        mainDot.setV(new Dot.Coords(
                mainDot.getV().x() + dv.x(),
                mainDot.getV().y() + dv.y()
        ));

        mainDot.setP(new Dot.Coords(
                mainDot.getP().x() + dp.x(),
                mainDot.getP().y() + dp.y()
        ));

        mainDot.setF(new Dot.Coords(0.0, 0.0));
    }

    public void addOnMainDotPositionListener(ChangeListener<Dot.Coords> listener) {
        mainDot.pProperty().addListener(listener);
    }

    public void addOnMainDotVelocityListener(ChangeListener<Dot.Coords> listener) {
        mainDot.vProperty().addListener(listener);
    }
    public Dot[] getDots() {
        return dots;
    }
}