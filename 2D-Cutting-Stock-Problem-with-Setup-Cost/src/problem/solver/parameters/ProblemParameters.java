package problem.solver.parameters;

import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;

public class ProblemParameters
{
    public ProblemParameters(int maxNumberOfPatterns, int printPrice, int patternPrice)
    {
        this.coefs = new double[maxNumberOfPatterns];
        
        for(int i = 0; i < this.coefs.length; i++)
            this.coefs[i] = printPrice;
        
        this.constant = maxNumberOfPatterns * patternPrice;
        this.maxNumberOfPatterns = maxNumberOfPatterns;
        this.patternPrice = patternPrice;
        
        f = new LinearObjectiveFunction(getCoefs(), getConstant());
    }
    
    private final double[] coefs;
    public double[] getCoefs()
    {
        return coefs;
    }

    private final double patternPrice;
    public double getPatternPrice()
    {
        return patternPrice;
    }

    private final double constant;
    public double getConstant()
    {
        return constant;
    }

    private final int maxNumberOfPatterns;
    public int getMaxNumberOfPatterns()
    {
        return maxNumberOfPatterns;
    }
    
    private final LinearObjectiveFunction f;
    public LinearObjectiveFunction getObjectiveFunction()
    {
        return f;
    }
}
