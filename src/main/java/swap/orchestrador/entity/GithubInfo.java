package swap.orchestrador.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "${mongodb.collection.name.info.github}")
public class GithubInfo {
    @Id
    private String id;
    private String user;
    private String repository;
    private List<Issue> issues;
    private List<Contributor> contributors;

    public GithubInfo(List<Issue> issues) {
        this.issues = issues;
    }
}
