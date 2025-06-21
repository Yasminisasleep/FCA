package weka.classifiers.rules;

import weka.core.Instance;
import java.util.*;

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
            if (concept.getIntent().isEmpty()) continue;

            Map<Integer, Double> conditions = new HashMap<>();
            for (Integer attrIdx : concept.getIntent()) {
                conditions.put(attrIdx, avgAttributeValue(attrIdx, concept.getExtent()));
            }

            double predClass = majorityClass(concept.getExtent());
            rules.add(new GrandRule(conditions, predClass));
        }
        return rules;
    }

    private double avgAttributeValue(int attrIdx, Set<Integer> extent) {
        double sum = 0;
        for (int idx : extent)
            sum += context.getInstance(idx).value(attrIdx);
        return sum / extent.size();
    }

    private double majorityClass(Set<Integer> extent) {
        Map<Double, Integer> counts = new HashMap<>();
        for (int idx : extent) {
            double cls = context.getInstance(idx).classValue();
            counts.put(cls, counts.getOrDefault(cls, 0) + 1);
        }
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("Aucune classe majoritaire trouvée")).getKey();
    }
}
