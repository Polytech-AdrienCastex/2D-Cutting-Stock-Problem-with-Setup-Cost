/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.patternplacement;

import problem.solver.ImageKind;

/**
 *
 * @author Adrien
 */
public class ImageLocation
{
    public enum Direction
    {
        Vertical,
        Horizontal
    }
    
    public ImageLocation(int x, int y, Direction direction, ImageKind imageKind)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.imageKind = imageKind;
    }
    
    private final int x;
    private final int y;
    private final Direction direction;
    private final ImageKind imageKind;
    
    public int getY()
    {
        return y;
    }
    
    public int getX()
    {
        return x;
    }
    
    public Direction getDirection()
    {
        return direction;
    }
    
    public ImageKind getImageKind()
    {
        return imageKind;
    }
}
