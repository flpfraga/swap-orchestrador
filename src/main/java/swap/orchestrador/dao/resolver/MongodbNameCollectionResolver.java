package swap.orchestrador.dao.resolver;

import org.springframework.beans.factory.annotation.Value;

public class MongodbNameCollectionResolver {
    @Value("${mongodb.collection.name.info.github:info_github}")
    private String infoGithubCollection;

    public String getInfoGithubCollection() {
        return infoGithubCollection;
    }

    public void setInfoGithubCollection(String infoGithubCollection) {
        this.infoGithubCollection = infoGithubCollection;
    }
}
