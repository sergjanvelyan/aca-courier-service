package com.aca.acacourierservice.aspect;

import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.exception.InvalidStoreCredentialsException;
import com.aca.acacourierservice.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ApiKeySecretValidationAspect {
    private final HttpServletRequest httpServletRequest;
    private final StoreService storeService;
    @Autowired
    public ApiKeySecretValidationAspect(HttpServletRequest httpServletRequest, StoreService storeService) {
        this.httpServletRequest = httpServletRequest;
        this.storeService = storeService;
    }

    @Around(value = "@annotation(ValidateApiKeySecret)")
    public Object doValidating(ProceedingJoinPoint pjp) throws Throwable {
        String apiKey = httpServletRequest.getHeader("apiKey");
        String apiSecret = httpServletRequest.getHeader("apiSecret");
        Store store;
        try{
            store = storeService.getStoreByApiKey(apiKey);
        }catch (CourierServiceException e){
            throw new InvalidStoreCredentialsException("Bad credentials");
        }
        if(!apiSecret.equals(store.getApiSecret())){
            throw new InvalidStoreCredentialsException("Bad credentials");
        }
        httpServletRequest.setAttribute("storeId",store.getId());
        return pjp.proceed();
    }
}