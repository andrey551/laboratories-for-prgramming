package base.Interaction;

import base.Vehicle.Coordinates;
import base.Vehicle.FuelType;
import base.Vehicle.Vehicle;

import java.io.Serializable;

public class WrappedVehicle implements Serializable {
    private String name;
    private Coordinates coordinates;
    private Double enginePower;
    private Long capacity;
    private int fuelConsumption;
    private FuelType fuelType;

    public WrappedVehicle(String name, Coordinates coordinates, Double enginePower,
                          Long capacity, int fuelConsumption, FuelType fuelType) {
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
       this.capacity = capacity;
        this.fuelConsumption = fuelConsumption;
        this.fuelType = fuelType;
    }

    public String getName() {
        return this.name;
    }

    public Coordinates getCoordinates () {
        return this.coordinates;
    }

    public Double getEnginePower() {
        return this.enginePower;
    }
    public Long getCapacity () {
        return this.capacity;
    }
    public int getFuelConsumption() {
        return this.fuelConsumption;
    }

    public FuelType getFuelType() {
        return this.fuelType;
    }

    @Override
    public String toString() {
        String info = "";
        info += "\n Name:" + this.getName();
        info += "\n Coordinate: " + this.getCoordinates().toString();
        info += "\n Engine Power: " + this.getEnginePower();
        info += "\n Capacity: " + this.getCapacity();
        info += "\n Fuel Consumption: " + this.getFuelConsumption();
        info += "\n Fuel Type: " + this.getFuelType();

        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + enginePower.hashCode()+
                capacity.hashCode()+ fuelConsumption + fuelType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj instanceof Vehicle) {
            Vehicle vehicleToCompare = (Vehicle) obj;
            return name.equals(vehicleToCompare.getName()) && coordinates.equals(vehicleToCompare.getCoordinates())&&
                    enginePower.equals(vehicleToCompare.getEnginePower()) && fuelConsumption == vehicleToCompare.getFuelConsumption()&&
                    fuelType.equals(vehicleToCompare.getFuelType()) && capacity.equals(vehicleToCompare.getCapacity());
        }
        return false;
    }
}
