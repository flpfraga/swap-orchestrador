package swap.orchestrador.controller;

public class InfoRepositoryGithubRequest {
    private String userName;
    private String repository;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
