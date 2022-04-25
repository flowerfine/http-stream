package cn.sliew.http.stream.arrow.json.consumer;

import org.apache.arrow.vector.FieldVector;

public abstract class BaseJsonConsumer<T extends FieldVector> implements Consumer<T> {

    protected T vector;
    protected int currentIndex;

    public BaseJsonConsumer(T vector) {
        this.vector = vector;
    }

    @Override
    public void addNull() {
        currentIndex++;
    }

    @Override
    public void setPosition(int index) {
        this.currentIndex = index;
    }

    @Override
    public FieldVector getVector() {
        return vector;
    }

    public boolean resetValueVector(T vector) {
        this.vector = vector;
        this.currentIndex = 0;
        return true;
    }

    @Override
    public void close() throws Exception {
        vector.close();
    }
}
