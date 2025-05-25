package weka.classifiers.rules;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.rules.GrandClassifierModel;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.core.Utils;

public class Grand extends AbstractClassifier {

    private GrandClassifierModel model;

    @Override
    public void buildClassifier(Instances data) throws Exception {
        // Vérification des capacités (important dans Weka)
        getCapabilities().testWithFail(data);

        // Nettoyage
        data = new Instances(data);
        data.deleteWithMissingClass();

        // Construction du modèle GRAND
        FormalContext context = FormalContext.fromInstances(data);
        ConceptLattice lattice = new ConceptLattice(context);
        GrandRuleExtractor extractor = new GrandRuleExtractor(lattice, context);
        model = new GrandClassifierModel(extractor.extractRules());
    }

    @Override
    public double classifyInstance(Instance instance) throws Exception {
        if (model == null) {
            throw new Exception("Model is not built yet!");
        }
        return model.classify(instance);
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] distribution = new double[instance.numClasses()];
        int predicted = (int) classifyInstance(instance);
        distribution[predicted] = 1.0;
        return distribution;
    }

    @Override
    public Capabilities getCapabilities() {
        Capabilities caps = super.getCapabilities();
        caps.enable(Capability.NOMINAL_ATTRIBUTES);
        caps.enable(Capability.NOMINAL_CLASS);
        caps.setMinimumNumberInstances(1);
        return caps;
    }

    @Override
    public String toString() {
        if (model == null) {
            return "Grand: No model built yet.";
        }
        return model.toString();
    }

    @Override
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 1.0 $");
    }

    public static void main(String[] args) {
        runClassifier(new Grand(), args);
    }
}
