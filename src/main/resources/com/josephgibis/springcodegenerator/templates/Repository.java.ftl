package ${basePackage}.${repositoryPackage};

import ${basePackage}.${entityPackage}.${entityName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


<#if idType == "UUID">
    import java.util.UUID;
</#if>


@Repository
public interface ${entityName?cap_first}Repository<${entityName?cap_first}, ${idType}> {
    <#-- TODO: implement methods-->

}
