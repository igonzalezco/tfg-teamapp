package com.unir.teamapp.persist.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.unir.teamapp.persist.annotation.UniqueEntity;
import com.unir.teamapp.persist.util.FieldConstants;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@UniqueEntity(fields = { FieldConstants.EMAIL })
public class Usuario extends AuditableEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7620236984223510860L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @ToString.Exclude
    @NotNull
    @Column(name = "password")
    private String password;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<UsuarioEquipo> usuarioEquipos;
}
