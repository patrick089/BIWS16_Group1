package replace.bi.replacer;

/**
 * Created by Patrick on 28.11.16.
 */

import java.util.ArrayList;
import java.util.List;
import replace.bi.data.*;
import replace.bi.DataMiningException;

public class ignoreAttributeWhenMissingValue extends handleMissingValues {

    public ignoreAttributeWhenMissingValue(DataSet dataset) {
        super(dataset);
    }

    @Override
    protected double missingFieldValue(Attribute attribute, Cl cl) {
        throw new DataMiningException("Cannot occour in this Replacer!");
    }

    // Calculate before which of the Attributes have missing values
    @Override
    protected List<Attribute> resolveNewAttributes() {
        List<Attribute> attributes = new ArrayList<> ();
        List<Attribute> attributesWithMissingFields = new ArrayList<>();
        for (Row row : getDataset().getRows()) {
            for (Attribute attribute : getDataset().getAttributes()) {
                // Only attributes that are not class identifiers
                if (attribute.isCl()) {
                    continue;
                }

                Field field = row.getField(attribute);

                if (field.isMissing()) {
                    attributesWithMissingFields.add(attribute);
                }
            }
        }
        for (Attribute attribute : getDataset().getAttributes()) {
            if (!attributesWithMissingFields.contains(attribute)) {
                attributes.add(attribute);
            }
        }
        return attributes;
    }

}
