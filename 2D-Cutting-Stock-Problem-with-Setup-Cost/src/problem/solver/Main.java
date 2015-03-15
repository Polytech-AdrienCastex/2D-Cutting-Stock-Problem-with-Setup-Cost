package problem.solver;

import problem.solver.neighborselection.INextSolutionGenerator;
import problem.solver.neighborselection.TabouMethod;
import problem.solver.operators.Addition;
import problem.solver.operators.INeighborOperator;
import problem.solver.operators.Subtraction;


public class Main
{
    public static double[][] getD(double[][] d, int i, int y)
    {
        d[i][y]++;
        return d;
    }
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
        }, pk, null);
        */
        INextSolutionGenerator generator = new TabouMethod(new INeighborOperator[]
        {
            new Addition(),
            new Subtraction()
        }, pk, null, 100);
        
        Solution solution = new Solution(3, pk, generator);
        System.out.println(solution);
        
        int i = 0;
        try
        {
            for(i = 0; i < 500; i++)
            {
                System.out.println("*******************************");
                //solution.getNeighbors().stream().forEach(s -> System.out.println(s));

                solution = generator.selectNextSolution(solution);
                System.out.println(solution);
            }
        }
        catch(Exception ex)
        {}
        System.out.println("*************FINAL***************");
        System.out.println("i = " + i);
        System.out.println(solution);
        
        System.out.println("----");
        for(double d : solution.getPatternNumbers())
            System.out.print("'" + d + "' ");
        
        
        
        
        
        
        
        
        /*
        double[][] images = new double[2][2];
        images[0][0] = 5;
        Collection<double[][]> neighbors = new ArrayList<>();
        double[][] temp = images;
        
        for(int i = 0; i < images.length; i++)
            for(int p = 0; p < images[0].length; p++)
            {
                
                // +
                if(temp[i][p] < 10)
                {
                    temp[i][p]++;
                    neighbors.add(getD(temp, i, p));
                    temp[i][p]--;
                }
            }
        for(double[][] t : neighbors)
        {
            for(int i = 0; i < images.length; i++)
                for(int p = 0; p < images[0].length; p++)
                System.out.print(t[i][p]);
            System.out.println("");
        }
        
        
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] { 2, 2, 1 }, 0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[] { 1, 1, 0 }, Relationship.GEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 1, 0, 1 }, Relationship.GEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 1, 1, 0 }, Relationship.GEQ,  1));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new MaxIter(100), f, new LinearConstraintSet(constraints), GoalType.MINIMIZE, new NonNegativeConstraint(true));
        /*
        System.out.println(solution.getValue());
        System.out.println(solution.getFirst());
        System.out.println(solution.getSecond());
        System.out.println(solution.getValue());
        for(double d : solution.getPoint())
        System.out.println(d);*/
    }
    
}
