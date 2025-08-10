package ${basePackage}.${mapperPackage};

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ${basePackage}.${entityPackage}.${entityNamePascal};

import ${basePackage}.${dtoPackage}.${entityNamePascal}ResponseDTO;
import ${basePackage}.${dtoPackage}.Create${entityNamePascal}RequestDTO;
import ${basePackage}.${dtoPackage}.requests.Update${entityNamePascal}RequestDTO;

@Mapper(componentModel = "spring")
public interface ${entityNamePascal}Mapper {
    
    ${entityNamePascal}ResponseDTO toDTO(${entityNamePascal} ${entityNameCamel});

    ${entityNamePascal} toEntity(Create${entityNamePascal}RequestDTO create${entityNamePascal}RequestDTO);

    void update${entityNamePascal}FromDto(Update${entityNamePascal}RequestDTO update${entityNamePascal}RequestDTO, @MappingTarget ${entityNamePascal} ${entityNameCamel});
}