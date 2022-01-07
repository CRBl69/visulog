package up.visulog.gitrawdata;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
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
    public final String id;
    public final Date date;
    public final String author;
    public final String description;
    public final boolean mergeCommit;
    public final int linesAdded;
    public final int linesRemoved;
    public final HashMap<String, Integer> files;

    public Commit(String id, String author, Date date, String description, boolean mergeCommit, int linesAdded, int linesRemoved, HashMap<String, Integer> files) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergeCommit = mergeCommit;
        this.linesAdded = linesAdded;
        this.linesRemoved = linesRemoved;
        this.files = files;
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
     * Transform a JGit revCommit into a regular Commit object.
     * @throws IOException
     * @throws IncorrectObjectTypeException
     * @throws MissingObjectException
     */

    public static Commit commitOfRevCommit (AnyObjectId id, RevCommit rCommit, Repository repo, boolean calculateDiff) throws MissingObjectException, IncorrectObjectTypeException, IOException{
        var author = rCommit.getAuthorIdent();
        var name = author.getName();
        var email = author.getEmailAddress();

        // get LocalDateTime of commit
        var instant = Instant.ofEpochSecond(rCommit.getCommitTime());
        var date = Date.from(instant);

        // Getting the number of added/deleted lines
        // https://stackoverflow.com/questions/19467305/using-the-jgit-how-can-i-retrieve-the-line-numbers-of-added-deleted-lines
        int linesDeleted = 0;
        int linesAdded = 0;
        HashMap<String, Integer> files = new HashMap<>();
        if(calculateDiff) {
            RevWalk rw = new RevWalk(repo);
            RevCommit parent = rCommit.getParentCount() == 0 ? null :  rw.parseCommit(rCommit.getParent(0).getId());
            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
            df.setRepository(repo);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            List<DiffEntry> diffs;
            diffs = df.scan(parent == null ? null : parent.getTree(), rCommit.getTree());
            for (DiffEntry diff : diffs) {
                String newpath = diff.getNewPath();
                Path path = Paths.get(newpath);
                String filename = path.getFileName().toString();
                for (Edit edit : df.toFileHeader(diff).toEditList()) {
                    linesDeleted += edit.getEndA() - edit.getBeginA();
                    linesAdded += edit.getEndB() - edit.getBeginB();
                }
                files.put(filename, linesAdded+linesDeleted);
            }
            rw.close();
            df.close();
        }
        boolean mergeCommit = rCommit.getParentCount() > 1 ? true : false;

        var commit =
            new Commit(id.getName(),
                name + " <" + email+">",
                date,
                rCommit.getFullMessage(),
                mergeCommit,
                linesAdded,
                linesDeleted,
                files);
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
            return commitOfRevCommit(id, rCommit, repo, true);
        }
    }

    public static List<Commit> getFilteredCommits(Repository repo, List<Filter> filters){
        List<Commit> res = getAllCommitsWithoutDiff(repo);
        for (int i=0; i<filters.size(); i++){
            for (int j=0; j<res.size(); j++){
                if (!filters.get(i).filter(res.get(j))) {
                    res.remove(j);
                    j--;
                }
            }
        }
        return getCommitsFromList(repo, res.stream().map(commit -> commit.id).collect(Collectors.toList()));
    }

    public static List<Commit> getAllCommitsWithoutDiff(Repository repo) {
        try {
            List<Commit> commits = new ArrayList<>();
            Git git = new Git(repo);
            Iterable<RevCommit> rCommits = git.log().all().call();
            for(var rCommit : rCommits) {
                commits.add(commitOfRevCommit(rCommit.getId(), rCommit, repo, false));
            }
            git.close();
            return commits;
        } catch (RevisionSyntaxException | IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static List<Commit> getCommitsFromList(Repository repo, List<String> hashes) {
        try {
            List<Commit> commits = new ArrayList<Commit>();
            Git git = new Git(repo);
            Iterable<RevCommit> rCommits = git.log().all().call();
            for(var rCommit : rCommits) {
                if(hashes.contains(rCommit.getName())) {
                    commits.add(commitOfRevCommit(rCommit.getId(), rCommit, repo, true));
                    hashes.remove(rCommit.getName());
                }
            }
            git.close();
            return commits;
        } catch (RevisionSyntaxException | IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
