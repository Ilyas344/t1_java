package ru.t1.java.demo.service;


import java.util.List;

public interface ParseService<T> {
    List<T> parseJson(String name, Class<T[]> arrayClass);
}
