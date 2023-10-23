package com.example.demo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.Room;
import com.example.demo.RoomRepository;

@Service
public class RoomService {
	
	private final RoomRepository roomRepository;
	
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<String> listType() {
        // Recupere os dados do repositório e retorne apenas o atributo desejado
        List<Room> objetos = (List<Room>) roomRepository.findAll();
        return objetos.stream()
                      .map(objeto -> objeto.getType())
                      .collect(Collectors.toList());
    }
    
    public List<String> listDisponibility() {
        // Recupere os dados do repositório e retorne apenas o atributo desejado
        List<Room> objetos = (List<Room>) roomRepository.findAll();
        return objetos.stream()
                      .map(objeto -> objeto.getDisponibility())
                      .collect(Collectors.toList());
    }
    
    public List<String> listPrice() {
        // Recupere os dados do repositório e retorne apenas o atributo desejado
        List<Room> objetos = (List<Room>) roomRepository.findAll();
        return objetos.stream()
                      .map(objeto -> objeto.getPrice())
                      .collect(Collectors.toList());
    }

}
