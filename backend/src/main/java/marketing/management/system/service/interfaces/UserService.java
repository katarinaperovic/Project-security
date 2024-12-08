package marketing.management.system.service.interfaces;

public interface UserService {

    boolean checkIfEmailExists(String email);

    String getSalt(String email);
}
