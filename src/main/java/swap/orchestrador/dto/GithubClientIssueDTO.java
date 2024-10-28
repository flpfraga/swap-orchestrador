package swap.orchestrador.dto;

import java.util.List;

public class GithubClientIssueDTO {
    private String title;
    private GithubClientUserDTO user;
    private List<GithubClientLabelDTO> labels;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GithubClientUserDTO getUser() {
        return user;
    }

    public void setUser(GithubClientUserDTO user) {
        this.user = user;
    }

    public List<GithubClientLabelDTO> getLabels() {
        return labels;
    }

    public void setLabels(List<GithubClientLabelDTO> labels) {
        this.labels = labels;
    }
}
