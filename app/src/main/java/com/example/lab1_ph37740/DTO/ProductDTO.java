package com.example.lab1_ph37740.DTO;

public class ProductDTO {
    int id;
    String name;
    String price;
    String id_cat;

    public String toString(){
        return "ID cat: "+id+", name: " + name+",Price: " +price+",id_cat: "+id_cat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId_cat() {
        return id_cat;
    }

    public void setId_cat(String id_cat) {
        this.id_cat = id_cat;
    }
}
