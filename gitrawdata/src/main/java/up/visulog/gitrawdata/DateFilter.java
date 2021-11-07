package up.visulog.gitrawdata;

import java.time.LocalDateTime;

public class DateFilter implements Filter {
    private LocalDateTime dateEnd;
    private LocalDateTime dateStart;

    public DateFilter(String start, String end){
        dateStart = LocalDateTime.parse(start);
        dateEnd = LocalDateTime.parse(end);
    }

    public boolean filter(Commit commit){
        if (commit.date.compareTo(dateStart)>=0 && commit.date.compareTo(dateEnd)<=0) return true;
        return false;
    }
}
