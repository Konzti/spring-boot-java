package demo.com.jwtdemo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy= AUTO)
    private Long id;
    private String name;
}
