package Smart;

import Utils.ULNN;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Utente
 */
public class GeneticAlgorithm {
    //Tasso di mutazione
    public final double MutationRate = 0.015;
    /**
     * Insieme delle reti neurali "pilota"
     * costituenti la popolazione in gara
     */
    public NeuralNetwork[] popolazione;
    /**
     * Indice della rete-pilota attualmente in gara
     */
    public int ActualIndex;
    //Generazione
    public int gen;
    
    /**
     * Inizializza una nuova popolazione casuale
     */
    public GeneticAlgorithm() {
        popolazione = new NeuralNetwork[50];
        for (int i = 0; i < popolazione.length; i++) {
            popolazione[i] = new NeuralNetwork(ULNN.ambient.car);
        }
        ActualIndex = 0;
    }
    
    /**
     * Inizializza la popolazione prendendo come pesi
     * quelli presenti nella matrice passata come parametro
     * @param weights matrice pesi conforme alla struttura della rete
     */
    public GeneticAlgorithm(double[][] weights) {
        popolazione = new NeuralNetwork[500];
        for (int i = 0; i < popolazione.length; i++) {
            double[] connection = new double[weights[i].length];
            System.out.println(i+") CONNECTION: "+connection.length);
            for(int o = 0; o < connection.length; o++){
                connection[o] = weights[i][o];
            }
            popolazione[i] = new NeuralNetwork(ULNN.ambient.car);
            popolazione[i].arrayToWeights(connection);
        }
        ActualIndex = 0;
    }

    /**
     * Ritorna una rete neurale presente nella popolazione seguendo
     * la classica regola della natura, secondo cui il più forte vince:
     * le reti con un maggior fitness hanno più probabilità di essere estratte.
     * @return Rete Neurale Pilota estratta
     */
    private NeuralNetwork MatingPool() {
        double sum = 0;
        for (NeuralNetwork nn : popolazione) {
            sum += nn.fitness;
        }
        double point = Math.random() * sum;
        sum = 0;
        for (NeuralNetwork popolazione1 : popolazione) {
            sum += popolazione1.fitness;
            if (point <= sum) {
                return popolazione1;
            }
        }
        return MatingPool();
    }

    /**
     * Combina i pesi di due reti distinte e, secondo il tasso di
     * mutazione, ne altera qualche valore.
     * I nuovi pesi vengono impostati alla rete ritornata (Figlio)
     * @param dad Rete1
     * @param mom Rete2
     * @return Figlio
     */
    private NeuralNetwork combine(NeuralNetwork dad, NeuralNetwork mom){
        NeuralNetwork son = new NeuralNetwork(ULNN.ambient.car);
        double[] DadWeight = dad.weightsToArray();
        double[] MomWeight = mom.weightsToArray();
        double[] SonWeight = new double[MomWeight.length];
        for(int i = 0; i < DadWeight.length; i++){
            SonWeight[i] = Math.random() < MutationRate ? 
                           Math.random() * 2 - 1 :
                           i < DadWeight.length / 2 ?
                           DadWeight[i] :
                           MomWeight[i];
        }
        son.arrayToWeights(SonWeight);
        return son;
    }
    
    /**
     * Avvia la generazione successiva, formata dai figli delle reti presenti
     * nella scorsa popolazione
     */
    public void nextGen() {
        for(NeuralNetwork n : popolazione) n.fitness*=n.fitness;
        NeuralNetwork[] NewPopulation = new NeuralNetwork[popolazione.length];
        for(int i = 0; i < NewPopulation.length; i++){
            NewPopulation[i] = combine(MatingPool(), MatingPool());            
        }
        popolazione = NewPopulation;
        ActualIndex = 0;
        gen++;
    }
    
    /**
     * Stabilisce il fitness medio della popolazione
     * @return fitness medio
     */
    public double getAvgFitness(){
        double sum = 0;
        int i = 0;
        for(; i < ActualIndex; i++){
            sum += popolazione[i].fitness;
        }
        return sum / i;
    }
}
