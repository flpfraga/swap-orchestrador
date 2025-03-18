package swap.orchestrador.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoRepositoryGithubRequest {
    @NotBlank(message = "Nome do usuário é obrigatório")
    private String userName;

    @NotBlank(message = "Nome do repositório é obrigatório")
    private String repository;
}
