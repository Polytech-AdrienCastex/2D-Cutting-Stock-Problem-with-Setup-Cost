
package problem.solver.neighborselection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
 * 
 * Méthode tabou
 */
public class NeighborTabou extends INeighborSelector
{
    public NeighborTabou(INeighborOperator[] operators, int tabouListSize, PatternPlacement patternPlacement)
    {
        this.operators = operators;
        this.patternPlacement = patternPlacement;
        this.tabouList = new LinkedList<>();
        this.maxTabouSize = 0;
    }
    
    private class TabouElement
    {
        public TabouElement(INeighborOperator operator, int index)
        {
            this.operator = operator;
            this.index = index;
        }
        
        private INeighborOperator operator;
        private int index;
        
        public boolean isMatching(INeighborOperator operator, int index)
        {
            return this.index == index && this.operator.equals(operator);
        }
    }
    
    private final int maxTabouSize;
    private final Queue<TabouElement> tabouList;
    private void appendTabouValue(INeighborOperator operator, int index)
    {
        tabouList.add(new TabouElement(operator, index));
        if(tabouList.size() >= maxTabouSize)
            tabouList.poll();
    }
    
    private final INeighborOperator[] operators;
    private final PatternPlacement patternPlacement;
    
    private class SolutionTabou
    {
        public SolutionTabou(Solution solution, TabouElement tabouElement)
        {
            this.solution = solution;
            this.tabouElement = tabouElement;
        }
        
        private Solution solution;
        private TabouElement tabouElement;
        
        
    }
    
    public Solution selectNextSolution(Solution solution)
    {
        
    }
    
    @Override
    public Solution selectSolution(List<Solution> solutions) throws SolverException
    {
        if(solutions.isEmpty())
            throw new SolverException("No usable operator.");
        
        Stream<Solution> stream = solutions.stream().filter(s -> s.isPossible());
        
        if(stream.count() == 0)
            throw new SolverException("No usable operator.");
            
        return (Solution)stream.min(Comparator.comparing(s -> (int)s.getFitnessValue())).get();
    }
    
    @Override
    public List<Pattern> getNeighbors(Pattern pattern)
    {
        List<Pattern> patterns = new ArrayList<>();
        PatternKind patternKind = pattern.getPatternKind();
        
        for(INeighborOperator operator : operators)
            for(ImageKind imageKind : patternKind.getImageKinds())
            {
                double[] values = operator.getFrom(pattern.getImages(), imageKind);
                if(values != null)
                {
                    Pattern newPattern = new Pattern(pattern, values);

                    if(patternPlacement == null || patternPlacement.isPossible(newPattern))
                        patterns.add(newPattern);
                }
            }
        
        return patterns;
    }
}
