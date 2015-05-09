package problem.solver.operators;

import problem.solver.parameters.ImageKind;

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
        double[] value = copy(array);
        value[imageKind.getPatternIndex()]--;
        return value;
    }

    @Override
    public boolean canApply(double[] array, ImageKind imageKind)
    {
        return array[imageKind.getPatternIndex()] > 0;
    }
}
