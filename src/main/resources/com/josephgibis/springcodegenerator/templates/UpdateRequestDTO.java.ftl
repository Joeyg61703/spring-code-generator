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
public class Update${entityNamePascal}RequestDTO{

<#list properties as property>
    <#if property.includeInUpdateRequest>
    private ${property.type} ${property.name}<#if property.defaultValue?has_content && property.defaultValue != ""> = <#if property.type == "String">"${property.defaultValue}"<#else>${property.defaultValue}</#if></#if>;
    </#if>
</#list>

<#if !useLombok>
    public Update${entityNamePascal}RequestDTO() {}
    
    public Update${entityNamePascal}RequestDTO(<#list properties as property><#if property.includeInUpdateRequest>${property.type} ${property.name}<#if property?has_next>, </#if></#if></#list>) {
<#list properties as property>
        <#if property.includeInUpdateRequest>
        this.${property.name} = ${property.name};
        </#if>
</#list>
    }

<#list properties as property>
    <#if property.includeInUpdateRequest>
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