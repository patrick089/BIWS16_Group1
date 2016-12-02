package replace;

/**
 * Created by Patrick on 28.11.16.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import replace.model.Attribute;
import replace.model.DataSet;
import replace.model.Field;
import replace.model.Row;

public class DataSetWriter {

    private static final String SEPERATION_CHAR = ",";

    private String filename;
    private BufferedWriter writer;
    private DataSet dataset;

    public DataSetWriter(String filename, DataSet dataset) {
        this.filename = filename;
        this.dataset = dataset;
    }

    public void write() {
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writeHeader(dataset.getAttributeNames(), "");
            for (Row value : dataset.getRows()) {
                writeLine(value.getFields(), value.getCl());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("error while writing file - " + e.getMessage());
        }
    }
    
    public void writeHeader(Map<Attribute, ?> items, String clName) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Attribute attribute : dataset.getAttributes()) {
            if (first) {
                first = false;
            } else {
                builder.append(SEPERATION_CHAR);
            }
            if (attribute.isCl() && !clName.isEmpty()) {
                builder.append(clName);
            } else {
                builder.append(items.get(attribute) + "");
            }
        }
        try {
            writer.write(builder.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("error writing file - " + e.getMessage());
        }
    }

    public void writeLine(Map<Attribute, Field> items, String clName) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Attribute attribute : dataset.getAttributes()) {
            if (first) {
                first = false;
            } else {
                builder.append(SEPERATION_CHAR);
            }
            if (attribute.isCl() && !clName.isEmpty()) {
                builder.append(clName);
            } else if(null!=items.get(attribute)) {
                builder.append(items.get(attribute).getValue() + "");
            }
        }
        builder.append(clName);
        try {
            writer.write(builder.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("error writing file - " + e.getMessage());
        }
    }

}
