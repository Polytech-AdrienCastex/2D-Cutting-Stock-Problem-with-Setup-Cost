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
        if(array[imageKind.getPatternIndex()] >= imageKind.getMaximumNumber())
            return null;
        
        double[] value = copy(array);
        value[imageKind.getPatternIndex()] /= 2;
        return value;
    }
}
