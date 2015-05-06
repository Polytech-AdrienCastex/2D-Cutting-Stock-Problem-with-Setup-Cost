package problem.solver;

import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import java.util.Random;

public class Pattern
{
    public Pattern(PatternKind patternKind)
    {
        this.images = new double[patternKind.getNumberOfImages()];
    }
    public Pattern(double[] images)
    {
        this.images = images;
    }
    
    private final double[] images;
    
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
    
    public static Pattern createPattern(PatternKind patternKind, double... values)
    {
        Pattern pattern = new Pattern(patternKind);
        
        patternKind.getImageKinds()
                .stream()
                .map((ik) -> ik.getPatternIndex())
                .forEach((id) -> pattern.images[id] = values[id]);
        
        return pattern;
    }
    public static Pattern createRandomPattern(PatternKind patternKind)
    {
        return createRandomPattern(patternKind, new Random());
    }
    public static Pattern createRandomPattern(PatternKind patternKind, Random rnd)
    {
        Pattern pattern = new Pattern(patternKind);
        
        for(ImageKind ik : patternKind.getImageKinds())
            if(ik.getMaximumNumber() / 4 == 0)
                pattern.images[ik.getPatternIndex()] = rnd.nextInt(ik.getMaximumNumber());
            else
                pattern.images[ik.getPatternIndex()] = rnd.nextInt(ik.getMaximumNumber() / 4);
        
        return pattern;
    }
    public static Pattern createRandomPattern(PatternKind patternKind, Random rnd, boolean[] t)
    {
        Pattern pattern = new Pattern(patternKind);
        
        for(ImageKind ik : patternKind.getImageKinds())
            if(ik.getMaximumNumber() / 4 - 1 <= 0)
                pattern.images[ik.getPatternIndex()] = t[ik.getPatternIndex()] ? rnd.nextInt(ik.getMaximumNumber()) : rnd.nextInt(ik.getMaximumNumber() - 1) + 1;
            else
                pattern.images[ik.getPatternIndex()] = t[ik.getPatternIndex()] ? rnd.nextInt(ik.getMaximumNumber() / 4) : rnd.nextInt(ik.getMaximumNumber() / 4 - 1) + 1;
        
        return pattern;
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
