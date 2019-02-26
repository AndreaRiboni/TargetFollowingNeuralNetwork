/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Smart;

import java.util.ArrayList;
import javafx.scene.shape.Ellipse;

/**
 * La rete neurale in sè.
 * @author Andrea
 */
public class NeuralNetwork {
    //Layer (3)
    private final double[] InputLayer, HiddenLayer, OutputLayer, bias;
    //Pesi Input-Hidden e Hidden-Output
    private double[][] InHid, HidOut;
    /**
     * Macchina pilotata dalla rete
     */
    public final Car car;
    /**
     * Fitness della rete
     */
    public double fitness;
    public double reached;
    
    /**
     * Tangente Iperbolica. Funzione di attivazione.
     * @param x valore
     * @return tanh(x)
     */
    private double HyperbolicTangent(double x) {
        return Math.tanh(x / 5);
    }

    /**
     * Costruttore.
     * @param car Macchina relativa
     */
    public NeuralNetwork(Car car) {
        //Inizializzo
        InputLayer = new double[2];
        HiddenLayer = new double[5];
        OutputLayer = new double[2];
        bias = new double[2];
        InHid = new double[InputLayer.length][HiddenLayer.length];
        HidOut = new double[HiddenLayer.length][OutputLayer.length];
        //Inizializzo con pesi [-1, +1]
        for (double[] InHid1 : InHid) {
            for (int o = 0; o < InHid1.length; o++) {
                InHid1[o] = Math.random() * 2 - 1;
            }
        }
        for (double[] HidOut1 : HidOut) {
            for (int o = 0; o < HidOut1.length; o++) {
                HidOut1[o] = Math.random() * 2 - 1;
            }
        }
        bias[0] = Math.random() * 2 - 1;
        bias[1] = Math.random() * 2 - 1;
        this.car = car;
    }

//    /**
//     * Considero tutti gli ostacoli presenti nella finestra.
//     * Classifico ogni ostacolo mediante le due distanze (X e Y).
//     * Scelgo le 2 X minori e le 2 Y minori.
//     * Questi 4 valori andranno a costituire gli input della rete
//     * @param obstacles ostacoli presenti (solo cerchi)
//     */
//    public void setInputs(ArrayList<Ellipse> obstacles) {
//        double[] min = {10000, 10000, 10000, 10000};
//        /*Guardo ogni ellisse:
//            min[0] è la distanza dell'ellisse più vicino in alto
//            min[1] è la distanza dell'ellisse più vicino in basso
//            min[2] è la distanza dell'ellisse più vicino a sinistra
//            min[3] è la distanza dell'ellisse più vicino a destra
//         */
//        for (int i = 0; i < obstacles.size(); i++) {
//            double distY = Math.abs(obstacles.get(i).getCenterY() - car.posy) - car.dim - obstacles.get(i).getRadiusX();
//            int indexY = obstacles.get(i).getCenterY() >= car.posy ? 3 : 2;
//            if (distY < min[indexY]) {
//                min[indexY] = distY;
//            }
//            double distX = Math.abs(obstacles.get(i).getCenterX() - car.posx) - car.dim - obstacles.get(i).getRadiusX();
//            int indexX = obstacles.get(i).getCenterX() >= car.posy ? 1 : 0;
//            if (distX < min[indexX]) {
//                min[indexX] = distX;
//            }
//        }
//        for (int i = 0; i < 4; i++) {
//            InputLayer[i] = min[i];
//        }
//    }
    
    /**
     * Imposta gli input nella rete
     * @param target ellisse da raggiungere
     */
    public void setInputs(Ellipse target){
        InputLayer[0] = target.getCenterX() - car.car.getCenterX();
        InputLayer[1] = target.getCenterY() - car.car.getCenterY();
    }

    /**
     * Propagazione dell'input in avanti nella rete.
     * Imposto già la nuova posizione della macchina secondo gli offset ottenuti:
     * I valori due neuroni in output (uno per X e uno per Y) andranno
     * direttamente sommati alla posizione della macchina.
     */
    public void feedforward() {
        //Viaggio nell'hidden layer
        for (int i = 0; i < HiddenLayer.length; i++) {
            double sum = 0;
            for (int o = 0; o < InputLayer.length; o++) {
                sum += InputLayer[o] * InHid[o][i];
            }
            HiddenLayer[i] = HyperbolicTangent(sum + bias[0]);
        }
        //Viaggio nell'output layer
        for (int i = 0; i < OutputLayer.length; i++) {
            double sum = 0;
            for (int o = 0; o < HiddenLayer.length; o++) {
                sum += HiddenLayer[o] * HidOut[o][i];
            }
            OutputLayer[i] = HyperbolicTangent(sum + bias[1]) * 5;
        }
//        System.out.println("  X += " + OutputLayer[0]);
//        System.out.println("  Y += " + OutputLayer[1]);
        car.posx = car.car.getCenterX() + OutputLayer[0];
        car.car.setCenterX(car.posx);
        car.posy = car.car.getCenterY() + OutputLayer[1];
        car.car.setCenterY(car.posy);
    }

    /**
     * Salva tutti i pesi in un singolo array
     * @return Array contenente i pesi
     */
    public double[] weightsToArray() {
        int dim = InHid.length * InHid[0].length + HidOut.length * HidOut[0].length + bias.length;
        double[] weights = new double[dim];
        int cont = 0;
        for (double[] InHid1 : InHid) {
            for (int o = 0; o < InHid1.length; o++) {
                weights[cont++] = InHid1[o];
            }
        }
        for (double[] HidOut1 : HidOut) {
            for (int o = 0; o < HidOut1.length; o++) {
                weights[cont++] = HidOut1[o];
            }
        }
        for (double d : bias) {
            weights[cont++] = d;
        }
        return weights;
    }

    /**
     * Dato un array di pesi congruente alla rete, imposta i valori
     * settandoli come pesi per i vari layer della rete.
     * @param weights array di pesi conforme alla rete
     */
    public void arrayToWeights(double[] weights) {
        int cont = 0;
        for (int i = 0; i < InHid.length; i++) {
            for (int o = 0; o < InHid[i].length; o++) {
                InHid[i][o] = weights[cont++];
            }
        }
        for (int i = 0; i < HidOut.length; i++) {
            for (int o = 0; o < HidOut[i].length; o++) {
                HidOut[i][o] = weights[cont++];
            }
        }
        for (int i = 0; i < bias.length; i++) {
            bias[i] = weights[cont++];
        }
    }
}
