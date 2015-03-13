
package problem.solver.neighborselection;

import java.util.ArrayList;
import java.util.Comparator;
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
 * 
 * Converge vers le minimum local.
 */
public class NeighborStandard extends INeighborSelector
{
    public NeighborStandard(INeighborOperator[] operators, PatternPlacement patternPlacement)
    {
        this.operators = operators;
        this.patternPlacement = patternPlacement;
    }
    
    private final INeighborOperator[] operators;
    private final PatternPlacement patternPlacement;
    
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