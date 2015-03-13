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
import problem.solver.neighborselection.INextSolutionGenerator;

/**
 *
 * @author Adrien
 */
public class Pattern
{
    public Pattern(PatternKind patternKind)
    {
        this.patternKind = patternKind;
        this.images = new double[patternKind.getNumberOfImages()];
    }
    public Pattern(Pattern parent, double[] images)
    {
        this.patternKind = parent.patternKind;
        this.images = images;
    }
    
    private final PatternKind patternKind;
    private final double[] images;
    /*
    public PatternKind getPatternKind()
    {
        return this.patternKind;
    }*/
    
    public double[] getImageNumber()
    {
        return images;
    }
    public double getImageNumber(int index)
    {
        return images[index];
    }
    public double getImageNumber(ImageKind ik)
    {
        return Pattern.this.getImageNumber(ik.getPatternIndex());
    }
    
    public static Pattern createRandomPatter(PatternKind patternKind)
    {
        return createRandomPatter(patternKind, new Random());
    }
    public static Pattern createRandomPatter(PatternKind patternKind, Random rnd)
    {
        Pattern pattern = new Pattern(patternKind);
        
        for(ImageKind ik : patternKind.getImageKinds())
        {
            pattern.images[ik.getPatternIndex()] = rnd.nextInt(ik.getMaximumNumber());
        }
        
        return pattern;
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
