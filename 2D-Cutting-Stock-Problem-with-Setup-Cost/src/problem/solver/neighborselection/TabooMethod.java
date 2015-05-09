package problem.solver.neighborselection;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import problem.solver.parameters.PatternKind;
import problem.solver.solution.Solution;
import problem.solver.SolverException;
import problem.solver.operators.INeighborOperator;
import problem.solver.parameters.ImageKind;
import problem.solver.patternplacement.PatternPlacement;
import problem.solver.solution.Pattern;

public class TabooMethod extends INextSolutionGenerator
{
    public TabooMethod(INeighborOperator[] operators, PatternKind patternKind, PatternPlacement patternPlacement, int numberOfTabooOperations)
    {
        super(operators, patternKind, patternPlacement);
        System.out.println("numberOfTabooOperations : " + numberOfTabooOperations);
        this.numberOfTabooOperations = numberOfTabooOperations;
        this.tabooList = new LinkedList<>();
        this.tabooListCurrentNumber = 0;
    }
    
    private int numberOfTabooOperations;
    private final Queue<Choice<Solution>> tabooList;
    private int tabooListCurrentNumber;
    
    private void addTaboo(Choice<Solution> solution)
    {/*
        if(numberOfTabooOperations == 0)
            numberOfTabooOperations = (solution.getElement().getPatterns().length * solution.getElement().getImageNumbers().length * operators.size())/5;
        */if(tabooListCurrentNumber == numberOfTabooOperations)
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
        
        if(current.getFitnessValue() <= choice.getElement().getFitnessValue())
            addTaboo(choice);
        /*
        if(current.getFitnessValue() == choice.getElement().getFitnessValue())
        {
            Pattern[] ps1 = current.getPatterns();
            Pattern[] ps2 = choice.getElement().getPatterns();
            
            System.out.println("``````````````````````````````````````");
            for(int p = 0; p < ps1.length; p++)
                for(ImageKind ik : patternKind.getImageKinds())
                {
                    if(ps1[p].getImageNumber(ik) != ps2[p].getImageNumber(ik))
                        System.out.println("Dif: " + ik.getPatternIndex());
                }
            
            System.out.println(current);
            System.out.println(choice.getElement());
            System.out.println("``````````````````````````````````````");
        }*/
        
        return choice.getElement();
    }

    @Override
    public INextSolutionGenerator clone()
    {
        return new TabooMethod(operators.stream().toArray(INeighborOperator[]::new), patternKind, patternPlacement, numberOfTabooOperations);
    }
}
