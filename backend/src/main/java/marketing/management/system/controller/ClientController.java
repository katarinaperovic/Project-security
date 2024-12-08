package marketing.management.system.controller;

import marketing.management.system.dto.UserDTO;
import marketing.management.system.mapper.ClientMapper;
import marketing.management.system.model.Client;
import marketing.management.system.service.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    ClientMapper clientMapper;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('CLIENT')")
    public ResponseEntity<UserDTO> getByID(@PathVariable("id") Long id) throws Exception {

        Client client = clientService.getByID(id);

        return ResponseEntity.ok(clientMapper.toDTO(client));
    }


}
