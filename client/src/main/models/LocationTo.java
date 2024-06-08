package main.models;

import main.utils.InvalidValueException;
import main.utils.Validatable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "location_to")
public class LocationTo implements Validatable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long x; //Поле не может быть null
    private Long y; //Поле не может быть null
    private Double z; //Поле не может быть null

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        if (x == null) throw new InvalidValueException("x", "поле не может быть null");
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        if (y == null) throw new InvalidValueException("y", "поле не может быть null");
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        if (z == null) throw new InvalidValueException("z", "поле не может быть null");
        this.z = z;
    }

    @Override
    public String toString() {
        return "x: " + x +
                ", y: " + y +
                ", z: " + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationTo that = (LocationTo) o;
        return x.equals(that.x) && y.equals(that.y) && z.equals(that.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public void validate() {
        if (x == null) throw new InvalidValueException("x", "поле не может быть null");
        if (y == null) throw new InvalidValueException("y", "поле не может быть null");
        if (z == null) throw new InvalidValueException("z", "поле не может быть null");
    }
}
