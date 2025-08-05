package com.newdev.inservice;


import com.newdev.inservice.models.Admin;
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

import java.time.LocalDate;
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

        Admin client = (Admin) userRepository.getById("071ff29b-2b0e-44f3-a0ae-035d554a548d");
       // Tasker client = new Tasker();
        client.setFName("admin");
        client.setGender(Gender.MALE);
        client.setLName("admin");
        client.setEmail("admin@gmail.com");
        client.setPassword(passwordEncoder.encode("123456789"));
        client.setCin("CD151515");
        client.setRole(RoleEnum.ADMIN);
        client.setPhone("123456789");
        client.setBirthdate(LocalDate.of(2000, 8, 5));
//        client.setTaskerArea("Fes");
//        client.setTaskerType(TaskerType.SHOP_OWNER);
//        client.setExperience("5years");
//        client.setSkill(SkillType.ELECTRICIEN);
//        client.setJobNumber(500);

       // userRepository.save(client);

       List<User> users = userRepository.findAll();

       for (var user : users) {
           System.out.println(user);
       }


    }
}
