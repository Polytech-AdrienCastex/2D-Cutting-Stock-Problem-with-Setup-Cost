package problem.solver.cutpacking;

import problem.solver.parameters.ImageKind;
import problem.solver.parameters.Sizable;
import problem.solver.patternplacement.ImageLocation;


public class Elmt extends Sizable implements Cloneable
{
    public Elmt(ImageKind ik)
    {
        super(ik.getWidth(), ik.getHeight());
        
        this.ik = ik;
    } 
    
    public Elmt(ImageKind ik, int pieces)
    {
        this(ik);
        
        if(pieces > 0)
            this.pieces = pieces;
    }
    
    private final ImageKind ik;
    private int pieces = 1;
    private Node fit;
    private boolean isRotated = false;
    
    
    /* operates a 90° rotation of the element */
    public void rotate()
    {
        isRotated = !isRotated;
    }
    
    public boolean isRotated()
    {
        return isRotated;
    }
    
    @Override
    public Elmt clone()
    {
        try
        {
            return (Elmt)super.clone();
        }
        catch (CloneNotSupportedException ex)
        {
            return null;
        }
    }
    
    /* makes the element 1 piece smaller*/
    public void reduce()
    {
        if(pieces > 1)
            pieces --;
    }
    
    /* check wether there's a possibility to reduce the gap between element extremity
    and template border (right & bottom) by splitting the block*/
    public boolean isBetter(Node node)
    {
        if(Localisation.FULL.equals(node.getLocal()))
            return true;
        
        int ndh = node.getH();
        int ndw = node.getW();
        int goal;
        int limit;
        int min;
        int temp;
        Elmt seeker = this.clone();
        
        switch(node.getLocal())
        {
            case VERTICAL:
                goal = ndh;
                limit = ndw;
                min = goal - this.getH();
                break;
                
            case HORIZONTAL:
                goal = ndw;
                limit = ndh;
                min = goal - this.getW();
                break;
                
            default:
                goal = -1;
                limit = -1;
                min = ndh + ndw;
                break;
        }
            
        while(seeker.getNbPieces() > 1)
        {
            seeker.reduce();
            if(seeker.getH() <= limit && (temp = goal - seeker.getW()) >= 0 && temp < min)
                return false;
            if(seeker.getW() <= limit && (temp = goal - seeker.getH()) >= 0 && temp < min)
                return false;
        }
        return true;        
    }
    
    public ImageKind getImageKind()
    {
        return ik;
    }
    
    public int getW()
    {
        int aggwidth = this.getWidth() * pieces;
        if(!isRotated)
        {            
            if(aggwidth > this.getHeight())
                return this.getHeight();
            else
                return aggwidth;
        }
        else
        {
            if(aggwidth > this.getHeight())
                return aggwidth;
            else
                return this.getHeight(); 
        }
    }
    
    public int getH()
    {
        int aggwidth = this.getWidth() * pieces;
        if(!isRotated)
        {
            if(aggwidth > this.getHeight())
                return aggwidth;
            else
                return this.getHeight();            
        }
        else
        {
            if(aggwidth > this.getHeight())
                return this.getHeight();
            else
                return aggwidth;
        }
    }
    
    
    public ImageLocation[] getElmtPositions()
    {
        if(fit == null)
            return new ImageLocation[0];
        
        ImageLocation.Direction dir;
        int nbPieces = this.getNbPieces();
        
        ImageLocation[] items = new ImageLocation[nbPieces];
        
        int x = fit.getX();
        int y = fit.getY();
        
        for(int i = 0; i < nbPieces; i++)
        {
            if(this.isRotated())
            {
                dir = ImageLocation.Direction.Vertical;
                if(i > 0)
                    x += this.getWidth();
            }
            else
            {
                dir = ImageLocation.Direction.Horizontal;
                if(i > 0)
                    y += this.getWidth();
            }
            
            items[i] = new ImageLocation(x, y, dir, ik);
        }
        
        return items;
    }
    
    public int getNbPieces()
    {
        return pieces;
    }

    public void setFit(Node fit)
    {
        this.fit = fit;
    }
}
