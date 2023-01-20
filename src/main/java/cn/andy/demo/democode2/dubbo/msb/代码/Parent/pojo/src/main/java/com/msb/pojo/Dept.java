package com.msb.pojo;

import java.io.Serializable;

/**
 * Created by IBM on 2020/9/27.
 */
public class Dept implements Serializable{

    private Integer id;

    private String name  ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
