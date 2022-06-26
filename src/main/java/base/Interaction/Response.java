package base.Interaction;

import base.Vehicle.Vehicle;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.NavigableSet;

public class Response implements Serializable {
    ArrayDeque<Vehicle> vehiclesCollection;
    private ResponseStatus res;
    private String body;
    private String[] responseBodyArgs;

    public Response(ResponseStatus res, String body, String[] responseBodyArgs, ArrayDeque<Vehicle> vehiclesCollection) {
        this.res = res;
        this.body = body;
        this.vehiclesCollection = vehiclesCollection;
        this.responseBodyArgs = responseBodyArgs;
    }

    public ResponseStatus getRes() {
        return res;
    }

    public ArrayDeque<Vehicle> getVehiclesCollection() {
        return vehiclesCollection;
    }

    public String[] getResponseBodyArgs() {
        return responseBodyArgs;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "res=" + res +
                ", body='" + body + '\'' +
                '}';
    }
}
