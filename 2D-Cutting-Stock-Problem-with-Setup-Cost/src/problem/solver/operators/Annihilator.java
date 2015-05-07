package problem.solver.operators;

import problem.solver.parameters.ImageKind;

public class Annihilator extends INeighborOperator
{
    public Annihilator()
    {
        super();
        this.setNegativeOperator(new Subtraction(this));
    }
    public Annihilator(INeighborOperator negativeOperator)
    {
        super();
        this.setNegativeOperator(negativeOperator);
    }
    
    @Override
    public double[] getFrom(double[] array, ImageKind imageKind)
    {
        return new double[array.length];
    }
}
