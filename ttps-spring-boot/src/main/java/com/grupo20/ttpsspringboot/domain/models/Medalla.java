package com.grupo20.ttpsspringboot.domain.models;


import com.grupo20.ttpsspringboot.domain.enums.MedallaEnum;
import com.grupo20.ttpsspringboot.domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medalla extends IdentifiableEntity {


    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private MedallaEnum tipo;

    @Column(nullable = false)
    private Date fechaAsignacion;
    private Date fechaVencimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // --- getters ---

}
