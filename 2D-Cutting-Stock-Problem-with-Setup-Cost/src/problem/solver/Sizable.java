/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver;

/**
 *
 * @author Adrien
 */
public abstract class Sizable
{
    public Sizable(int sizeW, int sizeH)
    {
        this.sizeW = sizeW;
        this.sizeH = sizeH;
    }
    
    private final int sizeW;
    private final int sizeH;
    
    public int getWidth()
    {
        return sizeW;
    }
    
    public int getHeight()
    {
        return sizeH;
    }
}
