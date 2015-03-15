package problem.solver;

public class SolverException extends Exception
{
    public SolverException(String message)
    {
        this.msg = message;
    }
    
    private String msg;
    
    public String getMessage()
    {
        return msg;
    }

    @Override
    public String toString()
    {
        return getMessage();
    }
}
