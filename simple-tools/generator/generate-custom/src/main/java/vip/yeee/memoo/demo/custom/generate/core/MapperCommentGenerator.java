package vip.yeee.memoo.demo.custom.generate.core;

import cn.hutool.core.util.StrUtil;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.Set;

public class MapperCommentGenerator implements CommentGenerator {
    //开始的分隔符，例如mysql为`，sqlserver为[
    private String beginningDelimiter = "";
    //结束的分隔符，例如mysql为`，sqlserver为]
    private String endingDelimiter = "";
    //强制生成注解
    private boolean forceAnnotation;
    //是否生成swagger注解
    private boolean needsSwagger;

    private String genType;

    public MapperCommentGenerator() {
        super();
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
    }

    /**
     * xml中的注释
     */
    @Override
    public void addComment(XmlElement xmlElement) {
        xmlElement.addElement(new TextElement("<!--"));
        String sb = "  WARNING - " +
                MergeConstants.NEW_ELEMENT_TAG;
        xmlElement.addElement(new TextElement(sb));
        xmlElement.addElement(new TextElement("-->"));
    }

    @Override
    public void addRootComment(XmlElement rootElement) {
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        String beginningDelimiter = properties.getProperty("beginningDelimiter");
        if (StringUtility.stringHasValue(beginningDelimiter)) {
            this.beginningDelimiter = beginningDelimiter;
        }
        String endingDelimiter = properties.getProperty("endingDelimiter");
        if (StringUtility.stringHasValue(endingDelimiter)) {
            this.endingDelimiter = endingDelimiter;
        }
        String forceAnnotation = properties.getProperty("forceAnnotation");
        if (StringUtility.stringHasValue(forceAnnotation)) {
            this.forceAnnotation = "TRUE".equalsIgnoreCase(forceAnnotation);
        }
        String needsSwagger = properties.getProperty("needsSwagger");
        if (StringUtility.stringHasValue(needsSwagger)) {
            this.needsSwagger = "TRUE".equalsIgnoreCase(needsSwagger);
        }
        this.genType = properties.getProperty("genType");
    }

    public String getDelimiterName(String name) {
        return beginningDelimiter +
                name +
                endingDelimiter;
    }

    /**
     * 删除标记
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * Example使用
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
    }

    /**
     * 给字段添加数据库备注
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            field.addJavaDocLine("/**");
            String sb = " * " +
                    introspectedColumn.getRemarks();
            field.addJavaDocLine(sb);
            field.addJavaDocLine(" */");
        }
        //添加注解
        if (field.isTransient()) {
            //@Column
            field.addAnnotation("@Transient");
        }
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            if (introspectedColumn == column) {
                if ("mp".equals(genType)) {
//                    field.addAnnotation("@TableId(type = IdType.AUTO)");
                } else {
                    field.addAnnotation("@Id");
                }
                break;
            }
        }
        String column = introspectedColumn.getActualColumnName();
        if (StringUtility.stringContainsSpace(column) || introspectedTable.getTableConfiguration().isAllColumnDelimitingEnabled()) {
            column = introspectedColumn.getContext().getBeginningDelimiter()
                    + column
                    + introspectedColumn.getContext().getEndingDelimiter();
        }
        if ("mp".equals(genType)) {
            if (!column.equals(introspectedColumn.getJavaProperty()) && !StrUtil.toCamelCase(column).equals(introspectedColumn.getJavaProperty())) {
                //@Column
                field.addAnnotation("@TableField(\"" + getDelimiterName(column) + "\")");
            } else if (StringUtility.stringHasValue(beginningDelimiter) || StringUtility.stringHasValue(endingDelimiter)) {
                field.addAnnotation("@TableField(\"" + getDelimiterName(column) + "\")");
            } else if (forceAnnotation) {
                field.addAnnotation("@TableField(\"" + getDelimiterName(column) + "\")");
            }
            if (introspectedColumn.isIdentity()) {
                field.addAnnotation("@TableId(type = IdType.AUTO)");
            }
        } else {
            if (!column.equals(introspectedColumn.getJavaProperty()) && !StrUtil.toCamelCase(column).equals(introspectedColumn.getJavaProperty())) {
                //@Column
                field.addAnnotation("@Column(name = \"" + getDelimiterName(column) + "\")");
            } else if (StringUtility.stringHasValue(beginningDelimiter) || StringUtility.stringHasValue(endingDelimiter)) {
                field.addAnnotation("@Column(name = \"" + getDelimiterName(column) + "\")");
            } else if (forceAnnotation) {
                field.addAnnotation("@Column(name = \"" + getDelimiterName(column) + "\")");
            }
            if (introspectedColumn.isIdentity()) {
                if ("JDBC".equals(introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement())) {
                    field.addAnnotation("@GeneratedValue(generator = \"JDBC\")");
                } else {
                    field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
                }
            } else if (introspectedColumn.isSequenceColumn()) {
                //在 Oracle 中，如果需要是 SEQ_TABLENAME，那么可以配置为 select SEQ_{1} from dual
                String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
                String sql = MessageFormat.format(introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement(), tableName, tableName.toUpperCase());
                field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY, generator = \"" + sql + "\")");
            }
        }
        // region swagger注解
        if (this.needsSwagger) {
            String remarks = introspectedColumn.getRemarks();
            if (remarks == null) {
                remarks = "";
            }
            String swaggerAnnotation = "@ApiModelProperty(value = \"%s\" da )";
            field.addAnnotation("@ApiModelProperty(\"" + remarks.replaceAll("\r", "").replaceAll("\n", "") + "\")");
        }
        // endregion
    }

    /**
     * Example使用
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
    }

    /**
     * getter方法注释
     */
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        method.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" * 获取");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
        }
        sb.setLength(0);
        sb.append(" * @return ");
        sb.append(introspectedColumn.getActualColumnName());
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" - ");
            sb.append(introspectedColumn.getRemarks());
        }
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }

    /**
     * setter方法注释
     */
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        method.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" * 设置");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
        }
        Parameter parm = method.getParameters().get(0);
        sb.setLength(0);
        sb.append(" * @param ");
        sb.append(parm.getName());
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" ");
            sb.append(introspectedColumn.getRemarks());
        }
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }

    /**
     * Example使用
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
    }

    /**
     * @since mbg 1.3.6
     */
    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    /**
     * @since mbg 1.3.6
     */
    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    /**
     * @since mbg 1.3.6
     */
    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    /**
     * @since mbg 1.3.6
     */
    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    /**
     * @since mbg 1.3.6
     */
    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }
}
