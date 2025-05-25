package weka.classifiers.rules;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class ConceptLattice {
    private final List<FormalConcept> concepts = new ArrayList<>();

    public ConceptLattice(FormalContext context) {
        Set<Integer> allObjects = new HashSet<>();
        for (int i = 0; i < context.getObjectCount(); i++)
            allObjects.add(i);

        Set<Integer> intent = context.commonAttributes(allObjects);
        concepts.add(new FormalConcept(allObjects, intent));
        // Ajouter ici vraie logique pour générer tous les concepts nécessaires
    }

    public List<FormalConcept> getConcepts() {
        return concepts;
    }
}

