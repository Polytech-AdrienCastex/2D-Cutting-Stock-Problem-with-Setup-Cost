/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.neighborselection;

import java.util.List;
import problem.solver.Pattern;
import problem.solver.PatternKind;

/**
 *
 * @author Adrien
 */
public interface INeighborSelector
{
    List<Pattern> getNeighbors(Pattern pattern, PatternKind patternKind);
}
