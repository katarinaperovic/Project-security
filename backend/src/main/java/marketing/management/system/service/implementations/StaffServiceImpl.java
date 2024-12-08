package marketing.management.system.service.implementations;

import marketing.management.system.repository.StaffRepository;
import marketing.management.system.service.interfaces.StaffService;
import marketing.management.system.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    UserService userService;
    @Autowired
    StaffRepository staffRepository;
}
