package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private LodgingRepository lodgingRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private FeedingRepository feedingRepository;
  
  

    

    @GetMapping(path = "/")
    public String showLoginForm(Model model) {
        return "login";
    }
    
    @GetMapping(path = "/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/registersave")
    public String registerUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null && error.equals("true")) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
        	if(user.getRole().equals(UserRole.HOTEL_MANAGER)) {
        		session.setAttribute("admin", user);
        		return "redirect:/mainpage";
        	} else {
        		session.setAttribute("client", user);
                return "redirect:/mainpageclient";
        	}
        } else {
            return "redirect:/login?error=true";
        }
    }
    
    @GetMapping(path="/mainpage")
	public String showMainAdminPage(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("admin");
        if (user != null) {
            model.addAttribute("user", user);
            return "mainpage";
        } else {
            return "redirect:/login";
        }
	}

    @GetMapping("/mainpageclient")
    public String showDashboard(HttpSession session, Model model) {
    	int encontrado = 0;
        User user = (User) session.getAttribute("client");
        List<Client> clientList = new ArrayList<>();
        clientRepository.findAll().forEach(clientList::add);
        int clientCount = clientList.size();
        if (user != null) {
            model.addAttribute("user", user);
            for(int i = 0; i < clientCount; i++) {
            	if(clientList.get(i).getUser() != null) {
            		if(clientList.get(i).getUser().getId() == user.getId()) {
                		encontrado = 1;
                    	model.addAttribute("client", clientList.get(i));
                    	model.addAttribute("pets", clientList.get(i).getPets());
                    }
            	}
            }
            if(encontrado == 0) {
            	// create model attribute to bind form data
           	 	Client client = new Client();
           	 	model.addAttribute("new_client", client);
            }
            return "mainpageclient";
        } else {
            return "redirect:/login";
        }
    }
    
    @PostMapping("/vcsaveClient")
    public String saveClientVC(@ModelAttribute("client") Client client) {
        if (client.getId() == null) {
            // Create a new client
            clientRepository.save(client);
        } else {
            // Update an existing client
            Client existingClient = clientRepository.findById(client.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));

            // Update the existing client with the new values
            existingClient.setName(client.getName());
            existingClient.setAddress(client.getAddress());
            existingClient.setPhone(client.getPhone());

            clientRepository.save(existingClient);
        }

        return "redirect:/mainpageclient";
    }

    
    
    @GetMapping("/vcupdateclient/{clientId}")
    public String showUpdateClientPageVC(@PathVariable("clientId") Long clientId, Model model) {
        // Retrieve the client from the database based on the provided clientId
        Client client = clientRepository.findById(clientId).orElse(null);
        
        if (client != null) {
            model.addAttribute("client", client);
            return "vcupdateclient";
        } else {
            // Client not found, handle the error accordingly
            // You can redirect to an error page or display an error message
            return "error";
        }
    }
    
    
    @GetMapping("/pets/delete/{id}")
    public String deletePetVC(@PathVariable(value = "id") Long id) {
        petRepository.deleteById(id);
        return "redirect:/mainpageclient";
    }
    
    @GetMapping("/vcupdatepet/{petId}")
    public String showUpdatePetPageVC(@PathVariable("petId") Long petId, Model model) {
        // Retrieve the client from the database based on the provided clientId
        Pet pet = petRepository.findById(petId).orElse(null);
        
        if (pet != null) {
            model.addAttribute("update_pet", pet);
            return "vcupdatepet";
        } else {
            // Client not found, handle the error accordingly
            // You can redirect to an error page or display an error message
            return "error";
        }
    }

    
    @GetMapping("/showVCNewPetForm")
    public String showNewPetForm(Model model) {
        model.addAttribute("new_pet", new Pet());
        return "vcnewpetclient";
    }
    
    @PostMapping("/vcsavePet")
    public String savePetVC(@ModelAttribute("new_pet") Pet pet, HttpSession session) {
        User user = (User) session.getAttribute("client");
        List<Client> clientList = new ArrayList<>();
        Client client = null;
        clientRepository.findAll().forEach(clientList::add);
        int clientCount = clientList.size();
        for(int i = 0; i < clientCount; i++) {
            if(clientList.get(i).getUser() != null) {
                if(clientList.get(i).getUser().getId() == user.getId()) {
                    client = clientList.get(i);
                }
            }
        }

        pet.setClient(client); // Assign the client to the pet object
        petRepository.save(pet);
        return "redirect:/mainpageclient";
    }
    
    @PostMapping("/vcsaveupdatePet")
    public String savePetUpdateVC(@ModelAttribute("new_pet") Pet updatedPet, HttpSession session) {
        User user = (User) session.getAttribute("client");
        List<Client> clientList = new ArrayList<>();
        Client client = null;
        clientRepository.findAll().forEach(clientList::add);
        int clientCount = clientList.size();
        for (int i = 0; i < clientCount; i++) {
            if (clientList.get(i).getUser() != null) {
                if (clientList.get(i).getUser().getId() == user.getId()) {
                    client = clientList.get(i);
                }
            }
        }

        Pet existingPet = petRepository.findById(updatedPet.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID"));

        // Update the existing pet with the new values
        existingPet.setAge(updatedPet.getAge());
        existingPet.setName(updatedPet.getName());
        existingPet.setRace(updatedPet.getRace());
        existingPet.setSpecie(updatedPet.getSpecie());
        existingPet.setClient(client);

        petRepository.save(existingPet);

        return "redirect:/mainpageclient";
    }
    
    @GetMapping(path = "/vcfeeding")
	public String showFeeding(Model model, HttpSession session) {
    	User user = (User) session.getAttribute("client");
        List<Client> clientList = new ArrayList<>();
        Client client = null;
        clientRepository.findAll().forEach(clientList::add);
        int clientCount = clientList.size();
        for (int i = 0; i < clientCount; i++) {
            if (clientList.get(i).getUser() != null) {
                if (clientList.get(i).getUser().getId() == user.getId()) {
                    client = clientList.get(i);
                }
            }
        }
        
        // Search for every feeding
        Iterable<Feeding> iterableFeedings = feedingRepository.findAll();
        List<Feeding> listFeedings = new ArrayList<>();
        iterableFeedings.forEach(listFeedings::add);
        
        List<Feeding> listFeedingsClient = new ArrayList<>();
        for(int i = 0; i < listFeedings.size(); i++) {
        	if(client.getPets().contains(listFeedings.get(i).getPet())) {
        		listFeedingsClient.add(listFeedings.get(i));
        	}
        }
		model.addAttribute("ListFeeding", listFeedingsClient);
		return "vcfeeding";
	}
    
    @GetMapping("/showVCNewFeedingForm")
    public String showNewFeedingForm(Model model, HttpSession session) {
    	User user = (User) session.getAttribute("client");
        List<Client> clientList = new ArrayList<>();
        Client client = null;
        clientRepository.findAll().forEach(clientList::add);
        int clientCount = clientList.size();
        for (int i = 0; i < clientCount; i++) {
            if (clientList.get(i).getUser() != null) {
                if (clientList.get(i).getUser().getId() == user.getId()) {
                    client = clientList.get(i);
                }
            }
        }
        
        // Search for every feeding
        Iterable<Feeding> iterableFeedings = feedingRepository.findAll();
        List<Feeding> listFeedings = new ArrayList<>();
        iterableFeedings.forEach(listFeedings::add);
        
        // Search for every pet
        List<Pet> clientpet;
        clientpet =  client.getPets();
        
        for(int i = 0; i < listFeedings.size(); i++) {
        	clientpet.remove(listFeedings.get(i).getPet());
        }
            
        
        
        
    	 model.addAttribute("ListPets" , clientpet);
		 Feeding fed = new Feeding();
		 model.addAttribute("new_feeding", fed);
		 return "vcnewfeeding";
    }
    
    @PostMapping("/vcsaveFeeding")
	public String saveFeeding(@ModelAttribute("newfeeding") Feeding feed) {
		// save feed to database
		feedingRepository.save(feed);
		return "redirect:/vcfeeding";
	}
    
    @GetMapping("/fed/delete/{id}")
	public String deleteFeeding(@PathVariable(value = "id") Long id) {
		feedingRepository.deleteById(id);
		return "redirect:/vcfeeding";
	}
    
    

    @GetMapping("/vcbooking")
    public String showBookingPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("client");


        if (user == null) {
            // Handle the case where the user is not logged in
            // You can redirect the user to a login page or display an error message
            return "redirect:/login"; // Replace with your desired redirection URL
        }

        List<Client> clientList = new ArrayList<>();
        Client client = null;
        clientRepository.findAll().forEach(clientList::add);
        int clientCount = clientList.size();
        for (int i = 0; i < clientCount; i++) {
            if (clientList.get(i).getUser() != null) {
                if (clientList.get(i).getUser().getId() == user.getId()) {
                    client = clientList.get(i);
                }
            }
        }

        // Search for every booking
        Iterable<Lodging> iterableLodgings = lodgingRepository.findAll();
        List<Lodging> listLodgings = new ArrayList<>();
        iterableLodgings.forEach(listLodgings::add);
        
        
        LocalDate currentDate = LocalDate.now();
        for(int i = 0; i < listLodgings.size(); i++) {
        	if(listLodgings.get(i).getCheckInDate().isBefore(currentDate)) {
        		listLodgings.remove(listLodgings.get(i));
        	}
        	
        }

       
        // Search for every booking correspondent to user in session
        List<Lodging> listLodgingsClient = new ArrayList<>();
        for (int i = 0; i < listLodgings.size(); i++) {
            if (listLodgings.get(i).getPet().getClient().getUser() != null &&
                    listLodgings.get(i).getPet().getClient().getUser().getId() == user.getId()) {
                listLodgingsClient.add(listLodgings.get(i));
            }
        }
   
        model.addAttribute("listLodgingsClient", listLodgingsClient);
   


        return "vcbooking";
    }
    
    
    @GetMapping("/showVCNewBookingForm")
    public String showNewBookingForm(Model model, HttpSession session) {
    	User user = (User) session.getAttribute("client");
        List<Client> clientList = new ArrayList<>();
        Client client = null;
        clientRepository.findAll().forEach(clientList::add);
        int clientCount = clientList.size();
        for (int i = 0; i < clientCount; i++) {
            if (clientList.get(i).getUser() != null) {
                if (clientList.get(i).getUser().getId() == user.getId()) {
                    client = clientList.get(i);
                }
            }
        }
        
        
    	 model.addAttribute("ListPetsU" , client.getPets());
		 Lodging lodg = new Lodging();
		 model.addAttribute("new_lodging", lodg);
		 return "vcnewbooking";
    }
    
    
    @PostMapping("/vcsaveLodging")
    public String vcsaveLodging(@ModelAttribute("newlodging") Lodging lodg,
                                @RequestParam("checkInDate") LocalDate checkInDate,
                                @RequestParam("checkOutDate") LocalDate checkOutDate,
                                @RequestParam("roomType") String roomType,
                                Model model, HttpSession session) {
    	
    	// Check if the same pet is already booked for overlapping date intervals
        boolean isPetAlreadyBooked = lodgingRepository.existsByPetAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(lodg.getPet(), checkInDate, checkOutDate);
        if (isPetAlreadyBooked) {
            String errorMessage = "This pet is already booked for the selected date interval.";
            User user = (User) session.getAttribute("client");
            List<Client> clientList = new ArrayList<>();
            Client client = null;
            clientRepository.findAll().forEach(clientList::add);
            int clientCount = clientList.size();
            for (int i = 0; i < clientCount; i++) {
                if (clientList.get(i).getUser() != null) {
                    if (clientList.get(i).getUser().getId() == user.getId()) {
                        client = clientList.get(i);
                    }
                }
            }
            
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("ListPetsU" , client.getPets());
   		 	Lodging lodg1 = new Lodging();
   		 	model.addAttribute("new_lodging", lodg1);
            return "vcnewbooking";
        }
        
        // Find an available room based on the provided criteria
        Room room = findAvailableRoom(checkInDate, checkOutDate, roomType);
        
 

        if (room != null) {
        	if(lodg.getCheckInDate().isAfter(LocalDate.now()) && lodg.getCheckOutDate().isAfter(lodg.getCheckInDate())) {
        		// Set the room for the lodging
                lodg.setRoom(room);

                // Save the lodging to the repository
                lodgingRepository.save(lodg);
                return "redirect:/vcbooking";
        	} else {
        		String errorMessage = "Can't proceed Check-in or Check-out dates...";
        		User user = (User) session.getAttribute("client");
                List<Client> clientList = new ArrayList<>();
                Client client = null;
                clientRepository.findAll().forEach(clientList::add);
                int clientCount = clientList.size();
                for (int i = 0; i < clientCount; i++) {
                    if (clientList.get(i).getUser() != null) {
                        if (clientList.get(i).getUser().getId() == user.getId()) {
                            client = clientList.get(i);
                        }
                    }
                }
                
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("ListPetsU" , client.getPets());
       		 	Lodging lodg1 = new Lodging();
       		 	model.addAttribute("new_lodging", lodg1);
                return "vcnewbooking";
        	}
        } else {
        	User user = (User) session.getAttribute("client");
            List<Client> clientList = new ArrayList<>();
            Client client = null;
            clientRepository.findAll().forEach(clientList::add);
            int clientCount = clientList.size();
            for (int i = 0; i < clientCount; i++) {
                if (clientList.get(i).getUser() != null) {
                    if (clientList.get(i).getUser().getId() == user.getId()) {
                        client = clientList.get(i);
                    }
                }
            }
            
            model.addAttribute("ListPetsU" , client.getPets());
   		 	Lodging lodg1 = new Lodging();
   		 	model.addAttribute("new_lodging", lodg1);
        	String errorMessage = "No available room found. Please choose different dates or room type.";
            model.addAttribute("errorMessage", errorMessage);
            return "vcnewbooking";
             
        }

    }
    
    private Room findAvailableRoom(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        List<Room> rooms = roomRepository.findByType(roomType);

        for (Room room : rooms) {
            int bookingsCount = lodgingRepository.countBookingsForRoomWithinDateRange(room.getId(), checkInDate, checkOutDate);

            if (bookingsCount == 0) {
                return room;
            }
        }

        return null;
    }
    

    @GetMapping("/logs/delete/{id}")
    public String deleteBookingVC(@PathVariable(value = "id") Long id) {
        lodgingRepository.deleteById(id);
        return "redirect:/vcbooking";
    }
    
    
    
   


    



}

