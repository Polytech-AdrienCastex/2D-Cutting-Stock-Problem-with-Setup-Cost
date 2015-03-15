package problem.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import problem.solver.neighborselection.INextSolutionGenerator;

public class Solution implements Comparable
{
    public Solution(Solution parent, Pattern[] patterns)
    {
        this.patterns = patterns;
        //this.neighborSelector = parent.neighborSelector;
        this.fitnessValueSolution = null;
        this.patternKind = parent.patternKind;
    }
    public Solution(int numberOfPatterns, PatternKind patternKind, INextSolutionGenerator neighborSelector)
    {
        this.patterns = new Pattern[numberOfPatterns];
        //this.neighborSelector = neighborSelector;
        this.fitnessValueSolution = null;
        this.patternKind = patternKind;
        /*
        for(int i = 0; i < numberOfPatterns; i++)
            patterns[i] = new Pattern(patternKind, neighborSelector);
        */
        
        Random rnd = new Random();
        do
        {
            for(int i = 0; i < numberOfPatterns; i++)
                patterns[i] = Pattern.createRandomPatter(patternKind, rnd);
        } while(!isPossible());
    }
    
    private final Pattern[] patterns;
    //private final INextSolutionGenerator neighborSelector;
    private final PatternKind patternKind;
            
    private PointValuePair fitnessValueSolution;
    private void computeFitnessValue()
    {
        LinearObjectiveFunction f = new LinearObjectiveFunction(
                new double[] { 1, 1, 1 },
                3*20);
        fitnessValueSolution = new SimplexSolver()
                .optimize(new MaxIter(100),
                        f,
                        new LinearConstraintSet(getConstraints()),
                        GoalType.MINIMIZE,
                        new NonNegativeConstraint(true));
    }
    
    public double getFitnessValue()
    {
        if(fitnessValueSolution != null)
            return fitnessValueSolution.getValue();
        
        computeFitnessValue();
        
        return fitnessValueSolution.getValue();
    }
    public double[] getPatternNumbers()
    {
        if(fitnessValueSolution != null)
            return fitnessValueSolution.getFirst();
        
        computeFitnessValue();
        
        return fitnessValueSolution.getFirst();
    }
    
    private Collection<LinearConstraint> getConstraints()
    {
        Collection<LinearConstraint> constraints = new ArrayList<>();
        double[] pattern;
        PatternKind pk = patternKind;
        
        for(ImageKind ik : pk.getImageKinds())
        {
            pattern = new double[patterns.length];
            for(int i = 0; i < pattern.length; i++)
                pattern[i] = patterns[i].getImageNumber(ik);
            
            constraints.add(new LinearConstraint(pattern, Relationship.GEQ, ik.getDemand()));
        }
        
        return constraints;
    }
    
    public Pattern[] getPatterns()
    {
        return patterns;
    }
    
    public boolean isPossible()
    {
        boolean[] images = new boolean[patternKind.getNumberOfImages()];
        for(int i = 0; i < images.length; i++)
            images[i] = false;
        
        for(Pattern p : patterns)
        {
            double[] values = p.getImageNumber();
            for(int i = 0; i < values.length; i++)
                images[i] |= values[i] > 0;
        }
        
        for(boolean b : images)
            if(!b)
                return false;
        return true;
    }
    /*
    public Solution selectNextSolution() throws SolverException
    {
        return neighborSelector.selectNextSolution(this);
    }*/

    @Override
    public int compareTo(Object o)
    {
        return (int)(this.getFitnessValue() - ((Solution)o).getFitnessValue());
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder("{ ");
        
        for(Pattern p : patterns)
        {
            str.append(p);
            str.append(" ");
        }
        
        str.append("} = ");
        str.append(getFitnessValue());
        
        return str.toString();
    }
}
