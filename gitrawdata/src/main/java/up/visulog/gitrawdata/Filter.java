package up.visulog.gitrawdata;

public abstract class Filter {
    public abstract boolean filter(Commit commit);

    public static Filter getFilter(String filterType, String filterValue) throws IllegalArgumentException {
        if (filterType.equals("author")) {
            return new NameFilter(filterValue);
        } else if (filterType.equals("date")) {
            String[] date = filterValue.split("~");
            if (date.length == 1) {
                return new DateFilter(date[0]);
            }
            return new DateFilter(date[0], date[1]);
        } else {
            throw new IllegalArgumentException("Invalid filter type: " + filterType);
        }
    }
}
