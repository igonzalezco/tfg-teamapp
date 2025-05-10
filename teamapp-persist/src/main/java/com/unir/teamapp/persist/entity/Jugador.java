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
@Table(name = "jugador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Jugador extends AuditableEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7669576984229874860L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jugador_seq")
    @SequenceGenerator(name = "jugador_seq", sequenceName = "jugador_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "dorsal")
    private Integer dorsal;

    @JoinColumn(name = "equipo", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Equipo equipo;

    @JoinColumn(name = "posicion", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Posicion posicion;

    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuario;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "jugador", fetch = FetchType.LAZY)
    private List<CuotaJugador> cuotaJugadores;

}
