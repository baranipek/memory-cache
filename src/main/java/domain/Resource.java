package domain;

public  class Resource<V> {
    private final V value;
    public Resource(V value) { this.value = value; }
    public V getValue() { return value; }
}
