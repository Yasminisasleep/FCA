package weka.classifiers.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GrandRuleExtractor {

    private final ConceptLattice lattice;
    private final FormalContext context;

    public GrandRuleExtractor(ConceptLattice lattice, FormalContext context) {
        this.lattice = lattice;
        this.context = context;
    }

    public List<GrandRule> extractRules() {
        List<GrandRule> rules = new ArrayList<>();

        for (FormalConcept concept : lattice.getConcepts()) {
            boolean[] conditions = new boolean[context.getAttributeCount()];

            for (Integer index : concept.getIntent()) {
                conditions[index] = true;
            }

            double predictedClass = determineClass(concept.getExtent());
            rules.add(new GrandRule(conditions, predictedClass));
        }
        return rules;
    }

    private double determineClass(Set<Integer> extent) {
        // Adapte avec ta vraie logique
        return extent.size() % 2;
    }
}
