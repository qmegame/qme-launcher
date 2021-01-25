package org.qme.release;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Main class for handling fetching of release information
 * @author cameron
 * @since 1.0.0
 */
public class QmeReleaseManager {

    public static final String REPOSITORY_URL = "qmegame/qme-releases";

    ArrayList<QmeRelease> releases = new ArrayList<>();

    /**
     * The url will default to the main releases url
     * @throws IOException
     */
    public QmeReleaseManager() throws IOException {
        this(REPOSITORY_URL);
    }

    /**
     * Creates a new instance of QmeReleaseManager
     * This will also get a list of releases from github
     * @param url the github repository to get releases from in owner/name format
     * @throws IOException
     */
    public QmeReleaseManager(String url) throws IOException {
        GitHub github = GitHub.connect();
        GHRepository repository = github.getRepository(url);

        repository.listReleases().forEach(release -> {
            releases.add(new QmeRelease(release.getTagName(), release.getAssetsUrl(), release.getBody(), release.getName(), release.isPrerelease(), release.getPublished_at()));
        });
    }

    /**
     * Gets the releases of QME
     * @return a list of releases
     * @see QmeRelease
     */
    public ArrayList<QmeRelease> getReleases() {
        return releases;
    }

    /**
     * Returns the full changelog
     * @return full release changelog
     */
    public String getChangelog() {
        StringBuilder stringBuilder = new StringBuilder();
        releases.forEach(release -> {
            stringBuilder.append("(" + release.getVersion() + ") - " + release.getName() + "\n-------");
            stringBuilder.append("\n" + release.getDescription() + "\n-------\n\n");
        });
        return stringBuilder.toString();
    }

    /**
     * Returns a list of valid versions for releases
     * This also includes extra versions like latest-stable and latest-pre
     * @return a list of versions
     */
    public ArrayList<String> getVersions() {
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add("latest-stable");
        toReturn.add("latest-pre");

        getReleases().forEach(release -> {
            toReturn.add(release.getVersion());
        });
        return toReturn;
    }

    /**
     * Finds the latest stable release
     * @return the latest stable release
     */
     public QmeRelease getLatestStable() {
        ArrayList<QmeRelease> clonedArray = (ArrayList<QmeRelease>)getReleases().clone();
        clonedArray.removeIf(QmeRelease::isPreRelease);
        clonedArray.sort(new SortByDate());
        if (clonedArray.size() < 1) {
            return null;
        }
        return clonedArray.get(0);
     }

    /**
     * Finds the latest release, this doesn't necessarily mean it is a pre release only that it is the latest and greatest
     * @return the latest release
     */
    public QmeRelease getLatestPre() {
        ArrayList<QmeRelease> clonedArray = (ArrayList<QmeRelease>)getReleases().clone();
        clonedArray.sort(new SortByDate());
        if (clonedArray.size() < 1) {
            return null;
        }
        return clonedArray.get(0);
    }

    /**
     * Finds a release by it's version
     * This method also takes into account "latest-stable" and "latest-pre"
     * @param version the version that should be search for
     * @return the release for the specified version
     */
    public QmeRelease getReleaseByVersion(String version) {
        if (version == "latest-stable") {
            return getLatestStable();
        } else if (version == "latest-pre") {
            return getLatestPre();
        }
        return getReleases().stream().filter(release -> version == release.getVersion()).findFirst().get();
    }

    /**
     * A sorter that shorts releases by date.
     */
    private class SortByDate implements Comparator<QmeRelease> {
        public int compare(QmeRelease a, QmeRelease b)
        {
            return b.getReleaseDate().compareTo(a.getReleaseDate());
        }
    }
}
