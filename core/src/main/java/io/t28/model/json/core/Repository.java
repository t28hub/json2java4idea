package io.t28.model.json.core;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Repository {
    @SerializedName("total_count")
    private final int totalCount;

    @SerializedName("incomplete_results")
    private final boolean incompleteResults;

    @SerializedName("items")
    private final List<Items> items;

    public Repository(int totalCount, boolean incompleteResults, List<Items> items) {
        this.totalCount = totalCount;
        this.incompleteResults = incompleteResults;
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<Items> getItems() {
        return items;
    }

    public static class Items {
        @SerializedName("owner")
        private final Owner owner;

        @SerializedName("private")
        private final boolean _private;

        @SerializedName("stargazers_count")
        private final int stargazersCount;

        @SerializedName("pushed_at")
        private final String pushedAt;

        @SerializedName("open_issues_count")
        private final int openIssuesCount;

        @SerializedName("description")
        private final String description;

        @SerializedName("created_at")
        private final String createdAt;

        @SerializedName("language")
        private final String language;

        @SerializedName("url")
        private final String url;

        @SerializedName("score")
        private final double score;

        @SerializedName("fork")
        private final boolean fork;

        @SerializedName("full_name")
        private final String fullName;

        @SerializedName("updated_at")
        private final String updatedAt;

        @SerializedName("size")
        private final int size;

        @SerializedName("html_url")
        private final String htmlUrl;

        @SerializedName("name")
        private final String name;

        @SerializedName("default_branch")
        private final String defaultBranch;

        @SerializedName("id")
        private final int id;

        @SerializedName("watchers_count")
        private final int watchersCount;

        @SerializedName("master_branch")
        private final String masterBranch;

        @SerializedName("homepage")
        private final String homepage;

        @SerializedName("forks_count")
        private final int forksCount;

        public Items(Owner owner, boolean _private, int stargazersCount, String pushedAt,
                     int openIssuesCount, String description, String createdAt, String language,
                     String url, double score, boolean fork, String fullName, String updatedAt, int size,
                     String htmlUrl, String name, String defaultBranch, int id, int watchersCount,
                     String masterBranch, String homepage, int forksCount) {
            this.owner = owner;
            this._private = _private;
            this.stargazersCount = stargazersCount;
            this.pushedAt = pushedAt;
            this.openIssuesCount = openIssuesCount;
            this.description = description;
            this.createdAt = createdAt;
            this.language = language;
            this.url = url;
            this.score = score;
            this.fork = fork;
            this.fullName = fullName;
            this.updatedAt = updatedAt;
            this.size = size;
            this.htmlUrl = htmlUrl;
            this.name = name;
            this.defaultBranch = defaultBranch;
            this.id = id;
            this.watchersCount = watchersCount;
            this.masterBranch = masterBranch;
            this.homepage = homepage;
            this.forksCount = forksCount;
        }

        public Owner getOwner() {
            return owner;
        }

        public boolean isPrivate() {
            return _private;
        }

        public int getStargazersCount() {
            return stargazersCount;
        }

        public String getPushedAt() {
            return pushedAt;
        }

        public int getOpenIssuesCount() {
            return openIssuesCount;
        }

        public String getDescription() {
            return description;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getLanguage() {
            return language;
        }

        public String getUrl() {
            return url;
        }

        public double getScore() {
            return score;
        }

        public boolean isFork() {
            return fork;
        }

        public String getFullName() {
            return fullName;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int getSize() {
            return size;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public String getName() {
            return name;
        }

        public String getDefaultBranch() {
            return defaultBranch;
        }

        public int getId() {
            return id;
        }

        public int getWatchersCount() {
            return watchersCount;
        }

        public String getMasterBranch() {
            return masterBranch;
        }

        public String getHomepage() {
            return homepage;
        }

        public int getForksCount() {
            return forksCount;
        }

        public static class Owner {
            @SerializedName("received_events_url")
            private final String receivedEventsUrl;

            @SerializedName("avatar_url")
            private final String avatarUrl;

            @SerializedName("id")
            private final int id;

            @SerializedName("login")
            private final String login;

            @SerializedName("type")
            private final String type;

            @SerializedName("gravatar_id")
            private final String gravatarId;

            @SerializedName("url")
            private final String url;

            public Owner(String receivedEventsUrl, String avatarUrl, int id, String login,
                         String type, String gravatarId, String url) {
                this.receivedEventsUrl = receivedEventsUrl;
                this.avatarUrl = avatarUrl;
                this.id = id;
                this.login = login;
                this.type = type;
                this.gravatarId = gravatarId;
                this.url = url;
            }

            public String getReceivedEventsUrl() {
                return receivedEventsUrl;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public int getId() {
                return id;
            }

            public String getLogin() {
                return login;
            }

            public String getType() {
                return type;
            }

            public String getGravatarId() {
                return gravatarId;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}
