package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//This will be AUTO IMPLEMENTED by Spring into a Bean called departamentoRepository
//CRUD refers Create, Read, Update, Delete

public interface PetRepository extends CrudRepository<Pet, Long>{
	@Query("SELECT p.specie, COUNT(l.id) " +
	           "FROM Lodging l " +
	           "INNER JOIN l.pet p " +
	           "GROUP BY p.specie")
	List<Object[]> findLodgingCountBySpecie();
	
	
	@Query("SELECT p.specie, COUNT(l.id) " +
	           "FROM Lodging l " +
	           "INNER JOIN l.pet p " +
	           "WHERE l.checkInDate BETWEEN :startDate AND :endDate " +
	           "AND l.checkOutDate BETWEEN :startDate AND :endDate " +
	           "GROUP BY p.specie")
	    List<Object[]> findPetSpeciesCountByLodgingDate(
	            @Param("startDate") LocalDate startDate,
	            @Param("endDate") LocalDate endDate);
	
}