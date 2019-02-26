/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Smart.GeneticAlgorithm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Utente
 */
public class Manager extends Thread {

    private BufferedReader in;
    private GeneticAlgorithm ga;
    private BufferedWriter[] bw;

    public Manager(GeneticAlgorithm ga) {
        this.ga = ga;
        in = new BufferedReader(new InputStreamReader(System.in));
        bw = new BufferedWriter[ga.popolazione.length];
        
    }

    public void run() {
        for (int i = 0; i < bw.length; i++) {
            try {
                bw[i] = new BufferedWriter(new FileWriter("NEURAL" + i));
            } catch (IOException ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while (true) {
            try {
                if (in.readLine().equals("save")) {
                    for (int i = 0; i < bw.length; i++) {
                        double[] weights = ga.popolazione[i].weightsToArray();
                        bw[i].write("");
                        for (int o = 0; o < weights.length; o++) {
                            bw[i].append(weights[o] + "");
                            bw[i].newLine();
                        }
                        bw[i].close();
                    }
                    System.out.println("The population is saved.");
                }
            } catch (IOException e) {
            }
        }
    }
    
    public static double[][] getWeightsFromFiles() throws IOException{
        BufferedReader br = null;
        double[][] EveryWeights = new double[500][1];
        for(int i = 0; i < 500; i++){
            br = new BufferedReader(new FileReader("NEURAL"+(i)));
            String s;
            ArrayList<Double> weights = new ArrayList<>();
            while((s = br.readLine()) != null){
                weights.add(Double.parseDouble(s));
            }
            double[] ThisWeights = new double[weights.size()];
            for(int o = 0; o < ThisWeights.length; o++){
                ThisWeights[o] = weights.get(o);
            }
            EveryWeights[i] = ThisWeights;
        }
        return EveryWeights;
    }
}
