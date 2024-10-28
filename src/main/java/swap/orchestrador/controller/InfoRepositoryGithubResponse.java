package swap.orchestrador.controller;

import swap.orchestrador.entity.Contributor;
import swap.orchestrador.entity.Issue;

import java.util.List;

public class InfoRepositoryGithubResponse {
    private final String userName;
    private final String repositoryName;
    private final List<Issue> issues;
    private final List<Contributor> contributors;

    public InfoRepositoryGithubResponse(String userName, String repositoryName, List<Issue> issues, List<Contributor> contributors) {
        this.userName = userName;
        this.repositoryName = repositoryName;
        this.issues = issues;
        this.contributors = contributors;
    }
}
