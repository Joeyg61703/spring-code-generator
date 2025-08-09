package ${basePackage}.${entityPackage};

import jakarta.persistence.*;
<#list requiredImports as import>
import ${import};
</#list>
<#if hasList>
import java.util.List;
</#if>
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
    @Column(name = "${property.nameSnake}"<#if !property.nullable>, nullable = false</#if><#if property.unique>, unique = true</#if>)
    private ${property.type} ${property.name}<#if property.defaultValue?has_content && property.defaultValue != ""> = <#if property.type == "String">"${property.defaultValue}"<#else>${property.defaultValue}</#if></#if>;

</#list>
<#list relationships as relationship>
<#assign isSource = (relationship.sourceEntity.name == entityNamePascal)>
<#assign isTarget = (relationship.targetEntity.name == entityNamePascal)>
<#assign relType = relationship.relationshipType.name()>
<#if isSource>
    <#-- Source entity variables -->
    <#assign mappedByValue = entityNameCamel>
    <#assign joinColumnName = relationship.targetEntity.name?lower_case + "_id">
    <#assign fieldName = relationship.targetEntity.name?lower_case>
    <#assign fieldType = relationship.targetEntity.name>
    <#assign isCollection = (relType == "ONE_TO_MANY" || relType == "MANY_TO_MANY")>
<#elseif isTarget>
    <#-- Target entity variables -->
    <#assign mappedByValue = relationship.sourceEntity.name?lower_case>
    <#assign joinColumnName = relationship.sourceEntity.name?lower_case + "_id">
    <#assign fieldName = relationship.sourceEntity.name?lower_case>
    <#assign fieldType = relationship.sourceEntity.name>
    <#assign isCollection = (relType == "MANY_TO_ONE" || relType == "MANY_TO_MANY")>
</#if>
<#if isSource>
    <#if relType == "ONE_TO_MANY">
    @OneToMany(mappedBy = "${mappedByValue}")
    <#elseif relType == "MANY_TO_ONE">
    @ManyToOne
    @JoinColumn(name = "${joinColumnName}")
    <#elseif relType == "ONE_TO_ONE">
    @OneToOne
    @JoinColumn(name = "${joinColumnName}")
    <#elseif relType == "MANY_TO_MANY">
    @ManyToMany
    @JoinTable(
        name = "${entityNameSnake}_${fieldName}s",
        joinColumns = @JoinColumn(name = "${entityNameSnake}_id"),
        inverseJoinColumns = @JoinColumn(name = "${joinColumnName}")
    )
    </#if>  
<#elseif isTarget>
    <#if relType == "ONE_TO_MANY">
    @ManyToOne
    @JoinColumn(name = "${joinColumnName}")
    <#elseif relType == "MANY_TO_ONE">
    @OneToMany(mappedBy = "${mappedByValue}")
    <#elseif relType == "ONE_TO_ONE">
    @OneToOne(mappedBy = "${mappedByValue}")
    <#elseif relType == "MANY_TO_MANY">
    @ManyToMany(mappedBy = "${fieldName}s")
    </#if>
</#if>
<#if isCollection>
    private List<${fieldType}> ${fieldName}s;
<#else>
    private ${fieldType} ${fieldName};
</#if>

</#list>
<#if !useLombok>
    public ${idType} getId() {
        return id;
    }

    public void setId(${idType} id) {
        this.id = id;
    }

    <#list properties as property>
    public ${property.type} get${property.namePascal}() {
        return ${property.name};
    }

    public void set${property.namePascal}(${property.type} ${property.name}) {
        this.${property.name} = ${property.name};
    }

    </#list>
    <#list relationships as relationship>
        <#assign isSource = (relationship.sourceEntity.name == entityNamePascal)>
        <#assign isTarget = (relationship.targetEntity.name == entityNamePascal)>
        <#assign relType = relationship.relationshipType.name()>
        <#if isSource>
            <#assign fieldName = relationship.targetEntity.name?lower_case>
            <#assign fieldType = relationship.targetEntity.name>
            <#assign isCollection = (relType == "ONE_TO_MANY" || relType == "MANY_TO_MANY")>
        <#elseif isTarget>
            <#assign fieldName = relationship.sourceEntity.name?lower_case>
            <#assign fieldType = relationship.sourceEntity.name>
            <#assign isCollection = (relType == "MANY_TO_ONE" || relType == "MANY_TO_MANY")>
        </#if>
        <#if isCollection>
    public List<${fieldType}> get${fieldType}s() {
        return ${fieldName}s;
    }
    
    public void set${fieldType}s(List<${fieldType}> ${fieldName}s) {
        this.${fieldName}s = ${fieldName}s;
    }
    
    <#else>
    public ${fieldType} get${fieldType}() {
        return ${fieldName};
    }
    
    public void set${fieldType}(${fieldType} ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }
     
        </#if>
    </#list>
</#if>
}