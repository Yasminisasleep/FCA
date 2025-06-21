package weka.classifiers.rules;

import weka.core.Instance;
import weka.core.Instances;
import java.io.Serializable;

public class FormalContext implements Serializable {
    private final Instances data;

    public FormalContext(Instances data) {
        this.data = data;
    }

    public Instance getInstance(int idx) {
        return data.instance(idx);
    }

    public int getAttributeCount() {
        return data.numAttributes() - 1;
    }

    public int getObjectCount() {
        return data.numInstances();
    }
}
