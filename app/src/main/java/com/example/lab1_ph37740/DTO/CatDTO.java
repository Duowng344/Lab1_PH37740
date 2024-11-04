package com.example.lab1_ph37740.DTO;

public class CatDTO {
    int id;
    String name;

    public String toString (){
        return "ID cat: "+id+", name: " + name;
    }
    // tạo các hàm getter và setter bằng generate


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
}
