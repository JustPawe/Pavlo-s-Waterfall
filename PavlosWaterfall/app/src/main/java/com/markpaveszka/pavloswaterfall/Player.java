package com.markpaveszka.pavloswaterfall;

import java.util.ArrayList;

public class Player {

    private String name;
    private String gender;
    private ArrayList<String> mates = new ArrayList<>();
    private int consumedSips = 0;
    private int numberOfWaterfalls=0;

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getMates() {
        return mates;
    }

    public void addMate(String mate)
    {
       this.mates.add(mate);
    }

    public int getConsumedSips() {
        return consumedSips;
    }

    public void increaseConsumedSips()
    {
        this.consumedSips++;
    }

    public int getNumberOfWaterfalls() {
        return numberOfWaterfalls;
    }

    public void increaseWaterfalls()
    {
        this.numberOfWaterfalls++;
    }

    public void resetMates ()
    {
        this.mates.clear();
    }
}
