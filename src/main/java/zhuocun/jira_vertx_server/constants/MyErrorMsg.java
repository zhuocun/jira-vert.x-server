package zhuocun.jira_vertx_server.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MyErrorMsg {
    public static final String KEY = "error";
    public static final String INVALID_DB = "Invalid database type provided";
    public static final String BAD_REQUEST = "Bad request";
    public static final String DB_UTILS_NULL = "DBUtils is not initialised";
}
