package weka.classifiers.rules;

import weka.core.Instances;
import java.util.HashSet;
import java.util.Set;

public class FormalContext {
    private final boolean[][] context;
    private final int objectCount;
    private final int attributeCount;

    public FormalContext(boolean[][] context) {
        this.context = context;
        this.objectCount = context.length;
        this.attributeCount = context[0].length;
    }

    public static FormalContext fromInstances(Instances data) {
        int rows = data.numInstances();
        int cols = data.numAttributes() - 1;

        boolean[][] ctx = new boolean[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                ctx[i][j] = data.instance(i).value(j) > 0;

        return new FormalContext(ctx);
    }

    public Set<Integer> commonAttributes(Set<Integer> objects) {
        Set<Integer> common = new HashSet<>();
        for (int attr = 0; attr < attributeCount; attr++) {
            boolean shared = true;
            for (int obj : objects)
                if (!context[obj][attr]) {
                    shared = false;
                    break;
                }
            if (shared) common.add(attr);
        }
        return common;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public int getAttributeCount() {
        return attributeCount;
    }
}
