package problem.solver.operators;

import problem.solver.ImageKind;

public abstract class INeighborOperator
{
    public void setNegativeOperator(INeighborOperator negativeOperator)
    {
        this.negativeOperator = negativeOperator;
    }
    
    private INeighborOperator negativeOperator;
    
    public INeighborOperator getNegativeOperator()
    {
        return negativeOperator;
    }
    
    protected static double[] copy(double[] data)
    {
        double[] copiedData = new double[data.length];
        
        System.arraycopy(data, 0, copiedData, 0, data.length);
        
        return copiedData;
    }
    
    public abstract double[] getFrom(double[] array, ImageKind imageKind);
}
