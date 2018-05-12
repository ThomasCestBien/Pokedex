package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pokedex;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pokedex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PokedexRepository extends JpaRepository<Pokedex, Long> {

}
