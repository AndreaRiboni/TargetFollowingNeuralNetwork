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
public class JVector {
    public double x, y;
    
    public JVector(double x, double y){
        this.x = x;
        this.y =y;
    }
    
    public JVector(float x, float y){
        this((double)x, (double)y);        
    }
    
    public JVector(int x, int y){
        this((double)x, (double)y);
    }
    
    public static boolean intersect(double x1, double y1, double x2, double y2, double rad1, double rad2){
        double dist = Math.pow((
                Math.pow(x2 - x1, 2) + 
                Math.pow(y2 - y1, 2)
                ), 0.5);
//        double radius = Math.min(rad1, rad2);
        return dist <= rad1 + rad2;        
    }
    
    public static double dist(double x1, double y1, double x2, double y2, double rad1, double rad2){
        double dist = Math.pow((
                Math.pow(x2 - x1, 2) + 
                Math.pow(y2 - y1, 2)
                ), 0.5);
//        double radius = Math.min(rad1, rad2);
        return dist - rad1 - rad2;        
    }
}
