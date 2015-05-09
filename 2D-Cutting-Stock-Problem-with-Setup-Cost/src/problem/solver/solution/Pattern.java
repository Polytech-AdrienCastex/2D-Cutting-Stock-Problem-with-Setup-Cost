package problem.solver.solution;

import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;

public class Pattern
{
    public Pattern(PatternKind patternKind, int index)
    {
        this.images = new double[patternKind.getNumberOfImages()];
        this.index = index;
    }
    public Pattern(double[] images, int index)
    {
        this.images = images;
        this.index = index;
    }
    
    private final int index;
    private final double[] images;
    
    public int getIndex()
    {
        return index;
    }
    
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
        return getImageNumber(ik.getPatternIndex());
    }
    
    public double[] toDoubles()
    {
        return getImageNumber();
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder("[ ");
        
        for(int i = 0; i < images.length; i++)
        {
            if(i != 0)
                str.append(", ");
            str.append((int)images[i]);
        }
        str.append(" ]");
        
        return str.toString();
    }
}
