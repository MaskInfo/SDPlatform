<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${dao.basePackage}.${mapper.entitySimpleName}Mapper">

    <sql id="${mapper.lowerCaseEntitySimpleName}DaoColumns">
        <!--a.id AS "id",
        a.create_by AS "createBy.id",
        a.create_date AS "createDate",
        a.update_by AS "updateBy.id",
        a.update_date AS "updateDate",
        a.remarks AS "remarks",
        a.del_flag AS "delFlag",-->
	  <#list select.sqlMapColumnList as column>
		${select.simpleTableName}.${column.columnName}<#if !column.isSameName()> AS "${column.propertyName}"</#if><#if column_has_next>,</#if>
	  </#list>
    </sql>

    <sql id="${mapper.lowerCaseEntitySimpleName}Joins">
        <!--todo-->
    </sql>

    <select id="get" resultType="${entityPackage}.${mapper.entitySimpleName}">
        SELECT
        <include refid="${mapper.lowerCaseEntitySimpleName}DaoColumns"/>
        FROM ${mapper.tableName} a
        WHERE a.id = ${r"#{"}${mapper.id}${r"}"} and del_flag = $\{@cn.org.upthink.persistence.mybatis.entity.BaseEntity@DEL_FLAG_NORMAL\}
    </select>

    <select id="find" resultType="${entityPackage}.${mapper.entitySimpleName}">
        SELECT
        <include refid="${mapper.lowerCaseEntitySimpleName}DaoColumns"/>
        FROM ${mapper.tableName} a
        <where>
            a.del_flag = ${r"#{DEL_FLAG_NORMAL}"}
        </where>
        <choose>
            <when test="page !=null and page.orderBy != null and page.orderBy != ''">
                ORDER BY ${r"${page.orderBy}"}
            </when>
            <otherwise>
                ORDER BY a.update_date DESC
            </otherwise>
        </choose>
    </select>

    <select id="findList" resultType="${entityPackage}.${mapper.entitySimpleName}">
        SELECT
        <include refid="${mapper.lowerCaseEntitySimpleName}DaoColumns"/>
        FROM ${mapper.tableName} a
        <where>
            a.del_flag = ${r"#{delFlag}"}
            <!--<if test="name != null and name != ''">
                AND a.name LIKE
                <if test="dbName == 'mysql'">CONCAT('%', ${r"#{name}"}, '%')</if>
            </if>-->
            <#list fieldList.sqlMapColumnList as column>
                <#if column.isQuery()>
             <if test="${column.propertyName} != null and ${column.propertyName} != ''">
                AND a.${column.columnName} LIKE CONCAT('%', ${r"#{"}${column.propertyName}${r"}"}, '%')
             </if>
                </#if>
            </#list>
		${r"${sqlMap.dsf}"}
        </where>
        ORDER BY a.update_date DESC
    </select>

    <insert id="insert">
        INSERT INTO ${mapper.tableName}(
			<!--id,
			create_by,
			create_date,
			update_by,
			update_date,
			remarks,
			del_flag,-->
		  <#list insert.sqlMapColumnList as column>
			${column.columnName}<#if column_has_next>,</#if>
		  </#list>
        ) VALUES (
			<!--${r"#{id}"},
			${r"#{createBy.id}"},
			${r"#{createDate}"},
			${r"#{updateBy.id}"},
			${r"#{updateDate}"},
			${r"#{remarks}"},
			${r"#{delFlag}"},-->
		  <#list insert.sqlMapColumnList as column>
		    ${r"#{"}${column.propertyName}${r"}"}<#if column_has_next>,</#if>
		  </#list>
        )
    </insert>

    <update id="update">
        UPDATE ${mapper.tableName}
        <trim prefix="SET" suffixOverrides=",">
		<#list update.sqlMapColumnList as column>
		  <#if !column.isId()>
		   <if test="${column.propertyName}!=null">${column.columnName} = ${r"#{"}${column.propertyName}${r"}"},</if>
		  </#if>
		</#list>
		</trim>
        WHERE id = ${r"#{"}${mapper.id}${r"}"}
    </update>

    <update id="delete">
        UPDATE ${mapper.tableName} SET del_flag = ${r"#{DEL_FLAG_DELETE}"} WHERE id = ${r"#{"}${mapper.id}${r"}"}
    </update>

</mapper>