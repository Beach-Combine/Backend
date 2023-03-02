package beachcombine.backend.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeachRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beachRange_id")
    private Long id;

    @OneToOne(mappedBy = "beachRange", fetch= LAZY, cascade = CascadeType.ALL)
    private Beach beach;

    private String coords="";
}
