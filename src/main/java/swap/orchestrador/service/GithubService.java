package swap.orchestrador.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import swap.orchestrador.client.GithubClient;
import swap.orchestrador.dao.GithubInfoMongoDAO;
import swap.orchestrador.dto.GithubClientContributorsDTO;
import swap.orchestrador.dto.GithubClientIssueDTO;
import swap.orchestrador.dto.GithubClientUserDTO;
import swap.orchestrador.dto.ProducerMessageDTO;
import swap.orchestrador.entity.Contributor;
import swap.orchestrador.entity.GithubInfo;
import swap.orchestrador.entity.Issue;
import swap.orchestrador.kafka.producer.Producer;
import swap.producer.info.RepositoryInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GithubService {
    private static final Logger LOG = LoggerFactory.getLogger(GithubClient.class);
    private final GithubInfoMongoDAO githubInfoMongoDAO;
    private final GithubClient githubClient;
    private final Producer producer;

    @Value("${app.kafka.producer.github-info.topic}")
    private String GITHUB_INFO_TOPIC;

    @Value("${app.kafka.producer.github-info.message-key}")
    private String GITHUB_INFO_MESSAGE_KEY;

    @Value("${app.client.github.repository-info.path}")
    private String REPOSITORY_INFO_PATH;

    @Value("${app.client.github.issue.path}")
    private String ISSUE_PATH;

    @Value("${app.client.github.contributor.path}")
    private String CONTRIBUTOR_PATH;

    @Value("${app.client.github.user.path}")
    private String USER_PATH;

    @Value("${app.client.github.endpoint}")
    private String ENDPOINT;

    public GithubService(GithubInfoMongoDAO githubInfoMongoDAO,
                         GithubClient githubClient, Producer producer) {
        this.githubInfoMongoDAO = githubInfoMongoDAO;
        this.githubClient = githubClient;
        this.producer = producer;
    }

    public void managerGithubInfo(String user, String repository) {
        try {
            GithubInfo githubInfo = callGithubClient(user, repository);

            saveMongoDB(githubInfo);

            sendGithubInfoKakfaProducer(githubInfo);
            LOG.warn("Completed management " + user + repository);
        } catch (Exception ex) {
            LOG.error("Erro for github" + user + repository + ex);
        }

    }

    private void sendGithubInfoKakfaProducer(GithubInfo githubInfo) {
            producer.producer(new ProducerMessageDTO(
                    GITHUB_INFO_MESSAGE_KEY,
                    GITHUB_INFO_TOPIC,
                    convertGithubInfoToAvroRepositoryInfo(githubInfo)));
    }

    private RepositoryInfo convertGithubInfoToAvroRepositoryInfo(GithubInfo githubInfo) {
        List<swap.producer.info.Issue> issuesAvro = convertIssueToAvroIsse(githubInfo);
        List<swap.producer.info.Contributor> contributorsAvro = convertContributorToAvroContributor(githubInfo);

        return RepositoryInfo.newBuilder()
                .setRepository(githubInfo.getRepository())
                .setUser(githubInfo.getUser())
                .setIssues(issuesAvro)
                .setContributors(contributorsAvro)
                .build();
    }

    private static List<swap.producer.info.Contributor> convertContributorToAvroContributor(GithubInfo githubInfo) {
        List<swap.producer.info.Contributor> contributorsAvro = new ArrayList<>();
        for (Contributor contributor : githubInfo.getContributors()) {
            swap.producer.info.Contributor contributorAvro = swap.producer.info.Contributor.newBuilder()
                    .setName(contributor.getName())
                    .setUser(contributor.getUser())
                    .setQtdCommits(contributor.getQtd_commits())
                    .build();
            contributorsAvro.add(contributorAvro);
        }
        return contributorsAvro;
    }

    private static List<swap.producer.info.Issue> convertIssueToAvroIsse(GithubInfo githubInfo) {
        List<swap.producer.info.Issue> issuesAvro = new ArrayList<>();
        for (Issue issue : githubInfo.getIssues()) {
            swap.producer.info.Issue issueAvro = swap.producer.info.Issue.newBuilder()
                    .setAuthor(issue.getAuthor())
                    .setLabels(issue.getLabels())
                    .setTitle(issue.getTitle())
                    .build();
            issuesAvro.add(issueAvro);
        }
        return issuesAvro;
    }

    private GithubInfo callGithubClient(String user, String repository) {
        List<GithubClientIssueDTO> githubIssueResponse = githubClient.callGithubInfoPageable(
                        generateInfoUri(user, repository, ISSUE_PATH), GithubClientIssueDTO[].class)
                .orElseGet(() -> {
                            LOG.warn("Not results for Issues");
                            return new ArrayList<>();
                        }
                );

        Optional<List<GithubClientContributorsDTO>> githubContributorsResponse = githubClient.callGithubInfoPageable(
                generateInfoUri(user, repository, CONTRIBUTOR_PATH), GithubClientContributorsDTO[].class);

        List<GithubClientUserDTO> contributorsWithName = new ArrayList<>();
        if (githubContributorsResponse.isPresent()) {


            for (GithubClientContributorsDTO contributor : githubContributorsResponse.get()) {
                Optional<GithubClientUserDTO> githubClientUserDTO = githubClient.callGithubInfo(
                        generateUserUri(contributor.getLogin()), GithubClientUserDTO.class);
                githubClientUserDTO.ifPresent(clientUserDTO ->
                        contributorsWithName.add(new GithubClientUserDTO(clientUserDTO.getName(), contributor)));

            }
        }
        return convertClientResponse(githubIssueResponse, contributorsWithName, user, repository);
    }

    private String generateInfoUri(String user, String repository, String type) {
        return ENDPOINT + "/" +
                REPOSITORY_INFO_PATH + "/" +
                user + "/" +
                repository + "/" +
                type;
    }

    private String generateUserUri(String user) {
        return ENDPOINT + "/" +
                USER_PATH + "/" +
                user;
    }

    private void saveMongoDB(GithubInfo githubInfo) {
        try {
            var responde = githubInfoMongoDAO.save(githubInfo);
            if (ObjectUtils.isEmpty(responde)) {
                throw new RuntimeException("Erro in save operation in mongoDB");
            }
        }catch (Exception ex){
            LOG.error("ERROR " + ex);
        }
    }

    private GithubInfo convertClientResponse(List<GithubClientIssueDTO> githubClientIssuesResponse,
                                             List<GithubClientUserDTO> githubClientContributorsResponse,
                                             String user,
                                             String repository) {
        GithubInfo githubInfo = new GithubInfo();
        githubInfo.setUser(user);
        githubInfo.setRepository(repository);

        githubInfo.setIssues(githubClientIssuesResponse.stream().map(issue ->
                        new Issue(issue.getTitle(), issue.getUser().getLogin(), issue.getLabels().toString()))
                .collect(Collectors.toList()));

        githubInfo.setContributors(githubClientContributorsResponse.stream().map(contributor ->
                        new Contributor(contributor.getName(), contributor.getLogin(), contributor.getContributions()))
                .collect(Collectors.toList()));

        return githubInfo;
    }

}
