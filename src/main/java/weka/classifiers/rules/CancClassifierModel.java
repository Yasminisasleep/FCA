package weka.classifiers.rules;

import weka.core.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class CancClassifierModel implements Serializable {

    private final List<FormalConcept> concepts;
    private final Instances header;

    public CancClassifierModel(List<FormalConcept> concepts, Instances header) {
        this.concepts = concepts;
        this.header = header;
    }

    public double classify(Instance instance) {
        FormalConcept bestMatch = null;
        int maxMatchedAttributes = -1;

        for (FormalConcept concept : concepts) {
            if (matches(concept, instance)) {
                int size = concept.getIntent().size();
                if (size > maxMatchedAttributes) {
                    bestMatch = concept;
                    maxMatchedAttributes = size;
                }
            }
        }

        if (bestMatch != null) {
            return majorityClass(bestMatch);
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
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== CanC FCA Classification Rules ===\n\n");
        int ruleIdx = 1;

        for (FormalConcept concept : concepts) {
            if (concept.getIntent().isEmpty())
                continue;

            Map<String, Long> classCounts = concept.getExtent().stream()
                    .map(i -> header.instance(i).stringValue(header.classIndex()))
                    .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

            Map.Entry<String, Long> majorityEntry = classCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).get();

            double confidence = (majorityEntry.getValue() * 100.0) / concept.getExtent().size();
            int support = concept.getExtent().size();

            sb.append("Rule ").append(ruleIdx++).append(":\n");
            sb.append("IF {");
            sb.append(concept.getIntent().stream()
                    .map(idx -> header.attribute(idx).name() + "=1")
                    .collect(Collectors.joining(", ")));
            sb.append("}\n");

            sb.append("THEN class = ").append(majorityEntry.getKey());
            sb.append(" [confidence: ").append(String.format("%.1f%%", confidence));
            sb.append(", support: ").append(support).append(" instances]\n\n");
        }

        return sb.toString();
    }
}
