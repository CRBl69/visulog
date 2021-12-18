package up.visulog.gitrawdata;

import java.util.ArrayList;
import java.util.Map;

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

    public static ArrayList<Filter> getFilters(Map<String, String> filtersSet){
        ArrayList<Filter> filters = new ArrayList<Filter>();
        for (var options : filtersSet.entrySet()){
            try {
                filters.add(Filter.getFilter(options.getKey(), options.getValue()));
            } catch (IllegalArgumentException e) {
            }
        }
        return filters;
    }
}
