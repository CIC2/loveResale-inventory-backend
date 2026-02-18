package com.resale.resaleinventory.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.resale.resaleinventory.models.ActionType;
import com.resale.resaleinventory.models.InventoryExceptionLog;
import com.resale.resaleinventory.models.InventoryLog;
import com.resale.resaleinventory.repositories.InventoryExceptionLogRepository;
import com.resale.resaleinventory.repositories.InventoryLogRepository;
import com.resale.resaleinventory.security.CookieBearerTokenResolver;
import com.resale.resaleinventory.security.JwtTokenUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final InventoryLogRepository inventoryLogRepository;
    private final InventoryExceptionLogRepository inventoryExceptionLogRepository;
    private final HttpServletRequest request;
    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CookieBearerTokenResolver cookieBearerTokenResolver;

    @Around("@annotation(logActivity)")
    public Object log(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {

        ActionType actionType = logActivity.value();
        InventoryLog inventoryLog = new InventoryLog();
        InventoryExceptionLog inventoryExceptionLog = new InventoryExceptionLog();
        long start = System.currentTimeMillis();
        String httpMethod = request.getMethod();

        String identityType = "GUEST";
        Integer identityId = null;
        String token = null;

        try {
            token = cookieBearerTokenResolver.resolve(request);
        } catch (Exception ignored) {}


        if (token != null) {
            try {
                Integer customerId = jwtTokenUtil.extractCustomerId(token);
                if (customerId != null) {
                    identityType = "CUSTOMER";
                    identityId = customerId;
                }
            } catch (Exception ignored) {}

            if (identityId == null) {
                try {
                    Integer userId = jwtTokenUtil.extractUserId(token);
                    if (userId != null) {
                        identityType = "USER";
                        identityId = userId;
                    }
                } catch (Exception ignored) {}
            }
        }

        if (identityId == null) {
            try {
                var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
                    Integer customerId = jwt.getClaim("customerId");
                    Integer userId = jwt.getClaim("userId");

                    if (customerId != null) {
                        identityType = "CUSTOMER";
                        identityId = customerId;
                    } else if (userId != null) {
                        identityType = "USER";
                        identityId = userId;
                    }
                }
            } catch (Exception ignored) {}
        }

        ActionType action = logActivity.value();
        int actionCode = action.getCode();
        String actionName = action.name();


        String requestBodyJson = null;
        if (!"GET".equalsIgnoreCase(httpMethod)) {
            try {
                requestBodyJson = objectMapper.writeValueAsString(joinPoint.getArgs());
            } catch (Exception ignored) {}
        }
        String headersJson = extractHeaders();
        String paramsJson = extractQueryParams();
        Integer modelId = extractIdFromParams("modelId");
        Integer unitId = extractIdFromParams("unitId");
        Integer projectId = extractIdFromParams("projectId");
        Integer locationId = extractIdFromParams("locationId");

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            String responseJson = null;
            try {
                responseJson = objectMapper.writeValueAsString(result);
            } catch (Exception ignored) {}

            int status = 200;
            if (result instanceof ResponseEntity<?> res) {
                status = res.getStatusCode().value();
            }
            long countIntegers = Arrays.stream(joinPoint.getArgs())
                    .filter(arg -> arg instanceof Integer)
                    .count();

            if (countIntegers == 1) {

                for (Object arg : joinPoint.getArgs()) {
                    if (arg instanceof Integer) {

                        Integer idValue = (Integer) arg;

                        switch (actionType) {

                            case GET_LOCATION_DETAILS:
                            case UPDATE_LOCATION:
                            case CREATE_LOCATION:
                            case DELETE_PROJECT_FROM_LOCATION:
                                inventoryLog.setLocationId(idValue);
                                break;

                            case GET_PROJECT_DETAILS:
                            case GET_PROJECTS:
                                inventoryLog.setProjectId(idValue);
                                break;

                            case GET_MODEL_DETAILS:
                            case UPDATE_MODEL:
                            case GET_MODELS:
                            case CUSTOMER_MODEL_COMPARISON:
                            case CUSTOMER_GET_MODELS:
                            case CUSTOMER_MODEL_FILTER:
                                inventoryLog.setModelId(idValue);
                                break;

                            case GET_UNIT_DETAILS:
                            case GET_UNIT_COMPARISON:
                            case GET_UNITS:
                            case GET_UNITS_SEARCH:
                            case UNIT_FILTER:
                            case GET_UNIT_PAYMENT_PLAN:
                            case GET_SINGLE_PAYMENT:
                            case CUSTOMER_GET_MODEL_DETAILS:
                                inventoryLog.setUnitId(idValue);
                                break;

                            default:
                                inventoryLog.setProjectId(idValue);
                                break;
                        }
                    }
                }
            }

            inventoryLog.setIdentityType(identityType);
            inventoryLog.setIdentityId(identityId);
            inventoryLog.setActionCode(actionCode);
            inventoryLog.setActionName(actionName);
            inventoryLog.setHttpMethod(httpMethod);
            inventoryLog.setStatusCode(status);
            inventoryLog.setRequestBody(requestBodyJson);
            inventoryLog.setResponseBody(responseJson);
            inventoryLog.setExecutionTimeMs(executionTime);
            inventoryLog.setCreatedAt(LocalDateTime.now());
            inventoryLog.setHeaders(headersJson);
            inventoryLog.setQueryParams(paramsJson);

            inventoryLogRepository.save(inventoryLog);

            return result;

        }catch (Exception ex) {
            long countIntegers = Arrays.stream(joinPoint.getArgs())
                    .filter(arg -> arg instanceof Integer)
                    .count();

            if (countIntegers == 1) {
                Integer idValue = (Integer) Arrays.stream(joinPoint.getArgs())
                        .filter(arg -> arg instanceof Integer)
                        .findFirst()
                        .orElse(null);

                if (idValue != null) {
                    switch (actionType) {
                        case GET_PROJECT_DETAILS:
                            inventoryExceptionLog.setProjectId(idValue);
                            break;
                        case GET_UNIT_DETAILS:
                            inventoryExceptionLog.setUnitId(idValue);
                            break;
                        case GET_MODEL_DETAILS:
                            inventoryExceptionLog.setModelId(idValue);
                            break;
                        case GET_LOCATION_DETAILS:
                            inventoryExceptionLog.setLocationId(idValue);
                            break;
                        default:
                            inventoryExceptionLog.setProjectId(idValue);
                    }
                }
            }

            inventoryExceptionLog.setIdentityType(identityType);
            inventoryExceptionLog.setIdentityId(identityId);
            inventoryExceptionLog.setActionCode(actionCode);
            inventoryExceptionLog.setActionName(actionName);
            inventoryExceptionLog.setHttpMethod(httpMethod);
            inventoryExceptionLog.setExceptionType(ex.getClass().getSimpleName());
            inventoryExceptionLog.setMessage(ex.getMessage());
            inventoryExceptionLog.setStacktrace(getStackTrace(ex));
            inventoryExceptionLog.setCreatedAt(LocalDateTime.now());
            inventoryExceptionLog.setHeaders(headersJson);
            inventoryExceptionLog.setQueryParams(paramsJson);

            inventoryExceptionLogRepository.save(inventoryExceptionLog);

            throw ex;
        }

    }

    private Integer extractIdFromParams(String paramName) {
        String value = request.getParameter(paramName);
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractHeaders() {
        try {
            Map<String, String> headers = new LinkedHashMap<>();
            Enumeration<String> names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                headers.put(name, request.getHeader(name));
            }
            return objectMapper.writeValueAsString(headers);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractQueryParams() {
        try {
            return objectMapper.writeValueAsString(request.getParameterMap());
        } catch (Exception e) {
            return null;
        }
    }

    private String getStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement ste : t.getStackTrace()) {
            sb.append(ste).append("\n");
        }
        return sb.toString();
    }
}


