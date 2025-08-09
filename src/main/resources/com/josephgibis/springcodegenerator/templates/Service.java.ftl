package ${basePackage}.${servicePackage};

import ${basePackage}.${dtoPackage}.${entityNamePascal}ResponseDTO;
import ${basePackage}.${dtoPackage}.Create${entityNamePascal}RequestDTO;
import ${basePackage}.${dtoPackage}.requests.Update${entityNamePascal}RequestDTO;
import java.util.List;
<#if idType == "UUID">
import java.util.UUID;
</#if>

public interface ${entityNamePascal}Service {

    ${entityNamePascal}ResponseDTO create${entityNamePascal}(Create${entityNamePascal}RequestDTO create${entityNamePascal}RequestDTO);

    ${entityNamePascal}ResponseDTO get${entityNamePascal}ById(${idType} id);

    List<${entityNamePascal}ResponseDTO> getAll${pluralEntityNamePascal}();

    ${entityNamePascal}ResponseDTO update${entityNamePascal}(${idType} id, Update${entityNamePascal}RequestDTO update${entityNamePascal}RequestDTO);

    void delete${entityNamePascal}(${idType} id);

}