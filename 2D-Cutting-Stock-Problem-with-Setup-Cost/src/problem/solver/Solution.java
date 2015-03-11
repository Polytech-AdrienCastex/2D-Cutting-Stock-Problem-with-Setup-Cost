/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver;

import problem.solver.neighborselection.INeighborSelector;

/**
 *
 * @author Adrien
 */
public class Solution
{
    public Solution(int numberOfPatterns, PatternKind patternKind, INeighborSelector neighborSelector)
    {
        this.patterns = new Pattern[numberOfPatterns];
        
        for(int i = 0; i < numberOfPatterns; i++)
            patterns[i] = new Pattern(patternKind, neighborSelector);
    }
    
    private final Pattern[] patterns;
    
    public int getFitnessValue()
    {
        return 0;
    }
}
