package swap.orchestrador.dto;

public class GithubClientUserDTO extends GithubClientContributorsDTO{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GithubClientUserDTO() {
    }

    public GithubClientUserDTO(String name, GithubClientContributorsDTO githubClientContributorsDTO) {
        this.name = name;
        this.setContributions(githubClientContributorsDTO.getContributions());
        this.setLogin(githubClientContributorsDTO.getLogin());
    }
}
