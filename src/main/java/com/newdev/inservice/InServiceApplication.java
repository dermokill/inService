package com.newdev.inservice;

import com.newdev.inservice.models.Gender;
import com.newdev.inservice.models.RoleEnum;
import com.newdev.inservice.models.User;
import com.newdev.inservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

        User user = new User();
        user.setFName("hamid");

        userRepository.save(user);


    }
}
