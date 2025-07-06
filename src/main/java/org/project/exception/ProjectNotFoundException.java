package org.project.exception;

public class ProjectNotFoundException extends NotFoundInAppException {

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
