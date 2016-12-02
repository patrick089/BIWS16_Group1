package replace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import replace.model.Attribute;
import replace.model.Cl;
import replace.model.DataSet;
import replace.model.Field;
import replace.model.Row;

/**
 * Created by Patrick on 28.11.16.
 */
public class HandleMissingValues {

    private DataSet dataset;
    private Map<Attribute, Attribute> attributeResolver = new HashMap<Attribute, Attribute>();
    private DataSet newDataset = new DataSet();
    private DataSetAnalyzer datasetAnalyzer;

    public HandleMissingValues(DataSet dataset) {
        this.dataset = dataset;
        datasetAnalyzer = new DataSetAnalyzer(dataset);
    }

    private List<Attribute> resolveNewAttributes(boolean ignore) {
    	
    	if(ignore){
    		List<Attribute> attributes = new ArrayList<> ();
            List<Attribute> attributesWithMissingFields = new ArrayList<>();
            boolean first = true;
            for (Row row : dataset.getRows()) {
            	System.out.println(row);
                for (Attribute attribute : dataset.getAttributes()) {
                    // Only attributes that are not class identifiers
                    if (attribute.isCl()) {
                        continue;
                    }
                    
                    Field field = row.getField(attribute);

                    if(null!=field){
	                    if (field.isMissing()) {
	                        attributesWithMissingFields.add(attribute);
	                    }
                    }
                }
            }
            for (Attribute attribute : dataset.getAttributes()) {
                if (!attributesWithMissingFields.contains(attribute)) {
                    attributes.add(attribute);
                }
            }
            return attributes;
    	}
    	else {
    		return dataset.getAttributes();
    	}
    }
    
    public DataSet replace(boolean ignore, boolean meanByAttribute, boolean meanByClass) {
        
        List<Attribute> attributes = resolveNewAttributes(ignore);
        
        int i = 0;
        for (Attribute attribute : attributes) {
        	if(i == attributes.size()-1){
        		Attribute newAttribute = newDataset.addAttribute(attribute.getName(), true);
                attributeResolver.put(attribute, newAttribute);
        	} else {
	        	Attribute newAttribute = newDataset.addAttribute(attribute.getName(), false);
	            attributeResolver.put(attribute, newAttribute);
        	}
            i++;
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

                // If attribute is ignored
                if (newAttribute == null) {
                    continue;
                }

                Object value = 0.0;
                if(null!=field){
                if (!field.isMissing()) {
                    value = field.getValue();
                } else if (meanByAttribute){
                    value = datasetAnalyzer.getMean(attribute);
                } else if(meanByClass){
                	value = datasetAnalyzer.getMean(attribute, cl);
                }
                fields.put(newAttribute, Field.newField(value));
                }
            }
            Row newRow = new Row(cl.getName(), fields);
            newDataset.add(newRow);
        }
        return newDataset;
    }

    protected DataSet getDataset() {
        return dataset;
    }

}
