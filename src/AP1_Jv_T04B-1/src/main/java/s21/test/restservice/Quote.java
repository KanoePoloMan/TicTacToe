package s21.test.restservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Quote(String type, Value value) {

}
