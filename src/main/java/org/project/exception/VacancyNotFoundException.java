package org.project.exception;

public class VacancyNotFoundException extends NotFoundInAppException {

    public VacancyNotFoundException(String message) {
        super(message);
    }
}
