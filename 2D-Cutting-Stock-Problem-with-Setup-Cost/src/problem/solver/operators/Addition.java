/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.operators;

/**
 *
 * @author Adrien
 */
public class Addition extends INeighborOperator
{
    private final double MAX_VALUE = 15;
    
    @Override
    public double[][] getFrom(double[][] array, int x, int y)
    {
        if(array[x][y] >= MAX_VALUE)
            return null;
        
        double[][] value = copy(array);
        value[x][y]++;
        return value;
    }
}
