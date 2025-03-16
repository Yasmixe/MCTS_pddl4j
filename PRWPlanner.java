package fr.uga.pddl4j.examples;

import fr.uga.pddl4j.parser.DefaultParsedProblem;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.parser.Message;
import fr.uga.pddl4j.parser.Parser;
import fr.uga.pddl4j.problem.DefaultProblem;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.problem.operator.Condition;
import fr.uga.pddl4j.problem.operator.ConditionalEffect;
import fr.uga.pddl4j.problem.State;
import fr.uga.pddl4j.plan.Plan;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

public class PRWPlanner {
    private static final int num_walk = 100; // Nombre de marches aléatoires
    private static final int length_walk = 10; // Longueur de chaque marche aléatoire

    public PRWPlanner() {
        super();
    }

    public Plan solve(Problem problem, State s0, Condition goal) {
        Random random = new Random(); // pour utiliser le random dans les actions a choisir
        State Smin = null;
        double Hmin = Double.MAX_VALUE;

        for (int i = 0; i < num_walk; i++) {
            State Sprime = s0;
            for (int j = 0; j < length_walk; j++) {
                // obtenir les actions dans une liste
                List<Action> applicableActions = problem.getActions();
                if (applicableActions.isEmpty()) {
                    break;
                }
                Action uniformlyrandomAction = applicableActions.get(random.nextInt(applicableActions.size()));

                for (Action action : applicableActions) {
                    List<ConditionalEffect> effects = action.getConditionalEffects(); // Obtenir les effets conditionnels
                    Sprime.apply(effects);
                }
                /**  if (goal.evaluate(Sprime, problem)) {
                    // Retourner un plan valide si l'objectif est atteint
                    return extractPlan(Sprime, problem);
                }
                double h = evaluateHeuristic(Sprime, problem);
                if (h < Hmin) {
                    Hmin = h;
                    Smin = Sprime;
                }* */
            }
        }
        return null; // Retourner extractPlan(Smin, problem)
    }
    /** private double evaluateHeuristic(State state, Problem problem) {
       
    }

    private Plan extractPlan(State state, Problem problem) {
        
        
    } */
    public static void main(String[] args) throws FileNotFoundException {
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
            problem.instantiate();
            State s0 = new State(problem.getInitialState()); // s0 c'est initial state
            Condition goal = problem.getGoal();
            // creer le plan
            PRWPlanner planner = new PRWPlanner();
            // executer
            Plan plan = planner.solve(problem, s0, goal);
            if (plan != null) {
                System.out.println("Plan trouvé :");
                for (Action a : problem.getActions()) {
                    System.out.println(problem.toString(a));
                }
            } else {
                System.out.println("Aucun plan trouvé.");
            }
        }
    }
}