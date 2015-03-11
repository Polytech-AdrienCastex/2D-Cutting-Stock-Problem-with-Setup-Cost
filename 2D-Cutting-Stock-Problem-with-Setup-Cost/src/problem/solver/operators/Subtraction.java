/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.operators;

import problem.solver.ImageKind;

/**
 *
 * @author Adrien
 */
public class Subtraction extends INeighborOperator
{
    @Override
    public double[] getFrom(double[] array, ImageKind imageKind)
    {
        if(array[imageKind.getPatternIndex()] <= 0)
            return null;
        
        double[] value = copy(array);
        value[imageKind.getPatternIndex()]--;
        return value;
    }
}
