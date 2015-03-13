/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.neighborselection;

import java.util.ArrayList;
import java.util.List;
import problem.solver.Pattern;
import problem.solver.PatternKind;
import problem.solver.Solution;
import problem.solver.SolverException;

/**
 *
 * @author Adrien
 */
public abstract class INeighborSelector
{
    public List<Solution> getNeighbors(Solution solution) throws SolverException
    {
        List<Solution> solutions = new ArrayList<>();
        Pattern[] ps;
        Pattern[] patterns = solution.getPatterns();
        
        for(int i = 0; i < patterns.length; i++)
        {
            for(Pattern p : patterns[i].getNeighbors())
            {
                ps = new Pattern[patterns.length];
                for(int j = 0; j < patterns.length; j++)
                    if(j == i)
                        ps[j] = p;
                    else
                        ps[j] = patterns[j];
                
                solutions.add(new Solution(solution, ps));
            }
        }
        
        return solutions;
    }
    
    public abstract List<Pattern> getNeighbors(Pattern pattern);
    public abstract Solution selectSolution(List<Solution> solutions) throws SolverException;
}
