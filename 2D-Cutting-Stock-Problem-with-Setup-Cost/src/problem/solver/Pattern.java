/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import problem.solver.neighborselection.INeighborSelector;

/**
 *
 * @author Adrien
 */
public class Pattern
{
    public Pattern(PatternKind patternKind, INeighborSelector neighborSelector)
    {
        this.patternKind = patternKind;
        this.neighborSelector = neighborSelector;
        this.images = new double[patternKind.getNumberOfImages()];
    }
    public Pattern(Pattern parent, double[] images)
    {
        this.patternKind = parent.patternKind;
        this.neighborSelector = parent.neighborSelector;
        this.images = images;
    }
    
    private final PatternKind patternKind;
    private final double[] images;
    private final INeighborSelector neighborSelector;
    
    public PatternKind getPatternKind()
    {
        return this.patternKind;
    }
    
    public double[] getImages()
    {
        return images;
    }
    public double getImage(int index)
    {
        return images[index];
    }
    public double getImage(ImageKind ik)
    {
        return getImage(ik.getPatternIndex());
    }
    
    public void Randomize(Random rnd)
    {
        Iterator<ImageKind> imageKinds = patternKind.getImageKinds().iterator();
        
        for(int i = 0; i < images.length; i++)
        {
            ImageKind ik = imageKinds.next();
            images[i] = rnd.nextInt(ik.getMaximumNumber());
        }
    }
    
    public Collection<Pattern> getNeighbors()
    {
        return neighborSelector.getNeighbors(this);
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder("( ");
        
        for(int i = 0; i < images.length; i++)
        {
            str.append(images[i]);
            str.append(" ");
        }
        str.append(")");
        
        return str.toString();
    }
}
