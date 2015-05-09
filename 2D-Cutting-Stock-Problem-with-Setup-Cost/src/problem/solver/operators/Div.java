package problem.solver.operators;

import problem.solver.parameters.ImageKind;

public class Div extends INeighborOperator
{
    public Div(double coef)
    {
        super();
        this.coef = coef;
        this.setNegativeOperator(new Mul(this));
    }
    public Div(INeighborOperator negativeOperator)
    {
        super();
        this.coef = ((Mul)negativeOperator).getCoef();
        this.setNegativeOperator(negativeOperator);
    }
    
    private final double coef;
    public double getCoef()
    {
        return coef;
    }
    
    @Override
    public double[] getFrom(double[] array, ImageKind imageKind)
    {
        double[] value = copy(array);
        value[imageKind.getPatternIndex()] /= coef;
        return value;
    }

    @Override
    public boolean canApply(double[] array, ImageKind imageKind)
    {
        return (int)(array[imageKind.getPatternIndex()] / coef) <= imageKind.getMaximumNumber();
    }
}
