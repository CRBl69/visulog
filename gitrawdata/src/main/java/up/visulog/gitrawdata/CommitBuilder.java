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

    public CommitBuilder setLinesAdded(int lines){
        this.linesAdded = lines;
        return this;
    }

    public CommitBuilder setLinesRemoved(int lines){
        this.linesRemoved = lines;
        return this;
    }

    public Commit createCommit() {
        return new Commit(id, author, date, description, linesAdded, linesRemoved);
    }
}
