/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa un hobby dentro del sistema.
 *
 * Los hobbies forman un catálogo reutilizable que puede ser asociado a
 * múltiples estudiantes mediante una relación muchos a muchos.
 *
 * Características: - Cada hobby tiene un nombre único - Puede estar asociado a
 * múltiples estudiantes - No se elimina normalmente, solo se remueven
 * relaciones
 *
 * @author Afgord
 */
@Entity
@Table(name = "hobby")
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hobby")
    private Long id;

    /**
     * Nombre del hobby.
     *
     * Debe ser único para evitar duplicados en el catálogo.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    /**
     * Descripción opcional del hobby.
     *
     * Máximo 500 caracteres.
     */
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    /**
     * Conjunto de estudiantes asociados a este hobby.
     *
     * Relación inversa de la asociación muchos a muchos definida en la entidad
     * Estudiante.
     *
     * Se utiliza "mappedBy" porque la tabla intermedia es gestionada desde
     * Estudiante.
     */
    @ManyToMany(mappedBy = "hobbies")
    private Set<Estudiante> estudiantes = new HashSet<>();

    public Hobby() {
    }

    public Hobby(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(Set<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    /**
     * Calcula el hash basado en el identificador del hobby.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compara dos hobbies basándose en su identificador.
     *
     * Nota: Este método depende de que el ID esté asignado, por lo que puede no
     * comportarse como se espera en entidades no persistidas.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hobby)) {
            return false;
        }
        Hobby other = (Hobby) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Representación en cadena del hobby.
     *
     * Incluye información básica y evita referencias a estudiantes para
     * prevenir salidas extensas o recursivas.
     */
    @Override
    public String toString() {
        return "Hobby{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + '}';
    }
}
