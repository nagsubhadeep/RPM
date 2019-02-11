package project2;

import java.util.ArrayList;
import java.util.Stack;

public class Transformation {
    private RavensObject source;
    private RavensObject target;
    ArrayList<RavensAttribute> mutations;

    public RavensObject getSource() {
        return source;
    }

    public void setSource(RavensObject source) {
        this.source = source;
    }

    public RavensObject getTarget() {
        return target;
    }

    public void setTarget(RavensObject target) {
        this.target = target;
    }

    public void computeMutations() {
        mutations = getMutations();
    }

    public ArrayList<RavensAttribute> getMutations() {
        ArrayList<RavensAttribute> mutations = new ArrayList<RavensAttribute>();

        for(RavensAttribute targetAttribute: target.getAttributes()) {
            boolean found = false;
            for(RavensAttribute sourceAttribute: source.getAttributes()) {
                if(targetAttribute.getName().compareTo(sourceAttribute.getName()) == 0) {
                    found = true;
                    if(targetAttribute.getValue().compareTo(sourceAttribute.getValue()) != 0) {
                        mutations.add(targetAttribute);
                    }
                }

            }

            if(!found)
                mutations.add(targetAttribute);
        }

        return mutations;
    }

    public void print() {
        System.out.println("\t" + getSource().getName() + " ---> " + getTarget().getName());
        for(RavensAttribute attr: getMutations()) {
            System.out.println("\t\t" + attr.getName() + "(" + attr.getValue() + ")");
        }
    }
}

