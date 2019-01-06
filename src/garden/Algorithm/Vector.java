package garden.Algorithm;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class defines behaviour related to 2D vectors properties (like norms, products, and resizing)
 *
 */
public class Vector {
    /**
     * The start point of the vector
     */
    private Point start;
    /**
     * The end point of the vector
     */
    private Point end;
    /**
     * The delta coordinate of x
     */
    private double deltaX;
    /**
     * The delta coordinate of y
     */
    private double deltaY;
    /**
     *  The length of the vector
     */
    private double norm;

    public Vector(Point p1,Point p2){
        this.start = p1;
        this.end  = p2;
        this.deltaX = p1.getX()-p2.getX();
        this.deltaY = p1.getY() - p2.getY();
        this.norm = Math.sqrt(Math.pow(this.deltaX,2)+Math.pow(this.deltaY,2));

    }
    /**
     * Returns the scalar product of 2 vectors with components `x` and `y`
     *
     * @param otherVec the other vector
     */
    public double scalarProduct(Vector otherVec){
        return this.deltaX*otherVec.deltaX+this.deltaY*otherVec.deltaY;

    }

    /**
     * Returns the cosθ and sinθ of two vectors
     *
     * @param  otherVec the other vector
     */
    public ArrayList getCosAndSin(Vector otherVec){
        double cos = scalarProduct(otherVec)/(this.norm*otherVec.norm);
        double sin;
        if ((Math.sqrt(1-cos*cos))==Double.NaN){
          sin = 0;

        }else{  sin = Math.sqrt(1-cos*cos);}

        ArrayList rs = new ArrayList();
        rs.add(cos);
        rs.add(sin);
        return rs;
    }

    /**
     * Resizes a Vector of norm "x" to a norm of "newNorm" (keeping the same orientation)
     *
     * @param newNorm
     */
    public Vector resize(double newNorm){
        Vector vs = new Vector(this.start,this.end);
        if(this.norm!=newNorm){
            double newDeltaX = this.deltaX*newNorm/this.norm;
            double newDeltaY = this.deltaY*newNorm/this.norm;
            Point newEnd= new Point();
            newEnd.setLocation(this.start.getX()+newDeltaX,this.start.getY()+newDeltaY);
           vs = new Vector(this.start,newEnd);
            return vs;
        }
        return vs;

    }

    public double getNorm(){
        return this.norm;
    }

    public Point getEnd(){
        return this.end;
    }


}
