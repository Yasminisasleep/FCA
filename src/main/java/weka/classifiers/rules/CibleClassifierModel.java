package weka.classifiers.rules;

import weka.core.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class CibleClassifierModel implements Serializable {

    private final List<FormalConcept> concepts;
    private final Instances header;

    public CibleClassifierModel(List<FormalConcept> concepts, Instances header) {
        this.concepts = concepts;
        this.header = header;
    }

    public double classify(Instance instance) {
        for (FormalConcept concept : concepts) {
            if (matches(concept, instance)) {
                return majorityClass(concept);
            }
        }
        return Utils.missingValue();
    }

    private boolean matches(FormalConcept concept, Instance instance) {
        for (Integer attrIdx : concept.getIntent()) {
            if (instance.value(attrIdx) == 0.0) {
                return false;
            }
        }
        return true;
    }

    private double majorityClass(FormalConcept concept) {
        List<Double> classes = concept.getExtent().stream()
                .map(i -> header.instance(i).classValue())
                .collect(Collectors.toList());

        return classes.stream()
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get().getKey();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== CIBLe FCA Binary Implications ===\n\n");
        int implicationIdx = 1;

        for (FormalConcept concept : concepts) {
            if (concept.getIntent().isEmpty())
                continue;

            Set<Integer> closure = concept.getExtent().stream()
                    .flatMap(objIdx -> concepts.stream()
                            .filter(c -> c.getExtent().contains(objIdx))
                            .flatMap(c -> c.getIntent().stream()))
                    .collect(Collectors.toSet());

            closure.removeAll(concept.getIntent());

            if (closure.isEmpty())
                continue;

            int support = concept.getExtent().size();

            sb.append("Implication ").append(implicationIdx++).append(":\n");
            sb.append("IF {");
            sb.append(concept.getIntent().stream()
                    .map(idx -> header.attribute(idx).name() + "=1")
                    .collect(Collectors.joining(", ")));
            sb.append("}\n");

            sb.append("THEN {");
            sb.append(closure.stream()
                    .map(idx -> header.attribute(idx).name() + "=1")
                    .collect(Collectors.joining(", ")));
            sb.append("}\n");

            sb.append("[Support: ").append(support).append(" instances]\n\n");
        }

        return sb.toString();
    }
}
