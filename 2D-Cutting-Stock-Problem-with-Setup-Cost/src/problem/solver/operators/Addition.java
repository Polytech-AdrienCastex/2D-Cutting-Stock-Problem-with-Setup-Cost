package problem.solver.operators;

import problem.solver.parameters.ImageKind;

public class Addition extends INeighborOperator
{
    public Addition()
    {
        super();
        this.setNegativeOperator(new Subtraction(this));
    }
    public Addition(INeighborOperator negativeOperator)
    {
        super();
        this.setNegativeOperator(negativeOperator);
    }
    
    @Override
    public double[] getFrom(double[] array, ImageKind imageKind)
    {
        if(array[imageKind.getPatternIndex()] >= imageKind.getMaximumNumber())
            return null;
        
        double[] value = copy(array);
        value[imageKind.getPatternIndex()]++;
        return value;
    }
}
