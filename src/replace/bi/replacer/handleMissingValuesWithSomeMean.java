package replace.bi.replacer;

/**
 * Created by Patrick on 28.11.16.
 */

import replace.bi.DataSetAnalyzer;
import replace.bi.data.DataSet;

public abstract class handleMissingValuesWithSomeMean extends handleMissingValues {

    private DataSetAnalyzer datasetAnalyzer;

    public handleMissingValuesWithSomeMean(DataSet dataset) {
        super(dataset);
    }

    protected void init() {
        datasetAnalyzer = new DataSetAnalyzer(getDataset());
    }

    protected DataSetAnalyzer getDatasetAnalyzer() {
        return datasetAnalyzer;
    }

}
