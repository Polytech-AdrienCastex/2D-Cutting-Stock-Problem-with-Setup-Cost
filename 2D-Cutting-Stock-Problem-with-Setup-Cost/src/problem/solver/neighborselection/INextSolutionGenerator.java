/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.neighborselection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import problem.solver.ImageKind;
import problem.solver.Pattern;
import problem.solver.PatternKind;
import problem.solver.Solution;
import problem.solver.SolverException;
import problem.solver.operators.INeighborOperator;
import problem.solver.patternplacement.PatternPlacement;

/**
 *
 * @author Adrien
 */
public abstract class INextSolutionGenerator
{
    public INextSolutionGenerator(INeighborOperator[] operators, PatternKind patternKind, PatternPlacement patternPlacement)
    {
        this.operators = operators;
        this.patternKind = patternKind;
        this.patternPlacement = patternPlacement;
    }
    
    private final PatternKind patternKind;
    private final INeighborOperator[] operators;
    private final PatternPlacement patternPlacement;
    
    protected class Choice<T>
    {
        public Choice(T element, INeighborOperator operator, ImageKind imageKind)
        {
            this.element = element;
            this.operator = operator;
            this.imageKind = imageKind;
        }
        
        private final T element;
        private final INeighborOperator operator;
        private final ImageKind imageKind;

        public T getElement()
        {
            return element;
        }
        public INeighborOperator getOperator()
        {
            return operator;
        }
        public ImageKind getImageKind()
        {
            return imageKind;
        }
    }
    
    private List<Choice<Pattern>> getNeighbors(Pattern pattern)
    {
        List<Choice<Pattern>> patterns = new ArrayList<>();
        
        for(INeighborOperator operator : operators)
            for(ImageKind imageKind : patternKind.getImageKinds())
            {
                double[] values = operator.getFrom(pattern.getImageNumber(), imageKind);
                if(values != null)
                {
                    Pattern newPattern = new Pattern(pattern, values);

                    if(patternPlacement == null || patternPlacement.isPossible(newPattern))
                        patterns.add(new Choice(newPattern, operator, imageKind));
                }
            }
        
        return patterns;
    }
    
    protected List<Choice<Solution>> getNeighbors(Solution solution) throws SolverException
    {
        List<Choice<Solution>> solutions = new ArrayList<>();
        Pattern[] ps;
        Pattern[] patterns = solution.getPatterns();
        
        for(int i = 0; i < patterns.length; i++)
        {
            for(Choice<Pattern> p : getNeighbors(patterns[i]))
            {
                ps = new Pattern[patterns.length];
                for(int j = 0; j < patterns.length; j++)
                    if(j == i)
                        ps[j] = p.getElement();
                    else
                        ps[j] = patterns[j];
                
                Solution sol = new Solution(solution, ps);
                if(sol.isPossible())
                    solutions.add(new Choice(sol, p.getOperator(), p.getImageKind()));
            }
        }
        
        return solutions;
    }
    
    public Solution selectNextSolution(Solution current) throws SolverException
    {
        List<Choice<Solution>> solutions = getNeighbors(current);
        
        if(solutions.isEmpty())
            throw new SolverException("No more solution possible found.");
        
        return selectNextSolution(current, solutions);
    }
    
    protected abstract Solution selectNextSolution(Solution current, List<Choice<Solution>> solutions) throws SolverException;
}
