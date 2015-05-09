package problem.solver.neighborselection;

import diagnosis.AverageMaker;
import diagnosis.TimeDiagnosis;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import problem.solver.solution.Pattern;
import problem.solver.solution.Solution;
import problem.solver.SolverException;
import problem.solver.operators.INeighborOperator;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.patternplacement.PatternPlacement;

public abstract class INextSolutionGenerator
{
    public INextSolutionGenerator(INeighborOperator[] operators, PatternKind patternKind, PatternPlacement patternPlacement)
    {
        this(Arrays.asList(operators), patternKind, patternPlacement);
    }
    public INextSolutionGenerator(List<INeighborOperator> operators, PatternKind patternKind, PatternPlacement patternPlacement)
    {
        this.operators = operators;
        this.patternKind = patternKind;
        this.patternPlacement = patternPlacement;
    }
    
    protected final PatternKind patternKind;
    protected final List<INeighborOperator> operators;
    protected final PatternPlacement patternPlacement;
    
    protected class Choice<T>
    {
        public Choice(T element, INeighborOperator operator, ImageKind imageKind)
        {
            this.element = element;
            this.operator = operator;
            this.imageKind = imageKind;
        }
        
        private final T element;
        private final INeighborOperator operator;
        private final ImageKind imageKind;

        public T getElement()
        {
            return element;
        }
        public INeighborOperator getOperator()
        {
            return operator;
        }
        public ImageKind getImageKind()
        {
            return imageKind;
        }
    }
    
    private List<Choice<Pattern>> getNeighbors(Pattern pattern)
    {
        return operators
                .parallelStream()
                .flatMap(operator ->
                {
                    return patternKind
                        .getImageKinds()
                        .parallelStream()
                        .filter(imageKind -> operator.canApply(pattern.getImageNumber(), imageKind))
                        .map(imageKind ->
                        {
                            double[] values = operator.getFrom(pattern.getImageNumber(), imageKind);
                            if(values != null)
                            {
                                Pattern newPattern = new Pattern(values, pattern.getIndex());
                                
                                if(patternPlacement == null || patternPlacement.isPossible(newPattern))
                                    return new Choice<Pattern>(newPattern, operator, imageKind);
                            }
                            return null;
                        });
                })
                .filter(c -> c != null)
                .collect(Collectors.toList());
    }
    
    public static AverageMaker avg4 = new AverageMaker();
    public static AverageMaker avg5 = new AverageMaker();
    protected List<Choice<Solution>> getNeighbors(Solution solution) throws SolverException
    {
        final Pattern[] patterns = solution.getPatterns();
        
        return Stream.of(patterns)
                .parallel()
                .flatMap(p -> getNeighbors(p).stream())
                .map(p -> {
                    int id = p.getElement().getIndex();
                    Pattern[] ps = new Pattern[patterns.length];
                    for(int j = 0; j < patterns.length; j++)
                        if(j == id)
                            ps[j] = p.getElement();
                        else
                            ps[j] = patterns[j];

                    Solution sol = new Solution(solution, ps);
                    if(!sol.isImageMissing())
                        return new Choice<Solution>(sol, p.getOperator(), p.getImageKind());
                    return null;
                })
                .filter(c -> c != null)
                .collect(Collectors.toList());
    }
    
    public Solution selectNextSolution(Solution current) throws SolverException
    {
        TimeDiagnosis td1 = new TimeDiagnosis();
        List<Choice<Solution>> solutions = getNeighbors(current);
        avg4.add(td1.tick());
        
        if(solutions.isEmpty())
            throw new SolverException("No more possible solution found.");
        
        TimeDiagnosis td2 = new TimeDiagnosis();
        Solution s = selectNextSolution(current, solutions);
        avg5.add(td2.tick());
        return s;
    }
    
    protected abstract Solution selectNextSolution(Solution current, List<Choice<Solution>> solutions) throws SolverException;
    public abstract INextSolutionGenerator clone();
}
