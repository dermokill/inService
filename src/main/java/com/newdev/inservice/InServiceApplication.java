package com.newdev.inservice;


import com.newdev.inservice.models.Client;
import com.newdev.inservice.models.Tasker;
import com.newdev.inservice.models.User;
import com.newdev.inservice.models.enums.Gender;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;
import com.newdev.inservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class InServiceApplication implements CommandLineRunner {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InServiceApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        Tasker client = new Tasker();
        client.setFName("saad");
        client.setGender(Gender.MALE);
        client.setLName("ham");
        client.setEmail("saad@gmail.com");
        client.setPassword(passwordEncoder.encode("123456789"));
        client.setTaskerArea("Fes");
        client.setTaskerType(TaskerType.SHOP_OWNER);
        client.setExperience("5years");
        client.setSkill(SkillType.ELECTRICIEN);
        client.setJobNumber(500);
        client.setRole(RoleEnum.TASKER);

      //  userRepository.save(client);

       List<User> users = userRepository.findAll();

       for (var user : users) {
           System.out.println(user);
       }


    }
}
