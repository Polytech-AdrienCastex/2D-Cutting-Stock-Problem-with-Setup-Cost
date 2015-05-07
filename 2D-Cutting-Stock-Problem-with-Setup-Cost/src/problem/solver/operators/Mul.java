package problem.solver.operators;

import problem.solver.parameters.ImageKind;

public class Mul extends INeighborOperator
{
    public Mul(double coef)
    {
        super();
        this.coef = coef;
        this.setNegativeOperator(new Subtraction(this));
    }
    public Mul(INeighborOperator negativeOperator)
    {
        super();
        this.coef = ((Div)negativeOperator).getCoef();
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
        if(array[imageKind.getPatternIndex()] * 2 > imageKind.getMaximumNumber())
            return null;
        
        double[] value = copy(array);
        value[imageKind.getPatternIndex()] *= 2;
        return value;
    }
}
