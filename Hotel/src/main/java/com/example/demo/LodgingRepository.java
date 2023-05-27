package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

//This will be AUTO IMPLEMENTED by Spring into a Bean called departamentoRepository
//CRUD refers Create, Read, Update, Delete

public interface LodgingRepository extends CrudRepository<Lodging, Long>{
	
}
