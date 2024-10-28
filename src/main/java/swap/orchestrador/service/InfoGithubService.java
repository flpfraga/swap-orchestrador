package swap.orchestrador.service;

import swap.orchestrador.controller.InfoRepositoryGithubRequest;
import swap.orchestrador.controller.InfoRepositoryGithubResponse;
import swap.orchestrador.dao.GithubInfoMongoDAO;
import swap.orchestrador.entity.GithubInfo;
import org.springframework.stereotype.Service;

@Service
public class InfoGithubService {

    private final GithubInfoMongoDAO githubInfoMongoDAO;

    public InfoGithubService(GithubInfoMongoDAO githubInfoMongoDAO) {
        this.githubInfoMongoDAO = githubInfoMongoDAO;
    }

    public InfoRepositoryGithubResponse findByUserAndRepository(InfoRepositoryGithubRequest infoRepositoryGithubRequest){
//        return infoGithubMongoDAO.findByUserAndRepository(
//                infoRepositoryGithubRequest.getUserName(), infoRepositoryGithubRequest.getRepository());
    return null;
    }
    public void save(GithubInfo GIthubInfo){
        githubInfoMongoDAO.save(GIthubInfo);
    }
}
