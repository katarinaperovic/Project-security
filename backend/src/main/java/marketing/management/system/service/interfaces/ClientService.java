package marketing.management.system.service.interfaces;

import marketing.management.system.exception.PermissionException;
import marketing.management.system.model.Client;
import marketing.management.system.dto.UserDTO;

public interface ClientService {
    Client registerClient(UserDTO userDTO);

    Client getByID(Long id)throws PermissionException;



}
