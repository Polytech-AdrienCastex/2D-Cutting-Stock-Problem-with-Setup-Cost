# 2D-Cutting-Stock-Problem-with-Setup-Cost
Math problem

<h2><b>Information</b></h2>

<table>
  <tr>
    <td><b>Language</b></td>
    <td>Java [8]</td>
  </tr>
  <tr>
    <td><b>IDE</b></td>
    <td>NetBeans [8]</td>
  </tr>
  <tr>
    <td><b>Modular</b></td>
    <td>Yes</td>
  </tr>
</table>

<br><hr><h2><b>Fast code information</b></h2>

<h4>Basic use (taboo method)</h4>

Pattern definition :
```java
PatternKind pk = new PatternKind(40, 60);
new ImageKind(24, 30, 246, pk);
new ImageKind(13, 56, 562, pk);
new ImageKind(14, 22, 1000, pk);
new ImageKind(9, 23, 3498, pk);
```

Call :
```java
INextSolutionGenerator generator = new TabooMethod(new INeighborOperator[]
{
    new Addition(),
    new Subtraction()
}, pk, new CutInterfacer(pk), 20); // Define the algorithm to use, here : taboo method

ProblemParameters pp = new ProblemParameters(4, 1, 10); // Define the parameters of the problem solver
FinalSolution fs = FinalSolution.findSolution(5000, 50, pp, pk, generator); // Compute

System.out.println(fs); // Print the final solution
```

<br><hr><h2><b>Available algorithms</b></h2>

<h4>Summary/Links</h4>
<table>
  <tr>
    <td><b>Name</b></td>
    <td><b>Class name</b></td>
  </tr>
  <tr>
    <td>Minimum local reacher</td>
    <td><i>LocalMinimumReacher</i></td>
    <td><a href="https://github.com/Polytech-AdrienCastex/2D-Cutting-Stock-Problem-with-Setup-Cost/blob/master/2D-Cutting-Stock-Problem-with-Setup-Cost/src/problem/solver/neighborselection/LocalMinimumReacher.java">»</a></td>
  </tr>
  <tr>
    <td>Taboo/Tabu method</td>
    <td><i>TabooMethod</i></td>
    <td><a href="https://github.com/Polytech-AdrienCastex/2D-Cutting-Stock-Problem-with-Setup-Cost/blob/master/2D-Cutting-Stock-Problem-with-Setup-Cost/src/problem/solver/neighborselection/TabouMethod.java">»</a></td>
  </tr>
</table>

<h4>Minimum local reacher</h4>
```java
INextSolutionGenerator generator = new LocalMinimumReacher(
  new INeighborOperator[]
  {
      new Addition(),
      new Subtraction()
  },  // List of operators to define the neighborhood
  pk, // Pattern kind
  new CutInterfacer(pk) // Patern placement algorithm
);
```

<h4>Taboo method</h4>
```java
INextSolutionGenerator generator = new TabooMethod(
  new INeighborOperator[]
  {
      new Addition(),
      new Subtraction()
  },  // List of operators to define the neighborhood
  pk, // Pattern kind
  new CutInterfacer(pk), // Patern placement algorithm
  20  // Number of taboo operations
);
```


<br><hr><h2><b>References</b></h2>
* <a href="http://commons.apache.org/proper/commons-math/download_math.cgi">Simplex method package</a> | Apache Commons Math - Download page
* <a href="https://en.wikipedia.org/wiki/Tabu_search">Taboo method</a> | Wikipedia information
* <a href="https://en.wikipedia.org/wiki/Simulated_annealing">Simulated annealing</a> | Wikipedia information
* <a href="https://en.wikipedia.org/wiki/Mathematical_optimization#Heuristics">Heuristic list</a> | Wikipedia information
