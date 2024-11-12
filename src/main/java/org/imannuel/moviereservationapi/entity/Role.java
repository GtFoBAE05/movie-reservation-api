package org.imannuel.moviereservationapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.imannuel.moviereservationapi.constant.Constant;
import org.imannuel.moviereservationapi.constant.RoleEnum;

@Entity
@Table(name = Constant.ROLE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleEnum name;
}
