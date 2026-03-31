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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Afgord
 */
@Entity
@Table(name = "matches",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"estudiante1_id", "estudiante2_id"})
        })
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_match")
    private Long id;

    @Column(name = "fecha_match", nullable = false)
    private LocalDate fechaMatch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante1_id", nullable = false)
    private Estudiante estudiante1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante2_id", nullable = false)
    private Estudiante estudiante2;

    public Match() {
    }

    public Match(LocalDate fechaMatch, Estudiante estudiante1, Estudiante estudiante2) {
        this.fechaMatch = fechaMatch;
        this.estudiante1 = estudiante1;
        this.estudiante2 = estudiante2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaMatch() {
        return fechaMatch;
    }

    public void setFechaMatch(LocalDate fechaMatch) {
        this.fechaMatch = fechaMatch;
    }

    public Estudiante getEstudiante1() {
        return estudiante1;
    }

    public void setEstudiante1(Estudiante estudiante1) {
        this.estudiante1 = estudiante1;
    }

    public Estudiante getEstudiante2() {
        return estudiante2;
    }

    public void setEstudiante2(Estudiante estudiante2) {
        this.estudiante2 = estudiante2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Match)) {
            return false;
        }
        Match other = (Match) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Match{" + "id=" + id + ", fechaMatch=" + fechaMatch + '}';
    }
}
