package weka.classifiers.rules;

import java.io.Serializable;
import java.util.Set;

public class FormalConcept implements Serializable {
    private final Set<Integer> extent;
    private final Set<Integer> intent;

    public FormalConcept(Set<Integer> extent, Set<Integer> intent) {
        this.extent = extent;
        this.intent = intent;
    }

    public Set<Integer> getExtent() { return extent; }
    public Set<Integer> getIntent() { return intent; }
}

