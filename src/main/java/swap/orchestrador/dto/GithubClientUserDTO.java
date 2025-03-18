package swap.orchestrador.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GithubClientUserDTO extends GithubClientContributorsDTO {
    private String name;

    public GithubClientUserDTO(String name, GithubClientContributorsDTO githubClientContributorsDTO) {
        super(githubClientContributorsDTO.getLogin(), githubClientContributorsDTO.getContributions());
        this.name = name;
    }
}
