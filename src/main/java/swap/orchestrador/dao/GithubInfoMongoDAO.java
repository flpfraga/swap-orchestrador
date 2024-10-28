package swap.orchestrador.dao;

import org.springframework.stereotype.Repository;
import swap.orchestrador.entity.GithubInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface GithubInfoMongoDAO extends MongoRepository<GithubInfo, String> {

    GithubInfo findByUserAndRepository(String user, String repository);
}
