package replace;

/**
 * Created by Patrick on 28.11.16.
 */

import replace.model.Attribute;
import replace.model.Cl;
import replace.model.DataSet;
import replace.model.Field;
import replace.model.Row;

import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.print.attribute.standard.MediaName;

public class DataSetAnalyzer {

    private DataSet dataset;
    private Map<Attribute, Double> means = new HashMap<Attribute, Double>();
    private Map<Attribute, Map<Cl, Double>> meansByCl = new HashMap<Attribute, Map<Cl, Double>>();
    private Map<Attribute, Map<String, Integer>> countForMedian = new HashMap<Attribute, Map<String,Integer>>();
    private Map<Attribute, Map<Cl, Map<String, Integer>>> countForMedianByCl = new HashMap<Attribute, Map<Cl,Map<String,Integer>>>();
    private Map<Attribute, Boolean> missingValues = new HashMap<Attribute, Boolean>();
    private Map<Attribute, Map<Cl, Boolean>> missingValuesByCl = new HashMap<Attribute, Map<Cl, Boolean>>();

    public DataSetAnalyzer(DataSet dataset) {
        this.dataset = dataset;
        calcmeans();
    }

    private void calcmeans() {
        Map<Attribute, Double> sum = new HashMap<Attribute, Double>();
        Map<Attribute, Double> size = new HashMap<Attribute, Double>();
        Map<Attribute, Map<Cl, Double>> sumByCl = new HashMap<Attribute, Map<Cl, Double>>();
        Map<Attribute, Map<Cl, Double>> sizeByCl = new HashMap<Attribute, Map<Cl, Double>>();

        for (Row row : dataset.getRows()) {
            Cl cl = dataset.getClByName(row.getCl());
            for (Attribute attribute : dataset.getAttributes()) {
                // Only attributes that are not class identifiers
                if (attribute.isCl()) {
                    continue;
                }

                if (!missingValues.containsKey(attribute)) {
                    missingValues.put(attribute, false);
                    missingValuesByCl.put(attribute,
                            new HashMap<Cl, Boolean>());
                }
                if (!missingValuesByCl.get(attribute).containsKey(cl)) {
                    missingValuesByCl.get(attribute).put(cl, false);
                }

                Field field = row.getField(attribute);
                
                if(null!=field){

	                // Only fields that are not missing
	                if (field.isMissing()) {
	                    missingValues.put(attribute, true);
	                    missingValuesByCl.get(attribute).put(cl, true);
	                    continue;
	                }
	
	                Object value = field.getValue();
	
	                if(value instanceof Double){
		                // init Lists when not existing
		                if (!sum.containsKey(attribute)) {
		                    sum.put(attribute, 0.0);
		                    size.put(attribute, 0.0);
		                    sumByCl.put(attribute, new HashMap<Cl, Double>());
		                    sizeByCl.put(attribute, new HashMap<Cl, Double>());
		                }
		
		                if (!sumByCl.get(attribute).containsKey(cl)) {
		                    sumByCl.get(attribute).put(cl, 0.0);
		                    sizeByCl.get(attribute).put(cl, 0.0);
		                }
		
		                // Sum values
		                //System.out.println(attribute + " sum: " + sum.get(attribute));
		                sum.put(attribute, sum.get(attribute) + (Double) value);
		                size.put(attribute, size.get(attribute) + 1);
		                sumByCl.get(attribute).put(cl,
		                        sumByCl.get(attribute).get(cl) + (Double) value);
		                sizeByCl.get(attribute).put(cl,
		                        sizeByCl.get(attribute).get(cl) + 1);
	                } else { //instanceof String

	                	if(!countForMedian.containsKey(attribute)){
	                		countForMedian.put(attribute, new HashMap<String, Integer>());
	                		countForMedianByCl.put(attribute, new HashMap<Cl, Map<String,Integer>>());
	                	}
	                	
	                	if(!countForMedian.get(attribute).containsKey(field.getValue().toString())){
	                		countForMedian.get(attribute).put(field.getValue().toString(), 0);
	                	}
	                	
	                	if(!countForMedianByCl.get(attribute).containsKey(cl)){
	                		countForMedianByCl.get(attribute).put(cl, new HashMap<String, Integer>());
	                	}
	                	if(!countForMedianByCl.get(attribute).get(cl).containsKey(field.getValue().toString())){
	                		countForMedianByCl.get(attribute).get(cl).put(field.getValue().toString(), 0);
	                	}
	                	
	                	Map<String, Integer> map = countForMedian.get(attribute);
	                	map.put(field.getValue().toString(), map.get(field.getValue().toString())+1);
	                	
	                	//System.out.println(field.getValue().toString() + " " + map.get(field.getValue().toString()));
	                	
	                	Map<Cl, Map<String, Integer>> mapForAtt = countForMedianByCl.get(attribute);
	                	Map<String, Integer> mapForClass = mapForAtt.get(cl);
	                	mapForClass.put(field.getValue().toString(), map.get(field.getValue().toString())+1);
	                	
	                }
	            }
            }
        }

        // calc means
        for (Attribute attribute : dataset.getAttributes()) {
            // Only attributes that are not class identifiers
            if (attribute.isCl()) {
                continue;
            }

            // calc for whole attribute
            //Instanceof Double
            if(null!=sum.get(attribute)){
	            double mean = sum.get(attribute) / size.get(attribute);
	            means.put(attribute, mean);
	            meansByCl.put(attribute, new HashMap<Cl, Double>());
	
	            for (Cl cl : dataset.getCles()) {
	                double meanForCl = sumByCl.get(attribute).get(cl)
	                        / sizeByCl.get(attribute).get(cl);
	                meansByCl.get(attribute).put(cl, meanForCl);
	            }
            }
            // Instance of String
            else {
            	//TODO nothing??
            }
        }
    }


    public Object getMean(Attribute attribute) {
    	if(null!=means.get(attribute)){
            return means.get(attribute);
    	}
    	
    	Entry<String, Integer> median = null;
    	for(Entry<String, Integer> count : countForMedian.get(attribute).entrySet()){
    		if(median == null){
    			median = count;
    		}
    		else if (median.getValue() < count.getValue()){
    			median = count;
    		}
    	}
    	return median.getKey();
    }

    public Object getMean(Attribute attribute, Cl cl) {
    	if(null!=meansByCl.get(attribute)){
    		return meansByCl.get(attribute).get(cl);
    	}
    	
    	Entry<String,Integer> median = null;
    	for (Entry<String, Integer> counts : countForMedianByCl.get(attribute).get(cl).entrySet()) {
			if(median == null){
				median = counts;
			}
			else if (median.getValue() < counts.getValue()){
				median = counts;
			}
		}
    	return median.getKey();
    }

    public boolean hasMissingValue(Attribute attribute) {
        return missingValues.get(attribute);
    }

    public boolean hasMissingValue(Attribute attribute, Cl cl) {
        return missingValuesByCl.get(attribute).get(cl);
    }

    private static double roundmean(double mean) {
        return Math.round(mean * 10000) / ((double) 10000);
    }

    private static String fixedLenthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    private static String missingValueString(boolean hasMissingValue) {
        return (hasMissingValue == true) ? "mv" : "";
    }

}
