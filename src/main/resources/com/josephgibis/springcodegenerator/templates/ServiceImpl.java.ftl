package ${basePackage}.${servicePackage};

import ${basePackage}.${entityPackage}.${entityNamePascal};

import ${basePackage}.${repositoryPackage}.${entityNamePascal}Repository;
import ${basePackage}.${dtoPackage}.${entityNamePascal}ResponseDTO;
import ${basePackage}.${dtoPackage}.Create${entityNamePascal}RequestDTO;
import ${basePackage}.${dtoPackage}.requests.Update${entityNamePascal}RequestDTO;

import ${basePackage}.${exceptionPackage}.${entityNamePascal}NotFoundException;
import ${basePackage}.${mapperPackage}.${entityNamePascal}Mapper;

import java.util.List;
<#if idType == "UUID">
import java.util.UUID;
</#if>

@Service
<#if useLombok>
@NoArgsConstructor
@RequiredArgsConstructor
</#if>
public class ${entityNamePascal}ServiceImpl implements ${entityNamePascal}Service {

    private final ${entityNamePascal}Repository ${entityNameCamel}Repository;
    private final ${entityNamePascal}Mapper ${entityNameCamel}Mapper;

    <#if !useLombok>
    public ${entityNamePascal}ServiceImpl(${entityNamePascal}Repository ${entityNameCamel}Repository) {
        this.${entityNameCamel}Repository = ${entityNameCamel}Repository;
        this.${entityNamePascal}Mapper = ${entityNameCamel}Mapper;
    }
    </#if>

    @Override
    public ${entityNamePascal}ResponseDTO create${entityNamePascal}(Create${entityNamePascal}RequestDTO create${entityNamePascal}RequestDTO){
        <#if implementServiceBody>
        ${entityNamePascal} ${entityNameCamel} = ${entityNamePascal} ${entityNamePascal}Mapper.toEntity(create${entityNamePascal}RequestDTO);
        return ${entityNameCamel}Repository.save(${entityNameCamel});
        <#else>
        //TODO: implement create${entityNamePascal}
        </#if>
    }

    @Override
    public ${entityNamePascal}ResponseDTO get${entityNamePascal}ById(${idType} id){
        <#if implementServiceBody>
        return ${entityNamePascal}Mapper.toDTO(${entityNameCamel}Repository.findById(id));
        <#else>
        //TODO: implement get${entityNamePascal}ById
        </#if>
    }

    @Override
    public List<${entityNamePascal}ResponseDTO> getAll${pluralEntityNamePascal}(){
        <#if implementServiceBody>
        return ${entityNameCamel}Repository
                .findAll()
                .stream()
                .map(${entityNamePascal}Mapper::toDTO)
                .toList();
        <#else>
        //TODO: implement getAll${entityNamePascal}
        </#if>
    }

    @Override
    ${entityNamePascal}ResponseDTO update${entityNamePascal}(${idType} id, Update${entityNamePascal}RequestDTO update${entityNamePascal}RequestDTO){
        <#if implementServiceBody>
        ${entityNamePascal} ${entityNameCamel} = ${entityNameCamel}Repository.findById(id)
            .orElseThrow(() -> new ${entityNamePascal}NotFoundException("${entityNamePascal} not found by id: " + id));
        ${entityNameCamel}Mapper.update${entityNamePascal}FromDto(update${entityNamePascal}RequestDTO, ${entityNameCamel});
        ${entityNameCamel} = ${entityNameCamel}Repository.save(${entityNameCamel});
        return ${entityNameCamel}Mapper.toDTO(entity);
         <#else>
        //TODO: implement update${entityNamePascal}
        </#if>
    }

    @Override
    public void delete${entityNamePascal}(${idType} id){
        <#if implementServiceBody>
        ${entityNameCamel}Repository.deleteById(id);
         <#else>
        //TODO: implement delete${entityNamePascal}
        </#if>
    }

}