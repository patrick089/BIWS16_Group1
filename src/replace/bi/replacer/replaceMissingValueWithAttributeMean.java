package replace.bi.replacer;

/**
 * Created by Patrick on 28.11.16.
 */

import replace.bi.data.DataSet;
import replace.bi.data.Attribute;
import replace.bi.data.Cl;

public class replaceMissingValueWithAttributeMean extends handleMissingValuesWithSomeMean {

    public replaceMissingValueWithAttributeMean(DataSet dataset) {
        super(dataset);
    }

    @Override
    protected double missingFieldValue(Attribute attribute, Cl cl) {
        return this.getDatasetAnalyzer().getMean(attribute);
    }

}
