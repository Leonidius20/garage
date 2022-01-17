package ua.leonidius.garage.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ExceptionWhileDataFetching;
import graphql.execution.ExecutionPath;

import java.util.Collections;

public class SanitizedError extends ExceptionWhileDataFetching {

    public SanitizedError(ExceptionWhileDataFetching inner) {
        super(ExecutionPath.fromList(Collections.emptyList()),
                inner.getException(), null);
    }

    @Override
    @JsonIgnore
    public Throwable getException() {
        return super.getException();
    }
}