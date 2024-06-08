package main.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;
import main.utils.Validatable;
import main.utils.InvalidValueException;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "routes")
public class Route implements Comparable<Route>, Validatable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "значение поля должно быть больше 0")
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    @NotNull(message = "поле не может быть null")
    @NotBlank(message = "строка не может быть пустой")
    private String created_by;

    @Column(nullable = false)
    @NotNull(message = "поле не может быть null")
    @NotBlank(message = "строка не может быть пустой")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", nullable = false)
    @NotNull(message = "поле не может быть null")
    private Coordinates coordinates; //Поле не может быть null

    @Column(nullable = false)
    @NotNull(message = "поле не может быть null")
    private java.time.LocalDateTime creationDate = java.time.LocalDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location_from_id")
    private LocationFrom from; //Поле может быть null

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location_to_id", nullable = false)
    @NotNull(message = "поле не может быть null")

    private LocationTo to; //Поле не может быть null

    @Column(nullable = false)
    @NotNull(message = "поле не может быть null")
    @Min(value = 1, message = "значение поля должно быть больше 1")
    private Long distance; //Поле не может быть null, Значение поля должно быть больше 1

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) throw new InvalidValueException("id", "значение поля должно быть больше 0");
        this.id = id;
    }

    public String getCreatedBy() {
        return created_by;
    }

    public void setCreatedBy(String created_by) {
        if (created_by == null) throw new InvalidValueException("created_by", "поле не может быть null");
        if (created_by.isBlank()) throw new InvalidValueException("created_by", "строка не может быть пустой");
        this.created_by = created_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new InvalidValueException("name", "поле не может быть null");
        if (name.isBlank()) throw new InvalidValueException("name", "строка не может быть пустой");
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new InvalidValueException("coordinates", "поле не может быть null");
        this.coordinates = coordinates;
    }

    public LocationFrom getFrom() {
        return from;
    }

    public void setFrom(LocationFrom from) {
        if (from == null) throw new InvalidValueException("from", "поле не может быть null");
        this.from = from;
    }

    public LocationTo getTo() {
        return to;
    }

    public void setTo(LocationTo to) {
        if (to == null) throw new InvalidValueException("to", "поле не может быть null");
        this.to = to;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        if (distance <= 0) throw new InvalidValueException("distance", "значение поля должно быть больше 0");
        this.distance = distance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) throw new InvalidValueException("creationDate", "поле не может быть null");
        this.creationDate = creationDate;
    }

    @Override
    public int compareTo(Route o) {
        return Long.compare(this.distance, o.distance);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", created_by='" + created_by + '\'' +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id == route.id && name.equals(route.name) && coordinates.equals(route.coordinates) && creationDate.equals(route.creationDate) && from.equals(route.from) && to.equals(route.to) && distance.equals(route.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to, distance);
    }

    @Override
    public void validate() {
        if (id <= 0) throw new InvalidValueException("id", "значение поля должно быть больше 0");
        if (name == null) throw new InvalidValueException("name", "поле не может быть null");
        if (name.isBlank()) throw new InvalidValueException("name", "строка не может быть пустой");
        if (coordinates == null) throw new InvalidValueException("coordinates", "поле не может быть null");
        if (creationDate == null) throw new InvalidValueException("creationDate", "поле не может быть null");
        if (from == null) throw new InvalidValueException("from", "поле не может быть null");
        if (to == null) throw new InvalidValueException("to", "поле не может быть null");
        if (distance <= 0) throw new InvalidValueException("distance", "значение поля должно быть больше 0");
    }
}
