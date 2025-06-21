package weka.classifiers.rules;

import weka.core.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GrandClassifierModel implements Serializable {

    private final List<FormalConcept> concepts;
    private final Instances header;

    public GrandClassifierModel(List<FormalConcept> concepts, Instances header) {
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
                .entrySet().stream().max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get().getKey();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== GRAND FCA Concept Lattice ===\n\n");
        int idx = 1;

        for (FormalConcept concept : concepts) {
            sb.append("Concept ").append(idx++).append(":\n");

            sb.append("Intent (attributes): {");
            List<String> attributes = concept.getIntent().stream()
                    .map(attrIdx -> header.attribute(attrIdx).name())
                    .collect(Collectors.toList());
            sb.append(String.join(", ", attributes)).append("}\n");

            sb.append("Extent (instances - class values): {");
            List<String> instances = concept.getExtent().stream()
                    .map(i -> header.instance(i).stringValue(header.classIndex())) // Ici strictement la vraie donnée
                    .collect(Collectors.toList());
            sb.append(String.join(", ", instances)).append("}\n\n");
        }

        return sb.toString();
    }
}
