package com.newdev.inservice.Mapping;


import com.newdev.inservice.models.*;
import com.newdev.inservice.responseDtos.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {

    public AdminDto mapToAdminDto(Admin admin) {
        return new AdminDto(
                admin.getId(),
                admin.getFName(),
                admin.getLName(),
                admin.getGender(),
                admin.getBirthdate(),
                admin.getCin(),
                admin.getPhone(),
                admin.getEmail(),
                admin.getRole(),
                admin.getProfileImage(),
                admin.getCreatedAt(),
                admin.getUpdatedAt());
    }

    public ClientDto mapToClientDto(Client client) {
        List<DemandResponseDto> demandDtos = client.getDemands().stream()
                .map(this::mapToDemandDto)
                .toList();

        List<JobResponseDto> jobDtos = client.getJobs().stream()
                .map(this::mapToJobDto)
                .toList();

        return new ClientDto(
                client.getId(),
                client.getFName(),
                client.getLName(),
                client.getGender(),
                client.getBirthdate(),
                client.getCin(),
                client.getPhone(),
                client.getEmail(),
                client.getRole(),
                client.getProfileImage(),
                client.getCreatedAt(),
                client.getUpdatedAt(),
                client.getClientCity(),
                client.getClientArea(),
                client.getPersonalAddress(),
                demandDtos,
                jobDtos
        );
    }

    public TaskerShopOwnerDto mapToTaskerShopOwnerDto(Tasker tasker) {
        List<DemandResponseDto> demands = tasker.getDemands().stream()
                .map(this::mapToDemandDto)
                .toList();

        List<JobResponseDto> jobs = tasker.getJobs().stream()
                .map(this::mapToJobDto)
                .toList();

        return new TaskerShopOwnerDto(
                tasker.getId(),
                tasker.getFName(),
                tasker.getLName(),
                tasker.getGender(),
                tasker.getBirthdate(),
                tasker.getCin(),
                tasker.getPhone(),
                tasker.getEmail(),
                tasker.getRole(),
                tasker.getProfileImage(),
                tasker.getCreatedAt(),
                tasker.getUpdatedAt(),
                tasker.getTaskerType(),
                tasker.getSkill(),
                tasker.getTaskerCity(),
                tasker.getTaskerArea(),
                tasker.getExperience(),
                tasker.getJobNumber(),
                tasker.getMainRating(),
                tasker.getRatings(),
                tasker.getReviews(),
                tasker.getMainPicture(),
                tasker.getPictures(),
                tasker.isVerified(),
                tasker.getShopOwner().getShopAddress(),
                tasker.getShopOwner().getShopLicenceNumber(),
                demands,
                jobs
        );
    }

    public TaskerEntrepriseDto mapToTaskerEntrepriseDto(Tasker tasker) {
        List<DemandResponseDto> demands = tasker.getDemands().stream()
                .map(this::mapToDemandDto)
                .toList();

        List<JobResponseDto> jobs = tasker.getJobs().stream()
                .map(this::mapToJobDto)
                .toList();

        return new TaskerEntrepriseDto(
                tasker.getId(),
                tasker.getFName(),
                tasker.getLName(),
                tasker.getGender(),
                tasker.getBirthdate(),
                tasker.getCin(),
                tasker.getPhone(),
                tasker.getEmail(),
                tasker.getRole(),
                tasker.getProfileImage(),
                tasker.getCreatedAt(),
                tasker.getUpdatedAt(),
                tasker.getTaskerType(),
                tasker.getSkill(),
                tasker.getTaskerCity(),
                tasker.getTaskerArea(),
                tasker.getExperience(),
                tasker.getJobNumber(),
                tasker.getMainRating(),
                tasker.getRatings(),
                tasker.getReviews(),
                tasker.getMainPicture(),
                tasker.getPictures(),
                tasker.isVerified(),
                tasker.getEnterprise().getEntrepriseAddress(),
                tasker.getEnterprise().getEntrepriseName(),
                tasker.getEnterprise().getEntrepriseLicenceNumber(),
                tasker.getEnterprise().getEmployeeNumber(),
                demands,
                jobs
        );
    }



    public DemandResponseDto mapToDemandDto(Demand demand) {
        List<MessageResponseDto> messages = demand.getMessages().stream()
                .map(this::mapToMessageDto)
                .toList();

        return new DemandResponseDto(
                demand.getId(),
                new SubClientDto(demand.getClient().getId(),
                        demand.getClient().getFName() + " " + demand.getClient().getLName(),
                        demand.getClient().getRole()),
                new SubTaskerDto(demand.getTasker().getId(),
                        demand.getTasker().getFName() + " " + demand.getTasker().getLName(),
                        demand.getTasker().getRole(),
                        demand.getTasker().getTaskerType(),
                        demand.getTasker().getSkill()),
                demand.getCreatedAt(),
                demand.getUpdatedAt(),
                demand.getStatus(),
                demand.getTaskType(),
                demand.getDescription(),
                demand.getLocation(),
                demand.getRequestDate(),
                messages
        );
    }

    public JobResponseDto mapToJobDto(Job job) {
        Demand originalDemand = job.getOriginalDemand();

        return new JobResponseDto(
                job.getId(),
                mapToDemandDto(originalDemand),
                new SubClientDto(
                        originalDemand.getClient().getId(),
                        originalDemand.getClient().getFName() + " " + originalDemand.getClient().getLName(),
                        originalDemand.getClient().getRole()),
                new SubTaskerDto(
                        originalDemand.getTasker().getId(),
                        originalDemand.getTasker().getFName() + " " + originalDemand.getTasker().getLName(),
                        originalDemand.getTasker().getRole(),
                        originalDemand.getTasker().getTaskerType(),
                        originalDemand.getTasker().getSkill()),
                job.getStartedAt(),
                job.getFinishedAt(),
                job.getUpdatedAt(),
                job.getStatus(),
                job.getClientRating(),
                job.getClientFeedback()
        );
    }

    public MessageResponseDto mapToMessageDto(Message message) {
        return new MessageResponseDto(
                message.getId(),
                new SenderDto(
                        message.getSender().getId(),
                        message.getSender().getFName() + " " + message.getSender().getLName(),
                        message.getSender().getRole()),
                message.getContent(),
                message.getSentAt()
        );
    }


}
