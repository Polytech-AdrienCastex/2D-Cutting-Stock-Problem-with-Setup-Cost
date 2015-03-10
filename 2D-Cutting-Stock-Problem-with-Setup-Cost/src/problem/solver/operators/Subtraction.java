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
public class Subtraction extends INeighborOperator
{
    @Override
    public double[][] getFrom(double[][] array, int x, int y)
    {
        double[][] value = copy(array);
        value[x][y]--;
        return value;
    }
}
