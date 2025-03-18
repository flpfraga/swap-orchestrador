package swap.orchestrador.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contributor {
    private String name;
    private String user;
    private Integer qtd_commits;
}
