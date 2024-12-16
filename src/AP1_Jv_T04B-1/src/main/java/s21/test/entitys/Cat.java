package s21.test.entitys;

import org.springframework.stereotype.Component;

@Component
public class Cat {
    private String name = "Meow";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
