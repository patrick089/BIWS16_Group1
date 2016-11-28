package replace.bi.data;

/**
 * Created by Patrick on 28.11.16.
 */

import java.util.ArrayList;
import java.util.List;

public class Cl {

    private String name;
    private List<Row> values = new ArrayList<Row>();

    public Cl(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<Row> getValues() {
        return this.values;
    }

    public void addValue(Row value) {
        this.values.add(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cl other = (Cl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
