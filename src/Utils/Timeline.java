/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author Utente
 */
public class Timeline {

    private static void MovingObstacles() {
        for (int i = 0; i < ULNN.ambient.shapes.length; i++) {
            ULNN.ambient.shapes[i].setCenterX(ULNN.ambient.shapes[i].getCenterX() + ULNN.ambient.speeds[i][0]);
            ULNN.ambient.shapes[i].setCenterY(ULNN.ambient.shapes[i].getCenterY() + ULNN.ambient.speeds[i][1]);
            if (ULNN.ambient.shapes[i].getCenterX() > ULNN.DIMX || ULNN.ambient.shapes[i].getCenterX() < 0) {
                ULNN.ambient.speeds[i][0] *= -1;
            }
            if (ULNN.ambient.shapes[i].getCenterY() > ULNN.DIMY || ULNN.ambient.shapes[i].getCenterY() < 0) {
                ULNN.ambient.speeds[i][1] *= -1;
            }
//            ULNN.ambient.shapes[i].setRadiusX(
//                    Math.max(Math.min(ULNN.ambient.shapes[i].getRadiusX()+ Math.sin(ULNN.ambient.angles[i]), 50), 5)
//            );
//            ULNN.ambient.shapes[i].setRadiusY(
//                    Math.max(Math.min(ULNN.ambient.shapes[i].getRadiusY() + Math.sin(ULNN.ambient.angles[i]), 50), 5)
//            );
//            ULNN.ambient.angles[i] += Math.random()*0.1;
        }
    }

    private static int dirX = 1, dirY = 1;
    private static double speedX = 0.9, speedY = 0.45;

    private static void moveTarget() {
        ULNN.ambient.target.setCenterX(ULNN.ambient.target.getCenterX() + speedX * dirX);
        ULNN.ambient.target.setCenterY(ULNN.ambient.target.getCenterY() + speedY * dirY);

        if (ULNN.ambient.target.getCenterX() < 250 || ULNN.ambient.target.getCenterX() > ULNN.DIMX - 250) {
            dirX *= -1;
        }
        if (ULNN.ambient.target.getCenterY() < 250 || ULNN.ambient.target.getCenterY() > ULNN.DIMY - 250) {
            dirY *= -1;
        }
    }

    private static void moveCar() {
        ULNN.ambient.car.setPilota(ULNN.algo.popolazione[ULNN.algo.ActualIndex]);
        ULNN.ambient.car.move();

        if (JVector.dist(ULNN.ambient.target.getCenterX(),
                ULNN.ambient.target.getCenterY(),
                ULNN.algo.popolazione[ULNN.algo.ActualIndex].car.car.getCenterX(),
                ULNN.algo.popolazione[ULNN.algo.ActualIndex].car.car.getCenterX(),
                ULNN.algo.popolazione[ULNN.algo.ActualIndex].car.car.getRadiusX(),
                ULNN.ambient.target.getRadiusX()) < 5) {
            ULNN.algo.popolazione[ULNN.algo.ActualIndex].reached++;
        }

        if (System.currentTimeMillis() - ULNN.ambient.car.start > 5000) {
            ULNN.ambient.car.alive = false;
        }
    }

    private static boolean isDead() {
        return !ULNN.ambient.car.alive;
    }

    private static void calculateFitness() {
//        double lifetime = (System.currentTimeMillis() - ULNN.algo.popolazione[ULNN.algo.ActualIndex].car.start) / 1000;
        ULNN.algo.popolazione[ULNN.algo.ActualIndex].fitness = ULNN.algo.popolazione[ULNN.algo.ActualIndex].reached;
    }

    private static void resetCar() {
        calculateFitness();
        System.out.println("   Fitness: " + ULNN.algo.popolazione[ULNN.algo.ActualIndex].fitness);
        System.out.println("   Avg Fit: " + ULNN.algo.getAvgFitness());
        System.out.println("   Act Gen: " + ULNN.algo.gen + "\n");
        ULNN.ambient.car.resetPos();
        //La attivo
        ULNN.ambient.car.alive = true;
        //Passo alla prossima rete
        ULNN.algo.ActualIndex++;
        if (ULNN.algo.ActualIndex == ULNN.algo.popolazione.length) {
            ULNN.algo.nextGen();
            System.out.println("");
        }
        ULNN.algo.popolazione[ULNN.algo.ActualIndex].car.start = System.currentTimeMillis();
    }

    public static void animations() {
        ULNN.ambient.info.setText("Unsupervised Learning Neural Network\n\nCurrent Generation: " + ULNN.algo.gen + "\n"
                + "Average Fitness: " + ULNN.algo.getAvgFitness() + "\n"
                + "Current Population: " + ULNN.algo.popolazione.length + "\n"
                + "Mutation Rate: " + ULNN.algo.MutationRate + "\n"
                + "Actual Member: " + (ULNN.algo.ActualIndex + 1));
        //Muovo gli ostacoli
        MovingObstacles();
        //Muovo il target
        moveTarget();
        //Muovo l'auto con il pilota attuale
        moveCar();
        //Controllo lo stato della macchina
        if (isDead()) {
            resetCar();
        }
    }

}
