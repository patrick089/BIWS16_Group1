package replace.bi.inputOutput;

/**
 * Created by Patrick on 28.11.16.
 */

import replace.bi.data.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import replace.bi.DataMiningException;

public class DataSetReader {

    private static Pattern pattern_numeric;
    private static final String SEPERATION_CHAR = ",";

    private String filename;
    private DataSet dataset;

    static {
        pattern_numeric = Pattern.compile("-?[\\d.]+(?:e-?\\d+)?");
    }

    public DataSetReader(String filename) {
        this.filename = filename;
    }

    public DataSet read() throws Exception {
        BufferedReader br = null;
        this.dataset = new DataSet();
        boolean first = true;
        try {
            br = new BufferedReader(new FileReader(this.filename));
            String line;
            // int i = 0;
            while ((line = br.readLine()) != null) {
                if (first) {
                    this.readHeader(line);
                    first = false;
                } else {
                    this.readLine(line);
                    // i++;
                }
            }
            // this.dataset.setLength(i);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DataMiningException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataMiningException();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new DataMiningException();
                }
            }
        }
        return dataset;
    }

    private void readHeader(String line) {
        StringTokenizer st = new StringTokenizer(line, SEPERATION_CHAR);
        while (st.hasMoreTokens()) {
            String inputString = st.nextToken();
            this.dataset.addAttribute(inputString);
        }
    }

    private void readLine(String line) {
        StringTokenizer st = new StringTokenizer(line, SEPERATION_CHAR);
        Map<Attribute, Field> fields = new HashMap<Attribute, Field>();
        String cl = "";
        int i = 0;
        while (st.hasMoreTokens()) {
            Attribute attribute = dataset.getAttributes().get(i);
            String inputString = st.nextToken();
            if (isNumeric(inputString)) {
                fields.put(attribute,
                        Field.newField(Double.parseDouble(inputString)));
            } else if (inputString.equals(Field.MISSING_VALUE_CHAR)) {
                fields.put(attribute, Field.newMissingField());
            } else {
                cl = inputString;
            }
            i++;
        }
        Row value = new Row(cl, fields);
        this.dataset.add(value);
    }

    private boolean isNumeric(String s) {
        Matcher m = pattern_numeric.matcher(s);
        return m.matches();
    }

}
