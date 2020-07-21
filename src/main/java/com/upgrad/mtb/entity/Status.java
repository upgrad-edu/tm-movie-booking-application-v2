package com.upgrad.mtb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Status {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "status_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;
    @Column(unique = true, nullable = false)
    private String status;

    @OneToMany(mappedBy = "status" , fetch = FetchType.EAGER)
    @MapKey
    @JsonManagedReference("status_movie")
    List<Movie> movies;

    public Status(){
    }

    public Status(int id, String status){
        this.id = id;
        this.status = status;
    }

    public Status(String status){
        this.status = status;
    }
}
