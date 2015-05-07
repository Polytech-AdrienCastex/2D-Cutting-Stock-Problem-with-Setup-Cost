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
import problem.solver.Pattern;
import problem.solver.Solution;
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
    
    private final PatternKind patternKind;
    private final List<INeighborOperator> operators;
    private final PatternPlacement patternPlacement;
    
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
        //List<Choice<Pattern>> patterns = new ArrayList<>();
        
        //TimeDiagnosis td = new TimeDiagnosis();
        
        return operators
                .parallelStream()
                .flatMap(operator ->
        {
            return patternKind
                    .getImageKinds()
                    .parallelStream()
                    .map(imageKind ->
                    {
                        double[] values = operator.getFrom(pattern.getImageNumber(), imageKind);
                        if(values != null)
                        {
                            Pattern newPattern = new Pattern(values);

                            if(patternPlacement == null || patternPlacement.isPossible(newPattern))
                                return new Choice<Pattern>(newPattern, operator, imageKind);
                        }
                        return null;
                    });
        }).
                filter(c -> c != null)
                .collect(Collectors.toList());
        /*
        //operators.parallelStream().forEach(operator -> {
        for(INeighborOperator operator : operators)
            
            for(ImageKind imageKind : patternKind.getImageKinds())
            {
                double[] values = operator.getFrom(pattern.getImageNumber(), imageKind);
                if(values != null)
                {
                    Pattern newPattern = new Pattern(values);

                    if(patternPlacement == null || patternPlacement.isPossible(newPattern))
                        patterns.add(new Choice(newPattern, operator, imageKind));
                }
            }/*
            for(ImageKind imageKind : patternKind.getImageKinds())
            {
                double[] values = operator.getFrom(pattern.getImageNumber(), imageKind);
                if(values != null)
                {
                    Pattern newPattern = new Pattern(values);

                    if(patternPlacement == null || patternPlacement.isPossible(newPattern))
                        return new Choice(newPattern, operator, imageKind);
                    else
                        return null;
                }
            }});
        //avg3.add(td.tick());
        
        return patterns;*/
    }
    
    public static AverageMaker avg1 = new AverageMaker();
    public static AverageMaker avg2 = new AverageMaker();
    public static AverageMaker avg3 = new AverageMaker();
    public static AverageMaker avg4 = new AverageMaker();
    public static AverageMaker avg5 = new AverageMaker();
    protected List<Choice<Solution>> getNeighbors(Solution solution) throws SolverException
    {
        //List<Choice<Solution>> solutions = new ArrayList<>();
        Pattern[] patterns = solution.getPatterns();
        
        //TimeDiagnosis td1 = new TimeDiagnosis();
        
        /*IntStream.range(0, patterns.length)
                .parallel()
                .forEach(i ->*/
        
        return Arrays.asList(patterns).parallelStream()
                .flatMap(p -> getNeighbors(p).stream())
                .map(p -> {
                    int id = p.getImageKind().getPatternIndex();
                    Pattern[] ps = new Pattern[patterns.length];
                    for(int j = 0; j < patterns.length; j++)
                        if(j == id)
                            ps[j] = p.getElement();
                        else
                            ps[j] = patterns[j];

                    Solution sol = new Solution(solution, ps);
                    if(sol.isPossible())
                        return new Choice<Solution>(sol, p.getOperator(), p.getImageKind());
                    return null;
                }).filter(c -> c != null).collect(Collectors.toList());
        /*
        for(int i = 0; i < patterns.length; i++)
        {
            //TimeDiagnosis td2 = new TimeDiagnosis();
            //td1.tick();
            
            //getNeighbors(patterns[i]).parallelStream().forEach(p ->
            for(Choice<Pattern> p : getNeighbors(patterns[i]))
            {
                //td2.tick();
                Pattern[] ps = new Pattern[patterns.length];
                for(int j = 0; j < patterns.length; j++)
                    if(j == i)
                        ps[j] = p.getElement();
                    else
                        ps[j] = patterns[j];
                
                Solution sol = new Solution(solution, ps);
                if(sol.isPossible())
                    solutions.add(new Choice(sol, p.getOperator(), p.getImageKind()));
                //avg2.add(td2.tick());
            }//);
            //avg1.add(td1.tick());
        }//);
        
        return solutions;*/
    }
    
    public Solution selectNextSolution(Solution current) throws SolverException
    {
        TimeDiagnosis td1 = new TimeDiagnosis();
        List<Choice<Solution>> solutions = getNeighbors(current);
        avg4.add(td1.tick());
        
        if(solutions.isEmpty())
            throw new SolverException("No more solution possible found.");
        
        TimeDiagnosis td2 = new TimeDiagnosis();
        Solution s = selectNextSolution(current, solutions);
        avg5.add(td2.tick());
        return s;
    }
    
    protected abstract Solution selectNextSolution(Solution current, List<Choice<Solution>> solutions) throws SolverException;
}
