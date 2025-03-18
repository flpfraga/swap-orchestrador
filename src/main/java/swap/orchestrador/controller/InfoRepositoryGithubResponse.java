package swap.orchestrador.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swap.orchestrador.entity.Contributor;
import swap.orchestrador.entity.Issue;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoRepositoryGithubResponse {
    private String user;
    private String repository;
    private String status;
    private List<Issue> issues;
    private List<Contributor> contributors;
}
