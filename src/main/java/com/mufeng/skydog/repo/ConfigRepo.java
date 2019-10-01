package com.mufeng.skydog.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

//@Service
//@Slf4j
public class ConfigRepo {

    public<T> List<T> getObjectList(String objectCode, Class<T> clas){
        InputStream inputStream = Object.class.getResourceAsStream("/code.properties");
        return null;
    }
}
