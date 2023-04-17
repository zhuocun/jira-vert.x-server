package zhuocun.jira_vertx_server.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MyError {
    public static final String KEY = "error";
    public static final String INVALID_DB = "Invalid database type provided";
    public static final String BAD_REQUEST = "Bad request";
}
