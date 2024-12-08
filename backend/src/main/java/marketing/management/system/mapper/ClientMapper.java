package marketing.management.system.mapper;

import marketing.management.system.dto.UserDTO;
import marketing.management.system.model.Address;
import marketing.management.system.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toModel(UserDTO userDTO) {
        Address address = new Address();
        address.setCity(userDTO.getAddress().getCity());
        address.setStreet(userDTO.getAddress().getStreet());
        address.setState(userDTO.getAddress().getState());

        Client client = new Client();
        client.setName(userDTO.getName());
        client.setSurname(userDTO.getSurname());
        client.setFirmName(userDTO.getFirmName());
        client.setPib(userDTO.getPib());
        client.setPhone(userDTO.getPhone());
        client.setEmail(userDTO.getEmail());
        client.setAddress(address);
        client.setPassword(userDTO.getPassword());
        client.setPackageType(userDTO.getPackageType());
        return client;
    }

    public UserDTO toDTO(Client client) {

        UserDTO dto = new UserDTO();
        dto.setName(client.getName());
        dto.setSurname(client.getSurname());
        dto.setFirmName(client.getFirmName());
        dto.setPib(client.getPib());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        dto.setId(client.getId());
        dto.setPackageType(client.getPackageType());
        return dto;
    }
}
