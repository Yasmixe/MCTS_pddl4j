package fr.uga.pddl4j.examples;
import fr.uga.pddl4j.parser.DefaultParsedProblem;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.parser.Message;
import fr.uga.pddl4j.parser.Parser;
import fr.uga.pddl4j.problem.DefaultProblem;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.heuristics.state.StateHeuristic;
import fr.uga.pddl4j.heuristics.state.FastForward; // heuristic FF
import fr.uga.pddl4j.plan.*; //pour creer le plan
import fr.uga.pddl4j.util.Plan;
import java.util.List;
import java.util.Random; //pour le random
import fr.uga.pddl4j.problem.State;
import java.io.FileNotFoundException;
import fr.uga.pddl4j.planners.statespace.StateSpacePlanner;
import fr.uga.pddl4j.problem.operator.Condition;

public class PRWPlanner extends StateSpacePlanner{
    private static final int num_walk = 100; // Nombre de marches aléatoires
    private static final int length_walk = 10; // Longueur de chaque marche aléatoire
    Applicable applicable = new Applicable();
    public PRWPlanner(){

        super();
    }

    @Override
    public Plan solve(Problem problem){
        State s0 = new State(problem.getInitialState()); //s0 c'est initial state
        Random random = new Random(); // pour utiliser le random dans les actions a choisir

        State Smin = null;
        double Hmin = Double.MAX_VALUE;
        for(int i = 0; i< num_walk; i++){
            State Sprime = s0;
            for(int j=0; j< length_walk; j++)
            {
                // obtenir les actions dans une liste 
                 List<Action> applicableActions = applicable.getApplicableActions(Sprime, problem);
                 if (applicableActions.isEmpty()){
                    break;
                 }
                 Action uniformlyrandomAction = applicableActions.get(random.nextInt(applicableActions.size()));
                 Sprime = problem.getSuccessor(Sprime, uniformlyrandomAction);
                 if (problem.isGoalState(Sprime)) 
                 {
                    // Retourner un plan valide si l'objectif est atteint
                    return extractPlan(Sprime, problem);
                 }
                 double h = evaluateHeuristic(Sprime, problem);
                 if(h< Hmin){
                    Hmin = h;
                    Smin= Sprime;
                 }
                 

            }
        }
        return extractPlan(Smin, problem);
    }

    private double evaluateHeuristic(State state, Problem problem){
        // ici on evalue 
    }
    private Plan extractPlan(State state, Problem problem) {
        // Extraire un plan à partir de l'état final (simplifié pour l'exemple)
        // Dans une implémentation réelle, vous devriez stocker les actions appliquées
        return new Plan();
    }
    private boolean isGoalState(State state, Condition goal){
        return state.satisfies(goal);
    }
public static void main(String[] args){
    // Checks the number of arguments from the command line
    if (args.length != 2) {
        System.out.println("Invalid command line");
        return;
    }
    String domainFile = args[0];
    String problemFile = args[1];
    // Creates an instance of the PDDL parser
    final Parser parser = new Parser();
    // Parses the domain and the problem files.
    final DefaultParsedProblem parsedProblem = parser.parse(args[0], args[1]);
    // Gets the error manager of the parser
    final ErrorManager errorManager = parser.getErrorManager();
    // Checks if the error manager contains errors
    if (!errorManager.isEmpty()) {
        // Prints the errors
        for (Message m : errorManager.getMessages()) {
            System.out.println(m.toString());
        }
    } else {
        // Prints that the domain and the problem were successfully parsed
        System.out.print("\nparsing domain file \"" + args[0] + "\" done successfully");
        System.out.print("\nparsing problem file \"" + args[1] + "\" done successfully\n\n");
        // Create a problem
        Problem problem = new DefaultProblem(parsedProblem);
        // creer le plan 
        PRWPlanner planner = new PRWPlanner();
        // executer
        Plan plan = planner.solve(problem);
        if (plan != null) {
            System.out.println("Plan trouvé :");
            for (Action action : plan) {
                System.out.println(action.getName());
            }
        } else {
            System.out.println("Aucun plan trouvé.");
        }
}

}