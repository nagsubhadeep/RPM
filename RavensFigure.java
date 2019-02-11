/*
 * DO NOT MODIFY THIS FILE.
 * 
 * Any modifications to this file will not be used when grading your project.
 * If you have any questions, please email the TAs.
 * 
 */
package project2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A single figure in a Raven's Progressive Matrix problem, comprised of a name 
 * and a list of RavensObjects.
 * 
 */
public class RavensFigure {
    private String name;
    private ArrayList<RavensObject> objects;
    private HashMap<String, RavensObject> objectsMap;
    
    /**
     * Creates a new figure for a Raven's Progressive Matrix given a name.
     * 
     * Your agent does not need to use this method.
     * 
     * @param name the name of the figure
     */
    public RavensFigure(String name) {
        this.name=name;
        objects=new ArrayList<>();
    }
    /**
     * Returns the name of the figure. The name of the figure will always match
     * the HashMap key for this figure.
     * 
     * The figures in the problem will be named A, B, and C for 2x1 and 2x2
     * problems. The figures in the problem will be named A, B, C, D, E, F, G,
     * and H in 3x3 problems. The first row is A, B, and C; the second row is
     * D, E, and F; and the third row is G and H.
     * 
     * Answer options will always be named 1 through 6.
     * 
     * The numbers for the answer options will be randomly generated on each run
     * of the problem. The correct answer will remain the same, but its number
     * will change.
     * 
     * @return the name of this figure
     */
    public String getName() {
        return name;
    }
    /**
     * Returns an ArrayList of RavensObjects from the figure.
     * 
     * @return an ArrayList of RavensObject
     */
    public ArrayList<RavensObject> getObjects() {
        return objects;
    }
}
