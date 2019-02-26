/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Smart;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import Utils.JVector;
import Utils.ULNN;

/**
 *
 * @author Utente
 */
public class Car {
    //Posizione e dimensione
    protected double posx, posy, dim;
    /**
     * Forma geometrica della macchina.
     * I movimenti verranno visualizzati mediante questo ellisse.
     */
    public Ellipse car;
    //Ostacoli presenti nella finestra
    private final ArrayList<Ellipse> obstacles;
    /**
     * Rete neurale pilota di questa macchina
     */
    protected NeuralNetwork pilot;
    //TIMER
    public double start;
    /**
     * Indica lo stato della macchina.
     * Non appena l'auto andrà a collidere con un ostacolo,
     * il valore verrà cambiato in FALSE.
     */
    public boolean alive = true;
    
    /**
     * Costruttore.
     * @param obst Ostacoli in campo
     */
    public Car(ArrayList<Ellipse> obst){
        obstacles = obst;
        dim = 10;
        start = System.currentTimeMillis();
        car = new Ellipse(dim, dim);
        resetPos();
        car.setFill(Color.BROWN);
        pilot = new NeuralNetwork(this);
    }
    
    /**
     * Imposto i nuovi input al pilota e applico il risultato ottenuto.
     * Controllo lo stato della macchina.
     */
    public void move(){
        if(alive){
//            pilot.setInputs(obstacles);
            pilot.setInputs(ULNN.ambient.target);
            pilot.feedforward();
            for(Ellipse e : obstacles){
                if(JVector.intersect(e.getCenterX(), e.getCenterY(), posx, posy, e.getRadiusX(), dim)) {
                    alive = false;
                }
            }
        }
    }
    
    /**
     * Imposto una nuova rete come pilota
     * @param pilot 
     */
    public void setPilota(NeuralNetwork pilot){
        this.pilot = pilot;
    }
    
    /**
     * Scelgo una posizione casuale per la macchina, evitando una
     * sovrapposizione con un ostacolo
     */
    public void resetPos(){
        boolean placed = false;
        do{
//           System.out.println("Testo Intersezioni");
           placed = true;
//           posx = Math.random()*ULNN.DIMX;
//           posy = Math.random()*ULNN.DIMY;
//           for(Ellipse e : obstacles){
//               if(JVector.intersect(posx, posy, e.getCenterX(), e.getCenterY(), dim, e.getRadiusX())){
//                   placed = false;
//                   break;
//               }
//           }
          posx = ULNN.DIMX / 2;
          posy = ULNN.DIMY / 2;
        } while(!placed);
        car.setCenterX(posx);
        car.setCenterY(posy);
    }
}
