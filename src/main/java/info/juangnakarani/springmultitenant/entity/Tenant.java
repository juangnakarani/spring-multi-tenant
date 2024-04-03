package info.juangnakarani.springmultitenant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Tenant implements Serializable {
    @Id
    private Long id;
    private String name;
    private String url;
    private String password;
}
