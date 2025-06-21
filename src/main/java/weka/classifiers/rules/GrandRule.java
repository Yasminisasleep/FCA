package weka.classifiers.rules;

import weka.core.Instance;
import weka.core.Instances;
import java.io.Serializable;
import java.util.*;

public class GrandRule implements Serializable {
    public final Map<Integer, Double> conditions;
    public final double predictedClass;

    public GrandRule(Map<Integer, Double> conditions, double predictedClass) {
        this.conditions = conditions;
        this.predictedClass = predictedClass;
    }

    public boolean matches(Instance instance) {
        for (Map.Entry<Integer, Double> entry : conditions.entrySet()) {
            if (instance.value(entry.getKey()) != entry.getValue())
                return false;
        }
        return true;
    }

    public String conditionsToString(Instances data) {
        List<String> condStr = new ArrayList<>();
        for (Map.Entry<Integer, Double> cond : conditions.entrySet()) {
            condStr.add(data.attribute(cond.getKey()).name() + " = " + cond.getValue());
        }
        return String.join(" AND ", condStr);
    }
}
