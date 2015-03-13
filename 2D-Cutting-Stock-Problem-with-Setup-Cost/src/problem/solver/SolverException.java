/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package problem.solver;

/**
 *
 * @author Adrien
 */
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
