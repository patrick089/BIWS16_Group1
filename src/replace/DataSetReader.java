package replace;

/**
 * Created by Patrick on 28.11.16.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import replace.model.Attribute;
import replace.model.DataSet;
import replace.model.Field;
import replace.model.Row;

public class DataSetReader {

    private static final String SEPERATION_CHAR = ",";

    private String filename;
    private DataSet dataset;

    public DataSetReader(String filename) {
        this.filename = filename;
    }

    public DataSet read() throws Exception {
        BufferedReader br = null;
        dataset = new DataSet();
        boolean first = true;
        try {
            br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                if (first) {
                    readHeader(line);
                    first = false;
                } else {
                    readContent(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("You must enter a correct filename - " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error reading file - " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Error reading file - " + e.getMessage());
                }
            }
        }
        return dataset;
    }

    private void readHeader(String line) {
    	String split[] = line.split(SEPERATION_CHAR);
    	for (int i = 0; i < split.length; i++) {
    		if(i == split.length-1){
    			dataset.addAttribute(split[i], true);
    		} else {
    			dataset.addAttribute(split[i], false);
    		}
		}
    }

    private void readContent(String line) {
    	String split[] = line.split(SEPERATION_CHAR);
        Map<Attribute, Field> fields = new HashMap<Attribute, Field>();
        String cl = "";
        for (int i = 0; i < split.length; i++) {
            Attribute attribute = dataset.getAttributes().get(i);
            String inputString = split[i];
            if (inputString.matches("-?[\\d.]+(?:e-?\\d+)?")) {
                fields.put(attribute,
                        Field.newField(Double.parseDouble(inputString)));
            } else if (inputString.equals(Field.MISSING_VALUE_CHAR)) {
                fields.put(attribute, Field.newMissingField());
            } else if (i == split.length - 1) {
            	cl = inputString;
            } else {
            	fields.put(attribute, Field.newField(inputString));
            }
        }
        Row value = new Row(cl, fields);
        dataset.add(value);
    }

}
