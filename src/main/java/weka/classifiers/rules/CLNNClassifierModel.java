package weka.classifiers.rules;

import weka.core.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class CLNNClassifierModel implements Serializable {

    private final List<FormalConcept> concepts;
    private final Instances header;

    public CLNNClassifierModel(List<FormalConcept> concepts, Instances header) {
        this.concepts = concepts;
        this.header = header;
    }

    public double classify(Instance instance) {
        FormalConcept nearestConcept = null;
        int minDistance = Integer.MAX_VALUE;

        for (FormalConcept concept : concepts) {
            int distance = logicalDistance(concept, instance);
            if (distance < minDistance) {
                nearestConcept = concept;
                minDistance = distance;
            }
        }

        return majorityClass(nearestConcept);
    }

    private int logicalDistance(FormalConcept concept, Instance instance) {
        int distance = 0;
        for (Integer attrIdx : concept.getIntent()) {
            if (attrIdx == instance.classIndex()) continue;

            if (instance.value(attrIdx) == 0.0)
                distance++;
        }
        return distance;
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
        StringBuilder sb = new StringBuilder("=== CLNN Logical Nearest Neighbor Classification ===\n\n");

        for (int idx = 0; idx < header.numInstances(); idx++) {
            Instance instance = header.instance(idx);

            FormalConcept nearestConcept = null;
            int minDistance = Integer.MAX_VALUE;

            for (FormalConcept concept : concepts) {
                int distance = logicalDistance(concept, instance);
                if (distance < minDistance) {
                    nearestConcept = concept;
                    minDistance = distance;
                }
            }

            String assignedClass = header.classAttribute().value((int)majorityClass(nearestConcept));

            sb.append("Nearest Neighbor Concept for instance \"").append(instance).append("\":\n");
            sb.append("Intent: {");
            sb.append(nearestConcept.getIntent().stream()
                    .map(attrIdx -> header.attribute(attrIdx).name() + "=1")
                    .collect(Collectors.joining(", "))).append("}\n");
            sb.append("Class assigned: ").append(assignedClass).append("\n");
            sb.append("Distance: ").append(minDistance).append(" (logical attribute mismatch)\n\n");
        }

        return sb.toString();
    }
}
