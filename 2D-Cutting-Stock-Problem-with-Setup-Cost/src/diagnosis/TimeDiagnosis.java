package diagnosis;

public class TimeDiagnosis
{
    public TimeDiagnosis()
    {
        startTime = System.nanoTime();
    }
    
    private long startTime;
    
    public long tick()
    {
        long t = System.nanoTime();
        long time = t - startTime;
        startTime = t;
        return time;
    }
    public long tickMs()
    {
        long t = System.currentTimeMillis();
        long time = t - startTime;
        startTime = t;
        return time;
    }
}
