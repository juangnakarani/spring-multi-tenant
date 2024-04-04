package info.juangnakarani.springmultitenant.interceptor;

//import com.baeldung.multitenant.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        String tenantName = request.getHeader("x-tenant-name");
        log.info("Request URI: {}", requestURI);
        log.info("Tenant Id: {}", tenantName);

        if (tenantName == null) {
            response.getWriter().write("x-tenant-name is not present in the request header");
            response.setStatus(400);
            return false;
        }
        TenantContext.setCurrentTenant(tenantName);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        log.info("Clear tenant context");
        TenantContext.clear();
    }

//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
//                                @Nullable Exception ex) throws Exception {
//        if (ex != null) {
//            ex.printStackTrace();
//            log.error("An exception occurred with message: {}", ex.getMessage());
//        }
//
//    }

}
