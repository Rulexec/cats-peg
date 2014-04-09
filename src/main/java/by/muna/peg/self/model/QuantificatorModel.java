package by.muna.peg.self.model;

public class QuantificatorModel {
    private int from, to;
    private boolean toInfinity;

    public QuantificatorModel(int from) {
        this.from = from;
        this.toInfinity = true;
    }
    public QuantificatorModel(int from, int to) {
        this.from = from;
        this.to = to;
        this.toInfinity = false;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public boolean isToInfinity() {
        return toInfinity;
    }
}
