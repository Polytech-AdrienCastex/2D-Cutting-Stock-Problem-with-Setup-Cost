package problem.solver.parameters;

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
