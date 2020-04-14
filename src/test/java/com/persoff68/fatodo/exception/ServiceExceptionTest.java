package com.persoff68.fatodo.exception;

import com.persoff68.fatodo.security.exception.OAuth2EmailNotFoundException;
import com.persoff68.fatodo.security.exception.OAuth2InternalException;
import com.persoff68.fatodo.security.exception.OAuth2ProviderNotSupportedException;
import com.persoff68.fatodo.security.exception.OAuth2WrongProviderException;
import com.persoff68.fatodo.security.exception.WrongProviderException;
import com.persoff68.fatodo.service.exception.ModelAlreadyExistsException;
import com.persoff68.fatodo.service.exception.ModelDuplicatedException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.PermissionException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceExceptionTest {

    @Test
    void testModelAlreadyExistsException_firstConstructor() {
        Object exception = new ModelAlreadyExistsException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testModelAlreadyExistsException_secondConstructor() {
        Object exception = new ModelAlreadyExistsException(Object.class);
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testModelDuplicatedException_firstConstructor() {
        Object exception = new ModelDuplicatedException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testModelDuplicatedException_secondConstructor() {
        Object exception = new ModelDuplicatedException(Object.class);
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testModelNotFoundException_firstConstructor() {
        Object exception = new ModelNotFoundException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testModelNotFoundException_secondConstructor() {
        Object exception = new ModelNotFoundException(Object.class);
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testPermissionException_firstConstructor() {
        Object exception = new PermissionException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testPermissionException_secondConstructor() {
        Object exception = new PermissionException("test_message");
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testWrongProviderException() {
        Object exception = new WrongProviderException("test_provider");
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testOAuth2EmailNotFoundException() {
        Object exception = new OAuth2EmailNotFoundException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testOAuth2InternalException() {
        Object exception = new OAuth2InternalException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testOAuth2ProviderNotSupportedException() {
        Object exception = new OAuth2ProviderNotSupportedException("test_provider");
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testOAuth2WrongProviderException() {
        Object exception = new OAuth2WrongProviderException("test_provider");
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
