package marketing.management.system.service.implementations;

import marketing.management.system.model.User;
import marketing.management.system.repository.UserRepository;
import marketing.management.system.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public boolean checkIfEmailExists(String email) {
        if(userRepository.findByEmail(email) == null)
            return false;
        return true;
    }

    @Override
    public String getSalt(String email) {
        User user = userRepository.findByEmail(email);
        return user.getSalt();
    }


}
