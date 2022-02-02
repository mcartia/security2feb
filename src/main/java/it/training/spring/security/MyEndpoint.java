package it.training.spring.security;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Endpoint(id="myendpoint")
public class MyEndpoint {

    private Map<String, MyConfigValue> myMap;

    public MyEndpoint() {
        this.myMap = new ConcurrentHashMap<>();
        myMap.put("a",new MyConfigValue("a"));
        myMap.put("b",new MyConfigValue("b"));
        myMap.put("c",new MyConfigValue("c"));
    }

    @ReadOperation
    public Map<String, MyConfigValue> list() {
        return myMap;
    }

    @ReadOperation
    public MyConfigValue get(@Selector String key) {
        return myMap.get(key);
    }

    @WriteOperation
    public void updateValue(@Selector String key, String newValue) {
        myMap.put(key, new MyConfigValue(newValue));
    }

}
