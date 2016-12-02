package replace.model;

/**
 * Created by Patrick on 28.11.16.
 */
public class Field {

    public static String MISSING_VALUE_CHAR = "?";

    private boolean missing = false;
    private Object value;

    public static Field newField(Object value) {
        return new Field(value);
    }

    public static Field newMissingField() {
        return new Field(true);
    }

    private Field(Object value) {
        this.value = value;
    }

    private Field(boolean missing) {
        this.missing = missing;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isMissing() {
        return this.missing;
    }

    @Override
	public String toString() {
		return "Field [missing=" + missing + ", value=" + value + "]";
	}

}
