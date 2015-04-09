package problem.solver;

import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.parameters.ProblemParameters;
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

public class Solution implements Comparable
{
    public Solution(Solution parent, Pattern[] patterns)
    {
        this.patterns = patterns;
        this.fitnessValueSolution = null;
        this.patternKind = parent.patternKind;
        this.problemParameters = parent.problemParameters;
    }
    public Solution(ProblemParameters problemParameters, PatternKind patternKind)
    {
        this.patterns = new Pattern[problemParameters.getMaxNumberOfPatterns()];
        this.fitnessValueSolution = null;
        this.patternKind = patternKind;
        this.problemParameters = problemParameters;
        
        Random rnd = new Random();
        do
        {
            for(int i = 0; i < patterns.length; i++)
                patterns[i] = Pattern.createRandomPattern(patternKind, rnd);
        } while(!isPossible());
    }
    
    private final ProblemParameters problemParameters;
    private final Pattern[] patterns;
    private final PatternKind patternKind;
            
    private PointValuePair fitnessValueSolution;
    private void computeFitnessValue()
    {
        LinearObjectiveFunction f = new LinearObjectiveFunction(
                problemParameters.getCoefs(),
                problemParameters.getConstant());
        fitnessValueSolution = new SimplexSolver()
                .optimize(new MaxIter(1000),
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
    
    public double[] getImageNumbers()
    {
        double[] patterns = getPatternNumbers();
        double[] images = new double[patternKind.getNumberOfImages()];
        
        for(ImageKind i : patternKind.getImageKinds())
            for(int p = 0; p < patterns.length; p++)
                images[i.getPatternIndex()] += patterns[p] * this.patterns[p].getImageNumber(i);
        
        return images;
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
        
        str.append("} ≈ ");
        str.append(getFitnessValue());
        
        return str.toString();
    }
}
 