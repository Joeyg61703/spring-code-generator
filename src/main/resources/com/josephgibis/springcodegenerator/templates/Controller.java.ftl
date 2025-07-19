package ${basePackage}.${controllerPackage};

import ${basePackage}.${dtoPackage}.${entityName?cap_first}Dto;

import ${basePackage}.${dtoPackage}.Create${entityName?cap_first}Request;
import ${basePackage}.${dtoPackage}..requests.Update${entityName?cap_first}Request;


import ${basePackage}.${entityPackage}.${entityName};

import com.spring_postgres.demo.services.${entityName?cap_first}Service;
import com.spring_postgres.demo.services.UserInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/${entityName}")
public class ${entityName?cap_first}Controller {

    private final ${entityName?cap_first}Service ${entityName}Service;
    private final UserInfoService userInfoService;

    public ${entityName?cap_first}Controller(${entityName?cap_first}Service ${entityName}Service, UserInfoService userInfoService) {
        this.${entityName}Service = ${entityName}Service;
        this.userInfoService = userInfoService;
    }

    // GET "/api/v1/${entityName}"
    @GetMapping("/")
    ResponseEntity<List<${entityName?cap_first}Dto>> get${entityName?cap_first}s(){
        return ResponseEntity.status(HttpStatus.OK).body(${entityName}Service.getAll${entityName?cap_first}s());
    }

    // GET "/api/v1/${entityName}/id"
    @GetMapping(params = "id")
    ResponseEntity<List<${entityName?cap_first}Dto>> get${entityName?cap_first}ById(@RequestParam ${idType} id){
        return ResponseEntity.status(HttpStatus.OK).body(${entityName}Service.get${entityName?cap_first}ById(id));
    }

}
