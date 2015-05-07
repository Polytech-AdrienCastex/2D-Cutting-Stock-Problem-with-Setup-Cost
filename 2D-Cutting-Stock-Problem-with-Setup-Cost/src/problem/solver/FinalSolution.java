package problem.solver;

import diagnosis.AverageMaker;
import java.io.PrintStream;
import problem.solver.parameters.PatternKind;
import problem.solver.parameters.ProblemParameters;
import java.util.ArrayList;
import problem.solver.neighborselection.INextSolutionGenerator;
import problem.solver.patternplacement.PatternPlacement;

public class FinalSolution
{
    private FinalSolution(int numberOfRestart, ArrayList<Exception> abortException, Solution solution, ProblemParameters problemParameters)
    {
        this.abortException = abortException;
        this.solution = solution;
        this.problemParameters = problemParameters;
        this.numberOfRestart = numberOfRestart;
    }
    
    private final ProblemParameters problemParameters;
    private final int numberOfRestart;
    
    private final ArrayList<Exception> abortException;
    public ArrayList<Exception> getAbortException()
    {
        return abortException;
    }
    
    private final Solution solution;
    public Solution getSolution()
    {
        return solution;
    }
    
    public int getFitness()
    {
        int result = 0;
        double[] coefs = problemParameters.getCoefs();
        double[] patterns = solution.getPatternNumbers();
        
        for(int i = 0; i < patterns.length && i < coefs.length; i++)
        {
            double value = (int)(Math.floor(coefs[i]) * Math.floor(patterns[i]));
            
            if(value > 0)
                result += value + problemParameters.getPatternPrice();
        }
        
        return result;
    }
    
    public double[] getPatterns()
    {
        return solution.getPatternNumbers();
    }
    public double[] getImages()
    {
        return solution.getImageNumbers();
    }
    
    public int getNumberOfPatterns()
    {
        int nb = 0;
        double[] coefs = problemParameters.getCoefs();
        double[] patterns = solution.getPatternNumbers();
        
        for(int i = 0; i < patterns.length; i++)
            if(coefs[i] * patterns[i] > 0)
                nb++;
        
        return nb;
    }

    public void printErrors()
    {
        printErrors(System.err);
    }
    public void printErrors(PrintStream ps)
    {
        getAbortException().stream().forEach(ex -> ex.printStackTrace(ps));
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Structure :\n");
        sb.append(this.getSolution());
        
        sb.append("\nImages :\n");
        for(double d : this.getImages())
        {
            sb.append((int)Math.floor(d));
            sb.append(" ");
        }
        
        sb.append("\nPatterns :\n");
        for(double d : this.getPatterns())
        {
            sb.append((int)Math.floor(d));
            sb.append(" ");
        }
        
        sb.append("\nFitness : ");
        sb.append(this.getFitness());
        
        sb.append("\nNumber of pattern : ");
        sb.append(this.getNumberOfPatterns());
        
        sb.append("\nAborted : ");
        sb.append(this.getAbortException().size());
        sb.append(" / ");
        sb.append(this.numberOfRestart + 1);
        
        return sb.toString();
    }
    
    
    public static FinalSolution findSolution(int maxNumberOfLoop, int numberOfRestart, ProblemParameters problemParameters, PatternKind pk, INextSolutionGenerator generator, PatternPlacement pp)
    {
        Solution bestSolution = null;
        ArrayList<Exception> abortException = new ArrayList<>();
        
        for(int restartId = 0; restartId <= numberOfRestart; restartId++)
        {
            if(restartId > 0)
                System.out.println("Restart n°" + restartId + " out of " + numberOfRestart);
            
            System.out.println("Recherche d'une solution de départ...");
            Solution solution = new Solution(problemParameters, pk, pp);
            System.out.println("Solution de départ trouvée. Application de l'algorithme...");
            //if(solution.isPossible())
            if(bestSolution == null)
                bestSolution = solution;
            else
                if(bestSolution.getFitnessValue() > solution.getFitnessValue())
                    bestSolution = solution;

            int i = 0;
            try
            {
                for(i = 0; i < maxNumberOfLoop; i++)
                {
                    solution = generator.selectNextSolution(solution);

            //if(solution.isPossible())
                    if(bestSolution.getFitnessValue() > solution.getFitnessValue())
                        bestSolution = solution;
                }
            }
            catch(Exception ex)
            {
                abortException.add(ex);
            }
        }
        
        return new FinalSolution(numberOfRestart, abortException, bestSolution, problemParameters);
    }
}
