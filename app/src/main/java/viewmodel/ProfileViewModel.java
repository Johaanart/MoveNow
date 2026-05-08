package viewmodel;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import model.User;
import repository.UserRepository;

public class ProfileViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private LiveData<User> userProfile;

    public LiveData<User> getUserProfile() {
        if (userProfile == null) {
            userProfile = userRepository.getUserProfile();
        }
        return userProfile;
    }

    public void saveUserProfile(String nivel, String objetivo, boolean dolor, boolean yaClasificado) {
        userRepository.saveUserProfile(nivel, objetivo, dolor, yaClasificado);
    }
}