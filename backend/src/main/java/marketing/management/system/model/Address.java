package marketing.management.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="addresses")

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "State must contain only letters")
    @Column
    private String state;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "City must contain only letters")
    @Column
    private String city;

    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Street must contain only letters and digits")
    @Column
    private String street;

}
