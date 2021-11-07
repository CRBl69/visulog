package up.visulog.gitrawdata;

public interface Filter {
    public boolean filter(Commit commit);
}
