package id.putraprima.retrofit.api.models;

public class Data<T> {

    T data;
    T error;

    public Data(T data) {
        this.data = data;
    }

    public T getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
