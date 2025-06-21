package weka.classifiers.rules;

import weka.classifiers.AbstractClassifier;
import weka.core.*;

public class Legal extends AbstractClassifier {

    private LegalClassifierModel model;

    @Override
    public void buildClassifier(Instances data) throws Exception {
        getCapabilities().testWithFail(data);
        data = new Instances(data);
        data.deleteWithMissingClass();

        FormalContext context = new FormalContext(data);
        ConceptLattice lattice = new ConceptLattice(context);

        model = new LegalClassifierModel(lattice.getConcepts(), data);
    }

    @Override
    public double classifyInstance(Instance instance) throws Exception {
        if (model == null)
            throw new Exception("Classifier hasn't been built.");
        return model.classify(instance);
    }

    @Override
    public Capabilities getCapabilities() {
        Capabilities caps = super.getCapabilities();
        caps.enable(Capabilities.Capability.NOMINAL_ATTRIBUTES);
        caps.enable(Capabilities.Capability.BINARY_ATTRIBUTES);
        caps.enable(Capabilities.Capability.NOMINAL_CLASS);
        caps.setMinimumNumberInstances(1);
        return caps;
    }

    @Override
    public String toString() {
        return model != null ? model.toString() : "No model built yet.";
    }

    public static void main(String[] args) {
        runClassifier(new Legal(), args);
    }
}
