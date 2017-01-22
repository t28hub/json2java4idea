package io.t28.jsoon.core;

import java.util.List;

public class Example {
    private final int id;

    private final String url;

    private final String repository_url;

    private final String labels_url;

    private final String comments_url;

    private final String events_url;

    private final String html_url;

    private final int number;

    private final String state;

    private final String title;

    private final String body;

    private final User user;

    private final List<Labels> labels;

    private final Assignee assignee;

    private final Milestone milestone;

    private final boolean locked;

    private final int comments;

    private final PullRequest pull_request;

    private final Object closed_at;

    private final String created_at;

    private final String updated_at;

    private final ClosedBy closed_by;

    public Example(int id, String url, String repository_url, String labels_url,
                   String comments_url, String events_url, String html_url, int number, String state,
                   String title, String body, User user, List<Labels> labels, Assignee assignee,
                   Milestone milestone, boolean locked, int comments, PullRequest pull_request,
                   Object closed_at, String created_at, String updated_at, ClosedBy closed_by) {
        this.id = id;
        this.url = url;
        this.repository_url = repository_url;
        this.labels_url = labels_url;
        this.comments_url = comments_url;
        this.events_url = events_url;
        this.html_url = html_url;
        this.number = number;
        this.state = state;
        this.title = title;
        this.body = body;
        this.user = user;
        this.labels = labels;
        this.assignee = assignee;
        this.milestone = milestone;
        this.locked = locked;
        this.comments = comments;
        this.pull_request = pull_request;
        this.closed_at = closed_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.closed_by = closed_by;
    }

    public int id() {
        return id;
    }

    public String url() {
        return url;
    }

    public String repository_url() {
        return repository_url;
    }

    public String labels_url() {
        return labels_url;
    }

    public String comments_url() {
        return comments_url;
    }

    public String events_url() {
        return events_url;
    }

    public String html_url() {
        return html_url;
    }

    public int number() {
        return number;
    }

    public String state() {
        return state;
    }

    public String title() {
        return title;
    }

    public String body() {
        return body;
    }

    public User user() {
        return user;
    }

    public List<Labels> labels() {
        return labels;
    }

    public Assignee assignee() {
        return assignee;
    }

    public Milestone milestone() {
        return milestone;
    }

    public boolean locked() {
        return locked;
    }

    public int comments() {
        return comments;
    }

    public PullRequest pull_request() {
        return pull_request;
    }

    public Object closed_at() {
        return closed_at;
    }

    public String created_at() {
        return created_at;
    }

    public String updated_at() {
        return updated_at;
    }

    public ClosedBy closed_by() {
        return closed_by;
    }

    public static class User {
        private final String login;

        private final int id;

        private final String avatar_url;

        private final String gravatar_id;

        private final String url;

        private final String html_url;

        private final String followers_url;

        private final String following_url;

        private final String gists_url;

        private final String starred_url;

        private final String subscriptions_url;

        private final String organizations_url;

        private final String repos_url;

        private final String events_url;

        private final String received_events_url;

        private final String type;

        private final boolean site_admin;

        public User(String login, int id, String avatar_url, String gravatar_id, String url,
                    String html_url, String followers_url, String following_url, String gists_url,
                    String starred_url, String subscriptions_url, String organizations_url,
                    String repos_url, String events_url, String received_events_url, String type,
                    boolean site_admin) {
            this.login = login;
            this.id = id;
            this.avatar_url = avatar_url;
            this.gravatar_id = gravatar_id;
            this.url = url;
            this.html_url = html_url;
            this.followers_url = followers_url;
            this.following_url = following_url;
            this.gists_url = gists_url;
            this.starred_url = starred_url;
            this.subscriptions_url = subscriptions_url;
            this.organizations_url = organizations_url;
            this.repos_url = repos_url;
            this.events_url = events_url;
            this.received_events_url = received_events_url;
            this.type = type;
            this.site_admin = site_admin;
        }

        public String login() {
            return login;
        }

        public int id() {
            return id;
        }

        public String avatar_url() {
            return avatar_url;
        }

        public String gravatar_id() {
            return gravatar_id;
        }

        public String url() {
            return url;
        }

        public String html_url() {
            return html_url;
        }

        public String followers_url() {
            return followers_url;
        }

        public String following_url() {
            return following_url;
        }

        public String gists_url() {
            return gists_url;
        }

        public String starred_url() {
            return starred_url;
        }

        public String subscriptions_url() {
            return subscriptions_url;
        }

        public String organizations_url() {
            return organizations_url;
        }

        public String repos_url() {
            return repos_url;
        }

        public String events_url() {
            return events_url;
        }

        public String received_events_url() {
            return received_events_url;
        }

        public String type() {
            return type;
        }

        public boolean site_admin() {
            return site_admin;
        }
    }

    public static class Labels {
        private final int id;

        private final String url;

        private final String name;

        private final String color;

        private final boolean is_default;

        public Labels(int id, String url, String name, String color, boolean is_default) {
            this.id = id;
            this.url = url;
            this.name = name;
            this.color = color;
            this.is_default = is_default;
        }

        public int id() {
            return id;
        }

        public String url() {
            return url;
        }

        public String name() {
            return name;
        }

        public String color() {
            return color;
        }

        public boolean is_default() {
            return is_default;
        }
    }

    public static class Assignee {
        private final String login;

        private final int id;

        private final String avatar_url;

        private final String gravatar_id;

        private final String url;

        private final String html_url;

        private final String followers_url;

        private final String following_url;

        private final String gists_url;

        private final String starred_url;

        private final String subscriptions_url;

        private final String organizations_url;

        private final String repos_url;

        private final String events_url;

        private final String received_events_url;

        private final String type;

        private final boolean site_admin;

        public Assignee(String login, int id, String avatar_url, String gravatar_id, String url,
                        String html_url, String followers_url, String following_url, String gists_url,
                        String starred_url, String subscriptions_url, String organizations_url,
                        String repos_url, String events_url, String received_events_url, String type,
                        boolean site_admin) {
            this.login = login;
            this.id = id;
            this.avatar_url = avatar_url;
            this.gravatar_id = gravatar_id;
            this.url = url;
            this.html_url = html_url;
            this.followers_url = followers_url;
            this.following_url = following_url;
            this.gists_url = gists_url;
            this.starred_url = starred_url;
            this.subscriptions_url = subscriptions_url;
            this.organizations_url = organizations_url;
            this.repos_url = repos_url;
            this.events_url = events_url;
            this.received_events_url = received_events_url;
            this.type = type;
            this.site_admin = site_admin;
        }

        public String login() {
            return login;
        }

        public int id() {
            return id;
        }

        public String avatar_url() {
            return avatar_url;
        }

        public String gravatar_id() {
            return gravatar_id;
        }

        public String url() {
            return url;
        }

        public String html_url() {
            return html_url;
        }

        public String followers_url() {
            return followers_url;
        }

        public String following_url() {
            return following_url;
        }

        public String gists_url() {
            return gists_url;
        }

        public String starred_url() {
            return starred_url;
        }

        public String subscriptions_url() {
            return subscriptions_url;
        }

        public String organizations_url() {
            return organizations_url;
        }

        public String repos_url() {
            return repos_url;
        }

        public String events_url() {
            return events_url;
        }

        public String received_events_url() {
            return received_events_url;
        }

        public String type() {
            return type;
        }

        public boolean site_admin() {
            return site_admin;
        }
    }

    public static class Milestone {
        private final String url;

        private final String html_url;

        private final String labels_url;

        private final int id;

        private final int number;

        private final String state;

        private final String title;

        private final String description;

        private final Creator creator;

        private final int open_issues;

        private final int closed_issues;

        private final String created_at;

        private final String updated_at;

        private final String closed_at;

        private final String due_on;

        public Milestone(String url, String html_url, String labels_url, int id, int number,
                         String state, String title, String description, Creator creator, int open_issues,
                         int closed_issues, String created_at, String updated_at, String closed_at,
                         String due_on) {
            this.url = url;
            this.html_url = html_url;
            this.labels_url = labels_url;
            this.id = id;
            this.number = number;
            this.state = state;
            this.title = title;
            this.description = description;
            this.creator = creator;
            this.open_issues = open_issues;
            this.closed_issues = closed_issues;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.closed_at = closed_at;
            this.due_on = due_on;
        }

        public String url() {
            return url;
        }

        public String html_url() {
            return html_url;
        }

        public String labels_url() {
            return labels_url;
        }

        public int id() {
            return id;
        }

        public int number() {
            return number;
        }

        public String state() {
            return state;
        }

        public String title() {
            return title;
        }

        public String description() {
            return description;
        }

        public Creator creator() {
            return creator;
        }

        public int open_issues() {
            return open_issues;
        }

        public int closed_issues() {
            return closed_issues;
        }

        public String created_at() {
            return created_at;
        }

        public String updated_at() {
            return updated_at;
        }

        public String closed_at() {
            return closed_at;
        }

        public String due_on() {
            return due_on;
        }

        public static class Creator {
            private final String login;

            private final int id;

            private final String avatar_url;

            private final String gravatar_id;

            private final String url;

            private final String html_url;

            private final String followers_url;

            private final String following_url;

            private final String gists_url;

            private final String starred_url;

            private final String subscriptions_url;

            private final String organizations_url;

            private final String repos_url;

            private final String events_url;

            private final String received_events_url;

            private final String type;

            private final boolean site_admin;

            public Creator(String login, int id, String avatar_url, String gravatar_id, String url,
                           String html_url, String followers_url, String following_url, String gists_url,
                           String starred_url, String subscriptions_url, String organizations_url,
                           String repos_url, String events_url, String received_events_url, String type,
                           boolean site_admin) {
                this.login = login;
                this.id = id;
                this.avatar_url = avatar_url;
                this.gravatar_id = gravatar_id;
                this.url = url;
                this.html_url = html_url;
                this.followers_url = followers_url;
                this.following_url = following_url;
                this.gists_url = gists_url;
                this.starred_url = starred_url;
                this.subscriptions_url = subscriptions_url;
                this.organizations_url = organizations_url;
                this.repos_url = repos_url;
                this.events_url = events_url;
                this.received_events_url = received_events_url;
                this.type = type;
                this.site_admin = site_admin;
            }

            public String login() {
                return login;
            }

            public int id() {
                return id;
            }

            public String avatar_url() {
                return avatar_url;
            }

            public String gravatar_id() {
                return gravatar_id;
            }

            public String url() {
                return url;
            }

            public String html_url() {
                return html_url;
            }

            public String followers_url() {
                return followers_url;
            }

            public String following_url() {
                return following_url;
            }

            public String gists_url() {
                return gists_url;
            }

            public String starred_url() {
                return starred_url;
            }

            public String subscriptions_url() {
                return subscriptions_url;
            }

            public String organizations_url() {
                return organizations_url;
            }

            public String repos_url() {
                return repos_url;
            }

            public String events_url() {
                return events_url;
            }

            public String received_events_url() {
                return received_events_url;
            }

            public String type() {
                return type;
            }

            public boolean site_admin() {
                return site_admin;
            }
        }
    }

    public static class PullRequest {
        private final String url;

        private final String html_url;

        private final String diff_url;

        private final String patch_url;

        public PullRequest(String url, String html_url, String diff_url, String patch_url) {
            this.url = url;
            this.html_url = html_url;
            this.diff_url = diff_url;
            this.patch_url = patch_url;
        }

        public String url() {
            return url;
        }

        public String html_url() {
            return html_url;
        }

        public String diff_url() {
            return diff_url;
        }

        public String patch_url() {
            return patch_url;
        }
    }

    public static class ClosedBy {
        private final String login;

        private final int id;

        private final String avatar_url;

        private final String gravatar_id;

        private final String url;

        private final String html_url;

        private final String followers_url;

        private final String following_url;

        private final String gists_url;

        private final String starred_url;

        private final String subscriptions_url;

        private final String organizations_url;

        private final String repos_url;

        private final String events_url;

        private final String received_events_url;

        private final String type;

        private final boolean site_admin;

        public ClosedBy(String login, int id, String avatar_url, String gravatar_id, String url,
                        String html_url, String followers_url, String following_url, String gists_url,
                        String starred_url, String subscriptions_url, String organizations_url,
                        String repos_url, String events_url, String received_events_url, String type,
                        boolean site_admin) {
            this.login = login;
            this.id = id;
            this.avatar_url = avatar_url;
            this.gravatar_id = gravatar_id;
            this.url = url;
            this.html_url = html_url;
            this.followers_url = followers_url;
            this.following_url = following_url;
            this.gists_url = gists_url;
            this.starred_url = starred_url;
            this.subscriptions_url = subscriptions_url;
            this.organizations_url = organizations_url;
            this.repos_url = repos_url;
            this.events_url = events_url;
            this.received_events_url = received_events_url;
            this.type = type;
            this.site_admin = site_admin;
        }

        public String login() {
            return login;
        }

        public int id() {
            return id;
        }

        public String avatar_url() {
            return avatar_url;
        }

        public String gravatar_id() {
            return gravatar_id;
        }

        public String url() {
            return url;
        }

        public String html_url() {
            return html_url;
        }

        public String followers_url() {
            return followers_url;
        }

        public String following_url() {
            return following_url;
        }

        public String gists_url() {
            return gists_url;
        }

        public String starred_url() {
            return starred_url;
        }

        public String subscriptions_url() {
            return subscriptions_url;
        }

        public String organizations_url() {
            return organizations_url;
        }

        public String repos_url() {
            return repos_url;
        }

        public String events_url() {
            return events_url;
        }

        public String received_events_url() {
            return received_events_url;
        }

        public String type() {
            return type;
        }

        public boolean site_admin() {
            return site_admin;
        }
    }
}
