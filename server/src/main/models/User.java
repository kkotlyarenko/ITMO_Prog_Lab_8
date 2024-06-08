package main.models;

import main.utils.InvalidValueException;
import main.utils.Validatable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable, Validatable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "поле не может быть null")
    private String login; //Поле не может быть null

    @Column(nullable = false)
    @NotNull(message = "поле не может быть null")
    private String password; //Поле не может быть null

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        if (login == null) throw new InvalidValueException("login", "поле не может быть null");
        this.login = login;
    }

    public void setPassword(String password) {
        if (password == null) throw new InvalidValueException("password", "поле не может быть null");
        this.password = password;
    }

    @Override
    public void validate() {
        if (login == null) throw new InvalidValueException("login", "поле не может быть null");
        if (password == null) throw new InvalidValueException("password", "поле не может быть null");
    }
}
