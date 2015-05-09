package problem.solver.solution.solutiongenerator;

import java.util.Arrays;
import problem.solver.parameters.PatternKind;
import problem.solver.patternplacement.PatternPlacement;
import problem.solver.solution.Pattern;

public class StaticSolutionGenerator extends SolutionGenerator
{
    public StaticSolutionGenerator(PatternPlacement patternPlacement, PatternKind patternKind, int maxLimit1)
    {
        super(patternPlacement, patternKind);
        
        this.maxLimit1 = maxLimit1;
    }
    
    protected final int maxLimit1;
    
    @Override
    public boolean compute(int nbPatterns)
    {
        this.patterns = new Pattern[nbPatterns];

        for(int i = 0; i < Math.min(patternKind.getNumberOfImages(), patterns.length); i++)
        {
            double[] imgs = new double[patternKind.getNumberOfImages()];
            imgs[i] = 1;//rnd.nextInt(2);//rnd.nextInt(patternKind.getImageKinds().stream().skip(i).findFirst().get().getMaximumNumber() - 1) + 1;
            patterns[i] = new Pattern(imgs, i);
        }

        if(patternKind.getNumberOfImages() < patterns.length)
        {
            for(int i = patternKind.getNumberOfImages(); i < patterns.length; i++)
            {
                double[] imgs = new double[patternKind.getNumberOfImages()];
                imgs[i % patternKind.getNumberOfImages()] = rnd.nextInt(2);//rnd.nextInt(patternKind.getImageKinds().stream().skip(i % patternKind.getNumberOfImages()).findFirst().get().getMaximumNumber() - 1) + 1;
                patterns[i] = new Pattern(imgs, i);
            }
        }
        else if(patternKind.getNumberOfImages() > patterns.length)
        {
            int id = -1;
            for(int i = patterns.length; i < patternKind.getNumberOfImages(); i++)
            {
                int nbTry = 0;
                int max = patternKind.getImageKinds().stream().skip(i).findFirst().get().getMaximumNumber();
                do
                {
                    if(id == 0)
                    {
                        nbTry++;
                        if(nbTry >= maxLimit1)
                            return false;
                    }
                    id = (id + 1) % patterns.length;
                    double[] imgs = Arrays.copyOf(patterns[id].getImageNumber(), patternKind.getNumberOfImages());
                    imgs[i] = 1;//rnd.nextInt(2);//rnd.nextInt(max - 1)/2 + 1;
                    patterns[id] = new Pattern(imgs, i);
                } while(!patternPlacement.isPossible(patterns[id]));
            }
        }
        
        return true;
    }
}
