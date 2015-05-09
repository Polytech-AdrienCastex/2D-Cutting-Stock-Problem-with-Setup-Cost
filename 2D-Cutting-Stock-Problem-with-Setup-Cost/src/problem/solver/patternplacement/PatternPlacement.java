/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.patternplacement;

import problem.solver.solution.Pattern;
import problem.solver.parameters.PatternKind;

/**
 *
 * @author Adrien
 */
public abstract class PatternPlacement
{
    public PatternPlacement(PatternKind patternKind)
    {
        this.patternKind = patternKind;
    }
    
    protected final PatternKind patternKind;
    
    public boolean isPossible(Pattern patterns)
    {
        ImageLocation[] locations = getLocations(patterns);
        return locations != null && locations.length > 0;
    }
    
    public abstract ImageLocation[] getLocations(Pattern pattern);
}
