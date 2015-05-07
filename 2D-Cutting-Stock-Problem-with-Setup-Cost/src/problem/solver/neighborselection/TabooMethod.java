package problem.solver.neighborselection;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import problem.solver.parameters.PatternKind;
import problem.solver.Solution;
import problem.solver.SolverException;
import problem.solver.operators.INeighborOperator;
import problem.solver.patternplacement.PatternPlacement;

public class TabooMethod extends INextSolutionGenerator
{
    public TabooMethod(INeighborOperator[] operators, PatternKind patternKind, PatternPlacement patternPlacement, int numberOfTabooOperations)
    {
        super(operators, patternKind, patternPlacement);
        
        this.numberOfTabooOperations = numberOfTabooOperations;
        this.tabooList = new LinkedList<>();
        this.tabooListCurrentNumber = 0;
    }
    
    private final int numberOfTabooOperations;
    private final Queue<Choice<Solution>> tabooList;
    private int tabooListCurrentNumber;
    
    private void addTaboo(Choice<Solution> solution)
    {
        if(tabooListCurrentNumber == numberOfTabooOperations)
            tabooList.remove();
        else
            tabooListCurrentNumber++;
        
        tabooList.add(solution);
    }

    @Override
    protected Solution selectNextSolution(Solution current, List<Choice<Solution>> solutions) throws SolverException
    {
        Choice<Solution> choice = solutions.parallelStream()
                // Filter with only not banned operation
                .filter(s -> tabooList.parallelStream().noneMatch(c -> c.getImageKind().equals(s.getImageKind()) && c.getOperator().equals(s.getOperator())))
                // Minimize the fitness value
                .min(Comparator.comparing(s -> s.getElement().getFitnessValue()))
                // Return the value found or throw the exception
                .orElseThrow(() -> new SolverException("No more solution possible found."));
        
        if(current.getFitnessValue() < choice.getElement().getFitnessValue())
            addTaboo(choice);
        
        return choice.getElement();
    }
}
