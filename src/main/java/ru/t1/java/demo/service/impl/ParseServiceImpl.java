package ru.t1.java.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.service.ParseService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseServiceImpl<T> implements ParseService<T> {


    @Override
    public List<T> parseJson(String name, Class<T[]> arrayClass) {
        ObjectMapper mapper = new ObjectMapper();

        T[] arrays;
        try {
            arrays = mapper.readValue(new File("src/main/resources/" + name + ".json"), arrayClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Arrays.asList(arrays);
    }
}
