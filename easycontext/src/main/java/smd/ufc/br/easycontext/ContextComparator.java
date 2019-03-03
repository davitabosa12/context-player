package smd.ufc.br.easycontext;

public interface ContextComparator {

    /**Compares this ContextDefinition to other.
     *
     * @param otherContext The other ContextDefinition
     *
     * @return a value between 0 and 1. 0 when they are different, 1 when they are identical.
     */
    float calculateConfidence(CurrentContext currentContext);
}
