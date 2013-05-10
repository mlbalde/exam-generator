package data;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A general directive. Each directive have various parameters.
 * Internally is a Map with key/object pairs. Both are strings.
 * 
 * @author visoft
 */
public class Directive {
    private Map<String,String> parameters;

    public Directive() {
        parameters=new TreeMap<String,String>();
    }

    public Map<String, String> getMap() {
        return parameters;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.parameters);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Directive other = (Directive) obj;
        if (!Objects.equals(this.parameters, other.parameters)) {
            return false;
        }
        return true;
    }

    public String get(Object key) {
        return parameters.get(key);
    }

    public String put(String key, String value) {
        return parameters.put(key, value);
    }


    /**
     * Creates a deep clone of the object.
     * @return 
     */
    public Directive DeepClone(){
        Directive dir=new Directive();
        for(String k: parameters.keySet()) {
            dir.parameters.put(k, parameters.get(k));
        }
        return dir;
    }
}
