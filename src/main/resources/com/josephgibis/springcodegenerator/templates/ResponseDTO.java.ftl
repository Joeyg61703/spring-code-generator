package ${basePackage}.${dtoPackage};

<#list requiredImports as import>
import ${import};
</#list>

<#if useLombok>
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
</#if>
public class ${entityNamePascal}ResponseDTO{

    private ${idType} id;
<#list properties as property>
    <#if property.includeInResponse>
    private ${property.type} ${property.name}<#if property.defaultValue?has_content && property.defaultValue != ""> = <#if property.type == "String">"${property.defaultValue}"<#else>${property.defaultValue}</#if></#if>;
    </#if>
</#list>

<#if !useLombok>
    public ${entityNamePascal}ResponseDTO() {}
    
    public ${entityNamePascal}ResponseDTO(${idType} id<#list properties as property><#if property.includeInResponse>, ${property.type} ${property.name}</#if></#list>) {
        this.id = id;
<#list properties as property>
        <#if property.includeInResponse>
        this.${property.name} = ${property.name};
        </#if>
</#list>
    }

    public ${idType} getId() {
        return id;
    }

    public void setId(${idType} id) {
        this.id = id;
    }
<#list properties as property>
    <#if property.includeInResponse>
    public ${property.type} get${property.name?cap_first}() {
        return ${property.name};
    }

    public void set${property.name?cap_first}(${property.type} ${property.name}) {
        this.${property.name} = ${property.name};
    }
    </#if>
</#list>
</#if>
}