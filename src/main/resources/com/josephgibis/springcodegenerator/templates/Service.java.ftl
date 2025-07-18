package ${basePackage}.${servicePackage};


import ${basePackage}.${dtoPackage}.${entityName?cap_first}Dto;

import ${basePackage}.${dtoPackage}.Create${entityName?cap_first}Request;
import ${basePackage}.${dtoPackage}..requests.Update${entityName?cap_first}Request;

import java.util.List;

<#if idType == "UUID">
    import java.util.UUID;
</#if>

public interface ${entityName?cap_first}Service {

${entityName}Dto createPost(Create${entityName?cap_first}Request create${entityName?cap_first}Request, ${idType} userId);

${entityName}Dto get${entityName?cap_first}ById(${idType} id);

List<${entityName?cap_first}Dto> getAll${entityName?cap_first}();

        ${entityName?cap_first}Dto update${entityName?cap_first}(${idType} id, Update${entityName?cap_first}Request update${entityName?cap_first}Request);

        void delete${entityName?cap_first}(${idType} id);

    }