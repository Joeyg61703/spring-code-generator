package ${basePackage}.${servicePackage};

import ${basePackage}.${dtoPackage}.${entityNamePascal}Dto;
import ${basePackage}.${dtoPackage}.Create${entityNamePascal}Request;
import ${basePackage}.${dtoPackage}.requests.Update${entityNamePascal}Request;
import java.util.List;
<#if idType == "UUID">
import java.util.UUID;
</#if>

public interface ${entityNamePascal}Service {

    ${entityNamePascal}Dto create${entityNamePascal}(Create${entityNamePascal}Request create${entityNamePascal}Request);

    ${entityNamePascal}Dto get${entityNamePascal}ById(${idType} id);

    List<${entityNamePascal}Dto> getAll${pluralEntityNamePascal}();

    ${entityNamePascal}Dto update${entityNamePascal}(${idType} id, Update${entityNamePascal}Request update${entityNamePascal}Request);

    void delete${entityNamePascal}(${idType} id);

}