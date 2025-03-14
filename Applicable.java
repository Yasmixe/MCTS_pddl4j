package fr.uga.pddl4j.examples;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.State;
import fr.uga.pddl4j.problem.operator.Action;

import java.util.ArrayList;
import java.util.List;
public class Applicable {
    public List<Action> getApplicableActions(State state, Problem problem) {
        List<Action> applicableActions = new ArrayList<>();

        // Parcourir toutes les actions du problème
        for (Action action : problem.getActions()) {
            // Vérifier si les préconditions de l'action sont satisfaites dans l'état actuel
            if (state.satisfies(action.getPrecondition())) {
                applicableActions.add(action);
            }
        }

        return applicableActions;
    }
}
