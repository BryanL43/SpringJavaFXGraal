package org.custom.example.entity;

public class Airline {
    private Long id;
    private String name;

    public Airline() {}

    public Airline(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Airline(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Airline { id=" + id + ", name=" + name + " }";
    }
}
