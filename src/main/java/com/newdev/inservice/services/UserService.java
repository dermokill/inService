package com.newdev.inservice.services;


import com.newdev.inservice.config.JwtProvider;
import com.newdev.inservice.exceptions.*;
import com.newdev.inservice.requestDtos.ImagesDto;
import com.newdev.inservice.requestDtos.RegisterClientDto;
import com.newdev.inservice.requestDtos.RegisterTaskerDto;
import com.newdev.inservice.serviceInterfaces.IAuthService;
import com.newdev.inservice.serviceInterfaces.IUserService;
import com.newdev.inservice.models.*;
import com.newdev.inservice.models.enums.Gender;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;
import com.newdev.inservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements IUserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images";

    private final IAuthService authService;


    @Autowired
    public UserService(UserRepository userRepository
            ,PasswordEncoder passwordEncoder
            ,IAuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Override
    public User getProfile(UserDetails userDetails) {

        return Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }

    @Override
    public Page<User> getClientsAndAdmins(UserDetails userDetails, String role, int page, int size) {

        User admin = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin profile not found"));

        if(!admin.getRole().equals(RoleEnum.ADMIN))
            throw new UnauthorizedException("only an Admin can see clients and admins");

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        return userRepository.findByRole(RoleEnum.valueOf(role.toUpperCase()), pageable);
    }

    @Override
    public String insertClient(RegisterClientDto dto) {

        if(userRepository.existsByEmail(dto.getEmail()))
            throw new ConflictException("Email already exists");

        if(userRepository.existsByPhone(dto.getPhone()))
            throw new ConflictException("PhoneNumber already exists");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(dto.getBirthdate(), formatter);
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            throw new BadRequestException("Invalid birth date format. Please use dd-MM-yyyy.");
        }

        String picture;
        if(dto.getGender().equals("MALE")) {
            picture = uploadDir + "/user_male.jpg";
        }else {
            picture = uploadDir + "/user_female.jpg";
        }

        RoleEnum role = RoleEnum.valueOf(dto.getRole());

        if(!role.equals(RoleEnum.CLIENT))
            throw new UnauthorizedException("only a client can use this API");

        Client client = new Client();
        //common user fields
        client.setFName(dto.getFirstName());
        client.setLName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRole(RoleEnum.CLIENT);
        client.setGender(Gender.valueOf(dto.getGender()));
        client.setBirthdate(birthDate);
        client.setCin(dto.getCin());
        client.setPhone(dto.getPhone());
        client.setProfileImage(picture);
        //client specific fields
        client.setClientCity(dto.getClientCity());
        client.setClientArea(dto.getClientArea());
        client.setPersonalAddress(dto.getPersonalAddress());

        userRepository.save(client);

        Authentication authentication = authService.authenticate(dto.getEmail(),dto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return JwtProvider.generateToken(authentication, dto.getRole().toUpperCase());
    }

    @Override
    public String insertTasker(RegisterTaskerDto dto) throws Exception {

        if(userRepository.existsByEmail(dto.getEmail()))
            throw new ConflictException("Email already exists");

        if(userRepository.existsByPhone(dto.getPhone()))
            throw new ConflictException("PhoneNumber already exists");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(dto.getBirthdate(), formatter);
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            throw new BadRequestException("Invalid birth date format. Please use dd-MM-yyyy.");
        }

        String picture;
        if(dto.getGender().equals("MALE")) {
            picture = uploadDir + "/user_male.jpg";
        }else {
            picture = uploadDir + "/user_female.jpg";
        }

        RoleEnum role = RoleEnum.valueOf(dto.getRole());

        if(!role.equals(RoleEnum.TASKER))
            throw new UnauthorizedException("only a tasker can use this API");

//        List<String> pictures = new ArrayList<>();
//        if(dto.getPictures() != null) {
//            for (MultipartFile file : dto.getPictures()) {
//                String fileName = insertImageByUserName(dto.getFirstName(), dto.getLastName(), file);
//                pictures.add(fileName);
//            }
//        }

        Tasker tasker = new Tasker();
        //common user fields
        tasker.setFName(dto.getFirstName());
        tasker.setLName(dto.getLastName());
        tasker.setEmail(dto.getEmail());
        tasker.setPassword(passwordEncoder.encode(dto.getPassword()));
        tasker.setRole(RoleEnum.TASKER);
        tasker.setGender(Gender.valueOf(dto.getGender()));
        tasker.setBirthdate(birthDate);
        tasker.setCin(dto.getCin());
        tasker.setPhone(dto.getPhone());
        tasker.setProfileImage(picture);
        // tasker specific fields
        tasker.setTaskerType(TaskerType.valueOf(dto.getTaskerType()));
        tasker.setSkill(SkillType.valueOf(dto.getSkill()));
        tasker.setTaskerCity(dto.getTaskerCity());
        tasker.setTaskerArea(dto.getTaskerArea());
        tasker.setExperience(dto.getExperience());
        tasker.setJobNumber(dto.getJobNumber());
        tasker.setRating(0);
        //tasker.setReviews(reviews);

        tasker.setVerified(false);

        if (dto.getTaskerType().equals(TaskerType.SHOP_OWNER.toString())) {
            ShopOwner shopOwner = new ShopOwner();
            shopOwner.setShopAddress(dto.getShopAddress());
            shopOwner.setShopLicenceNumber(dto.getShopLicenceNumber());
            tasker.setShopOwner(shopOwner);
        } else if (dto.getTaskerType().equals(TaskerType.ENTREPRISE.toString())) {
            Enterprise enterprise = new Enterprise();
            enterprise.setEntrepriseName(dto.getEntrepriseName());
            enterprise.setEntrepriseLicenceNumber(dto.getEntrepriseLicenceNumber());
            enterprise.setEntrepriseAddress(dto.getEntrepriseAddress());
            enterprise.setEmployeeNumber(dto.getEmployeeNumber());
            tasker.setEnterprise(enterprise);
        }
        userRepository.save(tasker);

        Authentication authentication = authService.authenticate(dto.getEmail(),dto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return JwtProvider.generateToken(authentication, dto.getRole().toUpperCase());
    }

    @Override
    public void insertTaskerImages(UserDetails userDetails, ImagesDto imagesDto) throws Exception {

        Tasker tasker = (Tasker) Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Tasker profile not found"));

        if(!tasker.getRole().equals(RoleEnum.TASKER))
            throw new UnauthorizedException("only a tasker can use this API");

        String mainImagePath = insertImageByUserName(tasker.getFName(),tasker.getLName(),imagesDto.getMainPicture());
        List<String> pictures = new ArrayList<>();

        for (MultipartFile image : imagesDto.getPictures()){

            String imagePath = insertImageByUserName(tasker.getFName(),tasker.getLName(),image);
            pictures.add(imagePath);
        }
        tasker.setMainPicture(mainImagePath);
        tasker.setPictures(pictures);
        tasker.setUpdatedAt(LocalDateTime.now());

        userRepository.save(tasker);
    }

    @Override
    public void validateTasker(UserDetails userDetails, String id) {

        User admin = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin profile not found"));

        if(!admin.getRole().equals(RoleEnum.ADMIN))
            throw new UnauthorizedException("only an Admin can validate accounts");

        Tasker tasker = (Tasker) userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tasker profile not found"));

        tasker.setVerified(true);
        tasker.setUpdatedAt(LocalDateTime.now());
        userRepository.save(tasker);
    }


    public String insertImageByUserName(String fName, String lName, MultipartFile picture) throws Exception {

        if (picture == null || picture.isEmpty()) {
            throw new IllegalArgumentException("Main picture is missing or empty");
        }

        // Sanitize names and create user directory
        String safeName = (fName + "_" + lName).trim().replaceAll("[^a-zA-Z0-9_\\-]", "_");
        Path userDir = Paths.get(uploadDir, safeName);

        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        // Extract and sanitize the file name
        String originalFilename = picture.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex != -1) {
            extension = originalFilename.substring(dotIndex);
        }

        String baseName = originalFilename.substring(0, dotIndex).replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String uniqueFileName = baseName + "_" + Instant.now().toEpochMilli() + extension;

        // Save file
        Path destination = userDir.resolve(uniqueFileName);
        picture.transferTo(destination.toFile());

        // Return the relative path (e.g., /uploads/John_Doe/pic_12345678.png)
        return Paths.get(safeName, uniqueFileName).toString().replace("\\", "/");
    }

}
