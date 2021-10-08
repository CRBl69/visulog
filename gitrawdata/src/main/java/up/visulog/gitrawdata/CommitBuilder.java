package up.visulog.gitrawdata;

public class CommitBuilder {
    private final String id;
    private String author;
    private String date;
    private String description;
    private int linesAdded;
    private int linesRemoved;

    public CommitBuilder(String id) {
        this.id = id;
    }

    public CommitBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public CommitBuilder setDate(String date) {
        this.date = date;
        return this;
    }

    public CommitBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public Commit createCommit() {
        return new Commit(id, author, date, description);
    }

    public CommitBuilder setLines(int lines){
        this.lines = lines;
        return this;
    }

    public Commit createCommit() {
        return new Commit(id, author, date, description, mergedFrom, lines);
    }
}
