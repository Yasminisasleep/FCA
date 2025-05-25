package weka.classifiers.rules;

import weka.core.Instance;
import java.io.Serializable;
import java.util.List;

public class GrandClassifierModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<GrandRule> rules;

    public GrandClassifierModel(List<GrandRule> rules) {
        this.rules = rules;
    }

    public double classify(Instance instance) {
        boolean[] binarized = new boolean[instance.numAttributes() - 1];
        for (int i = 0; i < binarized.length; i++) {
            binarized[i] = true; // à adapter selon ton vrai binariseur
        }

        for (GrandRule rule : rules) {
            if (rule.matches(binarized)) return rule.predictedClass;
        }

        return 0.0; // fallback default
    }

    @Override
    public String toString() {
        return "GrandClassifierModel with " + rules.size() + " rules.";
    }
}
