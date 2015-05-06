package problem.solver.operators;

import problem.solver.parameters.ImageKind;

public class Div extends INeighborOperator
{
    public Div()
    {
        super();
        this.setNegativeOperator(new Mul(this));
    }
    public Div(INeighborOperator negativeOperator)
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
        value[imageKind.getPatternIndex()] /= 2;
        return value;
    }
}
