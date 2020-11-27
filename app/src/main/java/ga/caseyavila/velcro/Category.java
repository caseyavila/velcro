package ga.caseyavila.velcro;

public class Category {

    private String name;
    private double weight;

    public Category(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}
