//package com.cacheing.cacheingtest.dao;
//
//import com.cacheing.cacheingtest.exception.UnleashAuthenticationException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class UnleashCustomClient {
//
//    @Value("${unleashurl}")
//    private String unleashUrl;
//
//    @Value("${token:user:dd1b86a3d56e35c9a39dbfde2fa23e1ab8d0b66e506a271b1ea4df92}")
//    private String defaultToken;
//
//    @Value("${projectName:default}")
//    private String projectName;
//
//    @Value("${featureName:redisClient}")
//    private String featureName;
//
//    @Value("${environment:development}")
//    private String environment;
//
//    private String value = "admin";
//    private String password = "unleash4all";
//
//    public static String token;
//
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//
//    public Boolean isRedisEnabled() throws UnleashAuthenticationException {
//        Boolean status = false;
//        if (StringUtils.isEmpty(token)) {
//            throw new UnleashAuthenticationException("Unable To Authenticate to Unleash Server!!!");
//        }
//        status = checkFeatureEnabledOrNot(token, status);
//        return status;
//    }
//
//    private Boolean checkFeatureEnabledOrNot(String token, Boolean status) {
//        String url = unleashUrl + "/api/admin/projects/" + projectName + "/features/" + featureName;
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", token);
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//        ResponseEntity responseEntity = (ResponseEntity) restTemplate.exchange(url, HttpMethod.GET, requestEntity, Object.class);
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            Map map = (Map) responseEntity.getBody();
//            List<Map> environments = (List<Map>) map.get("environments");
//            for (Map env : environments) {
//                if (env.get("type").equals(environment)) {
//                    if ((Boolean) env.get("enabled")) {
//                        status = true;
//                        break;
//                    }
//                }
//            }
//        }
//        return status;
//    }
//}
