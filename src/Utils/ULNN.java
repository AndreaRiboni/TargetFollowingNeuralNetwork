/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Smart.GeneticAlgorithm;
import Smart.NeuralNetwork;
import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Utente
 */
public class ULNN extends Application {
    public static Ambient ambient;
    public static final int DIMX = 600, DIMY = 600;
    public static GeneticAlgorithm algo;
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(ambient.obstacles, 600, 600);
        
        primaryStage.setTitle("ULNN - Autonomous Thing");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        AnimationTimer gameloop = new AnimationTimer(){
            @Override
            public void handle(long now) {
                Timeline.animations();
            }
            
        };
        gameloop.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ambient = new Ambient(0, DIMX, DIMY, 20);
//        algo = new GeneticAlgorithm(Manager.getWeightsFromFiles());
        algo = new GeneticAlgorithm();
        Manager manager = new Manager(algo);
        manager.start();
        launch(args);
    }
    
}
