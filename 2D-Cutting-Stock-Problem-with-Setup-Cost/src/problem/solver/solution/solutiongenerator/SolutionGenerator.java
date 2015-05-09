package problem.solver.solution.solutiongenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.patternplacement.PatternPlacement;
import problem.solver.solution.Pattern;

public abstract class SolutionGenerator
{
    public SolutionGenerator(PatternPlacement patternPlacement, PatternKind patternKind)
    {
        this.patternPlacement = patternPlacement;
        this.patternKind = patternKind;
        
        this.patterns = null;
        this.rnd = new Random();
    }
    
    public abstract boolean compute(int nbPatterns);
    
    protected Pattern[] patterns;
    
    protected final PatternPlacement patternPlacement;
    protected final PatternKind patternKind;
    protected final Random rnd;
    
    public Pattern[] getPatterns()
    {
        return patterns;
    }
}
