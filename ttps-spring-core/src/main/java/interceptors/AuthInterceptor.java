package interceptors;

import config.context.AuthContext;
import domain.models.Usuario;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.AuthService;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService; // Asume que tienes un servicio para la lógica de usuario/autenticación.

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if (token != null) {

            try {
                Usuario usuario = authService.validarToken(token);
                if (usuario != null) {
                    AuthContext.setUser(usuario);
                    return true;
                }
            } catch (Exception e) {
                // Manejar error de formato de token
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonResponse = "{"
                + "\"status\": 401,"
                + "\"error\": \"unauthorized\","
                + "\"message\": \"token invalido\""
                + "}";

        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse);
            writer.flush();
        } catch (IOException e) {
        }

        // 5. Devolver false
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContext.clear();
    }
}