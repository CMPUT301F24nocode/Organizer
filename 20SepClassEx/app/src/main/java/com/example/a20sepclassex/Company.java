/*
* Author: Fuad Chowdhury
* CCID: muhtasi3
* Student ID: 1768166
*/

package com.example.a20sepclassex;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Company {
    private List<Store> stores;
    private List<Vehicle> vehicles;

    public Company() {
        stores = new ArrayList<Store>();
        vehicles = new ArrayList<Vehicle>();
        stores.add(new Store(this));
    }

    public void shutDown() {
        for (Store store : stores) {
            store.shutdown();
        }
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}

public class Store {
    private Company company;
    private List<Vehicle> vehicles;
    private boolean alive;

    public Store(Company company) {
        this.company = company;
    }

    public Rental rentVehicle(Vehicle vehicle, Customer customer) {
        return new Rental(vehicle, this, customer);
    }

    public Rental returnRental(Rental rental) {
        return rental;
    }

    public void shutdown() {
        alive = false;
        vehicles.clear();
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}

public class Vehicle {
    private Company company;
    private double garageSpace = 23.0;

    public Vehicle(Company company, double garageSpace) {
        this.company = company;
        this.garageSpace = garageSpace;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public double getGarageSpace() {
        return garageSpace;
    }

    public void setGarageSpace(double garageSpace) {
        this.garageSpace = garageSpace;
    }
}

public class Customer {
    private Optional<Rental> rental;

    public void rent(Rental rental) {
        if (this.rental.isPresent()) {
            throw new RuntimeException("Already renting!");
        }
    }
}

public class Rental {
    private Vehicle vehicle;
    private Store from;
    private Customer customer;

    public Rental(Vehicle vehicle, Store from, Customer customer) {
        this.vehicle = vehicle;
        this.from = from;
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Store getFrom() {
        return from;
    }

    public void setFrom(Store from) {
        this.from = from;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

public interface RentStatus {
    boolean isRented();
    boolean canRent();
}

public class Rentable {
    private RentStatus rentStatus;

    public Rentable(RentStatus rentStatus) {
        this.rentStatus = rentStatus;
    }

    public RentStatus getRentStatus() {
        return rentStatus;
    }

    public void setAtStoreRentStatus(Store store) {
        this.rentStatus = new AtStoreRentStatus(store);
    }

    public void setRentedRentStatus(Rental rental) {
        this.rentStatus = new RentedRentStatus(rental);
    }
}

public class RentedRentStatus implements RentStatus {
    private Rental rental;

    @Override
    public boolean isRented() {
        return rental != null;
    }

    @Override
    public boolean canRent() {
        return rental == null;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public RentedRentStatus(Rental rental) {
        this.rental = rental;
    }
}

public class AtStoreRentStatus implements RentStatus {
    private Store store;

    @Override
    public boolean isRented() {
        return store.getVehicles().isEmpty();
    }

    @Override
    public boolean canRent() {
        return !store.getVehicles().isEmpty();
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public AtStoreRentStatus(Store store) {
        this.store = store;
    }
}





