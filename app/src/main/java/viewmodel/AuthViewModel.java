package viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();

    public AuthViewModel() {
        currentUser.setValue(userRepository.getCurrentFirebaseUser());
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return userRepository.getCurrentFirebaseUser() != null;
    }

    public void saveUserProfile(String nivel, String objetivo, boolean dolor, boolean yaClasificado) {
        userRepository.saveUserProfile(nivel, objetivo, dolor, yaClasificado);
    }
}