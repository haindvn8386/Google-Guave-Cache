package guavacache.cache.model;

import lombok.Data;

@Data
public class Airport {
    private String name;
    private String code;

    public Airport(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
