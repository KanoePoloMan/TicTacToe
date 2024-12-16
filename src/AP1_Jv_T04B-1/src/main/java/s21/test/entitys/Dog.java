package s21.test.entitys;

import org.springframework.stereotype.Component;

@Component
public class Dog {
    private String name = "Gav";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
