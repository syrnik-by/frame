package ru.autotestframework.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Test object.
 */
@Entity
@Table(name = "TEST")
public class TestObject {

    /**
     * The Id.
     */
    @Id
    public Long id;

    /**
     * The Name.
     */
    @Column(name = "NAME")
    public String name;

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
