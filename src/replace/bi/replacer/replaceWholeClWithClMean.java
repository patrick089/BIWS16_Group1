package replace.bi.replacer;

/**
 * Created by Patrick on 28.11.16.
 */

import replace.bi.data.DataSet;
import replace.bi.data.Attribute;
import replace.bi.data.Cl;
import replace.bi.data.Field;

public class replaceWholeClWithClMean extends handleMissingValuesWithSomeMean {

    public replaceWholeClWithClMean(DataSet dataset) {
        super(dataset);
    }

    @Override
    protected double missingFieldValue(Attribute attribute, Cl cl) {
        return this.getDatasetAnalyzer().getMean(attribute, cl);
    }

    @Override
    protected boolean isMissingValue(Field field, Attribute attribute,
                                     Cl cl) {
        return field.isMissing()
                || this.getDatasetAnalyzer().hasMissingValue(attribute, cl);
    }

}
