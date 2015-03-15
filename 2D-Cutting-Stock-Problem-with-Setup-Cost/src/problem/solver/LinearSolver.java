package problem.solver;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class LinearSolver
{
    private final double[] results;
    private LinearObjectiveFunction lof;
    private Collection<LinearConstraint> constraints;
    
    public LinearSolver(double[] results)
    {
        this.results = results;
        
        constraints = new ArrayList<>();
    }
    
    public void clear()
    {
        constraints.clear();
    }
    
    public void setObjective(double[] coefs)
    {
        lof = new LinearObjectiveFunction(coefs, 0);
    }
    public void setConstraint(double[] coefs, int resultId)
    {
        constraints.add(new LinearConstraint(coefs, Relationship.GEQ, results[resultId]));
    }
    
    public double getOpt()
    {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new MaxIter(100), lof, new LinearConstraintSet(constraints), GoalType.MINIMIZE, new NonNegativeConstraint(true));
        return solution.getValue();
    }
            
    /*
    void exemple()
    {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] { 2, 2, 1 }, 0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[] { 1, 1, 0 }, Relationship.LEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 1, 0, 1 }, Relationship.LEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 0, 1, 0 }, Relationship.LEQ,  1));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new MaxIter(100), f, new LinearConstraintSet(constraints), GoalType.MINIMIZE, new NonNegativeConstraint(true));
    }*/
    
}
