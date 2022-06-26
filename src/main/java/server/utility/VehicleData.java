package server.utility;

import base.Exception.DatabaseHandlingException;
import base.Vehicle.FuelType;
import base.Vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Operates the collection itself.
 */
public class VehicleData {
    private ArrayDeque<Vehicle> vehicleCollection = new ArrayDeque<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private DatabaseCollectionManager databaseCollectionManager;
    public VehicleData(DatabaseCollectionManager databaseCollectionManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
    }
    /**
     * @return The collection itself.
     */
    public ArrayDeque<Vehicle> getCollection() {
        return vehicleCollection;
    }
    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return this.lastInitTime;
    }
    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDateTime getLastSaveTime() {
        return this.lastSaveTime;
    }
    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return vehicleCollection.getClass().getName();
    }
    /**
     * @return Size of the collection.
     */
    public Integer collectionSize() {
        return vehicleCollection.size();
    }

    /**
     * add element to collection
     */
    public void addElement(Vehicle vehicle) {
        vehicleCollection.add(vehicle);
    }

    /**
     * get vehicle by id itself
     * @param id
     * @return vehicle
     */
    public Vehicle getById(int id) {

        return vehicleCollection
                .stream()
                .filter(vehicle ->
                        vehicle.getId()
                                .equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * return vehicle that equal itself
     * @param vehicle
     * @return vehicle
     */
    public Vehicle getByValue(Vehicle vehicle) {
        return vehicleCollection
                .stream()
                .filter(vehicle1->
                        vehicle1.equals(vehicle))
                .findFirst()
                .orElse(null);
    }

    /**
     * Update vehicle by ID itself
     * @param vehicle
     */
    public void updateById(Vehicle vehicle) {
        if(vehicleCollection.removeIf(vehicle1 -> vehicle1.getId().equals(vehicle.getId()))) {
            vehicleCollection.add(vehicle);
            vehicleCollection = vehicleCollection
                    .stream()
                    .sorted(Comparator
                            .comparing(Vehicle::getId))
                    .collect(Collectors.toCollection(ArrayDeque<Vehicle>::new));
        }
    }

    /**
     * Counting group that have the same capacity
     * @return map <capacity, value>
     */
    public HashMap<Long, Integer> groupCountingByCapacity() {
        HashMap<Long,Integer> result = new HashMap<>();

        List<Integer> temp = vehicleCollection
                            .stream()
                            .map(Vehicle::getFuelConsumption).collect(Collectors.toList());

        temp.forEach(integer -> result.put(Long.valueOf(integer), result.get(integer) + 1));

        return result;
    }

    /**
     * Count the number is vehicle that have lower fuel type than the given
     * @param fuelType
     * @return number of vehicle
     */
    public Integer CountLessThanFuelType(FuelType fuelType) {

        return (int) vehicleCollection.stream()
                .filter(vehicle -> vehicle.getFuelType().compareTo(fuelType) < 0)
                .count();
    }

    /**
     * return list of vehicle that have smaller consumption than the given
     * @param consumptionToCompare
     * @return ArrayDeque of vehicle
     */
    public ArrayDeque<Vehicle> FilterLessThanFuelConsumption(int consumptionToCompare) {

        ArrayDeque<Vehicle> result = vehicleCollection
                .stream()
                .filter(vehicle -> vehicle.getFuelConsumption() < consumptionToCompare)
                .collect(Collectors.toCollection(ArrayDeque<Vehicle>::new));

        return result;
    }

    /**
     * get last vehicle in the list
     * @return vehicle
     */
    public Vehicle getLast() {
        if(vehicleCollection.isEmpty()) return null;
        return vehicleCollection.peekLast();
    }

    /**
     * remove first element in collection
     */
    public void removeFirst() {
        vehicleCollection.removeFirst();
    }

    /**
     * clear collection
     */
    public void clearCollection() {
        vehicleCollection.clear();
    }

    /**
     * remove vehicle is given from collection
     * @param vehicle
     */
    public void removeFromCollection(Vehicle vehicle) {
                vehicleCollection.remove(vehicle);
    }

    /**
     * load collection from file
     */
    public void loadCollection() {
        try{
            vehicleCollection = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Output.println("Collection is loaded");
        } catch (DatabaseHandlingException e) {
            e.printStackTrace();
            vehicleCollection = new ArrayDeque<>();
            Output.printError("Collection cannot load!");
        }
    }

    /**
     * auto generate next id when vehicle is created
     * @return next ID
     */
    public Integer generateNextId() {
        if(vehicleCollection.isEmpty()) return 1;
        return vehicleCollection.getLast().getId() + 1;
    }

    /**
     * remove vehicle form list if the give is better
     * @param vehicleToCompare
     */
    public void removeGreater(Vehicle vehicleToCompare) {
        vehicleCollection.removeIf(vehicle ->vehicle.compareTo(vehicleToCompare) > 0);
    }

    @Override
    public String toString() {
        if(vehicleCollection.isEmpty()) return "Collection is empty!";

        String result = "";
        for(Vehicle vehicle : vehicleCollection) {
            result += vehicle + "\n";
        }

        return result;
    }
}
