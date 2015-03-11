
package problem.solver.operators;

import problem.solver.ImageKind;

/**
 *
 * @author Adrien
 */
public abstract class INeighborOperator
{
    protected static double[] copy(double[] data)
    {
        double[] copiedData = new double[data.length];
        
        for(int i = 0; i < data.length; i++)
            copiedData[i] = data[i];
        
        return copiedData;
    }
    
    public abstract double[] getFrom(double[] array, ImageKind imageKind);
}
