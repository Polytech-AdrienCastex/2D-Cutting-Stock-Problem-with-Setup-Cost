/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.neighborselection;

import java.util.ArrayList;
import java.util.List;
import problem.solver.ImageKind;
import problem.solver.Pattern;
import problem.solver.PatternKind;
import problem.solver.operators.INeighborOperator;
import problem.solver.patternplacement.PatternPlacement;

/**
 *
 * @author Adrien
 */
public class NeighborStandard implements INeighborSelector
{
    public NeighborStandard(INeighborOperator[] operators, PatternPlacement patternPlacement)
    {
        this.operators = operators;
        this.patternPlacement = patternPlacement;
    }
    
    private final INeighborOperator[] operators;
    private final PatternPlacement patternPlacement;
    

    @Override
    public List<Pattern> getNeighbors(Pattern pattern, PatternKind patternKind)
    {
        List<Pattern> patterns = new ArrayList<>();
        
        for(INeighborOperator operator : operators)
            for(ImageKind imageKind : patternKind.getImageKinds())
            {
                Pattern newPattern = new Pattern(pattern, operator.getFrom(pattern.getImages(), imageKind));
                
                if(patternPlacement.isPossible(newPattern))
                    patterns.add(newPattern);
            }
        
        return patterns;
    }
}
