package data;

import visitors.Visitor;

/**
 * Visitable interface, from visitor pattern. Is implemented by each class that 
 * needs to be visited
 * @author visoft
 */
public interface Visitable {
        public void accept(Visitor visitor );
}
