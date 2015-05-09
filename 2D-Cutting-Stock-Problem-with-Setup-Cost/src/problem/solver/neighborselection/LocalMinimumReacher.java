package problem.solver.neighborselection;

import java.util.Comparator;
import java.util.List;
import problem.solver.parameters.PatternKind;
import problem.solver.solution.Solution;
import problem.solver.SolverException;
import problem.solver.operators.INeighborOperator;
import problem.solver.patternplacement.PatternPlacement;

public class LocalMinimumReacher extends INextSolutionGenerator
{
    public LocalMinimumReacher(INeighborOperator[] operators, PatternKind patternKind, PatternPlacement patternPlacement)
    {
        super(operators, patternKind, patternPlacement);
    }

    @Override
    protected Solution selectNextSolution(Solution current, List<Choice<Solution>> solutions) throws SolverException
    {
        return solutions.parallelStream()
                // Minimize the fitness value
                .min(Comparator.comparing(s -> s.getElement().getFitnessValue()))
                .get().getElement();
    }

    @Override
    public INextSolutionGenerator clone()
    {
        return this;
    }
}
