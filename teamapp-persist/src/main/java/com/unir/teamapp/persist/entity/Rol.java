package com.unir.teamapp.persist.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Rol extends BaseEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7669856742523510860L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(name = "rol_seq", sequenceName = "rol_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "codigo")
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;
}
