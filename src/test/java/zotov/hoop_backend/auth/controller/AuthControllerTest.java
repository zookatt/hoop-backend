package zotov.hoop_backend.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;
import zotov.hoop_backend.auth.dto.ChangePasswordDTORequest;
import zotov.hoop_backend.auth.service.AuthService;
import zotov.hoop_backend.auth.service.TokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Test
    void tokenReturnsGeneratedToken() {
        TokenService tokenService = mock(TokenService.class);
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(tokenService, authService);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "admin@hoop.test",
                "password");

        when(tokenService.generateToken(authentication)).thenReturn("jwt-token");

        assertEquals("jwt-token", controller.token(authentication));
    }

    @Test
    void changePasswordDelegatesToService() {
        TokenService tokenService = mock(TokenService.class);
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(tokenService, authService);
        ChangePasswordDTORequest request = new ChangePasswordDTORequest(
                "worker@hoop.test",
                "Temporal1234",
                "Worker1234");

        controller.changePassword(request);

        verify(authService).changePassword(request);
    }

    @Test
    void changePasswordMapsWrongPasswordToUnauthorized() {
        TokenService tokenService = mock(TokenService.class);
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(tokenService, authService);
        ChangePasswordDTORequest request = new ChangePasswordDTORequest(
                "worker@hoop.test",
                "bad",
                "Worker1234");

        doThrow(new IllegalStateException("Current password is incorrect"))
                .when(authService)
                .changePassword(request);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.changePassword(request));

        assertEquals(401, exception.getStatusCode().value());
    }
}
