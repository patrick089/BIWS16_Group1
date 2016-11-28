package replace.data.data;

/**
 * Created by Patrick on 28.11.16.
 */
public class Attribute {

    private boolean cl = false;
    private String name;

    public static Attribute newAttribute (String name) {
        if (name.equals("class")) {
            return newAttributeForClass(name);
        }
        return new Attribute(name);
    }

    public static Attribute newAttributeForClass (String name) {
        Attribute attribute = new Attribute(name);
        attribute.cl = true;
        return attribute;
    }

    private Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isCl() {
        return this.cl;
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
        Attribute other = (Attribute) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
