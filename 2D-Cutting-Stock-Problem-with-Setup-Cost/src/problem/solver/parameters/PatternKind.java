package problem.solver.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PatternKind extends Sizable
{
    public PatternKind(int sizeW, int sizeH)
    {
        super(sizeW, sizeH);
        
        this.nbImages = 0;
        this.imageKinds = new ArrayList<>();
    }
    
    private final List<ImageKind> imageKinds;
    private int nbImages;
    
    public Collection<ImageKind> getImageKinds()
    {
        return imageKinds;
    }
    
    public int getNumberOfImages()
    {
        return nbImages;
    }
    
    public void addImageKind(int sizeW, int sizeH, int demand)
    {
        addImageKind(new ImageKind(sizeW, sizeH, demand, this));
    }
    public void addImageKind(ImageKind imageKind)
    {
        imageKinds.add(imageKind);
        nbImages++;
    }
}
