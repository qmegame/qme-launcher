package org.qme.release;

import java.util.Date;

/**
 * @author cameron
 * @since 1.0.0
 */
public class QmeRelease {

    private String version;
    private String url;
    private String description;
    private String name;
    private boolean preRelease;
    private Date releaseDate;

    public QmeRelease(String version, String url, String description, String name, Boolean preRelease, Date releaseDate) {
        this.version = version;
        this.url = url;
        this.description = description;
        this.name = name;
        this.preRelease = preRelease;
        this.releaseDate = releaseDate;
    }

    /**
     * Gets the version of this release
     * Releases follow semantic versioning standards outlined at semver.org
     * @return the version of the release
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the download url of the release
     * @return the download url of the release
     */
    public String getDownloadUrl() {
        return url;
    }

    /**
     * Gets the description of the release
     * These usually contain patchnotes and are in markdown
     * @return the description of the release
     */
    public String getDescription() {
        if (description.length() == 0) {
            return "No release description";
        }
        return description;
    }

    /**
     * Gets the title of this release
     * @return the title or name of this release
     */
    public String getName() {
        return name;
    }

    /**
     * Gets if the release is a pre release or not
     * @return if the release is a pre release
     */
    public boolean isPreRelease() {
        return preRelease;
    }

    /**
     * Gets the date the release was created
     * @return the date the release was created
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

}
