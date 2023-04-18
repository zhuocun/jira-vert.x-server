package zhuocun.jira_vertx_server.utils.exceptions;

import java.io.IOException;

public class MyRuntimeException extends RuntimeException {

    public MyRuntimeException(String msg, IOException e) {
        super(msg, e);
    }

    public MyRuntimeException(String msg) {
        super(msg);
    }

    public MyRuntimeException(String msg, Exception e) {
        super(msg, e);
    }

}
