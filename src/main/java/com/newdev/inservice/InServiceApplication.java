package com.newdev.inservice;


import com.newdev.inservice.models.Client;
import com.newdev.inservice.models.User;
import com.newdev.inservice.models.enums.Gender;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class InServiceApplication implements CommandLineRunner {


    private final UserRepository userRepository;

    @Autowired
    public InServiceApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(InServiceApplication.class, args);
        System.out.println("inService");
    }

    @Override
    public void run(String... args) throws Exception {

//        User user = User.builder()
//
//                .fName("saad")
//                .lName("boukili")
//                .email("saad@gmail.com")
//                .password("12354545400")
//                .role(RoleEnum.CLIENT)
//                .gender(Gender.MALE)
//                .build();

       // User client = new User();
        Client  client = new Client();
        client.setFName("hamid");
        client.setGender(Gender.MALE);
        client.setLName("ham");
        client.setEmail("hamid@gmail.com");
        client.setArea("Fes");
        client.setPersonalAddress("Saada");
        client.setRole(RoleEnum.CLIENT);

       // userRepository.save(client);
       // clientRepository.save(client);

       List<User> users = userRepository.findAll();

       for (var user : users) {
           System.out.println(user);
       }


    }
}
