package problem.solver;

import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.parameters.ProblemParameters;
import problem.solver.cutpacking.CutInterfacer;
import problem.solver.neighborselection.INextSolutionGenerator;
import problem.solver.neighborselection.TabooMethod;
import problem.solver.operators.Addition;
import problem.solver.operators.INeighborOperator;
import problem.solver.operators.Subtraction;


public class Main
{
    public static void main(String[] args) throws SolverException
    {
        PatternKind pk = new PatternKind(40, 60);
        new ImageKind(24, 30, 246, pk);
        new ImageKind(13, 56, 562, pk);
        new ImageKind(14, 22, 1000, pk);
        new ImageKind(9, 23, 3498, pk);
        
        /*
        INextSolutionGenerator generator = new LocalMinimumReacher(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction()
        }, pk, new CutInterfacer(pk));*/
        
        INextSolutionGenerator generator = new TabooMethod(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction()
        }, pk, new CutInterfacer(pk), 20);
        
        ProblemParameters pp = new ProblemParameters(4, 1, 10);
        FinalSolution fs = FinalSolution.findSolution(5000, 50, pp, pk, generator);
        
        System.out.println(fs);
    }
    
}
