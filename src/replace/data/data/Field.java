package replace.data.data;

/**
 * Created by Patrick on 28.11.16.
 */
public class Field {

    public static String MISSING_VALUE_CHAR = "?";

    private boolean missing = false;
    private double value;

    public static Field newField(double value) {
        return new Field(value);
    }

    public static Field newMissingField() {
        return new Field(true);
    }

    private Field(double value) {
        this.value = value;
    }

    private Field(boolean missing) {
        this.missing = missing;
    }

    public double getValue() {
        return this.value;
    }

    public boolean isMissing() {
        return this.missing;
    }

    public String toString () {
        if (missing) {
            return MISSING_VALUE_CHAR;
        } else {
            return this.value + "";
        }
    }

}
