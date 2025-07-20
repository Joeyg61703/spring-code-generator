package ${basePackage}.${repositoryPackage};

import ${basePackage}.${entityPackage}.${entityNamePascal};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
<#if idType == "UUID">
import java.util.UUID;
</#if>

@Repository
public interface ${entityNamePascal}Repository extends JpaRepository<${entityNamePascal}, ${idType}> {
<#list properties as property>
    <#if property.unique>
    Optional<${entityNamePascal}> findBy${entityNamePascal}(${property.type} ${property.name});

    boolean existsBy${property.name?cap_first}(${property.type} ${property.name});

    </#if>
</#list>
}
