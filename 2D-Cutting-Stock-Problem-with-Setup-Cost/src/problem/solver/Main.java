package problem.solver;

import diagnosis.TimeDiagnosis;
import problem.solver.solution.Pattern;
import problem.solver.solution.Solution;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.parameters.ProblemParameters;
import problem.solver.cutpacking.CutInterfacer;
import problem.solver.neighborselection.INextSolutionGenerator;
import problem.solver.neighborselection.LocalMinimumReacher;
import problem.solver.neighborselection.TabooMethod;
import problem.solver.operators.Addition;
import problem.solver.operators.Annihilator;
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
    {
        final boolean testMode = false;
        
        if(testMode)
            test();
        else
        {
            for(String letter : new String[] { "S", "V", "L" })
                for(int i = 2; i <= 5; i++)
                {
                    TimeDiagnosis td = new TimeDiagnosis();
                    td.tickMs();
                    run(i + "0" + letter);
                    long t = td.tickMs();
                    long s = t / 1000L;
                    long m = s / 60L;
                    if(m > 0)
                    {
                        s -= m * 60L;
                        System.out.println("TIME : " + m + " min " + s + " sec");
                    }
                    else
                        System.out.println("TIME : " + s + " sec");
                }
        }
    }
    
    public static void test() throws IOException
    {
        /*
        PatternKind pk = new PatternKind(40, 60);
        pk.addImageKind(24, 30, 246);
        pk.addImageKind(13, 56, 562);
        pk.addImageKind(14, 22, 1000);
        pk.addImageKind(9, 23, 3498);
        pk.addImageKind(19, 23, 3498);
        */
        //PatternKind pk = FileLoader.loadFromFile(new File("S:\\OptDiscrete\\data\\data_20Salpha.txt"));
        PatternKind pk = FileLoader.loadFromFile(new File("D:\\Documents\\opt\\data\\data_50Lalpha.txt"));
        //PatternKind pk = FileLoader.loadFromFile(new File("F:\\OperaPortable\\a.txt"));
        
        PatternPlacement ppl = new CutInterfacer(pk);
        
        /*
        INextSolutionGenerator generator = new LocalMinimumReacher(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction()
        }, pk, ppl);
        */
        INextSolutionGenerator generator = new TabooMethod(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction()
        }, pk, ppl, 15);
        
        ProblemParameters pp = new ProblemParameters(25, 1, 20);
        FinalSolution fs = FinalSolution.findSolution(10000, 0, pp, pk, generator, ppl);
        
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
        
        System.out.println("****************** Diagnosis ******************");
        System.out.println("Get neighborhood : " + Math.round(INextSolutionGenerator.avg4.getValue()) + " ns");
        System.out.println("Next solution    : " + Math.round(INextSolutionGenerator.avg5.getValue()) + " ns");
    }
    
    public static void run(String name) throws IOException
    {
        System.out.println("///////////////////////////////////////////////");
        System.out.println("///////////////////////////////////////////////");
        System.out.println("//////////// " + name);
        System.out.println("///////////////////////////////////////////////");
        System.out.println("///////////////////////////////////////////////");
        
        PatternKind pk = FileLoader.loadFromFile(new File("D:\\Documents\\opt\\data\\data_"+name+"alpha.txt"));
        
        PatternPlacement ppl = new CutInterfacer(pk);
        
        INextSolutionGenerator generator = new TabooMethod(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction()
        }, pk, ppl, 15);
        
        ProblemParameters pp = new ProblemParameters(20, 1, 20);
        FinalSolution fs = FinalSolution.findSolution(20000, 3, pp, pk, generator, ppl);
        
        Pattern[] ps = fs.getSolution().getPatterns();
        for(int i = 0; i < ps.length; i++)
        {
            System.out.println("****************** PATTERN "+i+" ******************");
            System.out.println(ps[i]);
            for(ImageLocation il : ppl.getLocations(ps[i]))
            {
                System.out.println(il);
            }
        }
        System.out.println("************************************");
        
        System.out.println(fs);
    }
}
