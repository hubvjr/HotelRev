package com.hotelrev;

import java.util.HashMap;
import java.util.Map;

public class UserDto {

    public String email;
    public String name;

    public UserDto() {
    }

    public UserDto(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        return result;
    }
}
