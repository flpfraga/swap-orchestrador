package swap.orchestrador.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swap.orchestrador.service.GithubService;

@RestController
@RequestMapping(value = "info-repository-github")
public class InfoRepositoryGithubController {

    private final GithubService githubService;

    public InfoRepositoryGithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/info")
    public ResponseEntity<InfoRepositoryGithubResponse> findForUserAndRepository(
            @RequestBody InfoRepositoryGithubRequest infoRepositoryGithubRequest){
        githubService.managerGithubInfo(infoRepositoryGithubRequest.getUserName(),
                infoRepositoryGithubRequest.getRepository());
        return null;
    }
}
