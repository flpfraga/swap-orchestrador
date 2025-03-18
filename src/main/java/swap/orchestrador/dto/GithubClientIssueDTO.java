package swap.orchestrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubClientIssueDTO {
    private String title;
    private GithubClientUserIssueDTO user;
    private List<GithubClientLabelDTO> labels;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GithubClientUserIssueDTO {
        private String login;
    }
}
