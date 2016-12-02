package replace.model;

/**
 * Created by Patrick on 28.11.16.
 */

import java.util.Map;
import java.util.HashMap;

public class Row {

    @Override
	public String toString() {
		return "Row [cl=" + cl + ", fields=" + fields + "]";
	}

	private String cl;
    private Map<Attribute, Field> fields = new HashMap<Attribute, Field> ();

    public Row(String cl, Map<Attribute, Field> fields) {
        this.cl = cl;
        this.fields = fields;
    }

    public String getCl() {
        return this.cl;
    }

    public Map<Attribute, Field> getFields() {
        return this.fields;
    }

    public Field getField(Attribute attribute) {
        return this.fields.get(attribute);
    }

}
