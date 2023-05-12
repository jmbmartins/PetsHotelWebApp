package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // This means that this class is a Controller
public class MainController {
	
	@Autowired
	private ClientRepository clientRepository;
	
	
	@GetMapping(path="/")
	public String showMainPage(Model model) {
		return "mainpage";
	}

	// 1. ---------  Entidades  -----------------------
	
	
	// 1.1 ----- Client -------
	
	@GetMapping("/showClient")
	public String showClient(Model model) {
		model.addAttribute("ListClients" , clientRepository.findAll());
		return "client";
	}
	
	@GetMapping("/showNewClientForm")
	public String showNewClientForm(Model model) {
	 // create model attribute to bind form data
	 Client client = new Client();
	 model.addAttribute("newclient", client);
	 return "new_client";
	}
	
	@PostMapping("/saveClient")
	public String saveClient (@ModelAttribute("newclient") Client client ) {
		// save dep to database
		clientRepository.save(client);
		return "redirect:/showClient";
	}
	
	@GetMapping("/deleteClient/{id}")
	public String deleteClient(@PathVariable(value = "id") Long id) {
	 clientRepository.deleteById(id);
		return "redirect:/showClient";
	}
	
	@GetMapping("/showUpdateClientForm/{id}")
	public String showUpdateDepForm(@PathVariable(value = "id") Long id, Model model) {
		Optional <Client> optional = clientRepository.findById(id);
		Client client = null;
		if (optional.isPresent()) {
			client = optional.get();
		} else {
			throw new RuntimeException(" Client not found for id :: " + id);
		}
		// set department as a model attribute to pre-populate the form
		model.addAttribute("client", client);
		return "update_client";
	}
	
	
}