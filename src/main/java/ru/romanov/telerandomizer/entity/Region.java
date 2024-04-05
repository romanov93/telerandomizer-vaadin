package ru.romanov.telerandomizer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "region")
@Setter
@Getter
@NoArgsConstructor
@NamedEntityGraph(
        name = "graph.Region.cities.cityCodes",
        attributeNodes = @NamedAttributeNode(value = "cities", subgraph = "City.cityCodes"),
        subgraphs = @NamedSubgraph(name = "City.cityCodes", attributeNodes = @NamedAttributeNode("codes"))
)
public class Region {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private int code;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "region")
    private Set<City> cities;

}
