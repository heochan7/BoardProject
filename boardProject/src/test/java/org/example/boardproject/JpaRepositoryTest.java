package org.example.boardproject;

import org.example.boardproject.config.JpaConfig;
import org.example.boardproject.domain.Article;
import org.example.boardproject.repository.ArticleCommentRepository;
import org.example.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
public class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void givenTestData_WhenSelecting_thenWorksFine()
    {
        long previousCount = articleRepository.count();

        List<Article> articles = articleRepository.findAll();
        assertThat(articleRepository.count()).isEqualTo(123);
    }

    @DisplayName("insert test")
    @Test
    void givenTestData_WhenInserting_thenWorksFine()
    {
        long previousCount = articleRepository.count();

        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));

        List<Article> articles = articleRepository.findAll();
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }


    @DisplayName("update test")
    @Test
    void givenTestData_WhenUpdating_thenWorksFine()
    {
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        Article savedArticle = articleRepository.saveAndFlush(article);


        assertThat(savedArticle.getHashtag()).isEqualTo("#springboot");
    }

    private static Logger logger = LoggerFactory.getLogger(JpaRepositoryTest.class);

    @DisplayName("delete test")
    @Test
    void givenTestData_WhenDeleting_thenWorksFine()
    {
        Article article = articleRepository.findById(1L).orElseThrow();

        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        long deletedCommentSize = article.getArticleComments().size();


        articleRepository.delete(article);

        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);
    }
}
