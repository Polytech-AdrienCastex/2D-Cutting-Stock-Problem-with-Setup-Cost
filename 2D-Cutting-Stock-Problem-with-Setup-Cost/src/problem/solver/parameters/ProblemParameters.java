package problem.solver.parameters;

import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;

public class ProblemParameters
{
    public ProblemParameters(int maxNumberOfPatterns, int printPrice, int patternPrice)
    {
        this.printPrice = printPrice;
        this.constant = maxNumberOfPatterns * patternPrice;
        this.maxNumberOfPatterns = maxNumberOfPatterns;
        this.patternPrice = patternPrice;
    }
    
    private final int printPrice;
    public double getPrintPrice()
    {
        return printPrice;
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
}
