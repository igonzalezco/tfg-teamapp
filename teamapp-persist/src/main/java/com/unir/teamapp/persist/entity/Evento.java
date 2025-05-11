package com.unir.teamapp.persist.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Evento extends AuditableEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7669856984229874860L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "evento_id_generator", sequenceName = "sq_evento_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_id_generator")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "titulo")
    private String titulo;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", referencedColumnName = "id", nullable = false)
    private Equipo equipo;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "evento", fetch = FetchType.LAZY)
    private List<Convocatoria> convocatorias;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "evento", fetch = FetchType.LAZY)
    private List<Necesidad> necesidades;

}
