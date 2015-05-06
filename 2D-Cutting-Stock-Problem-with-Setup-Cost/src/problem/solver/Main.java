package problem.solver;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.parameters.ProblemParameters;
import problem.solver.cutpacking.CutInterfacer;
import problem.solver.neighborselection.INextSolutionGenerator;
import problem.solver.neighborselection.TabooMethod;
import problem.solver.operators.Addition;
import problem.solver.operators.Div;
import problem.solver.operators.INeighborOperator;
import problem.solver.operators.Mul;
import problem.solver.operators.Subtraction;
import problem.solver.patternplacement.ImageLocation;
import problem.solver.patternplacement.PatternPlacement;
import problem.test.FileLoader;


public class Main
{
    public static void main(String[] args) throws SolverException, IOException
    {/*
        PatternKind pk = new PatternKind(40, 60);
        pk.addImageKind(24, 30, 246);
        pk.addImageKind(13, 56, 562);
        pk.addImageKind(14, 22, 1000);
        pk.addImageKind(9, 23, 3498);
        pk.addImageKind(19, 23, 3498);
        */
        PatternKind pk = FileLoader.loadFromFile(new File("C:\\Users\\Adrien\\Downloads\\download.txt"));
        
        PatternPlacement ppl = new CutInterfacer(pk);
        
        /*
        INextSolutionGenerator generator = new LocalMinimumReacher(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction()
        }, pk, ppl);*/
        
        INextSolutionGenerator generator = new TabooMethod(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction(),
            new Mul(),
            new Div()
        }, pk, ppl, 20);
        
        ProblemParameters pp = new ProblemParameters(5, 1, 20);
        FinalSolution fs = FinalSolution.findSolution(1000, 5, pp, pk, generator, ppl);
        
        for(Pattern p : fs.getSolution().getPatterns())
        {
            System.out.println("****************** NEW PATTERN ******************");
            System.out.println(p);
            for(ImageLocation il : ppl.getLocations(p))
            {
                System.out.println(il);
            }
        }
        System.out.println("************************************");
        
        System.out.println(fs);
    }
    
}
