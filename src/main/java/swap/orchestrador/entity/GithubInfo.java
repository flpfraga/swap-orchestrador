package swap.orchestrador.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "${mongodb.collection.name.info.github}")
public class GithubInfo {
    @Id
    private String id;
    private String user;
    private String repository;
    private List<Issue> issues;
    private List<Contributor> contributors;

    public GithubInfo() {
    }

    public GithubInfo(List<Issue> issues) {
        this.issues = issues;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }
}
