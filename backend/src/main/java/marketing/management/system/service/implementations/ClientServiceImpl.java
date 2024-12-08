package marketing.management.system.service.implementations;

import marketing.management.system.exception.PermissionException;
import marketing.management.system.mapper.ClientMapper;
import marketing.management.system.model.Client;
import marketing.management.system.dto.UserDTO;
import marketing.management.system.model.Role;
import marketing.management.system.repository.ClientRepository;
import marketing.management.system.service.interfaces.PermissionService;
import marketing.management.system.service.interfaces.RoleService;
import marketing.management.system.service.interfaces.ClientService;
import marketing.management.system.service.interfaces.UserService;
import marketing.management.system.util.SaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientMapper clientMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PermissionService permissionService;

    @Override
    public Client registerClient(UserDTO userDTO) {

        if(userService.checkIfEmailExists(userDTO.getEmail()))
            return null;

        String salt = SaltGenerator.generateSalt(8);


        Client client = clientMapper.toModel(userDTO);
        client.setPassword(passwordEncoder.encode(userDTO.getPassword().concat(salt)));

        client.setSalt(salt);
        client.setEnabled(true);
        client.setApproved(true);

        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findByName("ROLE_CLIENT"));
        client.setRoles(roles);
        clientRepository.save(client);
        return client;
    }

    @Override
    public Client getByID(Long id) throws PermissionException {
        permissionService.checkPermission("clientProfile");
        return clientRepository.findById(id).get();
    }


}
