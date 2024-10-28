package swap.orchestrador.client;

import java.util.List;
import java.util.Map;

public class GithubClientResponse {
    private String title;
    private Map<String,Object> user;
    private List labels;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    public List getLabels() {
        return labels;
    }

    public void setLabels(List labels) {
        this.labels = labels;
    }
}
