package replace.data.inputOutput;

/**
 * Created by Patrick on 28.11.16.
 */

import replace.data.data.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class DataSetWriter {

    private static final String SEPERATION_CHAR = ",";

    private String filename;
    private BufferedWriter writer;
    private DataSet dataset;

    public DatasetWriter(String filename, DataSet dataset) {
        this.filename = filename;
        this.dataset = dataset;
    }

    public void write() {
        try {
            this.writer = new BufferedWriter(new FileWriter(this.filename));
            this.writeLine(dataset.getAttributeNames(), "");
            for (Row value : dataset.getRows()) {
                this.writeLine(value.getFields(), value.getCl());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataMiningException();
        }
    }

    public void writeLine(Map<Attribute, ?> items, String clazzName) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Attribute attribute : dataset.getAttributes()) {
            if (first) {
                first = false;
            } else {
                builder.append(SEPERATION_CHAR);
            }
            if (attribute.isClazz() && !clazzName.isEmpty()) {
                builder.append(clazzName);
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
            throw new DataMiningException();
        }
    }

}
