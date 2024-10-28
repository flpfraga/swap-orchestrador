package swap.orchestrador.entity;

public class Contributor {
    private String name;
    private String user;
    private Integer qtd_commits;

    public Contributor() {
    }

    public Contributor(String name, String user, Integer qtd_commits) {
        this.name = name;
        this.user = user;
        this.qtd_commits = qtd_commits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getQtd_commits() {
        return qtd_commits;
    }

    public void setQtd_commits(Integer qtd_commits) {
        this.qtd_commits = qtd_commits;
    }
}
