package problem.solver.solution;

import problem.solver.solution.solutiongenerator.SolutionGenerator;
import problem.solver.solution.solutiongenerator.IncrementalSolutionGenerator;
import diagnosis.AverageMaker;
import diagnosis.TimeDiagnosis;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.parameters.ProblemParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import problem.solver.patternplacement.PatternPlacement;

public class Solution implements Comparable
{
    public Solution(Solution parent, Pattern[] patterns)
    {
        this.patterns = patterns;
        this.fitnessValueSolution = null;
        this.patternKind = parent.patternKind;
        this.patternPlacement = parent.patternPlacement;
        this.coefs = parent.coefs;
        this.f = parent.f;
    }
    public Solution(ProblemParameters problemParameters, PatternKind patternKind, PatternPlacement patternPlacement)
    {
        this.fitnessValueSolution = null;
        this.patternKind = patternKind;
        this.patternPlacement = patternPlacement;
        
        diagnosis.TimeDiagnosis td = new TimeDiagnosis();
        td.tick();
        
        SolutionGenerator sg = new IncrementalSolutionGenerator(patternPlacement, patternKind, 100000, 1000000, 0.001);
        
        int nbPatterns = 2;
        while(!sg.compute(nbPatterns) || !isPossible(sg.getPatterns(), patternKind, patternPlacement))
        {
            if(nbPatterns < problemParameters.getMaxNumberOfPatterns())
                nbPatterns++;
        }
        
        this.patterns = sg.getPatterns();
        
        System.out.println(td.tick());
        System.out.println("Solution de départ trouvée ["+nbPatterns+"]. Application de l'algorithme...");
        
        this.coefs = new double[nbPatterns];
        
        for(int i = 0; i < this.coefs.length; i++)
            this.coefs[i] = problemParameters.getPrintPrice();
        f = new LinearObjectiveFunction(this.coefs, problemParameters.getConstant());
        
        System.out.println(this);
    }
    
    private final PatternPlacement patternPlacement;
    private final Pattern[] patterns;
    private final PatternKind patternKind;
    
    
    private final double[] coefs;
    public double[] getCoefs()
    {
        return coefs;
    }
    private final LinearObjectiveFunction f;
    public LinearObjectiveFunction getObjectiveFunction()
    {
        return f;
    }
            
    private PointValuePair fitnessValueSolution;
    private void computeFitnessValue()
    {
        fitnessValueSolution = new SimplexSolver()
                .optimize(new MaxIter(500),
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
        return patternKind.getImageKinds()
                .parallelStream()
                .map(ik ->
                {
                    double[] pattern = new double[patterns.length];
                    for(int i = 0; i < pattern.length; i++)
                        pattern[i] = patterns[i].getImageNumber(ik);
                    return new LinearConstraint(pattern, Relationship.GEQ, ik.getDemand());
                })
                .collect(Collectors.toList());
    }
    
    public Pattern[] getPatterns()
    {
        return patterns;
    }
    
    public static AverageMaker avg = new AverageMaker();
    public boolean isPossible()
    {
        return isPossible(patterns, patternKind, patternPlacement);
    }
    public static boolean isPossible(Pattern[] patterns, PatternKind patternKind, PatternPlacement patternPlacement)
    {
        if(isImageMissing(patterns, patternKind))
            return false;
        
        return Arrays.asList(patterns)
                .parallelStream()
                .map(p -> patternPlacement.isPossible(p))
                .allMatch(b -> b);
    }
    
    public boolean isImageMissing()
    {
        return isImageMissing(patterns, patternKind);
    }
    public static boolean isImageMissing(Pattern[] patterns, PatternKind patternKind)
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
                return true;
        return false;
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
 