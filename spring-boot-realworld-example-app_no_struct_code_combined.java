package io.spring;

public class Util {
  public static boolean isEmpty(String value) {
    return value == null || value.isEmpty();
  }
}

package io.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealWorldApplication {

  public static void main(String[] args) {
    SpringApplication.run(RealWorldApplication.class, args);
  }
}

package io.spring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCustomizations {

  @Bean
  public Module realWorldModules() {
    return new RealWorldModules();
  }

  public static class RealWorldModules extends SimpleModule {
    public RealWorldModules() {
      addSerializer(DateTime.class, new DateTimeSerializer());
    }
  }

  public static class DateTimeSerializer extends StdSerializer<DateTime> {

    protected DateTimeSerializer() {
      super(DateTime.class);
    }

    @Override
    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      if (value == null) {
        gen.writeNull();
      } else {
        gen.writeString(ISODateTimeFormat.dateTime().withZoneUTC().print(value));
      }
    }
  }
}

package io.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig {}

package io.spring.core.comment;

import java.util.Optional;

public interface CommentRepository {
  void save(Comment comment);

  Optional<Comment> findById(String articleId, String id);

  void remove(Comment comment);
}

package io.spring.core.comment;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
  private String id;
  private String body;
  private String userId;
  private String articleId;
  private DateTime createdAt;

  public Comment(String body, String userId, String articleId) {
    this.id = UUID.randomUUID().toString();
    this.body = body;
    this.userId = userId;
    this.articleId = articleId;
    this.createdAt = new DateTime();
  }
}

package io.spring.core.favorite;

import java.util.Optional;

public interface ArticleFavoriteRepository {
  void save(ArticleFavorite articleFavorite);

  Optional<ArticleFavorite> find(String articleId, String userId);

  void remove(ArticleFavorite favorite);
}

package io.spring.core.favorite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ArticleFavorite {
  private String articleId;
  private String userId;

  public ArticleFavorite(String articleId, String userId) {
    this.articleId = articleId;
    this.userId = userId;
  }
}

package io.spring.core.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FollowRelation {
  private String userId;
  private String targetId;

  public FollowRelation(String userId, String targetId) {

    this.userId = userId;
    this.targetId = targetId;
  }
}

package io.spring.core.user;

import io.spring.Util;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
  private String id;
  private String email;
  private String username;
  private String password;
  private String bio;
  private String image;

  public User(String email, String username, String password, String bio, String image) {
    this.id = UUID.randomUUID().toString();
    this.email = email;
    this.username = username;
    this.password = password;
    this.bio = bio;
    this.image = image;
  }

  public void update(String email, String username, String password, String bio, String image) {
    if (!Util.isEmpty(email)) {
      this.email = email;
    }

    if (!Util.isEmpty(username)) {
      this.username = username;
    }

    if (!Util.isEmpty(password)) {
      this.password = password;
    }

    if (!Util.isEmpty(bio)) {
      this.bio = bio;
    }

    if (!Util.isEmpty(image)) {
      this.image = image;
    }
  }
}

package io.spring.core.user;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
  void save(User user);

  Optional<User> findById(String id);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  void saveRelation(FollowRelation followRelation);

  Optional<FollowRelation> findRelation(String userId, String targetId);

  void removeRelation(FollowRelation followRelation);
}

package io.spring.core.article;

import java.util.Optional;

public interface ArticleRepository {

  void save(Article article);

  Optional<Article> findById(String id);

  Optional<Article> findBySlug(String slug);

  void remove(Article article);
}

package io.spring.core.article;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "name")
public class Tag {
  private String id;
  private String name;

  public Tag(String name) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
  }
}

package io.spring.core.article;

import static java.util.stream.Collectors.toList;

import io.spring.Util;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Article {
  private String userId;
  private String id;
  private String slug;
  private String title;
  private String description;
  private String body;
  private List<Tag> tags;
  private DateTime createdAt;
  private DateTime updatedAt;

  public Article(
      String title, String description, String body, List<String> tagList, String userId) {
    this(title, description, body, tagList, userId, new DateTime());
  }

  public Article(
      String title,
      String description,
      String body,
      List<String> tagList,
      String userId,
      DateTime createdAt) {
    this.id = UUID.randomUUID().toString();
    this.slug = toSlug(title);
    this.title = title;
    this.description = description;
    this.body = body;
    this.tags = new HashSet<>(tagList).stream().map(Tag::new).collect(toList());
    this.userId = userId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
  }

  public void update(String title, String description, String body) {
    if (!Util.isEmpty(title)) {
      this.title = title;
      this.slug = toSlug(title);
      this.updatedAt = new DateTime();
    }
    if (!Util.isEmpty(description)) {
      this.description = description;
      this.updatedAt = new DateTime();
    }
    if (!Util.isEmpty(body)) {
      this.body = body;
      this.updatedAt = new DateTime();
    }
  }

  public static String toSlug(String title) {
    return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
  }
}

package io.spring.core.service;

import io.spring.core.article.Article;
import io.spring.core.comment.Comment;
import io.spring.core.user.User;

public class AuthorizationService {
  public static boolean canWriteArticle(User user, Article article) {
    return user.getId().equals(article.getUserId());
  }

  public static boolean canWriteComment(User user, Article article, Comment comment) {
    return user.getId().equals(article.getUserId()) || user.getId().equals(comment.getUserId());
  }
}

package io.spring.core.service;

import io.spring.core.user.User;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
  String toToken(User user);

  Optional<String> getSubFromToken(String token);
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironment;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.UserQueryService;
import io.spring.application.data.UserData;
import io.spring.application.data.UserWithToken;
import io.spring.core.service.JwtService;
import io.spring.graphql.DgsConstants.QUERY;
import io.spring.graphql.DgsConstants.USERPAYLOAD;
import io.spring.graphql.types.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
@AllArgsConstructor
public class MeDatafetcher {
  private UserQueryService userQueryService;
  private JwtService jwtService;

  @DgsData(parentType = DgsConstants.QUERY_TYPE, field = QUERY.Me)
  public DataFetcherResult<User> getMe(
      @RequestHeader(value = "Authorization") String authorization,
      DataFetchingEnvironment dataFetchingEnvironment) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken
        || authentication.getPrincipal() == null) {
      return null;
    }
    io.spring.core.user.User user = (io.spring.core.user.User) authentication.getPrincipal();
    UserData userData =
        userQueryService.findById(user.getId()).orElseThrow(ResourceNotFoundException::new);
    UserWithToken userWithToken = new UserWithToken(userData, authorization.split(" ")[1]);
    User result =
        User.newBuilder()
            .email(userWithToken.getEmail())
            .username(userWithToken.getUsername())
            .token(userWithToken.getToken())
            .build();
    return DataFetcherResult.<User>newResult().data(result).localContext(user).build();
  }

  @DgsData(parentType = USERPAYLOAD.TYPE_NAME, field = USERPAYLOAD.User)
  public DataFetcherResult<User> getUserPayloadUser(
      DataFetchingEnvironment dataFetchingEnvironment) {
    io.spring.core.user.User user = dataFetchingEnvironment.getLocalContext();
    User result =
        User.newBuilder()
            .email(user.getEmail())
            .username(user.getUsername())
            .token(jwtService.toToken(user))
            .build();
    return DataFetcherResult.<User>newResult().data(result).localContext(user).build();
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import graphql.execution.DataFetcherResult;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultPageInfo;
import io.spring.application.CommentQueryService;
import io.spring.application.CursorPageParameter;
import io.spring.application.CursorPager;
import io.spring.application.CursorPager.Direction;
import io.spring.application.DateTimeCursor;
import io.spring.application.data.ArticleData;
import io.spring.application.data.CommentData;
import io.spring.core.user.User;
import io.spring.graphql.DgsConstants.ARTICLE;
import io.spring.graphql.DgsConstants.COMMENTPAYLOAD;
import io.spring.graphql.types.Article;
import io.spring.graphql.types.Comment;
import io.spring.graphql.types.CommentEdge;
import io.spring.graphql.types.CommentsConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.joda.time.format.ISODateTimeFormat;

@DgsComponent
@AllArgsConstructor
public class CommentDatafetcher {
  private CommentQueryService commentQueryService;

  @DgsData(parentType = COMMENTPAYLOAD.TYPE_NAME, field = COMMENTPAYLOAD.Comment)
  public DataFetcherResult<Comment> getComment(DgsDataFetchingEnvironment dfe) {
    CommentData comment = dfe.getLocalContext();
    Comment commentResult = buildCommentResult(comment);
    return DataFetcherResult.<Comment>newResult()
        .data(commentResult)
        .localContext(
            new HashMap<String, Object>() {
              {
                put(comment.getId(), comment);
              }
            })
        .build();
  }

  @DgsData(parentType = ARTICLE.TYPE_NAME, field = ARTICLE.Comments)
  public DataFetcherResult<CommentsConnection> articleComments(
      @InputArgument("first") Integer first,
      @InputArgument("after") String after,
      @InputArgument("last") Integer last,
      @InputArgument("before") String before,
      DgsDataFetchingEnvironment dfe) {

    if (first == null && last == null) {
      throw new IllegalArgumentException("first 和 last 必须只存在一个");
    }

    User current = SecurityUtil.getCurrentUser().orElse(null);
    Article article = dfe.getSource();
    Map<String, ArticleData> map = dfe.getLocalContext();
    ArticleData articleData = map.get(article.getSlug());

    CursorPager<CommentData> comments;
    if (first != null) {
      comments =
          commentQueryService.findByArticleIdWithCursor(
              articleData.getId(),
              current,
              new CursorPageParameter<>(DateTimeCursor.parse(after), first, Direction.NEXT));
    } else {
      comments =
          commentQueryService.findByArticleIdWithCursor(
              articleData.getId(),
              current,
              new CursorPageParameter<>(DateTimeCursor.parse(before), last, Direction.PREV));
    }
    graphql.relay.PageInfo pageInfo = buildCommentPageInfo(comments);
    CommentsConnection result =
        CommentsConnection.newBuilder()
            .pageInfo(pageInfo)
            .edges(
                comments.getData().stream()
                    .map(
                        a ->
                            CommentEdge.newBuilder()
                                .cursor(a.getCursor().toString())
                                .node(buildCommentResult(a))
                                .build())
                    .collect(Collectors.toList()))
            .build();
    return DataFetcherResult.<CommentsConnection>newResult()
        .data(result)
        .localContext(
            comments.getData().stream().collect(Collectors.toMap(CommentData::getId, c -> c)))
        .build();
  }

  private DefaultPageInfo buildCommentPageInfo(CursorPager<CommentData> comments) {
    return new DefaultPageInfo(
        comments.getStartCursor() == null
            ? null
            : new DefaultConnectionCursor(comments.getStartCursor().toString()),
        comments.getEndCursor() == null
            ? null
            : new DefaultConnectionCursor(comments.getEndCursor().toString()),
        comments.hasPrevious(),
        comments.hasNext());
  }

  private Comment buildCommentResult(CommentData comment) {
    return Comment.newBuilder()
        .id(comment.getId())
        .body(comment.getBody())
        .updatedAt(ISODateTimeFormat.dateTime().withZoneUTC().print(comment.getCreatedAt()))
        .createdAt(ISODateTimeFormat.dateTime().withZoneUTC().print(comment.getCreatedAt()))
        .build();
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.execution.DataFetcherResult;
import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.application.user.RegisterParam;
import io.spring.application.user.UpdateUserCommand;
import io.spring.application.user.UpdateUserParam;
import io.spring.application.user.UserService;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.graphql.DgsConstants.MUTATION;
import io.spring.graphql.exception.GraphQLCustomizeExceptionHandler;
import io.spring.graphql.types.CreateUserInput;
import io.spring.graphql.types.UpdateUserInput;
import io.spring.graphql.types.UserPayload;
import io.spring.graphql.types.UserResult;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DgsComponent
@AllArgsConstructor
public class UserMutation {

  private UserRepository userRepository;
  private PasswordEncoder encryptService;
  private UserService userService;

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.CreateUser)
  public DataFetcherResult<UserResult> createUser(@InputArgument("input") CreateUserInput input) {
    RegisterParam registerParam =
        new RegisterParam(input.getEmail(), input.getUsername(), input.getPassword());
    User user;
    try {
      user = userService.createUser(registerParam);
    } catch (ConstraintViolationException cve) {
      return DataFetcherResult.<UserResult>newResult()
          .data(GraphQLCustomizeExceptionHandler.getErrorsAsData(cve))
          .build();
    }

    return DataFetcherResult.<UserResult>newResult()
        .data(UserPayload.newBuilder().build())
        .localContext(user)
        .build();
  }

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.Login)
  public DataFetcherResult<UserPayload> login(
      @InputArgument("password") String password, @InputArgument("email") String email) {
    Optional<User> optional = userRepository.findByEmail(email);
    if (optional.isPresent() && encryptService.matches(password, optional.get().getPassword())) {
      return DataFetcherResult.<UserPayload>newResult()
          .data(UserPayload.newBuilder().build())
          .localContext(optional.get())
          .build();
    } else {
      throw new InvalidAuthenticationException();
    }
  }

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.UpdateUser)
  public DataFetcherResult<UserPayload> updateUser(
      @InputArgument("changes") UpdateUserInput updateUserInput) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken
        || authentication.getPrincipal() == null) {
      return null;
    }
    io.spring.core.user.User currentUser = (io.spring.core.user.User) authentication.getPrincipal();
    UpdateUserParam param =
        UpdateUserParam.builder()
            .username(updateUserInput.getUsername())
            .email(updateUserInput.getEmail())
            .bio(updateUserInput.getBio())
            .password(updateUserInput.getPassword())
            .image(updateUserInput.getImage())
            .build();

    userService.updateUser(new UpdateUserCommand(currentUser, param));
    return DataFetcherResult.<UserPayload>newResult()
        .data(UserPayload.newBuilder().build())
        .localContext(currentUser)
        .build();
  }
}

package io.spring.graphql;

import io.spring.core.user.User;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
  public static Optional<User> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken
        || authentication.getPrincipal() == null) {
      return Optional.empty();
    }
    io.spring.core.user.User currentUser = (io.spring.core.user.User) authentication.getPrincipal();
    return Optional.of(currentUser);
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.execution.DataFetcherResult;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.CommentQueryService;
import io.spring.application.data.CommentData;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import io.spring.graphql.DgsConstants.MUTATION;
import io.spring.graphql.exception.AuthenticationException;
import io.spring.graphql.types.CommentPayload;
import io.spring.graphql.types.DeletionStatus;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class CommentMutation {

  private ArticleRepository articleRepository;
  private CommentRepository commentRepository;
  private CommentQueryService commentQueryService;

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.AddComment)
  public DataFetcherResult<CommentPayload> createComment(
      @InputArgument("slug") String slug, @InputArgument("body") String body) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Comment comment = new Comment(body, user.getId(), article.getId());
    commentRepository.save(comment);
    CommentData commentData =
        commentQueryService
            .findById(comment.getId(), user)
            .orElseThrow(ResourceNotFoundException::new);
    return DataFetcherResult.<CommentPayload>newResult()
        .localContext(commentData)
        .data(CommentPayload.newBuilder().build())
        .build();
  }

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.DeleteComment)
  public DeletionStatus removeComment(
      @InputArgument("slug") String slug, @InputArgument("id") String commentId) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);

    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return commentRepository
        .findById(article.getId(), commentId)
        .map(
            comment -> {
              if (!AuthorizationService.canWriteComment(user, article, comment)) {
                throw new NoAuthorizationException();
              }
              commentRepository.remove(comment);
              return DeletionStatus.newBuilder().success(true).build();
            })
        .orElseThrow(ResourceNotFoundException::new);
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import io.spring.application.TagsQueryService;
import io.spring.graphql.DgsConstants.QUERY;
import java.util.List;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class TagDatafetcher {
  private TagsQueryService tagsQueryService;

  @DgsData(parentType = DgsConstants.QUERY_TYPE, field = QUERY.Tags)
  public List<String> getTags() {
    return tagsQueryService.allTags();
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.execution.DataFetcherResult;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultPageInfo;
import graphql.schema.DataFetchingEnvironment;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.application.CursorPageParameter;
import io.spring.application.CursorPager;
import io.spring.application.CursorPager.Direction;
import io.spring.application.DateTimeCursor;
import io.spring.application.data.ArticleData;
import io.spring.application.data.CommentData;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.graphql.DgsConstants.ARTICLEPAYLOAD;
import io.spring.graphql.DgsConstants.COMMENT;
import io.spring.graphql.DgsConstants.PROFILE;
import io.spring.graphql.DgsConstants.QUERY;
import io.spring.graphql.types.Article;
import io.spring.graphql.types.ArticleEdge;
import io.spring.graphql.types.ArticlesConnection;
import io.spring.graphql.types.Profile;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.joda.time.format.ISODateTimeFormat;

@DgsComponent
@AllArgsConstructor
public class ArticleDatafetcher {

  private ArticleQueryService articleQueryService;
  private UserRepository userRepository;

  @DgsQuery(field = QUERY.Feed)
  public DataFetcherResult<ArticlesConnection> getFeed(
      @InputArgument("first") Integer first,
      @InputArgument("after") String after,
      @InputArgument("last") Integer last,
      @InputArgument("before") String before,
      DgsDataFetchingEnvironment dfe) {
    if (first == null && last == null) {
      throw new IllegalArgumentException("first 和 last 必须只存在一个");
    }

    User current = SecurityUtil.getCurrentUser().orElse(null);

    CursorPager<ArticleData> articles;
    if (first != null) {
      articles =
          articleQueryService.findUserFeedWithCursor(
              current,
              new CursorPageParameter<>(DateTimeCursor.parse(after), first, Direction.NEXT));
    } else {
      articles =
          articleQueryService.findUserFeedWithCursor(
              current,
              new CursorPageParameter<>(DateTimeCursor.parse(before), last, Direction.PREV));
    }
    graphql.relay.PageInfo pageInfo = buildArticlePageInfo(articles);
    ArticlesConnection articlesConnection =
        ArticlesConnection.newBuilder()
            .pageInfo(pageInfo)
            .edges(
                articles.getData().stream()
                    .map(
                        a ->
                            ArticleEdge.newBuilder()
                                .cursor(a.getCursor().toString())
                                .node(buildArticleResult(a))
                                .build())
                    .collect(Collectors.toList()))
            .build();
    return DataFetcherResult.<ArticlesConnection>newResult()
        .data(articlesConnection)
        .localContext(
            articles.getData().stream().collect(Collectors.toMap(ArticleData::getSlug, a -> a)))
        .build();
  }

  @DgsData(parentType = PROFILE.TYPE_NAME, field = PROFILE.Feed)
  public DataFetcherResult<ArticlesConnection> userFeed(
      @InputArgument("first") Integer first,
      @InputArgument("after") String after,
      @InputArgument("last") Integer last,
      @InputArgument("before") String before,
      DgsDataFetchingEnvironment dfe) {
    if (first == null && last == null) {
      throw new IllegalArgumentException("first 和 last 必须只存在一个");
    }

    Profile profile = dfe.getSource();
    User target =
        userRepository
            .findByUsername(profile.getUsername())
            .orElseThrow(ResourceNotFoundException::new);

    CursorPager<ArticleData> articles;
    if (first != null) {
      articles =
          articleQueryService.findUserFeedWithCursor(
              target,
              new CursorPageParameter<>(DateTimeCursor.parse(after), first, Direction.NEXT));
    } else {
      articles =
          articleQueryService.findUserFeedWithCursor(
              target,
              new CursorPageParameter<>(DateTimeCursor.parse(before), last, Direction.PREV));
    }
    graphql.relay.PageInfo pageInfo = buildArticlePageInfo(articles);
    ArticlesConnection articlesConnection =
        ArticlesConnection.newBuilder()
            .pageInfo(pageInfo)
            .edges(
                articles.getData().stream()
                    .map(
                        a ->
                            ArticleEdge.newBuilder()
                                .cursor(a.getCursor().toString())
                                .node(buildArticleResult(a))
                                .build())
                    .collect(Collectors.toList()))
            .build();
    return DataFetcherResult.<ArticlesConnection>newResult()
        .data(articlesConnection)
        .localContext(
            articles.getData().stream().collect(Collectors.toMap(ArticleData::getSlug, a -> a)))
        .build();
  }

  @DgsData(parentType = PROFILE.TYPE_NAME, field = PROFILE.Favorites)
  public DataFetcherResult<ArticlesConnection> userFavorites(
      @InputArgument("first") Integer first,
      @InputArgument("after") String after,
      @InputArgument("last") Integer last,
      @InputArgument("before") String before,
      DgsDataFetchingEnvironment dfe) {
    if (first == null && last == null) {
      throw new IllegalArgumentException("first 和 last 必须只存在一个");
    }

    User current = SecurityUtil.getCurrentUser().orElse(null);
    Profile profile = dfe.getSource();

    CursorPager<ArticleData> articles;
    if (first != null) {
      articles =
          articleQueryService.findRecentArticlesWithCursor(
              null,
              null,
              profile.getUsername(),
              new CursorPageParameter<>(DateTimeCursor.parse(after), first, Direction.NEXT),
              current);
    } else {
      articles =
          articleQueryService.findRecentArticlesWithCursor(
              null,
              null,
              profile.getUsername(),
              new CursorPageParameter<>(DateTimeCursor.parse(before), last, Direction.PREV),
              current);
    }
    graphql.relay.PageInfo pageInfo = buildArticlePageInfo(articles);

    ArticlesConnection articlesConnection =
        ArticlesConnection.newBuilder()
            .pageInfo(pageInfo)
            .edges(
                articles.getData().stream()
                    .map(
                        a ->
                            ArticleEdge.newBuilder()
                                .cursor(a.getCursor().toString())
                                .node(buildArticleResult(a))
                                .build())
                    .collect(Collectors.toList()))
            .build();
    return DataFetcherResult.<ArticlesConnection>newResult()
        .data(articlesConnection)
        .localContext(
            articles.getData().stream().collect(Collectors.toMap(ArticleData::getSlug, a -> a)))
        .build();
  }

  @DgsData(parentType = PROFILE.TYPE_NAME, field = PROFILE.Articles)
  public DataFetcherResult<ArticlesConnection> userArticles(
      @InputArgument("first") Integer first,
      @InputArgument("after") String after,
      @InputArgument("last") Integer last,
      @InputArgument("before") String before,
      DgsDataFetchingEnvironment dfe) {
    if (first == null && last == null) {
      throw new IllegalArgumentException("first 和 last 必须只存在一个");
    }

    User current = SecurityUtil.getCurrentUser().orElse(null);
    Profile profile = dfe.getSource();

    CursorPager<ArticleData> articles;
    if (first != null) {
      articles =
          articleQueryService.findRecentArticlesWithCursor(
              null,
              profile.getUsername(),
              null,
              new CursorPageParameter<>(DateTimeCursor.parse(after), first, Direction.NEXT),
              current);
    } else {
      articles =
          articleQueryService.findRecentArticlesWithCursor(
              null,
              profile.getUsername(),
              null,
              new CursorPageParameter<>(DateTimeCursor.parse(before), last, Direction.PREV),
              current);
    }
    graphql.relay.PageInfo pageInfo = buildArticlePageInfo(articles);
    ArticlesConnection articlesConnection =
        ArticlesConnection.newBuilder()
            .pageInfo(pageInfo)
            .edges(
                articles.getData().stream()
                    .map(
                        a ->
                            ArticleEdge.newBuilder()
                                .cursor(a.getCursor().toString())
                                .node(buildArticleResult(a))
                                .build())
                    .collect(Collectors.toList()))
            .build();
    return DataFetcherResult.<ArticlesConnection>newResult()
        .data(articlesConnection)
        .localContext(
            articles.getData().stream().collect(Collectors.toMap(ArticleData::getSlug, a -> a)))
        .build();
  }

  @DgsData(parentType = DgsConstants.QUERY_TYPE, field = QUERY.Articles)
  public DataFetcherResult<ArticlesConnection> getArticles(
      @InputArgument("first") Integer first,
      @InputArgument("after") String after,
      @InputArgument("last") Integer last,
      @InputArgument("before") String before,
      @InputArgument("authoredBy") String authoredBy,
      @InputArgument("favoritedBy") String favoritedBy,
      @InputArgument("withTag") String withTag,
      DgsDataFetchingEnvironment dfe) {
    if (first == null && last == null) {
      throw new IllegalArgumentException("first 和 last 必须只存在一个");
    }

    User current = SecurityUtil.getCurrentUser().orElse(null);

    CursorPager<ArticleData> articles;
    if (first != null) {
      articles =
          articleQueryService.findRecentArticlesWithCursor(
              withTag,
              authoredBy,
              favoritedBy,
              new CursorPageParameter<>(DateTimeCursor.parse(after), first, Direction.NEXT),
              current);
    } else {
      articles =
          articleQueryService.findRecentArticlesWithCursor(
              withTag,
              authoredBy,
              favoritedBy,
              new CursorPageParameter<>(DateTimeCursor.parse(before), last, Direction.PREV),
              current);
    }
    graphql.relay.PageInfo pageInfo = buildArticlePageInfo(articles);
    ArticlesConnection articlesConnection =
        ArticlesConnection.newBuilder()
            .pageInfo(pageInfo)
            .edges(
                articles.getData().stream()
                    .map(
                        a ->
                            ArticleEdge.newBuilder()
                                .cursor(a.getCursor().toString())
                                .node(buildArticleResult(a))
                                .build())
                    .collect(Collectors.toList()))
            .build();
    return DataFetcherResult.<ArticlesConnection>newResult()
        .data(articlesConnection)
        .localContext(
            articles.getData().stream().collect(Collectors.toMap(ArticleData::getSlug, a -> a)))
        .build();
  }

  @DgsData(parentType = ARTICLEPAYLOAD.TYPE_NAME, field = ARTICLEPAYLOAD.Article)
  public DataFetcherResult<Article> getArticle(DataFetchingEnvironment dfe) {
    io.spring.core.article.Article article = dfe.getLocalContext();

    User current = SecurityUtil.getCurrentUser().orElse(null);
    ArticleData articleData =
        articleQueryService
            .findById(article.getId(), current)
            .orElseThrow(ResourceNotFoundException::new);
    Article articleResult = buildArticleResult(articleData);
    return DataFetcherResult.<Article>newResult()
        .localContext(
            new HashMap<String, Object>() {
              {
                put(articleData.getSlug(), articleData);
              }
            })
        .data(articleResult)
        .build();
  }

  @DgsData(parentType = COMMENT.TYPE_NAME, field = COMMENT.Article)
  public DataFetcherResult<Article> getCommentArticle(
      DataFetchingEnvironment dataFetchingEnvironment) {
    CommentData comment = dataFetchingEnvironment.getLocalContext();
    User current = SecurityUtil.getCurrentUser().orElse(null);
    ArticleData articleData =
        articleQueryService
            .findById(comment.getArticleId(), current)
            .orElseThrow(ResourceNotFoundException::new);
    Article articleResult = buildArticleResult(articleData);
    return DataFetcherResult.<Article>newResult()
        .localContext(
            new HashMap<String, Object>() {
              {
                put(articleData.getSlug(), articleData);
              }
            })
        .data(articleResult)
        .build();
  }

  @DgsQuery(field = QUERY.Article)
  public DataFetcherResult<Article> findArticleBySlug(@InputArgument("slug") String slug) {
    User current = SecurityUtil.getCurrentUser().orElse(null);
    ArticleData articleData =
        articleQueryService.findBySlug(slug, current).orElseThrow(ResourceNotFoundException::new);
    Article articleResult = buildArticleResult(articleData);
    return DataFetcherResult.<Article>newResult()
        .localContext(
            new HashMap<String, Object>() {
              {
                put(articleData.getSlug(), articleData);
              }
            })
        .data(articleResult)
        .build();
  }

  private DefaultPageInfo buildArticlePageInfo(CursorPager<ArticleData> articles) {
    return new DefaultPageInfo(
        articles.getStartCursor() == null
            ? null
            : new DefaultConnectionCursor(articles.getStartCursor().toString()),
        articles.getEndCursor() == null
            ? null
            : new DefaultConnectionCursor(articles.getEndCursor().toString()),
        articles.hasPrevious(),
        articles.hasNext());
  }

  private Article buildArticleResult(ArticleData articleData) {
    return Article.newBuilder()
        .body(articleData.getBody())
        .createdAt(ISODateTimeFormat.dateTime().withZoneUTC().print(articleData.getCreatedAt()))
        .description(articleData.getDescription())
        .favorited(articleData.isFavorited())
        .favoritesCount(articleData.getFavoritesCount())
        .slug(articleData.getSlug())
        .tagList(articleData.getTagList())
        .title(articleData.getTitle())
        .updatedAt(ISODateTimeFormat.dateTime().withZoneUTC().print(articleData.getUpdatedAt()))
        .build();
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ProfileQueryService;
import io.spring.application.data.ArticleData;
import io.spring.application.data.CommentData;
import io.spring.application.data.ProfileData;
import io.spring.core.user.User;
import io.spring.graphql.DgsConstants.ARTICLE;
import io.spring.graphql.DgsConstants.COMMENT;
import io.spring.graphql.DgsConstants.QUERY;
import io.spring.graphql.DgsConstants.USER;
import io.spring.graphql.types.Article;
import io.spring.graphql.types.Comment;
import io.spring.graphql.types.Profile;
import io.spring.graphql.types.ProfilePayload;
import java.util.Map;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class ProfileDatafetcher {

  private ProfileQueryService profileQueryService;

  @DgsData(parentType = USER.TYPE_NAME, field = USER.Profile)
  public Profile getUserProfile(DataFetchingEnvironment dataFetchingEnvironment) {
    User user = dataFetchingEnvironment.getLocalContext();
    String username = user.getUsername();
    return queryProfile(username);
  }

  @DgsData(parentType = ARTICLE.TYPE_NAME, field = ARTICLE.Author)
  public Profile getAuthor(DataFetchingEnvironment dataFetchingEnvironment) {
    Map<String, ArticleData> map = dataFetchingEnvironment.getLocalContext();
    Article article = dataFetchingEnvironment.getSource();
    return queryProfile(map.get(article.getSlug()).getProfileData().getUsername());
  }

  @DgsData(parentType = COMMENT.TYPE_NAME, field = COMMENT.Author)
  public Profile getCommentAuthor(DataFetchingEnvironment dataFetchingEnvironment) {
    Comment comment = dataFetchingEnvironment.getSource();
    Map<String, CommentData> map = dataFetchingEnvironment.getLocalContext();
    return queryProfile(map.get(comment.getId()).getProfileData().getUsername());
  }

  @DgsData(parentType = DgsConstants.QUERY_TYPE, field = QUERY.Profile)
  public ProfilePayload queryProfile(
      @InputArgument("username") String username, DataFetchingEnvironment dataFetchingEnvironment) {
    Profile profile = queryProfile(dataFetchingEnvironment.getArgument("username"));
    return ProfilePayload.newBuilder().profile(profile).build();
  }

  private Profile queryProfile(String username) {
    User current = SecurityUtil.getCurrentUser().orElse(null);
    ProfileData profileData =
        profileQueryService
            .findByUsername(username, current)
            .orElseThrow(ResourceNotFoundException::new);
    return Profile.newBuilder()
        .username(profileData.getUsername())
        .bio(profileData.getBio())
        .image(profileData.getImage())
        .following(profileData.isFollowing())
        .build();
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import graphql.execution.DataFetcherResult;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.article.ArticleCommandService;
import io.spring.application.article.NewArticleParam;
import io.spring.application.article.UpdateArticleParam;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import io.spring.graphql.DgsConstants.MUTATION;
import io.spring.graphql.exception.AuthenticationException;
import io.spring.graphql.types.ArticlePayload;
import io.spring.graphql.types.CreateArticleInput;
import io.spring.graphql.types.DeletionStatus;
import io.spring.graphql.types.UpdateArticleInput;
import java.util.Collections;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class ArticleMutation {

  private ArticleCommandService articleCommandService;
  private ArticleFavoriteRepository articleFavoriteRepository;
  private ArticleRepository articleRepository;

  @DgsMutation(field = MUTATION.CreateArticle)
  public DataFetcherResult<ArticlePayload> createArticle(
      @InputArgument("input") CreateArticleInput input) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    NewArticleParam newArticleParam =
        NewArticleParam.builder()
            .title(input.getTitle())
            .description(input.getDescription())
            .body(input.getBody())
            .tagList(input.getTagList() == null ? Collections.emptyList() : input.getTagList())
            .build();
    Article article = articleCommandService.createArticle(newArticleParam, user);
    return DataFetcherResult.<ArticlePayload>newResult()
        .data(ArticlePayload.newBuilder().build())
        .localContext(article)
        .build();
  }

  @DgsMutation(field = MUTATION.UpdateArticle)
  public DataFetcherResult<ArticlePayload> updateArticle(
      @InputArgument("slug") String slug, @InputArgument("changes") UpdateArticleInput params) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    if (!AuthorizationService.canWriteArticle(user, article)) {
      throw new NoAuthorizationException();
    }
    article =
        articleCommandService.updateArticle(
            article,
            new UpdateArticleParam(params.getTitle(), params.getBody(), params.getDescription()));
    return DataFetcherResult.<ArticlePayload>newResult()
        .data(ArticlePayload.newBuilder().build())
        .localContext(article)
        .build();
  }

  @DgsMutation(field = MUTATION.FavoriteArticle)
  public DataFetcherResult<ArticlePayload> favoriteArticle(@InputArgument("slug") String slug) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavorite articleFavorite = new ArticleFavorite(article.getId(), user.getId());
    articleFavoriteRepository.save(articleFavorite);
    return DataFetcherResult.<ArticlePayload>newResult()
        .data(ArticlePayload.newBuilder().build())
        .localContext(article)
        .build();
  }

  @DgsMutation(field = MUTATION.UnfavoriteArticle)
  public DataFetcherResult<ArticlePayload> unfavoriteArticle(@InputArgument("slug") String slug) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    articleFavoriteRepository
        .find(article.getId(), user.getId())
        .ifPresent(
            favorite -> {
              articleFavoriteRepository.remove(favorite);
            });
    return DataFetcherResult.<ArticlePayload>newResult()
        .data(ArticlePayload.newBuilder().build())
        .localContext(article)
        .build();
  }

  @DgsMutation(field = MUTATION.DeleteArticle)
  public DeletionStatus deleteArticle(@InputArgument("slug") String slug) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);

    if (!AuthorizationService.canWriteArticle(user, article)) {
      throw new NoAuthorizationException();
    }

    articleRepository.remove(article);
    return DeletionStatus.newBuilder().success(true).build();
  }
}

package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ProfileQueryService;
import io.spring.application.data.ProfileData;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.graphql.DgsConstants.MUTATION;
import io.spring.graphql.exception.AuthenticationException;
import io.spring.graphql.types.Profile;
import io.spring.graphql.types.ProfilePayload;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class RelationMutation {

  private UserRepository userRepository;
  private ProfileQueryService profileQueryService;

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.FollowUser)
  public ProfilePayload follow(@InputArgument("username") String username) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    return userRepository
        .findByUsername(username)
        .map(
            target -> {
              FollowRelation followRelation = new FollowRelation(user.getId(), target.getId());
              userRepository.saveRelation(followRelation);
              Profile profile = buildProfile(username, user);
              return ProfilePayload.newBuilder().profile(profile).build();
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.UnfollowUser)
  public ProfilePayload unfollow(@InputArgument("username") String username) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    User target =
        userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    return userRepository
        .findRelation(user.getId(), target.getId())
        .map(
            relation -> {
              userRepository.removeRelation(relation);
              Profile profile = buildProfile(username, user);
              return ProfilePayload.newBuilder().profile(profile).build();
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  private Profile buildProfile(@InputArgument("username") String username, User current) {
    ProfileData profileData = profileQueryService.findByUsername(username, current).get();
    return Profile.newBuilder()
        .username(profileData.getUsername())
        .bio(profileData.getBio())
        .image(profileData.getImage())
        .following(profileData.isFollowing())
        .build();
  }
}

package io.spring.graphql.exception;

import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.ErrorType;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import io.spring.api.exception.FieldErrorResource;
import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.graphql.types.Error;
import io.spring.graphql.types.ErrorItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

@Component
public class GraphQLCustomizeExceptionHandler implements DataFetcherExceptionHandler {

  private final DefaultDataFetcherExceptionHandler defaultHandler =
      new DefaultDataFetcherExceptionHandler();

  @Override
  public DataFetcherExceptionHandlerResult onException(
      DataFetcherExceptionHandlerParameters handlerParameters) {
    if (handlerParameters.getException() instanceof InvalidAuthenticationException) {
      GraphQLError graphqlError =
          TypedGraphQLError.newBuilder()
              .errorType(ErrorType.UNAUTHENTICATED)
              .message(handlerParameters.getException().getMessage())
              .path(handlerParameters.getPath())
              .build();
      return DataFetcherExceptionHandlerResult.newResult().error(graphqlError).build();
    } else if (handlerParameters.getException() instanceof ConstraintViolationException) {
      List<FieldErrorResource> errors = new ArrayList<>();
      for (ConstraintViolation<?> violation :
          ((ConstraintViolationException) handlerParameters.getException())
              .getConstraintViolations()) {
        FieldErrorResource fieldErrorResource =
            new FieldErrorResource(
                violation.getRootBeanClass().getName(),
                getParam(violation.getPropertyPath().toString()),
                violation
                    .getConstraintDescriptor()
                    .getAnnotation()
                    .annotationType()
                    .getSimpleName(),
                violation.getMessage());
        errors.add(fieldErrorResource);
      }
      GraphQLError graphqlError =
          TypedGraphQLError.newBadRequestBuilder()
              .message(handlerParameters.getException().getMessage())
              .path(handlerParameters.getPath())
              .extensions(errorsToMap(errors))
              .build();
      return DataFetcherExceptionHandlerResult.newResult().error(graphqlError).build();
    } else {
      return defaultHandler.onException(handlerParameters);
    }
  }

  public static Error getErrorsAsData(ConstraintViolationException cve) {
    List<FieldErrorResource> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
      FieldErrorResource fieldErrorResource =
          new FieldErrorResource(
              violation.getRootBeanClass().getName(),
              getParam(violation.getPropertyPath().toString()),
              violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
              violation.getMessage());
      errors.add(fieldErrorResource);
    }
    Map<String, List<String>> errorMap = new HashMap<>();
    for (FieldErrorResource fieldErrorResource : errors) {
      if (!errorMap.containsKey(fieldErrorResource.getField())) {
        errorMap.put(fieldErrorResource.getField(), new ArrayList<>());
      }
      errorMap.get(fieldErrorResource.getField()).add(fieldErrorResource.getMessage());
    }
    List<ErrorItem> errorItems =
        errorMap.entrySet().stream()
            .map(kv -> ErrorItem.newBuilder().key(kv.getKey()).value(kv.getValue()).build())
            .collect(Collectors.toList());
    return Error.newBuilder().message("BAD_REQUEST").errors(errorItems).build();
  }

  private static String getParam(String s) {
    String[] splits = s.split("\\.");
    if (splits.length == 1) {
      return s;
    } else {
      return String.join(".", Arrays.copyOfRange(splits, 2, splits.length));
    }
  }

  private static Map<String, Object> errorsToMap(List<FieldErrorResource> errors) {
    Map<String, Object> json = new HashMap<>();
    for (FieldErrorResource fieldErrorResource : errors) {
      if (!json.containsKey(fieldErrorResource.getField())) {
        json.put(fieldErrorResource.getField(), new ArrayList<>());
      }
      ((List) json.get(fieldErrorResource.getField())).add(fieldErrorResource.getMessage());
    }
    return json;
  }
}

package io.spring.graphql.exception;

public class AuthenticationException extends RuntimeException {}

package io.spring.api;

import io.spring.application.TagsQueryService;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "tags")
@AllArgsConstructor
public class TagsApi {
  private TagsQueryService tagsQueryService;

  @GetMapping
  public ResponseEntity getTags() {
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("tags", tagsQueryService.allTags());
          }
        });
  }
}

package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.application.data.ArticleData;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import io.spring.core.user.User;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "articles/{slug}/favorite")
@AllArgsConstructor
public class ArticleFavoriteApi {
  private ArticleFavoriteRepository articleFavoriteRepository;
  private ArticleRepository articleRepository;
  private ArticleQueryService articleQueryService;

  @PostMapping
  public ResponseEntity favoriteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavorite articleFavorite = new ArticleFavorite(article.getId(), user.getId());
    articleFavoriteRepository.save(articleFavorite);
    return responseArticleData(articleQueryService.findBySlug(slug, user).get());
  }

  @DeleteMapping
  public ResponseEntity unfavoriteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    articleFavoriteRepository
        .find(article.getId(), user.getId())
        .ifPresent(
            favorite -> {
              articleFavoriteRepository.remove(favorite);
            });
    return responseArticleData(articleQueryService.findBySlug(slug, user).get());
  }

  private ResponseEntity<HashMap<String, Object>> responseArticleData(
      final ArticleData articleData) {
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("article", articleData);
          }
        });
  }
}

package io.spring.api;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.CommentQueryService;
import io.spring.application.data.CommentData;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles/{slug}/comments")
@AllArgsConstructor
public class CommentsApi {
  private ArticleRepository articleRepository;
  private CommentRepository commentRepository;
  private CommentQueryService commentQueryService;

  @PostMapping
  public ResponseEntity<?> createComment(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody NewCommentParam newCommentParam) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Comment comment = new Comment(newCommentParam.getBody(), user.getId(), article.getId());
    commentRepository.save(comment);
    return ResponseEntity.status(201)
        .body(commentResponse(commentQueryService.findById(comment.getId(), user).get()));
  }

  @GetMapping
  public ResponseEntity getComments(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    List<CommentData> comments = commentQueryService.findByArticleId(article.getId(), user);
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("comments", comments);
          }
        });
  }

  @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
  public ResponseEntity deleteComment(
      @PathVariable("slug") String slug,
      @PathVariable("id") String commentId,
      @AuthenticationPrincipal User user) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return commentRepository
        .findById(article.getId(), commentId)
        .map(
            comment -> {
              if (!AuthorizationService.canWriteComment(user, article, comment)) {
                throw new NoAuthorizationException();
              }
              commentRepository.remove(comment);
              return ResponseEntity.noContent().build();
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  private Map<String, Object> commentResponse(CommentData commentData) {
    return new HashMap<String, Object>() {
      {
        put("comment", commentData);
      }
    };
  }
}

@Getter
@NoArgsConstructor
@JsonRootName("comment")
class NewCommentParam {
  @NotBlank(message = "can't be empty")
  private String body;
}

package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ProfileQueryService;
import io.spring.application.data.ProfileData;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import java.util.HashMap;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "profiles/{username}")
@AllArgsConstructor
public class ProfileApi {
  private ProfileQueryService profileQueryService;
  private UserRepository userRepository;

  @GetMapping
  public ResponseEntity getProfile(
      @PathVariable("username") String username, @AuthenticationPrincipal User user) {
    return profileQueryService
        .findByUsername(username, user)
        .map(this::profileResponse)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @PostMapping(path = "follow")
  public ResponseEntity follow(
      @PathVariable("username") String username, @AuthenticationPrincipal User user) {
    return userRepository
        .findByUsername(username)
        .map(
            target -> {
              FollowRelation followRelation = new FollowRelation(user.getId(), target.getId());
              userRepository.saveRelation(followRelation);
              return profileResponse(profileQueryService.findByUsername(username, user).get());
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @DeleteMapping(path = "follow")
  public ResponseEntity unfollow(
      @PathVariable("username") String username, @AuthenticationPrincipal User user) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      User target = userOptional.get();
      return userRepository
          .findRelation(user.getId(), target.getId())
          .map(
              relation -> {
                userRepository.removeRelation(relation);
                return profileResponse(profileQueryService.findByUsername(username, user).get());
              })
          .orElseThrow(ResourceNotFoundException::new);
    } else {
      throw new ResourceNotFoundException();
    }
  }

  private ResponseEntity profileResponse(ProfileData profile) {
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("profile", profile);
          }
        });
  }
}

package io.spring.api;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.application.UserQueryService;
import io.spring.application.data.UserData;
import io.spring.application.data.UserWithToken;
import io.spring.application.user.RegisterParam;
import io.spring.application.user.UserService;
import io.spring.core.service.JwtService;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UsersApi {
  private UserRepository userRepository;
  private UserQueryService userQueryService;
  private PasswordEncoder passwordEncoder;
  private JwtService jwtService;
  private UserService userService;

  @RequestMapping(path = "/users", method = POST)
  public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam) {
    User user = userService.createUser(registerParam);
    UserData userData = userQueryService.findById(user.getId()).get();
    return ResponseEntity.status(201)
        .body(userResponse(new UserWithToken(userData, jwtService.toToken(user))));
  }

  @RequestMapping(path = "/users/login", method = POST)
  public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam) {
    Optional<User> optional = userRepository.findByEmail(loginParam.getEmail());
    if (optional.isPresent()
        && passwordEncoder.matches(loginParam.getPassword(), optional.get().getPassword())) {
      UserData userData = userQueryService.findById(optional.get().getId()).get();
      return ResponseEntity.ok(
          userResponse(new UserWithToken(userData, jwtService.toToken(optional.get()))));
    } else {
      throw new InvalidAuthenticationException();
    }
  }

  private Map<String, Object> userResponse(UserWithToken userWithToken) {
    return new HashMap<String, Object>() {
      {
        put("user", userWithToken);
      }
    };
  }
}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class LoginParam {
  @NotBlank(message = "can't be empty")
  @Email(message = "should be an email")
  private String email;

  @NotBlank(message = "can't be empty")
  private String password;
}

package io.spring.api;

import io.spring.application.ArticleQueryService;
import io.spring.application.Page;
import io.spring.application.article.ArticleCommandService;
import io.spring.application.article.NewArticleParam;
import io.spring.core.article.Article;
import io.spring.core.user.User;
import java.util.HashMap;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles")
@AllArgsConstructor
public class ArticlesApi {
  private ArticleCommandService articleCommandService;
  private ArticleQueryService articleQueryService;

  @PostMapping
  public ResponseEntity createArticle(
      @Valid @RequestBody NewArticleParam newArticleParam, @AuthenticationPrincipal User user) {
    Article article = articleCommandService.createArticle(newArticleParam, user);
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("article", articleQueryService.findById(article.getId(), user).get());
          }
        });
  }

  @GetMapping(path = "feed")
  public ResponseEntity getFeed(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "20") int limit,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(articleQueryService.findUserFeed(user, new Page(offset, limit)));
  }

  @GetMapping
  public ResponseEntity getArticles(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "20") int limit,
      @RequestParam(value = "tag", required = false) String tag,
      @RequestParam(value = "favorited", required = false) String favoritedBy,
      @RequestParam(value = "author", required = false) String author,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(
        articleQueryService.findRecentArticles(
            tag, author, favoritedBy, new Page(offset, limit), user));
  }
}

package io.spring.api;

import io.spring.application.UserQueryService;
import io.spring.application.data.UserData;
import io.spring.application.data.UserWithToken;
import io.spring.application.user.UpdateUserCommand;
import io.spring.application.user.UpdateUserParam;
import io.spring.application.user.UserService;
import io.spring.core.user.User;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class CurrentUserApi {

  private UserQueryService userQueryService;
  private UserService userService;

  @GetMapping
  public ResponseEntity currentUser(
      @AuthenticationPrincipal User currentUser,
      @RequestHeader(value = "Authorization") String authorization) {
    UserData userData = userQueryService.findById(currentUser.getId()).get();
    return ResponseEntity.ok(
        userResponse(new UserWithToken(userData, authorization.split(" ")[1])));
  }

  @PutMapping
  public ResponseEntity updateProfile(
      @AuthenticationPrincipal User currentUser,
      @RequestHeader("Authorization") String token,
      @Valid @RequestBody UpdateUserParam updateUserParam) {

    userService.updateUser(new UpdateUserCommand(currentUser, updateUserParam));
    UserData userData = userQueryService.findById(currentUser.getId()).get();
    return ResponseEntity.ok(userResponse(new UserWithToken(userData, token.split(" ")[1])));
  }

  private Map<String, Object> userResponse(UserWithToken userWithToken) {
    return new HashMap<String, Object>() {
      {
        put("user", userWithToken);
      }
    };
  }
}

package io.spring.api;

import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.application.article.ArticleCommandService;
import io.spring.application.article.UpdateArticleParam;
import io.spring.application.data.ArticleData;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles/{slug}")
@AllArgsConstructor
public class ArticleApi {
  private ArticleQueryService articleQueryService;
  private ArticleRepository articleRepository;
  private ArticleCommandService articleCommandService;

  @GetMapping
  public ResponseEntity<?> article(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
    return articleQueryService
        .findBySlug(slug, user)
        .map(articleData -> ResponseEntity.ok(articleResponse(articleData)))
        .orElseThrow(ResourceNotFoundException::new);
  }

  @PutMapping
  public ResponseEntity<?> updateArticle(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody UpdateArticleParam updateArticleParam) {
    return articleRepository
        .findBySlug(slug)
        .map(
            article -> {
              if (!AuthorizationService.canWriteArticle(user, article)) {
                throw new NoAuthorizationException();
              }
              Article updatedArticle =
                  articleCommandService.updateArticle(article, updateArticleParam);
              return ResponseEntity.ok(
                  articleResponse(
                      articleQueryService.findBySlug(updatedArticle.getSlug(), user).get()));
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @DeleteMapping
  public ResponseEntity deleteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
    return articleRepository
        .findBySlug(slug)
        .map(
            article -> {
              if (!AuthorizationService.canWriteArticle(user, article)) {
                throw new NoAuthorizationException();
              }
              articleRepository.remove(article);
              return ResponseEntity.noContent().build();
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  private Map<String, Object> articleResponse(ArticleData articleData) {
    return new HashMap<String, Object>() {
      {
        put("article", articleData);
      }
    };
  }
}

package io.spring.api.security;

import static java.util.Arrays.asList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public JwtTokenFilter jwtTokenFilter() {
    return new JwtTokenFilter();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf()
        .disable()
        .cors()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .antMatchers("/graphiql")
        .permitAll()
        .antMatchers("/graphql")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/articles/feed")
        .authenticated()
        .antMatchers(HttpMethod.POST, "/users", "/users/login")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/articles/**", "/profiles/**", "/tags")
        .permitAll()
        .anyRequest()
        .authenticated();

    http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(asList("*"));
    configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    // setAllowCredentials(true) is important, otherwise:
    // The value of the 'Access-Control-Allow-Origin' header in the response must not be the
    // wildcard '*' when the request's credentials mode is 'include'.
    configuration.setAllowCredentials(false);
    // setAllowedHeaders is important! Without it, OPTIONS preflight request
    // will fail with 403 Invalid CORS request
    configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}

package io.spring.api.security;

import io.spring.core.service.JwtService;
import io.spring.core.user.UserRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class JwtTokenFilter extends OncePerRequestFilter {
  @Autowired private UserRepository userRepository;
  @Autowired private JwtService jwtService;
  private final String header = "Authorization";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    getTokenString(request.getHeader(header))
        .flatMap(token -> jwtService.getSubFromToken(token))
        .ifPresent(
            id -> {
              if (SecurityContextHolder.getContext().getAuthentication() == null) {
                userRepository
                    .findById(id)
                    .ifPresent(
                        user -> {
                          UsernamePasswordAuthenticationToken authenticationToken =
                              new UsernamePasswordAuthenticationToken(
                                  user, null, Collections.emptyList());
                          authenticationToken.setDetails(
                              new WebAuthenticationDetailsSource().buildDetails(request));
                          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        });
              }
            });

    filterChain.doFilter(request, response);
  }

  private Optional<String> getTokenString(String header) {
    if (header == null) {
      return Optional.empty();
    } else {
      String[] split = header.split(" ");
      if (split.length < 2) {
        return Optional.empty();
      } else {
        return Optional.ofNullable(split[1]);
      }
    }
  }
}

package io.spring.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoAuthorizationException extends RuntimeException {}

package io.spring.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {}

package io.spring.api.exception;

public class InvalidAuthenticationException extends RuntimeException {

  public InvalidAuthenticationException() {
    super("invalid email or password");
  }
}

package io.spring.api.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomizeExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({InvalidRequestException.class})
  public ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
    InvalidRequestException ire = (InvalidRequestException) e;

    List<FieldErrorResource> errorResources =
        ire.getErrors().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldErrorResource(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage()))
            .collect(Collectors.toList());

    ErrorResource error = new ErrorResource(errorResources);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return handleExceptionInternal(e, error, headers, UNPROCESSABLE_ENTITY, request);
  }

  @ExceptionHandler(InvalidAuthenticationException.class)
  public ResponseEntity<Object> handleInvalidAuthentication(
      InvalidAuthenticationException e, WebRequest request) {
    return ResponseEntity.status(UNPROCESSABLE_ENTITY)
        .body(
            new HashMap<String, Object>() {
              {
                put("message", e.getMessage());
              }
            });
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<FieldErrorResource> errorResources =
        e.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldErrorResource(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage()))
            .collect(Collectors.toList());

    return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(new ErrorResource(errorResources));
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ResponseBody
  public ErrorResource handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    List<FieldErrorResource> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      FieldErrorResource fieldErrorResource =
          new FieldErrorResource(
              violation.getRootBeanClass().getName(),
              getParam(violation.getPropertyPath().toString()),
              violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
              violation.getMessage());
      errors.add(fieldErrorResource);
    }

    return new ErrorResource(errors);
  }

  private String getParam(String s) {
    String[] splits = s.split("\\.");
    if (splits.length == 1) {
      return s;
    } else {
      return String.join(".", Arrays.copyOfRange(splits, 2, splits.length));
    }
  }
}

package io.spring.api.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class FieldErrorResource {
  private String resource;
  private String field;
  private String code;
  private String message;
}

package io.spring.api.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorResourceSerializer extends JsonSerializer<ErrorResource> {
  @Override
  public void serialize(ErrorResource value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException, JsonProcessingException {
    Map<String, List<String>> json = new HashMap<>();
    gen.writeStartObject();
    gen.writeObjectFieldStart("errors");
    for (FieldErrorResource fieldErrorResource : value.getFieldErrors()) {
      if (!json.containsKey(fieldErrorResource.getField())) {
        json.put(fieldErrorResource.getField(), new ArrayList<String>());
      }
      json.get(fieldErrorResource.getField()).add(fieldErrorResource.getMessage());
    }
    for (Map.Entry<String, List<String>> pair : json.entrySet()) {
      gen.writeArrayFieldStart(pair.getKey());
      pair.getValue()
          .forEach(
              content -> {
                try {
                  gen.writeString(content);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
      gen.writeEndArray();
    }
    gen.writeEndObject();
    gen.writeEndObject();
  }
}

package io.spring.api.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize(using = ErrorResourceSerializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Getter
@JsonRootName("errors")
public class ErrorResource {
  private List<FieldErrorResource> fieldErrors;

  public ErrorResource(List<FieldErrorResource> fieldErrorResources) {
    this.fieldErrors = fieldErrorResources;
  }
}

package io.spring.api.exception;

import org.springframework.validation.Errors;

@SuppressWarnings("serial")
public class InvalidRequestException extends RuntimeException {
  private final Errors errors;

  public InvalidRequestException(Errors errors) {
    super("");
    this.errors = errors;
  }

  public Errors getErrors() {
    return errors;
  }
}

package io.spring.application;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Page {
  private static final int MAX_LIMIT = 100;
  private int offset = 0;
  private int limit = 20;

  public Page(int offset, int limit) {
    setOffset(offset);
    setLimit(limit);
  }

  private void setOffset(int offset) {
    if (offset > 0) {
      this.offset = offset;
    }
  }

  private void setLimit(int limit) {
    if (limit > MAX_LIMIT) {
      this.limit = MAX_LIMIT;
    } else if (limit > 0) {
      this.limit = limit;
    }
  }
}

package io.spring.application;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimeCursor extends PageCursor<DateTime> {

  public DateTimeCursor(DateTime data) {
    super(data);
  }

  @Override
  public String toString() {
    return String.valueOf(getData().getMillis());
  }

  public static DateTime parse(String cursor) {
    if (cursor == null) {
      return null;
    }
    return new DateTime().withMillis(Long.parseLong(cursor)).withZone(DateTimeZone.UTC);
  }
}

package io.spring.application;

public interface Node {
  PageCursor getCursor();
}

package io.spring.application;

import io.spring.application.CursorPager.Direction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CursorPageParameter<T> {
  private static final int MAX_LIMIT = 1000;
  private int limit = 20;
  private T cursor;
  private Direction direction;

  public CursorPageParameter(T cursor, int limit, Direction direction) {
    setLimit(limit);
    setCursor(cursor);
    setDirection(direction);
  }

  public boolean isNext() {
    return direction == Direction.NEXT;
  }

  public int getQueryLimit() {
    return limit + 1;
  }

  private void setCursor(T cursor) {
    this.cursor = cursor;
  }

  private void setLimit(int limit) {
    if (limit > MAX_LIMIT) {
      this.limit = MAX_LIMIT;
    } else if (limit > 0) {
      this.limit = limit;
    }
  }
}

package io.spring.application;

import static java.util.stream.Collectors.toList;

import io.spring.application.data.ArticleData;
import io.spring.application.data.ArticleDataList;
import io.spring.application.data.ArticleFavoriteCount;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.ArticleFavoritesReadService;
import io.spring.infrastructure.mybatis.readservice.ArticleReadService;
import io.spring.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleQueryService {
  private ArticleReadService articleReadService;
  private UserRelationshipQueryService userRelationshipQueryService;
  private ArticleFavoritesReadService articleFavoritesReadService;

  public Optional<ArticleData> findById(String id, User user) {
    ArticleData articleData = articleReadService.findById(id);
    if (articleData == null) {
      return Optional.empty();
    } else {
      if (user != null) {
        fillExtraInfo(id, user, articleData);
      }
      return Optional.of(articleData);
    }
  }

  public Optional<ArticleData> findBySlug(String slug, User user) {
    ArticleData articleData = articleReadService.findBySlug(slug);
    if (articleData == null) {
      return Optional.empty();
    } else {
      if (user != null) {
        fillExtraInfo(articleData.getId(), user, articleData);
      }
      return Optional.of(articleData);
    }
  }

  public CursorPager<ArticleData> findRecentArticlesWithCursor(
      String tag,
      String author,
      String favoritedBy,
      CursorPageParameter<DateTime> page,
      User currentUser) {
    List<String> articleIds =
        articleReadService.findArticlesWithCursor(tag, author, favoritedBy, page);
    if (articleIds.size() == 0) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    } else {
      boolean hasExtra = articleIds.size() > page.getLimit();
      if (hasExtra) {
        articleIds.remove(page.getLimit());
      }
      if (!page.isNext()) {
        Collections.reverse(articleIds);
      }

      List<ArticleData> articles = articleReadService.findArticles(articleIds);
      fillExtraInfo(articles, currentUser);

      return new CursorPager<>(articles, page.getDirection(), hasExtra);
    }
  }

  public CursorPager<ArticleData> findUserFeedWithCursor(
      User user, CursorPageParameter<DateTime> page) {
    List<String> followdUsers = userRelationshipQueryService.followedUsers(user.getId());
    if (followdUsers.size() == 0) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    } else {
      List<ArticleData> articles =
          articleReadService.findArticlesOfAuthorsWithCursor(followdUsers, page);
      boolean hasExtra = articles.size() > page.getLimit();
      if (hasExtra) {
        articles.remove(page.getLimit());
      }
      if (!page.isNext()) {
        Collections.reverse(articles);
      }
      fillExtraInfo(articles, user);
      return new CursorPager<>(articles, page.getDirection(), hasExtra);
    }
  }

  public ArticleDataList findRecentArticles(
      String tag, String author, String favoritedBy, Page page, User currentUser) {
    List<String> articleIds = articleReadService.queryArticles(tag, author, favoritedBy, page);
    int articleCount = articleReadService.countArticle(tag, author, favoritedBy);
    if (articleIds.size() == 0) {
      return new ArticleDataList(new ArrayList<>(), articleCount);
    } else {
      List<ArticleData> articles = articleReadService.findArticles(articleIds);
      fillExtraInfo(articles, currentUser);
      return new ArticleDataList(articles, articleCount);
    }
  }

  public ArticleDataList findUserFeed(User user, Page page) {
    List<String> followdUsers = userRelationshipQueryService.followedUsers(user.getId());
    if (followdUsers.size() == 0) {
      return new ArticleDataList(new ArrayList<>(), 0);
    } else {
      List<ArticleData> articles = articleReadService.findArticlesOfAuthors(followdUsers, page);
      fillExtraInfo(articles, user);
      int count = articleReadService.countFeedSize(followdUsers);
      return new ArticleDataList(articles, count);
    }
  }

  private void fillExtraInfo(List<ArticleData> articles, User currentUser) {
    setFavoriteCount(articles);
    if (currentUser != null) {
      setIsFavorite(articles, currentUser);
      setIsFollowingAuthor(articles, currentUser);
    }
  }

  private void setIsFollowingAuthor(List<ArticleData> articles, User currentUser) {
    Set<String> followingAuthors =
        userRelationshipQueryService.followingAuthors(
            currentUser.getId(),
            articles.stream()
                .map(articleData1 -> articleData1.getProfileData().getId())
                .collect(toList()));
    articles.forEach(
        articleData -> {
          if (followingAuthors.contains(articleData.getProfileData().getId())) {
            articleData.getProfileData().setFollowing(true);
          }
        });
  }

  private void setFavoriteCount(List<ArticleData> articles) {
    List<ArticleFavoriteCount> favoritesCounts =
        articleFavoritesReadService.articlesFavoriteCount(
            articles.stream().map(ArticleData::getId).collect(toList()));
    Map<String, Integer> countMap = new HashMap<>();
    favoritesCounts.forEach(
        item -> {
          countMap.put(item.getId(), item.getCount());
        });
    articles.forEach(
        articleData -> articleData.setFavoritesCount(countMap.get(articleData.getId())));
  }

  private void setIsFavorite(List<ArticleData> articles, User currentUser) {
    Set<String> favoritedArticles =
        articleFavoritesReadService.userFavorites(
            articles.stream().map(articleData -> articleData.getId()).collect(toList()),
            currentUser);

    articles.forEach(
        articleData -> {
          if (favoritedArticles.contains(articleData.getId())) {
            articleData.setFavorited(true);
          }
        });
  }

  private void fillExtraInfo(String id, User user, ArticleData articleData) {
    articleData.setFavorited(articleFavoritesReadService.isUserFavorite(user.getId(), id));
    articleData.setFavoritesCount(articleFavoritesReadService.articleFavoriteCount(id));
    articleData
        .getProfileData()
        .setFollowing(
            userRelationshipQueryService.isUserFollowing(
                user.getId(), articleData.getProfileData().getId()));
  }
}

package io.spring.application;

import io.spring.application.data.ProfileData;
import io.spring.application.data.UserData;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.UserReadService;
import io.spring.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProfileQueryService {
  private UserReadService userReadService;
  private UserRelationshipQueryService userRelationshipQueryService;

  public Optional<ProfileData> findByUsername(String username, User currentUser) {
    UserData userData = userReadService.findByUsername(username);
    if (userData == null) {
      return Optional.empty();
    } else {
      ProfileData profileData =
          new ProfileData(
              userData.getId(),
              userData.getUsername(),
              userData.getBio(),
              userData.getImage(),
              currentUser != null
                  && userRelationshipQueryService.isUserFollowing(
                      currentUser.getId(), userData.getId()));
      return Optional.of(profileData);
    }
  }
}

package io.spring.application;

import io.spring.infrastructure.mybatis.readservice.TagReadService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagsQueryService {
  private TagReadService tagReadService;

  public List<String> allTags() {
    return tagReadService.all();
  }
}

package io.spring.application;

import java.util.List;
import lombok.Getter;

@Getter
public class CursorPager<T extends Node> {
  private List<T> data;
  private boolean next;
  private boolean previous;

  public CursorPager(List<T> data, Direction direction, boolean hasExtra) {
    this.data = data;

    if (direction == Direction.NEXT) {
      this.previous = false;
      this.next = hasExtra;
    } else {
      this.next = false;
      this.previous = hasExtra;
    }
  }

  public boolean hasNext() {
    return next;
  }

  public boolean hasPrevious() {
    return previous;
  }

  public PageCursor getStartCursor() {
    return data.isEmpty() ? null : data.get(0).getCursor();
  }

  public PageCursor getEndCursor() {
    return data.isEmpty() ? null : data.get(data.size() - 1).getCursor();
  }

  public enum Direction {
    PREV,
    NEXT
  }
}

package io.spring.application;

public abstract class PageCursor<T> {
  private T data;

  public PageCursor(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  @Override
  public String toString() {
    return data.toString();
  }
}

package io.spring.application;

import io.spring.application.data.CommentData;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.CommentReadService;
import io.spring.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentQueryService {
  private CommentReadService commentReadService;
  private UserRelationshipQueryService userRelationshipQueryService;

  public Optional<CommentData> findById(String id, User user) {
    CommentData commentData = commentReadService.findById(id);
    if (commentData == null) {
      return Optional.empty();
    } else {
      commentData
          .getProfileData()
          .setFollowing(
              userRelationshipQueryService.isUserFollowing(
                  user.getId(), commentData.getProfileData().getId()));
    }
    return Optional.ofNullable(commentData);
  }

  public List<CommentData> findByArticleId(String articleId, User user) {
    List<CommentData> comments = commentReadService.findByArticleId(articleId);
    if (comments.size() > 0 && user != null) {
      Set<String> followingAuthors =
          userRelationshipQueryService.followingAuthors(
              user.getId(),
              comments.stream()
                  .map(commentData -> commentData.getProfileData().getId())
                  .collect(Collectors.toList()));
      comments.forEach(
          commentData -> {
            if (followingAuthors.contains(commentData.getProfileData().getId())) {
              commentData.getProfileData().setFollowing(true);
            }
          });
    }
    return comments;
  }

  public CursorPager<CommentData> findByArticleIdWithCursor(
      String articleId, User user, CursorPageParameter<DateTime> page) {
    List<CommentData> comments = commentReadService.findByArticleIdWithCursor(articleId, page);
    if (comments.isEmpty()) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    }
    if (user != null) {
      Set<String> followingAuthors =
          userRelationshipQueryService.followingAuthors(
              user.getId(),
              comments.stream()
                  .map(commentData -> commentData.getProfileData().getId())
                  .collect(Collectors.toList()));
      comments.forEach(
          commentData -> {
            if (followingAuthors.contains(commentData.getProfileData().getId())) {
              commentData.getProfileData().setFollowing(true);
            }
          });
    }
    boolean hasExtra = comments.size() > page.getLimit();
    if (hasExtra) {
      comments.remove(page.getLimit());
    }
    if (!page.isNext()) {
      Collections.reverse(comments);
    }
    return new CursorPager<>(comments, page.getDirection(), hasExtra);
  }
}

package io.spring.application;

import io.spring.application.data.UserData;
import io.spring.infrastructure.mybatis.readservice.UserReadService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserQueryService {
  private UserReadService userReadService;

  public Optional<UserData> findById(String id) {
    return Optional.ofNullable(userReadService.findById(id));
  }
}

package io.spring.application.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName("user")
@AllArgsConstructor
@NoArgsConstructor
public class RegisterParam {
  @NotBlank(message = "can't be empty")
  @Email(message = "should be an email")
  @DuplicatedEmailConstraint
  private String email;

  @NotBlank(message = "can't be empty")
  @DuplicatedUsernameConstraint
  private String username;

  @NotBlank(message = "can't be empty")
  private String password;
}

package io.spring.application.user;

import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserService {
  private UserRepository userRepository;
  private String defaultImage;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(
      UserRepository userRepository,
      @Value("${image.default}") String defaultImage,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.defaultImage = defaultImage;
    this.passwordEncoder = passwordEncoder;
  }

  public User createUser(@Valid RegisterParam registerParam) {
    User user =
        new User(
            registerParam.getEmail(),
            registerParam.getUsername(),
            passwordEncoder.encode(registerParam.getPassword()),
            "",
            defaultImage);
    userRepository.save(user);
    return user;
  }

  public void updateUser(@Valid UpdateUserCommand command) {
    User user = command.getTargetUser();
    UpdateUserParam updateUserParam = command.getParam();
    user.update(
        updateUserParam.getEmail(),
        updateUserParam.getUsername(),
        updateUserParam.getPassword(),
        updateUserParam.getBio(),
        updateUserParam.getImage());
    userRepository.save(user);
  }
}

@Constraint(validatedBy = UpdateUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@interface UpdateUserConstraint {

  String message() default "invalid update param";

  Class[] groups() default {};

  Class[] payload() default {};
}

class UpdateUserValidator implements ConstraintValidator<UpdateUserConstraint, UpdateUserCommand> {

  @Autowired private UserRepository userRepository;

  @Override
  public boolean isValid(UpdateUserCommand value, ConstraintValidatorContext context) {
    String inputEmail = value.getParam().getEmail();
    String inputUsername = value.getParam().getUsername();
    final User targetUser = value.getTargetUser();

    boolean isEmailValid =
        userRepository.findByEmail(inputEmail).map(user -> user.equals(targetUser)).orElse(true);
    boolean isUsernameValid =
        userRepository
            .findByUsername(inputUsername)
            .map(user -> user.equals(targetUser))
            .orElse(true);
    if (isEmailValid && isUsernameValid) {
      return true;
    } else {
      context.disableDefaultConstraintViolation();
      if (!isEmailValid) {
        context
            .buildConstraintViolationWithTemplate("email already exist")
            .addPropertyNode("email")
            .addConstraintViolation();
      }
      if (!isUsernameValid) {
        context
            .buildConstraintViolationWithTemplate("username already exist")
            .addPropertyNode("username")
            .addConstraintViolation();
      }
      return false;
    }
  }
}

package io.spring.application.user;

import io.spring.core.user.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class DuplicatedEmailValidator
    implements ConstraintValidator<DuplicatedEmailConstraint, String> {

  @Autowired private UserRepository userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (value == null || value.isEmpty()) || !userRepository.findByEmail(value).isPresent();
  }
}

package io.spring.application.user;

import io.spring.core.user.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

class DuplicatedUsernameValidator
    implements ConstraintValidator<DuplicatedUsernameConstraint, String> {

  @Autowired private UserRepository userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (value == null || value.isEmpty()) || !userRepository.findByUsername(value).isPresent();
  }
}

package io.spring.application.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName("user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserParam {

  @Builder.Default
  @Email(message = "should be an email")
  private String email = "";

  @Builder.Default private String password = "";
  @Builder.Default private String username = "";
  @Builder.Default private String bio = "";
  @Builder.Default private String image = "";
}

package io.spring.application.user;

import io.spring.core.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@UpdateUserConstraint
public class UpdateUserCommand {

  private User targetUser;
  private UpdateUserParam param;
}

package io.spring.application.user;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DuplicatedEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedEmailConstraint {
  String message() default "duplicated email";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

package io.spring.application.user;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DuplicatedUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@interface DuplicatedUsernameConstraint {
  String message() default "duplicated username";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

package io.spring.application.article;

import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.user.User;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class ArticleCommandService {

  private ArticleRepository articleRepository;

  public Article createArticle(@Valid NewArticleParam newArticleParam, User creator) {
    Article article =
        new Article(
            newArticleParam.getTitle(),
            newArticleParam.getDescription(),
            newArticleParam.getBody(),
            newArticleParam.getTagList(),
            creator.getId());
    articleRepository.save(article);
    return article;
  }

  public Article updateArticle(Article article, @Valid UpdateArticleParam updateArticleParam) {
    article.update(
        updateArticleParam.getTitle(),
        updateArticleParam.getDescription(),
        updateArticleParam.getBody());
    articleRepository.save(article);
    return article;
  }
}

package io.spring.application.article;

import io.spring.application.ArticleQueryService;
import io.spring.core.article.Article;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

class DuplicatedArticleValidator
    implements ConstraintValidator<DuplicatedArticleConstraint, String> {

  @Autowired private ArticleQueryService articleQueryService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !articleQueryService.findBySlug(Article.toSlug(value), null).isPresent();
  }
}

package io.spring.application.article;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("article")
public class UpdateArticleParam {
  private String title = "";
  private String body = "";
  private String description = "";
}

package io.spring.application.article;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName("article")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewArticleParam {
  @NotBlank(message = "can't be empty")
  @DuplicatedArticleConstraint
  private String title;

  @NotBlank(message = "can't be empty")
  private String description;

  @NotBlank(message = "can't be empty")
  private String body;

  private List<String> tagList;
}

package io.spring.application.article;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DuplicatedArticleValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedArticleConstraint {
  String message() default "article name exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileData {
  @JsonIgnore private String id;
  private String username;
  private String bio;
  private String image;
  private boolean following;
}

package io.spring.application.data;

import lombok.Value;

@Value
public class ArticleFavoriteCount {
  private String id;
  private Integer count;
}

package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.application.DateTimeCursor;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData implements io.spring.application.Node {
  private String id;
  private String slug;
  private String title;
  private String description;
  private String body;
  private boolean favorited;
  private int favoritesCount;
  private DateTime createdAt;
  private DateTime updatedAt;
  private List<String> tagList;

  @JsonProperty("author")
  private ProfileData profileData;

  @Override
  public DateTimeCursor getCursor() {
    return new DateTimeCursor(updatedAt);
  }
}

package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.application.DateTimeCursor;
import io.spring.application.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentData implements Node {
  private String id;
  private String body;
  @JsonIgnore private String articleId;
  private DateTime createdAt;
  private DateTime updatedAt;

  @JsonProperty("author")
  private ProfileData profileData;

  @Override
  public DateTimeCursor getCursor() {
    return new DateTimeCursor(createdAt);
  }
}

package io.spring.application.data;

import lombok.Getter;

@Getter
public class UserWithToken {
  private String email;
  private String username;
  private String bio;
  private String image;
  private String token;

  public UserWithToken(UserData userData, String token) {
    this.email = userData.getEmail();
    this.username = userData.getUsername();
    this.bio = userData.getBio();
    this.image = userData.getImage();
    this.token = token;
  }
}

package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class ArticleDataList {
  @JsonProperty("articles")
  private final List<ArticleData> articleDatas;

  @JsonProperty("articlesCount")
  private final int count;

  public ArticleDataList(List<ArticleData> articleDatas, int count) {

    this.articleDatas = articleDatas;
    this.count = count;
  }
}

package io.spring.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
  private String id;
  private String email;
  private String username;
  private String bio;
  private String image;
}

package io.spring.infrastructure.repository;

import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.infrastructure.mybatis.mapper.UserMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisUserRepository implements UserRepository {
  private final UserMapper userMapper;

  @Autowired
  public MyBatisUserRepository(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public void save(User user) {
    if (userMapper.findById(user.getId()) == null) {
      userMapper.insert(user);
    } else {
      userMapper.update(user);
    }
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.ofNullable(userMapper.findById(id));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(userMapper.findByUsername(username));
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(userMapper.findByEmail(email));
  }

  @Override
  public void saveRelation(FollowRelation followRelation) {
    if (!findRelation(followRelation.getUserId(), followRelation.getTargetId()).isPresent()) {
      userMapper.saveRelation(followRelation);
    }
  }

  @Override
  public Optional<FollowRelation> findRelation(String userId, String targetId) {
    return Optional.ofNullable(userMapper.findRelation(userId, targetId));
  }

  @Override
  public void removeRelation(FollowRelation followRelation) {
    userMapper.deleteRelation(followRelation);
  }
}

package io.spring.infrastructure.repository;

import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import io.spring.infrastructure.mybatis.mapper.ArticleFavoriteMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisArticleFavoriteRepository implements ArticleFavoriteRepository {
  private ArticleFavoriteMapper mapper;

  @Autowired
  public MyBatisArticleFavoriteRepository(ArticleFavoriteMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void save(ArticleFavorite articleFavorite) {
    if (mapper.find(articleFavorite.getArticleId(), articleFavorite.getUserId()) == null) {
      mapper.insert(articleFavorite);
    }
  }

  @Override
  public Optional<ArticleFavorite> find(String articleId, String userId) {
    return Optional.ofNullable(mapper.find(articleId, userId));
  }

  @Override
  public void remove(ArticleFavorite favorite) {
    mapper.delete(favorite);
  }
}

package io.spring.infrastructure.repository;

import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.infrastructure.mybatis.mapper.CommentMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyBatisCommentRepository implements CommentRepository {
  private CommentMapper commentMapper;

  @Autowired
  public MyBatisCommentRepository(CommentMapper commentMapper) {
    this.commentMapper = commentMapper;
  }

  @Override
  public void save(Comment comment) {
    commentMapper.insert(comment);
  }

  @Override
  public Optional<Comment> findById(String articleId, String id) {
    return Optional.ofNullable(commentMapper.findById(articleId, id));
  }

  @Override
  public void remove(Comment comment) {
    commentMapper.delete(comment.getId());
  }
}

package io.spring.infrastructure.repository;

import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import io.spring.infrastructure.mybatis.mapper.ArticleMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MyBatisArticleRepository implements ArticleRepository {
  private ArticleMapper articleMapper;

  public MyBatisArticleRepository(ArticleMapper articleMapper) {
    this.articleMapper = articleMapper;
  }

  @Override
  @Transactional
  public void save(Article article) {
    if (articleMapper.findById(article.getId()) == null) {
      createNew(article);
    } else {
      articleMapper.update(article);
    }
  }

  private void createNew(Article article) {
    for (Tag tag : article.getTags()) {
      Tag targetTag =
          Optional.ofNullable(articleMapper.findTag(tag.getName()))
              .orElseGet(
                  () -> {
                    articleMapper.insertTag(tag);
                    return tag;
                  });
      articleMapper.insertArticleTagRelation(article.getId(), targetTag.getId());
    }
    articleMapper.insert(article);
  }

  @Override
  public Optional<Article> findById(String id) {
    return Optional.ofNullable(articleMapper.findById(id));
  }

  @Override
  public Optional<Article> findBySlug(String slug) {
    return Optional.ofNullable(articleMapper.findBySlug(slug));
  }

  @Override
  public void remove(Article article) {
    articleMapper.delete(article.getId());
  }
}

package io.spring.infrastructure.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.DateTime;

@MappedTypes(DateTime.class)
public class DateTimeHandler implements TypeHandler<DateTime> {

  private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

  @Override
  public void setParameter(PreparedStatement ps, int i, DateTime parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setTimestamp(
        i, parameter != null ? new Timestamp(parameter.getMillis()) : null, UTC_CALENDAR);
  }

  @Override
  public DateTime getResult(ResultSet rs, String columnName) throws SQLException {
    Timestamp timestamp = rs.getTimestamp(columnName, UTC_CALENDAR);
    return timestamp != null ? new DateTime(timestamp.getTime()) : null;
  }

  @Override
  public DateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
    Timestamp timestamp = rs.getTimestamp(columnIndex, UTC_CALENDAR);
    return timestamp != null ? new DateTime(timestamp.getTime()) : null;
  }

  @Override
  public DateTime getResult(CallableStatement cs, int columnIndex) throws SQLException {
    Timestamp ts = cs.getTimestamp(columnIndex, UTC_CALENDAR);
    return ts != null ? new DateTime(ts.getTime()) : null;
  }
}

package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.comment.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
  void insert(@Param("comment") Comment comment);

  Comment findById(@Param("articleId") String articleId, @Param("id") String id);

  void delete(@Param("id") String id);
}

package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.favorite.ArticleFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleFavoriteMapper {
  ArticleFavorite find(@Param("articleId") String articleId, @Param("userId") String userId);

  void insert(@Param("articleFavorite") ArticleFavorite articleFavorite);

  void delete(@Param("favorite") ArticleFavorite favorite);
}

package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
  void insert(@Param("user") User user);

  User findByUsername(@Param("username") String username);

  User findByEmail(@Param("email") String email);

  User findById(@Param("id") String id);

  void update(@Param("user") User user);

  FollowRelation findRelation(@Param("userId") String userId, @Param("targetId") String targetId);

  void saveRelation(@Param("followRelation") FollowRelation followRelation);

  void deleteRelation(@Param("followRelation") FollowRelation followRelation);
}

package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.article.Article;
import io.spring.core.article.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
  void insert(@Param("article") Article article);

  Article findById(@Param("id") String id);

  Tag findTag(@Param("tagName") String tagName);

  void insertTag(@Param("tag") Tag tag);

  void insertArticleTagRelation(@Param("articleId") String articleId, @Param("tagId") String tagId);

  Article findBySlug(@Param("slug") String slug);

  void update(@Param("article") Article article);

  void delete(@Param("id") String id);
}

package io.spring.infrastructure.mybatis.readservice;

import io.spring.application.CursorPageParameter;
import io.spring.application.data.CommentData;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

@Mapper
public interface CommentReadService {
  CommentData findById(@Param("id") String id);

  List<CommentData> findByArticleId(@Param("articleId") String articleId);

  List<CommentData> findByArticleIdWithCursor(
      @Param("articleId") String articleId, @Param("page") CursorPageParameter<DateTime> page);
}

package io.spring.infrastructure.mybatis.readservice;

import io.spring.application.data.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserReadService {

  UserData findByUsername(@Param("username") String username);

  UserData findById(@Param("id") String id);
}

package io.spring.infrastructure.mybatis.readservice;

import io.spring.application.CursorPageParameter;
import io.spring.application.Page;
import io.spring.application.data.ArticleData;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleReadService {
  ArticleData findById(@Param("id") String id);

  ArticleData findBySlug(@Param("slug") String slug);

  List<String> queryArticles(
      @Param("tag") String tag,
      @Param("author") String author,
      @Param("favoritedBy") String favoritedBy,
      @Param("page") Page page);

  int countArticle(
      @Param("tag") String tag,
      @Param("author") String author,
      @Param("favoritedBy") String favoritedBy);

  List<ArticleData> findArticles(@Param("articleIds") List<String> articleIds);

  List<ArticleData> findArticlesOfAuthors(
      @Param("authors") List<String> authors, @Param("page") Page page);

  List<ArticleData> findArticlesOfAuthorsWithCursor(
      @Param("authors") List<String> authors, @Param("page") CursorPageParameter page);

  int countFeedSize(@Param("authors") List<String> authors);

  List<String> findArticlesWithCursor(
      @Param("tag") String tag,
      @Param("author") String author,
      @Param("favoritedBy") String favoritedBy,
      @Param("page") CursorPageParameter page);
}

package io.spring.infrastructure.mybatis.readservice;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagReadService {
  List<String> all();
}

package io.spring.infrastructure.mybatis.readservice;

import io.spring.application.data.ArticleFavoriteCount;
import io.spring.core.user.User;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleFavoritesReadService {
  boolean isUserFavorite(@Param("userId") String userId, @Param("articleId") String articleId);

  int articleFavoriteCount(@Param("articleId") String articleId);

  List<ArticleFavoriteCount> articlesFavoriteCount(@Param("ids") List<String> ids);

  Set<String> userFavorites(@Param("ids") List<String> ids, @Param("currentUser") User currentUser);
}

package io.spring.infrastructure.mybatis.readservice;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRelationshipQueryService {
  boolean isUserFollowing(
      @Param("userId") String userId, @Param("anotherUserId") String anotherUserId);

  Set<String> followingAuthors(@Param("userId") String userId, @Param("ids") List<String> ids);

  List<String> followedUsers(@Param("userId") String userId);
}

package io.spring.infrastructure.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.spring.core.service.JwtService;
import io.spring.core.user.User;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultJwtService implements JwtService {
  private final SecretKey signingKey;
  private final SignatureAlgorithm signatureAlgorithm;
  private int sessionTime;

  @Autowired
  public DefaultJwtService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.sessionTime}") int sessionTime) {
    this.sessionTime = sessionTime;
    signatureAlgorithm = SignatureAlgorithm.HS512;
    this.signingKey = new SecretKeySpec(secret.getBytes(), signatureAlgorithm.getJcaName());
  }

  @Override
  public String toToken(User user) {
    return Jwts.builder()
        .setSubject(user.getId())
        .setExpiration(expireTimeFromNow())
        .signWith(signingKey)
        .compact();
  }

  @Override
  public Optional<String> getSubFromToken(String token) {
    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
      return Optional.ofNullable(claimsJws.getBody().getSubject());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private Date expireTimeFromNow() {
    return new Date(System.currentTimeMillis() + sessionTime * 1000L);
  }
}

