package replace.bi.replacer;

import replace.bi.data.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Patrick on 28.11.16.
 */
public abstract class handleMissingValues {

    private DataSet dataset;
    private Map<Attribute, Attribute> attributeResolver = new HashMap<Attribute, Attribute>();
    private DataSet newDataset = new DataSet();

    public handleMissingValues(DataSet dataset) {
        this.dataset = dataset;
    }

    public DataSet replace() {
        init();
        List<Attribute> attributes = this.resolveNewAttributes();
        for (Attribute attribute : attributes) {
            addAttributeToNewDataset(attribute);
        }
        for (Row row : dataset.getRows()) {
            Cl cl = dataset.getClByName(row.getCl());
            Map<Attribute, Field> fields = new HashMap<Attribute, Field>();
            for (Attribute attribute : dataset.getAttributes()) {
                // Only attributes that are not class identifiers
                if (attribute.isCl()) {
                    continue;
                }

                Field field = row.getField(attribute);

                Attribute newAttribute = attributeResolver.get(attribute);

                // If attribute is not used anymore ignore this case
                if (newAttribute == null) {
                    continue;
                }

                double value;
                if (!isMissingValue(field, attribute, cl)) {
                    value = fieldValue(field, attribute, cl);
                } else {
                    value = missingFieldValue(attribute, cl);
                }
                fields.put(newAttribute, Field.newField(value));
            }
            Row newRow = new Row(cl.getName(), fields);
            newDataset.add(newRow);
        }
        return newDataset;
    }

    protected List<Attribute> resolveNewAttributes() {
        return this.dataset.getAttributes();
    }

    protected void addAttributeToNewDataset(Attribute attribute) {
        Attribute newAttribute = newDataset.addAttribute(attribute.getName());
        attributeResolver.put(attribute, newAttribute);
    }

    protected DataSet getDataset() {
        return this.dataset;
    }

    protected void init() {

    }

    protected double fieldValue(Field field, Attribute attribute, Cl cl) {
        return field.getValue();
    }

    protected boolean isMissingValue(Field field, Attribute attribute,
                                     Cl cl) {
        return field.isMissing();
    }

    protected abstract double missingFieldValue(Attribute attribute, Cl cl);

}
