package project2;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * <p/>
 * You may also create and submit new files in addition to modifying this file.
 * <p/>
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * <p/>
 * These methods will be necessary for the project's main method to run.
 */
public class Agent {
    ArrayList<Transformation> sourceTransformations;

    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * <p/>
     * Do not add any variables to this signature; they will not be used by
     * main().
     */
    public Agent() {
        sourceTransformations = new ArrayList<Transformation>();
    }

    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * <p/>
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * <p/>
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     *
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public String Solve(RavensProblem problem) {
//        if (problem.getName().compareTo("2x1 Basic Problem 04") != 0)
//            return "0";

        // Figure out transformation between A and B first
        RavensFigure A = problem.getFigures().get("A");
        RavensFigure B = problem.getFigures().get("B");
        RavensFigure C = problem.getFigures().get("C");

        this.sourceTransformations = computeTransformation(A, B);

        System.out.println(A.getName() + " ---> " + B.getName());
        printTransformations(sourceTransformations);

        Map<String, RavensFigure> figures = problem.getFigures();
        ArrayList<RavensFigure> candidateAnswers = new ArrayList<RavensFigure>();

        // If there is just one shape, try to generate the answer
        if (C.getObjects().size() == 1) {
            RavensFigure candidateAnswer = new RavensFigure("Candidate Answer");
            RavensObject transformedObject = applyTransformation(C.getObjects().get(0), sourceTransformations.get(0).getMutations());

            candidateAnswer.getObjects().add(transformedObject);
            RavensFigure foundFigure = figureSearch(candidateAnswer, figures.values());
            if (foundFigure != null) {
                candidateAnswers.add(foundFigure);
            }
        } else {
//            ArrayList<RavensFigure> generatedAnswers = new ArrayList<>();
//            ArrayList<Transformation> mappedTransformations = new ArrayList<>();
//            RavensFigure generation = new RavensFigure("Generated Answer");
//            for(RavensObject objectInC: C.getObjects()) {
//                for (Transformation transformation : sourceTransformations) {
//                    if(!mappedTransformations.contains(transformation)) {
//                        RavensObject transformedObject = applyTransformation(objectInC, transformation.getMutations());
//                        generation.getObjects().add(transformedObject);
//                        mappedTransformations.add(transformation);
//                    }
//                }
//            }

            ArrayList<RavensFigure> answers1 = method1(figures, C);
            ArrayList<RavensFigure> answers2 = method2(figures, A,B,C);

            if (answers1.size() < answers2.size()) {
                candidateAnswers = answers1;
            } else if (answers1.size() > answers2.size()) {
                candidateAnswers = answers2;
            } else {
                candidateAnswers = answers1;

            candidateAnswers = answers2;           }
//            System.out.println(C.getName() + " ---> " + candidateFigure.getName());
//            printTransformations(candidateTransformations);
        }

        if (candidateAnswers.size() == 1)
            return candidateAnswers.get(0).getName();

        else

        {
            RavensFigure answer = null;
            for (RavensFigure candidateFigure : candidateAnswers) {
                // Match the figure with the first shape being the same
                if (candidateFigure.getObjects().get(0).getAttributes().get(0).getValue().compareTo(C.getObjects().get(0).getAttributes().get(0).getValue()) == 0) {
                    answer = candidateFigure;
                } else {
                    // Just take a guess at the answer
                    Random number = new Random();
                    int guess = number.nextInt(candidateAnswers.size() - 1 - 1 + 1) + 1;
                    answer = candidateAnswers.get(guess);
                }

            }

            return answer.getName();
        }

    }

    private int getAngleDifference(Transformation sourceTransformation) {
        // Assume the first mutation is an angle mutation
        int angleMutation = Integer.parseInt(sourceTransformation.getMutations().get(0).getValue());
        int angleOriginal = Integer.parseInt(getAttributeFromObject(sourceTransformation.getSource(), "angle").getValue());

        return angleMutation - angleOriginal;
    }

    public RavensAttribute getAttributeFromObject(RavensObject object, String needle) {
        for (RavensAttribute attribute : object.getAttributes()) {
            if (attribute.getName().compareTo(needle) == 0)
                return attribute;
        }

        return null;
    }

    private boolean isAngleMutation(RavensAttribute candidateMutation) {
        return candidateMutation.getName().compareTo("angle") == 0;
    }

    public RavensFigure figureSearch(RavensFigure needle, Collection<RavensFigure> haystack) {
        RavensFigure resultFigure = null;
        for (RavensFigure figure : haystack) {
            if (figure.getName().compareTo("A") == 0 || figure.getName().compareTo("B") == 0 || figure.getName().compareTo("C") == 0)
                continue;

            // Same number of objects
            if (figure.getObjects().size() == needle.getObjects().size()) {
                for (RavensObject figureObject : figure.getObjects()) {
                    boolean found = false;
                    for (RavensObject needleObject : needle.getObjects()) {
                        if (!found)
                            found = objectsAreIdentical(figureObject, needleObject);
                    }
                    if (found)
                        resultFigure = figure;
                }
            }
        }

        return resultFigure;
    }

    public boolean objectsAreIdentical(RavensObject object1, RavensObject object2) {
        boolean found;
        for (RavensAttribute object1Attribute : object1.getAttributes()) {
            found = false;
            for (RavensAttribute object2Attribute : object2.getAttributes()) {
                if (!found)
                    found = attributesAreIdentical(object1Attribute, object2Attribute);
            }
            if (!found)
                return false;
        }

        return true;
    }

    /**
     * Compare 2 attributes
     *
     * @param attribute1
     * @param attribute2
     * @return
     */
    public boolean attributesAreIdentical(RavensAttribute attribute1, RavensAttribute attribute2) {
        if (attribute1.getName().compareTo(attribute2.getName()) == 0)
            if (attribute1.getValue().compareTo(attribute2.getValue()) == 0)
                return true;
        return false;
    }

    /**
     * Apply a set of mutations to an object
     *
     * @param object
     * @param mutations
     * @return
     */
    public RavensObject applyTransformation(RavensObject object, ArrayList<RavensAttribute> mutations) {
        RavensObject transformedObject = new RavensObject(object.getName());
        for (RavensAttribute attribute : object.getAttributes()) {
            boolean added = false;
            boolean found = false;
            for (RavensAttribute mutation : mutations) {
                if (attribute.getName().compareTo(mutation.getName()) == 0) {
                    found = true;
                    if (!added) {
                        transformedObject.getAttributes().add(mergeAttributes(attribute, mutation));
                        added = true;
                    }
                }
            }
            if (!found) {
                transformedObject.getAttributes().add(new RavensAttribute(attribute.getName(), attribute.getValue()));
            }
        }

        return transformedObject;
    }

    /**
     * Merge a set of attributes
     *
     * @param original
     * @param newAttributes
     * @return
     */
    public RavensAttribute mergeAttributes(RavensAttribute original, RavensAttribute newAttributes) {
        RavensAttribute merged;
        ArrayList<String> mergedProperties = new ArrayList<>();
        String name = original.getName();
        String[] originalProperties = original.getValue().split(",");
        String[] newProperties = newAttributes.getValue().split(",");

        // Initialize ArrayList with original properties
        for (String property : originalProperties)
            if (!isDiscreet(property))
                mergedProperties.add(property);

        for (String property : newProperties) {
            if (!Arrays.asList(originalProperties).contains(property))
                mergedProperties.add(property);
        }

        // Create concatinated string of properties
        String properties = "";
        for (String property : mergedProperties)
            properties += "," + property;

        merged = new RavensAttribute(original.getName(), properties.substring(1, properties.length()));

        // if is discreet just override it
        if (isDiscreet(newAttributes.getValue())) {
            if (newAttributes.getName().compareTo("angle") == 0) {
                if (newAttributes.getValue().compareTo("270") == 0) {
                    merged = new RavensAttribute(original.getName(), "0");
                }
            } else {
                merged = new RavensAttribute(original.getName(), newAttributes.getValue());
            }
        }

        return merged;
    }

    public boolean isDiscreet(String value) {
        if (value.compareTo("yes") == 0 || value.compareTo("no") == 0)
            return true;
        else if (value.compareTo("small") == 0 || value.compareTo("large") == 0)
            return true;
        else {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    }

    /**
     * Checks whether an attribute is a position attribute
     *
     * @param attribute
     * @return
     */
    public boolean isPositionMutation(RavensAttribute attribute) {
        switch (attribute.getName()) {
            case "above":
                return true;
            case "left-of":
                return true;
        }

        return false;
    }

    /**
     * Gets the number of mutations that a shape has
     *
     * @param shape
     * @param transformations
     * @return
     */
    public int getTargetDegree(String shape, ArrayList<Transformation> transformations) {
        for (Transformation transformation : transformations) {
            if (transformation.getTarget().getName().compareTo(shape) == 0) {
                return transformation.getMutations().size();
            }
        }
        return -1;
    }

    /**
     * Collects all the mutations from a set of transformations into one array
     *
     * @param transformations
     * @return
     */
    public ArrayList<RavensAttribute> collectMutations(ArrayList<Transformation> transformations) {
        ArrayList<RavensAttribute> mutations = new ArrayList<RavensAttribute>();

        for (Transformation transformation : transformations) {
            // Introduce a 'none' mutation to indicate no change
            if (transformation.getMutations().isEmpty()) {
                mutations.add(new RavensAttribute("none", "none"));
            }
            for (RavensAttribute mutation : transformation.getMutations()) {
                mutations.add(mutation);
            }
        }

        return mutations;
    }

    /**
     * Print transformation
     *
     * @param transformation
     */
    public void printTransformations(ArrayList<Transformation> transformation) {
        for (Transformation trans : transformation) {
            trans.print();
        }
    }

    /**
     * Computes the transfomration between two figures
     *
     * @param figure1
     * @param figure2
     * @return
     */
    private ArrayList<Transformation> computeTransformation(RavensFigure figure1, RavensFigure figure2) {
        ArrayList<Transformation> transformations = new ArrayList<Transformation>();
        ArrayList<RavensObject> targetsMapped = new ArrayList<RavensObject>();

        for (RavensObject objectInA : figure1.getObjects()) {
            Transformation transformation = new Transformation();
            transformation.setSource(objectInA);

            int lowest_diff = 9999;
            for (RavensObject objectInB : figure2.getObjects()) {
                // If object is already mapped, don't do anything
                if (targetsMapped.contains(objectInB))
                    continue;

                int diff = getDifferences(objectInA.getAttributes(), objectInB.getAttributes());

                if (diff < lowest_diff) {
                    lowest_diff = diff;
                    transformation.setTarget(objectInB);
                }
            }

            // Prevent mapping to same target shape again
            targetsMapped.add(transformation.getTarget());

            if (transformation.getTarget() != null) {
                transformation.computeMutations();
                transformations.add(transformation);
            }
        }

        return transformations;
    }

    /**
     * Assumes a 1-1 attribute mapping
     * Merges the name of the attribute with the value. Like "shape:circle"
     *
     * @param source
     * @param target
     * @return
     */
    public int getDifferences(ArrayList<RavensAttribute> source, ArrayList<RavensAttribute> target) {
        int differences = 0;
        for (RavensAttribute sourceAttribute : source) {
            boolean found = false;
            for (RavensAttribute targetAttribute : target) {
                if (sourceAttribute.getName().compareTo(targetAttribute.getName()) == 0) {
                    found = true;
                    if (sourceAttribute.getValue().compareTo(targetAttribute.getValue()) != 0) {
                        differences++;
                    }
                }
            }

            if (!found) {
                differences++;
            }
        }

        return differences;
    }

    public ArrayList<RavensFigure> method1(Map<String, RavensFigure> figures, RavensFigure C) {
        ArrayList<RavensFigure> candidateAnswers = new ArrayList<>();
        int lowest_diff = 9999;
        for (RavensFigure candidateFigure : figures.values()) {
            // Skip A, B, and C
            if (candidateFigure.getName().compareTo("A") != 0 && candidateFigure.getName().compareTo("B") != 0 && candidateFigure.getName().compareTo("C") != 0) {

                ArrayList<Transformation> candidateTransformations = computeTransformation(C, candidateFigure);

                ArrayList<RavensAttribute> sourceMutationsCollection = collectMutations(sourceTransformations);
                ArrayList<RavensAttribute> candidateMutationsCollection = collectMutations(candidateTransformations);

                int diff = 0;
                int number_found = 0;
                for (RavensAttribute sourceMutation : sourceMutationsCollection) {
                    boolean found = false;
                    for (RavensAttribute candidateMutation : candidateMutationsCollection) {
                        if (sourceMutation.getName().compareTo(candidateMutation.getName()) == 0) {
//                            if(sourceMutation.getValue().compareTo(candidateMutation.getValue()) == 0) {
                            found = true;
                            number_found++;
//                            }
                        }
                    }

                    if (!found)
                        diff++;
                }

                if (number_found < candidateMutationsCollection.size())
                    diff +=  number_found;
                else if(number_found <= candidateMutationsCollection.size() && sourceMutationsCollection.size() < candidateMutationsCollection.size()){
                    diff +=  number_found;
                }

                if (diff < lowest_diff) {
                    lowest_diff = diff;
                    candidateAnswers.clear();
                    candidateAnswers.add(candidateFigure);
                } else if (diff == lowest_diff) {
                    candidateAnswers.add(candidateFigure);
                }

                System.out.println(C.getName() + " ---> " + candidateFigure.getName());
                printTransformations(candidateTransformations);
            }
        }


        return candidateAnswers;
    }

    public ArrayList<RavensFigure> method2(Map<String, RavensFigure> figures, RavensFigure A, RavensFigure B, RavensFigure C) {
        ArrayList<RavensFigure> candidateAnswers = new ArrayList<>();
        for (RavensFigure candidateFigure : figures.values()) {
            ArrayList<Transformation> candidateTransformations = computeTransformation(C, candidateFigure);

            // Skip A, B, and C
            if (candidateFigure.getName().compareTo("A") != 0 && candidateFigure.getName().compareTo("B") != 0 && candidateFigure.getName().compareTo("C") != 0) {

                ArrayList<Transformation> mappedTransformations = new ArrayList<>();

                for (Transformation sourceTransformation : sourceTransformations) {
                    int sourceDegree = sourceTransformation.getMutations().size();
                    for (Transformation candidateTransformation : candidateTransformations) {
                        int candidateDegree = candidateTransformation.getMutations().size();
                        if (candidateDegree == sourceDegree) {
                            // If degree is 0 add to figures
                            if (candidateDegree == 0) {
                                if (!mappedTransformations.contains((sourceTransformation)))
                                    mappedTransformations.add(sourceTransformation);
                            } else {
                                // Compare Mutations
                                int matchingMutations = 0;
                                for (RavensAttribute sourceMutation : sourceTransformation.getMutations()) {
                                    for (RavensAttribute candidateMutation : candidateTransformation.getMutations()) {
                                        // Compare mutation name
                                        if (sourceMutation.getName().compareTo(candidateMutation.getName()) == 0) {
                                            // If its a position mutation validate it
                                            if (isPositionMutation(candidateMutation)) {
                                                // Find the target shape for the value, and see if its similar to the one in the source
                                                // Check for same number of position shapes
                                                String[] sourceShapeCount = sourceMutation.getValue().split(",");
                                                String[] candidateShapeCount = candidateMutation.getValue().split(",");
                                                if (sourceShapeCount.length == candidateShapeCount.length) {
                                                    if (getTargetDegree(candidateMutation.getValue(), candidateTransformations) == getTargetDegree(sourceMutation.getValue(), sourceTransformations)) {
                                                        // Mutation is matching
                                                        matchingMutations++;
                                                    }
                                                }
                                            } else if (isAngleMutation(candidateMutation)) {
                                                // This is an angle mutation, validate based on the angle difference from source mutation
                                                int angleDiff = getAngleDifference(sourceTransformation);
                                                if (sourceMutation.getValue().compareTo(candidateMutation.getValue()) == 0) {
                                                    matchingMutations++;
                                                }
                                            } else {
                                                // Not a position mutation, so validate exact value match
                                                if (candidateMutation.getValue().compareTo(sourceMutation.getValue()) == 0)
                                                    matchingMutations++;
                                            }
                                        }
                                    }
                                }
                                if (matchingMutations == sourceTransformation.getMutations().size())
                                    if (!mappedTransformations.contains(sourceTransformation))
                                        mappedTransformations.add(sourceTransformation);
                            }
                        }
                    }
                }
                if (mappedTransformations.size() == sourceTransformations.size()) {
                    // Check for object additions or removals
                    if ((B.getObjects().size() - A.getObjects().size()) == (candidateFigure.getObjects().size() - C.getObjects().size()))
                        candidateAnswers.add(candidateFigure);
                }
            }
        }

        return candidateAnswers;
    }
}
