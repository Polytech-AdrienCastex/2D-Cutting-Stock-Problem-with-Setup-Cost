package problem.solver;

import diagnosis.AverageMaker;
import diagnosis.TimeDiagnosis;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.parameters.ProblemParameters;
import java.util.ArrayList;
import java.util.Arrays;
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
import problem.solver.patternplacement.PatternPlacement;

public class Solution implements Comparable
{
    public Solution(Solution parent, Pattern[] patterns)
    {
        this.patterns = patterns;
        this.fitnessValueSolution = null;
        this.patternKind = parent.patternKind;
        this.problemParameters = parent.problemParameters;
        this.patternPlacement = parent.patternPlacement;
    }
    public Solution(ProblemParameters problemParameters, PatternKind patternKind, PatternPlacement patternPlacement)
    {
        this.patterns = new Pattern[problemParameters.getMaxNumberOfPatterns()];
        this.fitnessValueSolution = null;
        this.patternKind = patternKind;
        this.problemParameters = problemParameters;
        this.patternPlacement = patternPlacement;
        
        Random rnd = new Random();
        boolean retry;
        do
        {
            retry = false;
            
            for(int i = 0; i < Math.min(patternKind.getNumberOfImages(), patterns.length); i++)
            {
                double[] imgs = new double[patternKind.getNumberOfImages()];
                imgs[i] = rnd.nextInt(patternKind.getImageKinds().stream().skip(i).findFirst().get().getMaximumNumber() - 1) + 1;
                patterns[i] = new Pattern(imgs);
            }
            
            if(patternKind.getNumberOfImages() < patterns.length)
            {
                for(int i = patternKind.getNumberOfImages(); i < patterns.length; i++)
                {
                    double[] imgs = new double[patternKind.getNumberOfImages()];
                    imgs[i % patternKind.getNumberOfImages()] = rnd.nextInt(patternKind.getImageKinds().stream().skip(i % patternKind.getNumberOfImages()).findFirst().get().getMaximumNumber() - 1) + 1;
                    patterns[i] = new Pattern(imgs);
                }
            }
            else if(patternKind.getNumberOfImages() > patterns.length)
            {
                int id = -1;
                for(int i = patterns.length; i < patternKind.getNumberOfImages(); i++)
                {
                    int nbTry = 0;
                    int max = patternKind.getImageKinds().stream().skip(i).findFirst().get().getMaximumNumber();
                    do
                    {
                        if(id == 0)
                        {
                            nbTry++;
                            if(nbTry >= 200)
                            {
                                retry = true;
                                break;
                            }
                        }
                        id = (id + 1) % patterns.length;
                        double[] imgs = Arrays.copyOf(patterns[id].getImageNumber(), patternKind.getNumberOfImages());
                        imgs[i] = rnd.nextInt(max - 1)/2 + 1;
                        patterns[id] = new Pattern(imgs);
                    } while(!patternPlacement.isPossible(patterns[id]));
                    if(retry)
                        break;
                }
            }
        }
        while(retry || !isPossible());
        /*
            do
            {
        for(int i = 0; i < patterns.length - 1; i++)
        {
            do
            {
                patterns[i] = Pattern.createRandomPattern(patternKind, rnd);
            } while(!patternPlacement.isPossible(patterns[i]));
        }
        patterns[patterns.length - 1] = Pattern.createRandomPattern(patternKind, rnd, a());
            } while(!patternPlacement.isPossible(patterns[patterns.length - 1]));*/
    }
    
    protected boolean[] a()
    {
        boolean[] images = new boolean[patternKind.getNumberOfImages()];
        for(int i = 0; i < images.length; i++)
            images[i] = false;
        
        for(int j = 0; j < patterns.length - 1; j++)
        {
            double[] values = patterns[j].getImageNumber();
            for(int i = 0; i < values.length; i++)
                images[i] |= values[i] > 0;
        }
        
        return images;
    }
    
    private final PatternPlacement patternPlacement;
    private final ProblemParameters problemParameters;
    private final Pattern[] patterns;
    private final PatternKind patternKind;
            
    private PointValuePair fitnessValueSolution;
    private void computeFitnessValue()
    {
        fitnessValueSolution = new SimplexSolver()
                .optimize(new MaxIter(500),
                        problemParameters.getObjectiveFunction(),
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
    
    public static AverageMaker avg = new AverageMaker();
    public boolean isPossible()
    {
        TimeDiagnosis td = new TimeDiagnosis();
        boolean[] images = new boolean[patternKind.getNumberOfImages()];
        for(int i = 0; i < images.length; i++)
            images[i] = false;
        
        for(Pattern p : patterns)
        {
            if(!patternPlacement.isPossible(p))
                return false;
            
            double[] values = p.getImageNumber();
            for(int i = 0; i < values.length; i++)
                images[i] |= values[i] > 0;
        }
        
        avg.add(td.tick());
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
 