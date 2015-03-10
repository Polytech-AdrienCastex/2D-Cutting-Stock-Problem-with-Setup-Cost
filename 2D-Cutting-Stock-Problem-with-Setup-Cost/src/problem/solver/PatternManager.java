/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

/**
 *
 * @author Adrien
 */
public class PatternManager
{
    public PatternManager(int nbPatterns, int nbImages, int maxImg, double[] constraintRightPart)
    {
        this.nbPatterns = nbPatterns;
        this.nbImages = nbImages;
        this.maxImg = maxImg;
        this.constraintRightPart = constraintRightPart;
        
        images = new double[nbImages][nbPatterns];
        Clear();
    }
    
    private int nbPatterns;
    private int nbImages;
    private int maxImg;
    
    private double[] constraintRightPart;
    private double[][] images;
    private double getImage(int pattern, int image)
    {
        return images[image][pattern];
    }
    private void setImage(int pattern, int image, int value)
    {
        images[image][pattern] = value;
    }
    
    private double[] getImages(int image)
    {
        return images[image];
    }
    
    public void Clear()
    {
        for(int i = 0; i < nbImages; i++)
            for(int j = 0; j < nbPatterns; j++)
                images[i][j] = 0;
    }
    
    private static double[][] copy(double[][] data)
    {
        double[][] copiedData = new double[data.length][data[0].length];
        
        for(int i = 0; i < data.length; i++)
            for(int j = 0; j < data[0].length; j++)
                copiedData[i][j] = data[i][j];
        
        return copiedData;
    }
    private Collection<double[][]> getNeighbors()
    {
        Collection<double[][]> neighbors = new ArrayList<>();
        double[][] temp = images;
        
        for(int i = 0; i < nbImages; i++)
            for(int p = 0; p < nbPatterns; p++)
            {
                // -
                if(temp[i][p] > 0)
                {
                    temp[i][p]--;
                    neighbors.add(copy(temp));
                    temp[i][p]++;
                }
                
                // +
                if(temp[i][p] < maxImg)
                {
                    temp[i][p]++;
                    neighbors.add(copy(temp));
                    temp[i][p]--;
                }
            }
        
        return neighbors;
    }
    
    public Collection<LinearConstraint> getConstraints()
    {
        Collection<LinearConstraint> constraints = new ArrayList<>();
        
        for(int pat = 0; pat < nbPatterns; pat++)
            constraints.add(new LinearConstraint(getImages(pat), Relationship.GEQ,  constraintRightPart[pat]));
        
        return constraints;
    }
    
}
