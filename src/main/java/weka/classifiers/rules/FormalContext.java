package weka.classifiers.rules;

import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

public class FormalContext {

    private final Instances data;

    public FormalContext(Instances data) {
        this.data = data;
    }

    public static FormalContext fromInstances(Instances data) {
        return new FormalContext(data);
    }

    public int getAttributeCount() {
        // Exclure l'attribut de classe
        return data.numAttributes() - 1;
    }

    public Instance getInstance(int index) {
        return data.instance(index);
    }

    public int getObjectCount() {
        return data.numInstances();
    }

    public List<FormalConcept> getAllConcepts() {
        return new ArrayList<>();
    }

    public boolean[] getIntent(Instance instance) {
        boolean[] intent = new boolean[getAttributeCount()];
        for (int i = 0, j = 0; i < data.numAttributes(); i++) {
            if (i == data.classIndex()) continue;
            intent[j++] = !instance.isMissing(i) && instance.stringValue(i).equals("yes");
        }
        return intent;
    }

    public double getClassValue(Instance instance) {
        return instance.classValue();
    }
    public Set<Integer> commonAttributes(Set<Integer> objectIndices) {
        Set<Integer> common = new HashSet<>();

        if (objectIndices.isEmpty()) return common;

        // Initialiser tous à true au début
        boolean[] commonFlags = new boolean[getAttributeCount()];
        Arrays.fill(commonFlags, true);

        for (int index : objectIndices) {
            Instance inst = getInstance(index);
            boolean[] intent = getIntent(inst);

            for (int i = 0; i < commonFlags.length; i++) {
                commonFlags[i] &= intent[i]; // intersection logique
            }
        }

        // Convertir le tableau booléen en Set d'indices
        for (int i = 0; i < commonFlags.length; i++) {
            if (commonFlags[i]) {
                common.add(i);
            }
        }

        return common;
    }

}
