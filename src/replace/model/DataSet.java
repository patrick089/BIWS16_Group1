package replace.model;

/**
 * Created by Patrick on 28.11.16.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Collection;

public class DataSet {

    @Override
	public String toString() {
		return "DataSet [attributes=" + attributes + ", classes=" + classes
				+ ", rows=" + rows + "]";
	}

	public List<Attribute> attributes = new ArrayList<Attribute>();
    public Map<String, Cl> classes = new HashMap<String, Cl>();
    public List<Row> rows = new ArrayList<Row>();

    public Cl getClByName (String name) {
        return this.classes.get(name);
    }

    public Collection<Cl> getCles () {
        return classes.values();
    }

    public Attribute addAttribute(String name, boolean cl) {
        Attribute attribute = Attribute.newAttribute(name);
        this.attributes.add(attribute);
        return attribute;
    }

    public void addAttributeForClazz(String name) {
        this.attributes.add(Attribute.newAttributeForClass(name));
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public Map<Attribute, String> getAttributeNames() {
        Map<Attribute, String> names = new HashMap<Attribute, String>();
        for (Attribute attribute : this.attributes) {
            names.put(attribute, attribute.getName());
        }
        return names;
    }

    public void add(Row value) {
        String cl = value.getCl();
        if (!this.classes.containsKey(cl)) {
            this.classes.put(cl, new Cl(cl));
        }
        this.classes.get(cl).addValue(value);
        this.rows.add(value);
    }

    public List<Row> getRows() {
        return this.rows;
    }

}
