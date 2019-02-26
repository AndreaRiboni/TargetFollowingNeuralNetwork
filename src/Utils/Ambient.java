/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Smart.Car;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author Utente
 */
public class Ambient {

    /**
     * Ostacoli e target
     */
    protected Group obstacles;
    public Ellipse target;
    protected final Ellipse[] shapes;
    protected final double[][] speeds;
    public Car car;
    public Label info;
    
    public Ambient(int obst_num, int WinX, int WinY, int maxdim) {
        ArrayList<Ellipse> totalObst = new ArrayList<>();
        obstacles = new Group();
        info = new Label();
            obstacles.getChildren().add(info);
        shapes = new Ellipse[obst_num];
        speeds = new double[obst_num][2];
        target = new Ellipse(WinX/2, WinY/2, 25, 25);
            target.setFill(Color.GREEN);
            target.setOnMouseDragged(e -> { //Migliorarne la precisione
                target.setCenterX(e.getX());
                target.setCenterY(e.getY());
            });
            obstacles.getChildren().add(target);
            
        //Ostacoli casuali
        for (int i = 0; i < shapes.length; i++) {
            double dim = Math.max(Math.random() * maxdim, 10);
            double posx = 0, posy = 0;
            shapes[i] = new Ellipse(Math.random() * WinX, Math.random() * WinY, dim, dim);
            shapes[i].setFill(Color.CORNFLOWERBLUE);
            obstacles.getChildren().add(shapes[i]);
            totalObst.add(shapes[i]);
            speeds[i][0] = Math.random() * 0.9 - 0.45;
            speeds[i][1] = Math.random() * 0.9 - 0.45;

            boolean placed = false;
            do {
//           System.out.println("Testo Intersezioni");
                placed = true;
                posx = Math.random() * ULNN.DIMX;
                posy = Math.random() * ULNN.DIMY;
                for (int j = i; j >= 0; j--) {
                    if (JVector.intersect(posx, posy, shapes[i].getCenterX(), shapes[i].getCenterY(), dim, shapes[i].getRadiusX())) {
                        placed = false;
                        break;
                    }
                }
            } while (!placed);
            shapes[i].setCenterX(posx);
            shapes[i].setCenterY(posy);
        }

        //Ostacolo personale
//        Ellipse personal = new Ellipse(WinX / 2, WinY / 2, 40, 40);
//        personal.setFill(Color.DARKCYAN);
//        personal.setOnMouseDragged(e -> { //Migliorarne la precisione
//            personal.setCenterX(e.getX());
//            personal.setCenterY(e.getY());
//        });
//        obstacles.getChildren().add(personal);
//        totalObst.add(personal);

        //Contorno up down
        int stroke = WinX / 15;
        for (int i = 0; i < stroke / 2; i++) {
            Ellipse up = new Ellipse(15 * i * 2 + 15, 0, 15, 15);
            up.setFill(Color.CORNFLOWERBLUE);
//            obstacles.getChildren().add(up);
            totalObst.add(up);
            Ellipse down = new Ellipse(15 * i * 2 + 15, WinY, 15, 15);
            down.setFill(Color.CORNFLOWERBLUE);
//            obstacles.getChildren().add(down);
            totalObst.add(down);
        }
        //Contorno right left
        stroke = WinY / 15;
        for (int i = 0; i < stroke / 2; i++) {
            Ellipse up = new Ellipse(0, 15 * i * 2 + 15, 15, 15);
            up.setFill(Color.CORNFLOWERBLUE);
//            obstacles.getChildren().add(up);
            totalObst.add(up);
            Ellipse down = new Ellipse(WinY, 15 * i * 2 + 15, 15, 15);
            down.setFill(Color.CORNFLOWERBLUE);
//            obstacles.getChildren().add(down);
            totalObst.add(down);
        }
        //NO OSTACOLI AI LATI PER ORA
        totalObst.clear();
        car = new Car(totalObst);
        obstacles.getChildren().add(car.car);
    }
}
