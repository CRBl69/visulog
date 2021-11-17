package up.visulog.gitrawdata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class Commit {
    // AD: FIXME: (some of) these fields could have more specialized types than String
    public final String id;
    public final String date;
    public final String author;
    public final String description;
    public final String mergedFrom;
    public final int linesAdded;
    public final int linesRemoved;

    public Commit(String id, String author, String date, String description, String mergedFrom, int linesAdded, int linesRemoved) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergedFrom = mergedFrom;
        this.linesAdded = linesAdded;
        this.linesRemoved = linesRemoved;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Transforms a time encoded as long into a string with
     * the git log format.
     */
    static String stringOfTime(long time, TimeZone tz) {
        var dtfmt =
            new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        dtfmt.setTimeZone(tz);
        dtfmt.format(Long.valueOf(time));
        return dtfmt.format(Long.valueOf(time));
    }

    /**
     * Transform a JGit revCommit into a regular Commit object.
     * @throws IOException
     * @throws IncorrectObjectTypeException
     * @throws MissingObjectException
     */
    public static Commit commitOfRevCommit (AnyObjectId id, RevCommit rCommit, Repository repo) throws MissingObjectException, IncorrectObjectTypeException, IOException{
        var author = rCommit.getAuthorIdent();
        var name = author.getName();
        var email = author.getEmailAddress();
        var time = author.getWhen().getTime();
        var timeZone = author.getTimeZone();

        // Getting the number of added/deleted lines
        // https://stackoverflow.com/questions/19467305/using-the-jgit-how-can-i-retrieve-the-line-numbers-of-added-deleted-lines
        int linesDeleted = 0;
        int linesAdded = 0;
        RevWalk rw = new RevWalk(repo);
        RevCommit parent = rCommit.getParentCount() == 0 ? null :  rw.parseCommit(rCommit.getParent(0).getId());
        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
        df.setRepository(repo);
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);
        List<DiffEntry> diffs;
        diffs = df.scan(parent == null ? null : parent.getTree(), rCommit.getTree());
        for (DiffEntry diff : diffs) {
            for (Edit edit : df.toFileHeader(diff).toEditList()) {
                linesDeleted += edit.getEndA() - edit.getBeginA();
                linesAdded += edit.getEndB() - edit.getBeginB();
            }
        }
        rw.close();
        df.close();

        var commit =
            new Commit(id.getName(),
                name + " <" + email+">",
                stringOfTime(time, timeZone),
                rCommit.getFullMessage(),
                linesAdded,
                linesDeleted);
        return commit;
    }


    /**
     * Parses a log item and outputs a commit object. Exceptions will
     * be thrown in case the input does not have the proper format.
     */
    public static Commit parse (Repository repo, AnyObjectId id)
	throws MissingObjectException,
	       IncorrectObjectTypeException,
	       IOException {
        try (RevWalk walk = new RevWalk(repo)) {
            RevCommit rCommit = walk.parseCommit(id);
            walk.dispose();
            return commitOfRevCommit(id, rCommit, repo);
        }
    }
    
    public static List<Commit> getAllCommits(Repository repo) {
        try {
            List<Commit> commits = new ArrayList<Commit>();
            Git git = new Git(repo);
            Iterable<RevCommit> rCommits = git.log().all().call();
            for(var rCommit : rCommits) {
                commits.add(commitOfRevCommit(rCommit.getId(), rCommit, repo));
            }
            git.close();
            return commits;
        } catch (RevisionSyntaxException | IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    public boolean isMergeCommit() {
        return mergedFrom != null;
    }
}
