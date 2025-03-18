package swap.orchestrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubClientContributorsDTO {
    private String login;
    private Integer contributions;
}
