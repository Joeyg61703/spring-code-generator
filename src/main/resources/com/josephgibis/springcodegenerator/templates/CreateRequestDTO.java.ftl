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
public class Create${entityNamePascal}RequestDTO{

<#list properties as property>
    <#if property.includeInCreateRequest>
    private ${property.type} ${property.name}<#if property.defaultValue?has_content && property.defaultValue != ""> = <#if property.type == "String">"${property.defaultValue}"<#else>${property.defaultValue}</#if></#if>;
    </#if>
</#list>

<#if !useLombok>
    public Create${entityNamePascal}RequestDTO() {}
    
    public Create${entityNamePascal}RequestDTO(<#list properties as property><#if property.includeInCreateRequest>${property.type} ${property.name}<#if property?has_next>, </#if></#if></#list>) {
<#list properties as property>
        <#if property.includeInCreateRequest>
        this.${property.name} = ${property.name};
        </#if>
</#list>
    }

<#list properties as property>
    <#if property.includeInCreateRequest>
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