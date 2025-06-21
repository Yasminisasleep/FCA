package weka.classifiers.rules;

import weka.core.Instance;
import java.io.Serializable;
import java.util.*;

public class ConceptLattice implements Serializable {
    private final FormalContext context;
    private final List<FormalConcept> concepts = new ArrayList<>();

    public ConceptLattice(FormalContext context) {
        this.context = context;
        buildLattice();
    }

    private void buildLattice() {
        int numAttributes = context.getAttributeCount();
        int numObjects = context.getObjectCount();

        for (int i = 1; i < (1 << numAttributes); i++) {
            Set<Integer> intent = new HashSet<>();
            for (int bit = 0; bit < numAttributes; bit++)
                if ((i & (1 << bit)) != 0) intent.add(bit);

            Set<Integer> extent = new HashSet<>();
            for (int obj = 0; obj < numObjects; obj++) {
                Instance inst = context.getInstance(obj);
                boolean match = true;
                for (int attr : intent) {
                    if (inst.value(attr) == 0.0) {
                        match = false;
                        break;
                    }
                }
                if (match) extent.add(obj);
            }

            if (!extent.isEmpty())
                concepts.add(new FormalConcept(extent, intent));
        }
    }

    public List<FormalConcept> getConcepts() {
        return concepts;
    }
}
