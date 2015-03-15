package problem.solver.operators;

import problem.solver.ImageKind;

public class Subtraction extends INeighborOperator
{
    public Subtraction()
    {
        super();
        this.setNegativeOperator(new Addition(this));
    }
    public Subtraction(INeighborOperator negativeOperator)
    {
        super();
        this.setNegativeOperator(negativeOperator);
    }
    
    @Override
    public double[] getFrom(double[] array, ImageKind imageKind)
    {
        if(array[imageKind.getPatternIndex()] <= 0)
            return null;
        
        double[] value = copy(array);
        value[imageKind.getPatternIndex()]--;
        return value;
    }
}
