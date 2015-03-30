package com.feheren_fekete.espresso;

public interface ProgressReporter {
    public void reportLog(String message, CoffeeMachine coffeeMachine, CoffeeQueue coffeeQueue);
    public void reportStateChange(Object state);
}
