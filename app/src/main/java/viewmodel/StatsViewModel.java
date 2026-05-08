package viewmodel;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import model.WeeklyStats;
import repository.ProgressRepository;

public class StatsViewModel extends ViewModel {

    private final ProgressRepository progressRepository = new ProgressRepository();
    private LiveData<WeeklyStats> weeklyStats;

    public LiveData<WeeklyStats> getWeeklyStats() {
        if (weeklyStats == null) {
            weeklyStats = progressRepository.getWeeklyStats();
        }
        return weeklyStats;
    }

    public void refresh() {
        weeklyStats = progressRepository.getWeeklyStats();
    }
}
