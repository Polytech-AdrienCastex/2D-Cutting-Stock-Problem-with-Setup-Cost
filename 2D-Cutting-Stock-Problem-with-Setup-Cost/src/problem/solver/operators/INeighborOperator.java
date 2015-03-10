
package problem.solver.operators;

/**
 *
 * @author Adrien
 */
public abstract class INeighborOperator
{
    protected static double[][] copy(double[][] data)
    {
        double[][] copiedData = new double[data.length][data[0].length];
        
        for(int i = 0; i < data.length; i++)
            for(int j = 0; j < data[0].length; j++)
                copiedData[i][j] = data[i][j];
        
        return copiedData;
    }
    
    public abstract double[][] getFrom(double[][] array, int x, int y);
}
