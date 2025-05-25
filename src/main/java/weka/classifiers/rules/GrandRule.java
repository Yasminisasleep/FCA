package weka.classifiers.rules;

import java.io.Serializable;

public class GrandRule implements Serializable {
    private static final long serialVersionUID = 1L;

    public boolean[] conditions;
    public double predictedClass;

    public GrandRule(boolean[] conditions, double predictedClass) {
        this.conditions = conditions;
        this.predictedClass = predictedClass;
    }

    public boolean matches(boolean[] instance) {
        for (int i = 0; i < conditions.length; i++) {
            if (conditions[i] && !instance[i]) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Rule: [");
        for (int i = 0; i < conditions.length; i++) {
            if (conditions[i]) sb.append(i).append(" ");
        }
        sb.append("] => ").append(predictedClass);
        return sb.toString();
    }
}
