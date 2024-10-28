package swap.orchestrador.dto;

public class GithubClientContributorsDTO {
    private String login;
    private Integer contributions;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getContributions() {
        return contributions;
    }

    public void setContributions(Integer contributions) {
        this.contributions = contributions;
    }

    public GithubClientContributorsDTO() {
    }


}
