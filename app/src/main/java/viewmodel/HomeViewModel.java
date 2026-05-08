package viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import repository.ProgressRepository;
import repository.UserRepository;
import model.User;

public class HomeViewModel extends ViewModel {

    private final ProgressRepository progressRepository = new ProgressRepository();
    private final UserRepository userRepository = new UserRepository();

    private LiveData<Integer> todayProgress;
    private LiveData<User> userProfile;

    public LiveData<Integer> getTodayProgress() {
        if (todayProgress == null) {
            todayProgress = progressRepository.getTodayProgress();
        }
        return todayProgress;
    }

    public LiveData<User> getUserProfile() {
        if (userProfile == null) {
            userProfile = userRepository.getUserProfile();
        }
        return userProfile;
    }

    public void addProgress(int amount) {
        progressRepository.addProgress(amount);
    }
}
