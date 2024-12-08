package marketing.management.system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name="role")
@Getter
@Setter
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name")
    @EqualsAndHashCode.Include
    private String name;


    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }


}