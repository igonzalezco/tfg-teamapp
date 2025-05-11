package com.unir.teamapp.persist.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "encuesta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Encuesta extends AuditableEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7632784157829874860L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "encuesta_id_generator", sequenceName = "sq_encuesta_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encuesta_id_generator")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "multiple")
    private Boolean multiple;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", referencedColumnName = "id", nullable = false)
    private Equipo equipo;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "encuesta", fetch = FetchType.LAZY)
    private List<Opcion> opciones;

}
