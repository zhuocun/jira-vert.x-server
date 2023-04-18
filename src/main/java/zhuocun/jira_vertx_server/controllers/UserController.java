package zhuocun.jira_vertx_server.controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import zhuocun.jira_vertx_server.constants.MyError;
import zhuocun.jira_vertx_server.services.UserService;

@Singleton
public class UserController {

        private final UserService userService;

        @Inject
        public UserController(UserService userService) {
                this.userService = userService;
        }

        public void get(RoutingContext ctx) {
                String userId = getUserId(ctx);
                userService.get(userId).onSuccess(
                                user -> ctx.response().setStatusCode(200).end(user.encode()))
                                .onFailure(err -> ctx.response().setStatusCode(404)
                                                .end(new JsonObject()
                                                                .put(MyError.KEY, "User not found")
                                                                .encode()));
        }

        public void update(RoutingContext ctx) {
                String userId = getUserId(ctx);
                JsonObject updateData = ctx.body().asJsonObject();
                userService.update(userId, updateData)
                                .onSuccess(updatedUser -> ctx.response().setStatusCode(200)
                                                .end(new JsonObject().put("userInfo", updatedUser)
                                                                .encode()))
                                .onFailure(err -> ctx.response().setStatusCode(400)
                                                .end(new JsonObject()
                                                                .put(MyError.KEY,
                                                                                MyError.BAD_REQUEST)
                                                                .encode()));
        }

        public void getMembers(RoutingContext ctx) {
                userService.getMembers()
                                .onSuccess(members -> ctx.response().setStatusCode(200).end(
                                                new JsonObject().put("members", members).encode()))
                                .onFailure(err -> ctx.response().setStatusCode(404)
                                                .end(new JsonObject()
                                                                .put(MyError.KEY,
                                                                                "Members not found")
                                                                .encode()));
        }

        public void switchLikeStatus(RoutingContext ctx) {
                String userId = getUserId(ctx);
                String projectId = ctx.body().asJsonObject().getString("projectId");
                userService.switchLikeStatus(userId, projectId).onSuccess(updatedUser -> ctx
                                .response().setStatusCode(200)
                                .end(new JsonObject()
                                                .put("username", updatedUser.getString("username"))
                                                .put("likedProjects",
                                                                updatedUser.getJsonArray(
                                                                                "likedProjects"))
                                                .encode()))
                                .onFailure(err -> ctx.response().setStatusCode(400)
                                                .end(new JsonObject()
                                                                .put(MyError.KEY,
                                                                                MyError.BAD_REQUEST)
                                                                .encode()));
        }

        private static String getUserId(RoutingContext ctx) {
                // TODO: Implement your logic to get the user ID from the RoutingContext, e.g.,
                // from the request headers or JWT tokens.
                return null;
        }
}
