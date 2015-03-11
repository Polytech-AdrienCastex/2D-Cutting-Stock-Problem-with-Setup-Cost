/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Adrien
 */
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
    
    public void addImageKind(ImageKind imageKind)
    {
        imageKinds.add(imageKind);
        nbImages++;
    }
}
