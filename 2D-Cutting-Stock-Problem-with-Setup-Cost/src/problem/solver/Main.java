
package problem.solver;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;


public class Main
{
    public static double[][] getD(double[][] d, int i, int y)
    {
        d[i][y]++;
        return d;
    }
    public static void main(String[] args)
    {
        double[][] images = new double[2][2];
        images[0][0] = 5;
        Collection<double[][]> neighbors = new ArrayList<>();
        double[][] temp = images;
        
        for(int i = 0; i < images.length; i++)
            for(int p = 0; p < images[0].length; p++)
            {/*
                // -
                if(temp[i][p] > 0)
                {
                    temp[i][p]--;
                    neighbors.add(copy(temp));
                    temp[i][p]++;
                }*/
                
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
