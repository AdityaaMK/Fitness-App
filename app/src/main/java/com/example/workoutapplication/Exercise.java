package com.example.workoutapplication;

public class Exercise {
    String name;
    String type;
    String resistanceType;
    int repetitions;
    int sets;
    int resistance;
    int rest;

    public Exercise(String name, String type, int repetitions, int sets, int rest) {
        this.name = name;
        this.type = type;
        this.resistanceType = null;
        this.repetitions = repetitions;
        this.sets = sets;
        this.resistance = 0;
        this.rest = rest;
    }

    public Exercise(String name, String type, String resistanceType, int resistance, int repetitions, int sets, int rest) {
        this.name = name;
        this.type = type;
        this.resistanceType = resistanceType;
        this.repetitions = repetitions;
        this.sets = sets;
        this.resistance = resistance;
        this.rest = rest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResistanceType() {
        return resistanceType;
    }

    public void setResistanceType(String resistanceType) {
        this.resistanceType = resistanceType;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    @Override
    public String toString() {
        if (resistanceType == null || resistance <= 0) {
            return (sets + "\tx\t" + repetitions + "\t" + name + " (" + rest + "s. rest)");
        } else {
            return (sets + " \tx\t" + repetitions + "\t" + name + " w/ " + resistance + " lb. " + resistanceType + " resistance (" + rest + "s. rest)");
        }
    }
}
