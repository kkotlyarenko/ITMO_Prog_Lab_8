package main.models;

import main.utils.InvalidValueException;
import main.utils.Validatable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "location_from")
public class LocationFrom implements Validatable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long x; //Поле не может быть null
    private int y;
    private String name; //Строка не может быть пустой, Поле может быть null

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        if (x == null) throw new InvalidValueException("x", "поле не может быть null");
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new InvalidValueException("name", "поле не может быть null");
        if (name.isBlank()) throw new InvalidValueException("name", "строка не может быть пустой");
        this.name = name;
    }

    @Override
    public String toString() {
        return "x: " + x +
                ", y: " + y +
                ", name: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationFrom that = (LocationFrom) o;
        return y == that.y && x.equals(that.x) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    @Override
    public void validate() {
        if (x == null) throw new InvalidValueException("x", "поле не может быть null");
        if (name == null) throw new InvalidValueException("name", "поле не может быть null");
        if (name.isBlank()) throw new InvalidValueException("name", "строка не может быть пустой");
    }
}
