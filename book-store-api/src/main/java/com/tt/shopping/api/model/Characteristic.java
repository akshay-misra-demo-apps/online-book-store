package com.tt.shopping.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Characteristic<T> {

    private String name;

    private T value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Characteristic)) return false;
        Characteristic<?> that = (Characteristic<?>) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
