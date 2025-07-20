package ${basePackage}.${entityPackage};

import jakarta.persistence.*;
<#if useLombok>
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
</#if>

@Entity
<#if tableName?has_content>
@Table(name = "${tableName}")
<#else>
@Table(name = "${pluralEntityNameSnake}")
</#if>
<#if useLombok>
@Data
@NoArgsConstructor
@AllArgsConstructor
</#if>
public class ${entityNamePascal}<#if extendsClass?has_content && extendsClass != ""> extends ${extendsClass}</#if> {

    @Id
    @GeneratedValue(strategy = GenerationType.${idGeneration})
    private ${idType} id;

<#list properties as property>
    <#if !property.nullable || property.unique>
    @Column(<#if !property.nullable>nullable = false</#if><#if !property.nullable && property.unique>, </#if><#if property.unique>unique = true</#if>)
    </#if>
    private ${property.type} ${property.name}<#if property.defaultValue?has_content && property.defaultValue != ""> = <#if property.type == "String">"${property.defaultValue}"<#else>${property.defaultValue}</#if></#if>;

</#list>
<#if !useLombok>
    // Getters and Setters
    public ${idType} getId() {
    return id;
    }

    public void setId(${idType} id) {
    this.id = id;
    }

    <#list properties as property>
    public ${property.type} get${property.name?cap_first}() {
        return ${property.name};
    }
    public void set${property.name?cap_first}(${property.type} ${property.name}) {
        this.${property.name} = ${property.name};
    }

    </#list>
</#if>
}