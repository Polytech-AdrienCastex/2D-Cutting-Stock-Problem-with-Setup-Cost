
package problem.solver.neighborselection;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import problem.solver.PatternKind;
import problem.solver.Solution;
import problem.solver.SolverException;
import problem.solver.operators.INeighborOperator;
import problem.solver.patternplacement.PatternPlacement;

public class TabouMethod extends INextSolutionGenerator
{
    public TabouMethod(INeighborOperator[] operators, PatternKind patternKind, PatternPlacement patternPlacement, int numberOfTabouOperations)
    {
        super(operators, patternKind, patternPlacement);
        
        this.numberOfTabouOperations = numberOfTabouOperations;
        this.tabouList = new LinkedList<>();
        this.tabouListCurrentNumber = 0;
    }
    
    private final int numberOfTabouOperations;
    private final Queue<Choice<Solution>> tabouList;
    private int tabouListCurrentNumber;
    
    private void addTabou(Choice<Solution> solution)
    {
        if(tabouListCurrentNumber == numberOfTabouOperations)
            tabouList.remove();
        else
            tabouListCurrentNumber++;
        
        tabouList.add(solution);
    }

    @Override
    protected Solution selectNextSolution(Solution current, List<Choice<Solution>> solutions) throws SolverException
    {
        Choice<Solution> choice = solutions.stream()
                // Filter with only not banned operation
                .filter(s -> tabouList.stream().noneMatch(c -> c.getImageKind().equals(s.getImageKind()) && c.getOperator().equals(s.getOperator())))
                // Minimize the fitness value
                .min(Comparator.comparing(s -> s.getElement().getFitnessValue()))
                .get();
        
        if(current.getFitnessValue() < choice.getElement().getFitnessValue())
            addTabou(choice);
        
        return choice.getElement();
    }
}
