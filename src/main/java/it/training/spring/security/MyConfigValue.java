package it.training.spring.security;

public class MyConfigValue {

    String value;

    public MyConfigValue() {
        super();
    }

    public MyConfigValue(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MyConfigValue{" +
                "value='" + value + '\'' +
                '}';
    }
}
