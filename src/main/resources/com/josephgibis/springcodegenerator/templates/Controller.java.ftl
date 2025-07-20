package ${basePackage}.${controllerPackage};

import ${basePackage}.${dtoPackage}.${entityNamePascal}Dto;

import ${basePackage}.${dtoPackage}.Create${entityNamePascal}Request;
import ${basePackage}.${dtoPackage}.requests.Update${entityNamePascal}Request;


import ${basePackage}.${entityPackage}.${entityNamePascal};

import ${basePackage}.${servicePackage}.${entityNamePascal}Service;

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
@RequestMapping("/api/v1/${pluralEntityNameCamel}")
public class ${entityNamePascal}Controller {

    private final ${entityNamePascal}Service ${entityNameCamel}Service;

    public ${entityNamePascal}Controller(${entityNamePascal}Service ${entityNameCamel}Service) {
        this.${entityNameCamel}Service = ${entityNameCamel}Service;
    }

    // GET "/api/v1/${pluralEntityNameCamel}"
    @GetMapping("/")
    ResponseEntity<List<${entityNamePascal}Dto>> get${pluralEntityNamePascal}(){
        return ResponseEntity.status(HttpStatus.OK).body(${entityNameCamel}Service.getAll${pluralEntityNamePascal}());
    }

    // GET "/api/v1/${pluralEntityNameCamel}/id"
    @GetMapping(params = "id")
    ResponseEntity<List<${entityNamePascal}Dto>> get${entityNamePascal}ById(@RequestParam ${idType} id){
        return ResponseEntity.status(HttpStatus.OK).body(${entityNameCamel}Service.get${entityNamePascal}ById(id));
    }

    // POST "/api/v1/${pluralEntityNameCamel}"
    @PostMapping("/")
    ResponseEntity<${entityNamePascal}Dto> create${entityNamePascal}(@RequestBody Create${entityNamePascal}Request request){
            ${entityNamePascal}Dto created${entityNamePascal} = ${entityNameCamel}Service.create${entityNamePascal}(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created${entityNamePascal});
    }

}