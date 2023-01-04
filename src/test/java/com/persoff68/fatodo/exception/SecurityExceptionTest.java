package com.persoff68.fatodo.exception;

import com.persoff68.fatodo.security.exception.AuthUserNotFoundException;
import com.persoff68.fatodo.security.exception.AuthWrongPasswordException;
import com.persoff68.fatodo.security.exception.AuthWrongProviderException;
import com.persoff68.fatodo.security.exception.ForbiddenException;
import com.persoff68.fatodo.security.exception.OAuth2InternalException;
import com.persoff68.fatodo.security.exception.OAuth2ProviderNotSupportedException;
import com.persoff68.fatodo.security.exception.OAuth2UserNotFoundException;
import com.persoff68.fatodo.security.exception.OAuth2WrongProviderException;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityExceptionTest {

    @Test
    void testForbiddenException() {
        Object exception = new ForbiddenException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.forbidden");
    }

    @Test
    void testUnauthorizedException() {
        Object exception = new UnauthorizedException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.unauthorized");
    }

    @Test
    void testAuthUserNotFoundException() {
        Object exception = new AuthUserNotFoundException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.notRegistered");
    }

    @Test
    void testAuthWrongPasswordException() {
        Object exception = new AuthWrongPasswordException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.wrongPassword");
    }

    @Test
    void testAuthWrongProviderException() {
        Object exception = new AuthWrongProviderException("test_provider");
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.wrongProvider.test_provider");
    }

    @Test
    void testOAuth2EmailNotFoundException() {
        Object exception = new OAuth2UserNotFoundException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.oauth2.notRegistered");
    }

    @Test
    void testOAuth2InternalException() {
        Object exception = new OAuth2InternalException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.oauth2.internal");
    }

    @Test
    void testOAuth2ProviderNotSupportedException() {
        Object exception = new OAuth2ProviderNotSupportedException("test_provider");
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.oauth2.providerNotSupported");
    }

    @Test
    void testOAuth2WrongProviderException() {
        Object exception = new OAuth2WrongProviderException();
        assertThat(exception).isInstanceOf(AbstractException.class);
        AbstractException abstractException = (AbstractException) exception;
        assertThat(abstractException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(abstractException.getFeedBackCode()).isEqualTo("security.oauth2.wrongProvider");
    }

}
