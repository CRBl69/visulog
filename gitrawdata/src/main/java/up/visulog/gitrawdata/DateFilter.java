package up.visulog.gitrawdata;

import java.time.LocalDateTime;

public class DateFilter extends Filter {
    private LocalDateTime dateEnd;
    private LocalDateTime dateStart;

    public DateFilter(String start, String end){
        dateStart = LocalDateTime.parse(start + "T00:00:00");
        dateEnd = LocalDateTime.parse(end + "T23:59:59");
    }

    public DateFilter(String start){
        dateStart = LocalDateTime.parse(start + "T00:00:00");
        dateEnd = dateStart.plusDays(1);
    }

    public boolean filter(Commit commit){
        if (commit.date.compareTo(dateStart)>=0 && commit.date.compareTo(dateEnd)<=0) return true;
        return false;
    }
}
