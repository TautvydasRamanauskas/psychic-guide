package psychic.guide.api.services.internal.searchengine.limiters;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class DailyLimiter implements Limiter {
	private final Timer timer;
	private final int dailySearchLimit;
	private int dailySearchCount;

	public DailyLimiter(int dailySearchLimit) {
		this.dailySearchLimit = dailySearchLimit;
		this.dailySearchCount = dailySearchLimit;
		this.timer = createAndStartTimer();
	}

	@Override
	public int getScore() {
		return dailySearchCount;
	}

	@Override
	public void useLimit() {
		dailySearchCount--;
	}

	public void stopTimer() {
		timer.cancel();
	}

	private Timer createAndStartTimer() {
		Timer timer = new Timer();
		TimerTask timerTask = new ResetDailyCount();
		long delay = ChronoUnit.DAYS.getDuration().toMillis() - LocalTime.MIDNIGHT.getLong(ChronoField.MILLI_OF_DAY);
		timer.scheduleAtFixedRate(timerTask, delay, ChronoUnit.DAYS.getDuration().toMillis());
		return timer;
	}

	private class ResetDailyCount extends TimerTask {
		@Override
		public void run() {
			dailySearchCount = dailySearchLimit;
		}
	}
}
