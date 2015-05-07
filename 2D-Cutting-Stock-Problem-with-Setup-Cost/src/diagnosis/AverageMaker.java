package diagnosis;

public class AverageMaker
{
    public AverageMaker()
    {
        value = 0;
        number = 0;
    }
    
    private double value;
    private int number;
    
    public void add(double v)
    {
        value = (value * number + v) / (number+1);
        number++;
    }
    
    public double getValue()
    {
        return value;
    }
}
