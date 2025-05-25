package weka.classifiers.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.*;
import weka.core.Instance;

public class GrandRuleExtractor {
    private final FormalContext context;
    private final ConceptLattice lattice;

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
        Map<Double, Integer> classCounts = new HashMap<>();

        for (Integer i : extent) {
            Instance inst = context.getInstance(i);
            double classVal = inst.classValue();
            classCounts.put(classVal, classCounts.getOrDefault(classVal, 0) + 1);
        }

        return classCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0.0);
    }
}