package problem.solver.solution.solutiongenerator;

import java.util.ArrayList;
import java.util.List;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.patternplacement.PatternPlacement;
import problem.solver.solution.Pattern;

public class IncrementalSolutionGenerator extends SolutionGenerator
{
    public IncrementalSolutionGenerator(PatternPlacement patternPlacement, PatternKind patternKind, int maxLimit1, int maxLimit2, double coef)
    {
        super(patternPlacement, patternKind);
        
        this.maxLimit1 = maxLimit1;
        this.maxLimit2 = maxLimit2;
        this.coef = coef;
    }
    
    protected final int maxLimit1;
    protected final int maxLimit2;
    protected final double coef;
    
    @Override
    public boolean compute(int nbPatterns)
    {
        patterns = new Pattern[nbPatterns];

        List<List<ImageKind>> m = new ArrayList<>();
        for(int i = 0; i < patterns.length; i++)
            m.add(new ArrayList<>());

        boolean is_possible;
        int limit = maxLimit1;
        do
        {
            limit--;
            if(limit <= 0)
                return false;

            for(int i = 0; i < patterns.length; i++)
                m.get(i).clear();

            for(ImageKind ik : patternKind.getImageKinds())
                m.get(rnd.nextInt(patterns.length)).add(ik);

            for(int i = 0; i < m.size(); i++)
            {
                double[] imgs = new double[patternKind.getNumberOfImages()];
                for(ImageKind ik : m.get(i))
                    imgs[ik.getPatternIndex()] = 1;
                patterns[i] = new Pattern(imgs, i);
            }

            is_possible = true;
            for(Pattern p : patterns)
                if(!patternPlacement.isPossible(p))
                {
                    is_possible = false;
                    break;
                }
        } while(!is_possible);

        for(int i = 0; i < nbPatterns; i++)
        {
            Pattern ptn = null;
            double[] imgs = new double[patternKind.getNumberOfImages()];
            double nbDiv = 1.0;
            limit = maxLimit2;

            do
            {
                limit--;
                if(limit <= 0)
                    return false;

                for(ImageKind ik : m.get(i))
                    if((ik.getMaximumNumber() - 1) / ((int)nbDiv) <= 1)
                        imgs[ik.getPatternIndex()] = 1;
                    else
                        imgs[ik.getPatternIndex()] = rnd.nextInt((ik.getMaximumNumber() - 1) / ((int)nbDiv)) + 1;
                ptn = new Pattern(imgs, i);
                nbDiv += coef;
            } while(!patternPlacement.isPossible(ptn));

            patterns[i] = ptn; // validation of the pattern
        }
        
        return true;
    }
}
