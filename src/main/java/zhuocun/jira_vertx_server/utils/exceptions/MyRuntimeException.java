package zhuocun.jira_vertx_server.utils.exceptions;

import java.io.IOException;

public class MyRuntimeException extends RuntimeException {

    public MyRuntimeException(String string, IOException e) {
        super(string, e);
    }

    public MyRuntimeException(String string) {
        super(string);
    }

}
