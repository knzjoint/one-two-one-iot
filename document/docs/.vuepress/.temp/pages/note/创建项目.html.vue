<template><h1 id="初始化项目" tabindex="-1"><a class="header-anchor" href="#初始化项目" aria-hidden="true">#</a> 初始化项目</h1>
<h2 id="环境" tabindex="-1"><a class="header-anchor" href="#环境" aria-hidden="true">#</a> 环境</h2>
<p>开发环境Win10、jdk1.8、MySQL8.0、SpringBoot2.5.6</p>
<h2 id="创建工程" tabindex="-1"><a class="header-anchor" href="#创建工程" aria-hidden="true">#</a> 创建工程</h2>
<p><img src="http://note.makerknz.cn/image-20211002160053031.png" alt="image-20211002160053031"></p>
<p>包名cn.makerknz.product.server</p>
<p><img src="http://note.makerknz.cn/image-20211026124027120.png" alt="image-20211026124027120"></p>
<p>添加用到的库</p>
<p><img src="http://note.makerknz.cn/image-20211026124232616.png" alt="image-20211026124232616"></p>
<h2 id="使用mybatis-plus" tabindex="-1"><a class="header-anchor" href="#使用mybatis-plus" aria-hidden="true">#</a> 使用Mybatis Plus</h2>
<h3 id="引入包" tabindex="-1"><a class="header-anchor" href="#引入包" aria-hidden="true">#</a> 引入包</h3>
<p>在build.gradle中添加</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>implementation group: 'com.baomidou', name: 'mybatis-plus-boot-starter', version: '3.4.0'
/* 代码生成器依赖 */
testImplementation group: 'com.baomidou', name: 'mybatis-plus-generator', version: '3.4.0'
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br></div></div><p>生成Mybatis 代码，将代码复制到test文件中，然后修改以下内容</p>
<p><img src="http://note.makerknz.cn/image-20211027143348569.png" alt="image-20211027143348569"></p>
<div class="language-java ext-java line-numbers-mode"><pre v-pre class="language-java"><code><span class="token keyword">package</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server</span><span class="token punctuation">;</span>

<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>core<span class="token punctuation">.</span>exceptions<span class="token punctuation">.</span></span><span class="token class-name">MybatisPlusException</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>core<span class="token punctuation">.</span>toolkit<span class="token punctuation">.</span></span><span class="token class-name">StringPool</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>core<span class="token punctuation">.</span>toolkit<span class="token punctuation">.</span></span><span class="token class-name">StringUtils</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>generator<span class="token punctuation">.</span></span><span class="token class-name">AutoGenerator</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>generator<span class="token punctuation">.</span></span><span class="token class-name">InjectionConfig</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>generator<span class="token punctuation">.</span>config<span class="token punctuation">.</span></span><span class="token operator">*</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>generator<span class="token punctuation">.</span>config<span class="token punctuation">.</span>po<span class="token punctuation">.</span></span><span class="token class-name">TableInfo</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>generator<span class="token punctuation">.</span>config<span class="token punctuation">.</span>rules<span class="token punctuation">.</span></span><span class="token class-name">NamingStrategy</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>generator<span class="token punctuation">.</span>engine<span class="token punctuation">.</span></span><span class="token class-name">FreemarkerTemplateEngine</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>boot<span class="token punctuation">.</span>test<span class="token punctuation">.</span>context<span class="token punctuation">.</span></span><span class="token class-name">SpringBootTest</span><span class="token punctuation">;</span>

<span class="token keyword">import</span> <span class="token namespace">java<span class="token punctuation">.</span>util<span class="token punctuation">.</span></span><span class="token class-name">ArrayList</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">java<span class="token punctuation">.</span>util<span class="token punctuation">.</span></span><span class="token class-name">List</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">java<span class="token punctuation">.</span>util<span class="token punctuation">.</span></span><span class="token class-name">Scanner</span><span class="token punctuation">;</span>

<span class="token doc-comment comment">/**
 * cn.makerknz.product.server
 * 请输入表名，多个英文逗号分割：
 * user,login_logs,product,product_topic_bind,device,connect_logs,device_events,application_token
 */</span>

<span class="token comment">// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中</span>
<span class="token annotation punctuation">@SpringBootTest</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">CodeTableGenerator</span> <span class="token punctuation">{</span>

    <span class="token keyword">private</span> <span class="token keyword">static</span> <span class="token keyword">final</span> <span class="token class-name">String</span> URL <span class="token operator">=</span> <span class="token string">"jdbc:mysql://192.168.31.216:3307/product_manager?useUnicode=true&amp;useSSL=false&amp;characterEncoding=utf8"</span><span class="token punctuation">;</span>
    <span class="token keyword">private</span> <span class="token keyword">static</span> <span class="token keyword">final</span> <span class="token class-name">String</span> USERNAME <span class="token operator">=</span> <span class="token string">"product_manager"</span><span class="token punctuation">;</span>
    <span class="token keyword">private</span> <span class="token keyword">static</span> <span class="token keyword">final</span> <span class="token class-name">String</span> PASSWORD <span class="token operator">=</span> <span class="token string">"product_manager"</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">></span></span>
     * 读取控制台内容
     * <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">></span></span>
     */</span>

    <span class="token keyword">public</span> <span class="token keyword">static</span> <span class="token class-name">String</span> <span class="token function">scanner</span><span class="token punctuation">(</span><span class="token class-name">String</span> tip<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token class-name">Scanner</span> scanner <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">Scanner</span><span class="token punctuation">(</span><span class="token class-name">System</span><span class="token punctuation">.</span>in<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token class-name">StringBuilder</span> help <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">StringBuilder</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        help<span class="token punctuation">.</span><span class="token function">append</span><span class="token punctuation">(</span><span class="token string">"请输入"</span> <span class="token operator">+</span> tip <span class="token operator">+</span> <span class="token string">"："</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token class-name">System</span><span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span>help<span class="token punctuation">.</span><span class="token function">toString</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>scanner<span class="token punctuation">.</span><span class="token function">hasNext</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token class-name">String</span> ipt <span class="token operator">=</span> scanner<span class="token punctuation">.</span><span class="token function">next</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token keyword">if</span> <span class="token punctuation">(</span><span class="token class-name">StringUtils</span><span class="token punctuation">.</span><span class="token function">isNotBlank</span><span class="token punctuation">(</span>ipt<span class="token punctuation">)</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token keyword">return</span> ipt<span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span>
        <span class="token keyword">throw</span> <span class="token keyword">new</span> <span class="token class-name">MybatisPlusException</span><span class="token punctuation">(</span><span class="token string">"请输入正确的"</span> <span class="token operator">+</span> tip <span class="token operator">+</span> <span class="token string">"！"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">public</span> <span class="token keyword">static</span> <span class="token keyword">void</span> <span class="token function">main</span><span class="token punctuation">(</span><span class="token class-name">String</span><span class="token punctuation">[</span><span class="token punctuation">]</span> args<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token comment">// 代码生成器</span>
        <span class="token class-name">AutoGenerator</span> mpg <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">AutoGenerator</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// 全局配置</span>
        <span class="token class-name">GlobalConfig</span> gc <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">GlobalConfig</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token class-name">String</span> projectPath <span class="token operator">=</span> <span class="token class-name">System</span><span class="token punctuation">.</span><span class="token function">getProperty</span><span class="token punctuation">(</span><span class="token string">"user.dir"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        gc<span class="token punctuation">.</span><span class="token function">setOutputDir</span><span class="token punctuation">(</span>projectPath <span class="token operator">+</span> <span class="token string">"/src/main/java"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        gc<span class="token punctuation">.</span><span class="token function">setAuthor</span><span class="token punctuation">(</span><span class="token string">"maker knz"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        gc<span class="token punctuation">.</span><span class="token function">setOpen</span><span class="token punctuation">(</span><span class="token boolean">false</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">setGlobalConfig</span><span class="token punctuation">(</span>gc<span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// 数据源配置</span>
        <span class="token class-name">DataSourceConfig</span> dsc <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">DataSourceConfig</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        dsc<span class="token punctuation">.</span><span class="token function">setUrl</span><span class="token punctuation">(</span>URL<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token comment">// dsc.setSchemaName("public");</span>
        dsc<span class="token punctuation">.</span><span class="token function">setDriverName</span><span class="token punctuation">(</span><span class="token string">"com.mysql.cj.jdbc.Driver"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        dsc<span class="token punctuation">.</span><span class="token function">setUsername</span><span class="token punctuation">(</span>USERNAME<span class="token punctuation">)</span><span class="token punctuation">;</span>
        dsc<span class="token punctuation">.</span><span class="token function">setPassword</span><span class="token punctuation">(</span>PASSWORD<span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">setDataSource</span><span class="token punctuation">(</span>dsc<span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// 包配置</span>
        <span class="token class-name">PackageConfig</span> pc <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">PackageConfig</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
		<span class="token comment">// pc.setModuleName(scanner("输入模块的名称: "));</span>
		<span class="token comment">// pc.setParent("com.makerknz");</span>
        <span class="token class-name">System</span><span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token class-name">CodeTableGenerator</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">.</span><span class="token function">getPackage</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        pc<span class="token punctuation">.</span><span class="token function">setParent</span><span class="token punctuation">(</span><span class="token class-name">CodeTableGenerator</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">.</span><span class="token function">getPackage</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">setPackageInfo</span><span class="token punctuation">(</span>pc<span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// 自定义配置</span>
        <span class="token class-name">InjectionConfig</span> cfg <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">InjectionConfig</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token annotation punctuation">@Override</span>
            <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">initMap</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token comment">// to do nothing</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span><span class="token punctuation">;</span>

        <span class="token comment">// 如果模板引擎是 freemarker</span>
        <span class="token class-name">String</span> templatePath <span class="token operator">=</span> <span class="token string">"/templates/mapper.xml.ftl"</span><span class="token punctuation">;</span>
        <span class="token comment">// 如果模板引擎是 velocity</span>
        <span class="token comment">// String templatePath = "/templates/mapper.xml.vm";</span>

        <span class="token comment">// 自定义输出配置</span>
        <span class="token class-name">List</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">FileOutConfig</span><span class="token punctuation">></span></span> focList <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">ArrayList</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token punctuation">></span></span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token comment">// 自定义配置会被优先输出</span>
        focList<span class="token punctuation">.</span><span class="token function">add</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">FileOutConfig</span><span class="token punctuation">(</span>templatePath<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token annotation punctuation">@Override</span>
            <span class="token keyword">public</span> <span class="token class-name">String</span> <span class="token function">outputFile</span><span class="token punctuation">(</span><span class="token class-name">TableInfo</span> tableInfo<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token comment">// 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！</span>
                <span class="token keyword">return</span> projectPath <span class="token operator">+</span> <span class="token string">"/src/main/resources/mapper/"</span> <span class="token operator">+</span> pc<span class="token punctuation">.</span><span class="token function">getModuleName</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
                        <span class="token operator">+</span> <span class="token string">"/"</span> <span class="token operator">+</span> tableInfo<span class="token punctuation">.</span><span class="token function">getEntityName</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">+</span> <span class="token string">"Mapper"</span> <span class="token operator">+</span> <span class="token class-name">StringPool</span><span class="token punctuation">.</span>DOT_XML<span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token comment">/*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */</span>
        cfg<span class="token punctuation">.</span><span class="token function">setFileOutConfigList</span><span class="token punctuation">(</span>focList<span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">setCfg</span><span class="token punctuation">(</span>cfg<span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// 配置模板</span>
        <span class="token class-name">TemplateConfig</span> templateConfig <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">TemplateConfig</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// 配置自定义输出模板</span>
        <span class="token comment">//指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别</span>
        <span class="token comment">// templateConfig.setEntity("templates/entity2.java");</span>
        <span class="token comment">// templateConfig.setService();</span>
        <span class="token comment">// templateConfig.setController();</span>

        templateConfig<span class="token punctuation">.</span><span class="token function">setXml</span><span class="token punctuation">(</span><span class="token keyword">null</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">setTemplate</span><span class="token punctuation">(</span>templateConfig<span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// 策略配置</span>
        <span class="token class-name">StrategyConfig</span> strategy <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">StrategyConfig</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        strategy<span class="token punctuation">.</span><span class="token function">setNaming</span><span class="token punctuation">(</span><span class="token class-name">NamingStrategy</span><span class="token punctuation">.</span>underline_to_camel<span class="token punctuation">)</span><span class="token punctuation">;</span>
        strategy<span class="token punctuation">.</span><span class="token function">setColumnNaming</span><span class="token punctuation">(</span><span class="token class-name">NamingStrategy</span><span class="token punctuation">.</span>underline_to_camel<span class="token punctuation">)</span><span class="token punctuation">;</span>
		<span class="token comment">// strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");</span>
        strategy<span class="token punctuation">.</span><span class="token function">setEntityLombokModel</span><span class="token punctuation">(</span><span class="token boolean">true</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        strategy<span class="token punctuation">.</span><span class="token function">setRestControllerStyle</span><span class="token punctuation">(</span><span class="token boolean">true</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token comment">// 公共父类</span>
		<span class="token comment">// strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");</span>
        <span class="token comment">// 写于父类中的公共字段</span>
		<span class="token comment">// strategy.setSuperEntityColumns("id");</span>
        strategy<span class="token punctuation">.</span><span class="token function">setInclude</span><span class="token punctuation">(</span><span class="token function">scanner</span><span class="token punctuation">(</span><span class="token string">"表名，多个英文逗号分割"</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">split</span><span class="token punctuation">(</span><span class="token string">","</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        strategy<span class="token punctuation">.</span><span class="token function">setControllerMappingHyphenStyle</span><span class="token punctuation">(</span><span class="token boolean">true</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        strategy<span class="token punctuation">.</span><span class="token function">setTablePrefix</span><span class="token punctuation">(</span>pc<span class="token punctuation">.</span><span class="token function">getModuleName</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">+</span> <span class="token string">"_"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">setStrategy</span><span class="token punctuation">(</span>strategy<span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">setTemplateEngine</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">FreemarkerTemplateEngine</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        mpg<span class="token punctuation">.</span><span class="token function">execute</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br><span class="line-number">20</span><br><span class="line-number">21</span><br><span class="line-number">22</span><br><span class="line-number">23</span><br><span class="line-number">24</span><br><span class="line-number">25</span><br><span class="line-number">26</span><br><span class="line-number">27</span><br><span class="line-number">28</span><br><span class="line-number">29</span><br><span class="line-number">30</span><br><span class="line-number">31</span><br><span class="line-number">32</span><br><span class="line-number">33</span><br><span class="line-number">34</span><br><span class="line-number">35</span><br><span class="line-number">36</span><br><span class="line-number">37</span><br><span class="line-number">38</span><br><span class="line-number">39</span><br><span class="line-number">40</span><br><span class="line-number">41</span><br><span class="line-number">42</span><br><span class="line-number">43</span><br><span class="line-number">44</span><br><span class="line-number">45</span><br><span class="line-number">46</span><br><span class="line-number">47</span><br><span class="line-number">48</span><br><span class="line-number">49</span><br><span class="line-number">50</span><br><span class="line-number">51</span><br><span class="line-number">52</span><br><span class="line-number">53</span><br><span class="line-number">54</span><br><span class="line-number">55</span><br><span class="line-number">56</span><br><span class="line-number">57</span><br><span class="line-number">58</span><br><span class="line-number">59</span><br><span class="line-number">60</span><br><span class="line-number">61</span><br><span class="line-number">62</span><br><span class="line-number">63</span><br><span class="line-number">64</span><br><span class="line-number">65</span><br><span class="line-number">66</span><br><span class="line-number">67</span><br><span class="line-number">68</span><br><span class="line-number">69</span><br><span class="line-number">70</span><br><span class="line-number">71</span><br><span class="line-number">72</span><br><span class="line-number">73</span><br><span class="line-number">74</span><br><span class="line-number">75</span><br><span class="line-number">76</span><br><span class="line-number">77</span><br><span class="line-number">78</span><br><span class="line-number">79</span><br><span class="line-number">80</span><br><span class="line-number">81</span><br><span class="line-number">82</span><br><span class="line-number">83</span><br><span class="line-number">84</span><br><span class="line-number">85</span><br><span class="line-number">86</span><br><span class="line-number">87</span><br><span class="line-number">88</span><br><span class="line-number">89</span><br><span class="line-number">90</span><br><span class="line-number">91</span><br><span class="line-number">92</span><br><span class="line-number">93</span><br><span class="line-number">94</span><br><span class="line-number">95</span><br><span class="line-number">96</span><br><span class="line-number">97</span><br><span class="line-number">98</span><br><span class="line-number">99</span><br><span class="line-number">100</span><br><span class="line-number">101</span><br><span class="line-number">102</span><br><span class="line-number">103</span><br><span class="line-number">104</span><br><span class="line-number">105</span><br><span class="line-number">106</span><br><span class="line-number">107</span><br><span class="line-number">108</span><br><span class="line-number">109</span><br><span class="line-number">110</span><br><span class="line-number">111</span><br><span class="line-number">112</span><br><span class="line-number">113</span><br><span class="line-number">114</span><br><span class="line-number">115</span><br><span class="line-number">116</span><br><span class="line-number">117</span><br><span class="line-number">118</span><br><span class="line-number">119</span><br><span class="line-number">120</span><br><span class="line-number">121</span><br><span class="line-number">122</span><br><span class="line-number">123</span><br><span class="line-number">124</span><br><span class="line-number">125</span><br><span class="line-number">126</span><br><span class="line-number">127</span><br><span class="line-number">128</span><br><span class="line-number">129</span><br><span class="line-number">130</span><br><span class="line-number">131</span><br><span class="line-number">132</span><br><span class="line-number">133</span><br><span class="line-number">134</span><br><span class="line-number">135</span><br><span class="line-number">136</span><br><span class="line-number">137</span><br><span class="line-number">138</span><br><span class="line-number">139</span><br><span class="line-number">140</span><br><span class="line-number">141</span><br><span class="line-number">142</span><br><span class="line-number">143</span><br><span class="line-number">144</span><br><span class="line-number">145</span><br><span class="line-number">146</span><br><span class="line-number">147</span><br><span class="line-number">148</span><br><span class="line-number">149</span><br><span class="line-number">150</span><br><span class="line-number">151</span><br><span class="line-number">152</span><br><span class="line-number">153</span><br><span class="line-number">154</span><br></div></div><p>运行main，并输入生成的表名，以逗号分开</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>cn.makerknz.product.server
请输入表名，多个英文逗号分割：
user,login_logs,product,product_topic_bind,device,connect_logs,device_events,application_token
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br></div></div><h2 id="修改配置" tabindex="-1"><a class="header-anchor" href="#修改配置" aria-hidden="true">#</a> 修改配置</h2>
<h3 id="mybatis-plus配置" tabindex="-1"><a class="header-anchor" href="#mybatis-plus配置" aria-hidden="true">#</a> Mybatis Plus配置</h3>
<p>在config包中添加MybatisPlusConfig.java文件</p>
<p>包括mapper包扫描和分页插件</p>
<div class="language-java ext-java line-numbers-mode"><pre v-pre class="language-java"><code><span class="token keyword">package</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>config</span><span class="token punctuation">;</span>

<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">DbType</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>extension<span class="token punctuation">.</span>plugins<span class="token punctuation">.</span></span><span class="token class-name">MybatisPlusInterceptor</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>extension<span class="token punctuation">.</span>plugins<span class="token punctuation">.</span>inner<span class="token punctuation">.</span></span><span class="token class-name">PaginationInnerInterceptor</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>mybatis<span class="token punctuation">.</span>spring<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">MapperScan</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>context<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">Bean</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>context<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">Configuration</span><span class="token punctuation">;</span>

<span class="token doc-comment comment">/**
 * @Author: maker_knz
 * @Date: 2021/10/27/027 15:01
 * @Version 1.0
 */</span>

<span class="token annotation punctuation">@Configuration</span>
<span class="token annotation punctuation">@MapperScan</span><span class="token punctuation">(</span><span class="token string">"cn.makerknz.product.server.mapper"</span><span class="token punctuation">)</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">MybatisPlusConfig</span> <span class="token punctuation">{</span>

    <span class="token comment">// 最新版分页插件</span>
    <span class="token annotation punctuation">@Bean</span>
    <span class="token keyword">public</span> <span class="token class-name">MybatisPlusInterceptor</span> <span class="token function">mybatisPlusInterceptor</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token class-name">MybatisPlusInterceptor</span> interceptor <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">MybatisPlusInterceptor</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        interceptor<span class="token punctuation">.</span><span class="token function">addInnerInterceptor</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">PaginationInnerInterceptor</span><span class="token punctuation">(</span><span class="token class-name">DbType</span><span class="token punctuation">.</span>H2<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token keyword">return</span> interceptor<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

<span class="token punctuation">}</span>

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br><span class="line-number">20</span><br><span class="line-number">21</span><br><span class="line-number">22</span><br><span class="line-number">23</span><br><span class="line-number">24</span><br><span class="line-number">25</span><br><span class="line-number">26</span><br><span class="line-number">27</span><br><span class="line-number">28</span><br><span class="line-number">29</span><br></div></div><h3 id="数据库配置" tabindex="-1"><a class="header-anchor" href="#数据库配置" aria-hidden="true">#</a> 数据库配置</h3>
<p>在application.yml中添加</p>
<div class="language-yaml ext-yml line-numbers-mode"><pre v-pre class="language-yaml"><code><span class="token key atrule">spring</span><span class="token punctuation">:</span>
  <span class="token key atrule">datasource</span><span class="token punctuation">:</span>
    <span class="token key atrule">driver-class-name</span><span class="token punctuation">:</span> com.mysql.cj.jdbc.Driver
    <span class="token key atrule">url</span><span class="token punctuation">:</span> jdbc<span class="token punctuation">:</span>mysql<span class="token punctuation">:</span>//192.168.31.216<span class="token punctuation">:</span>3307/product_manager<span class="token punctuation">?</span>characterEncoding=utf<span class="token punctuation">-</span>8<span class="token important">&amp;useSSL=false</span>
    <span class="token key atrule">username</span><span class="token punctuation">:</span> product_manager
    <span class="token key atrule">password</span><span class="token punctuation">:</span> product_manager
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br></div></div><h2 id="测试" tabindex="-1"><a class="header-anchor" href="#测试" aria-hidden="true">#</a> 测试</h2>
<p>在user表中添加</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>INSERT INTO `product_manager`.`user`(`id`, `username`, `truename`, `icon`, `password`, `email`, `phone`, `role`, `user_desc`, `create_time`, `update_time`) VALUES (1, 'maker_knz', 'knz', NULL, '123456', '@qq.com', '123121231', 1, '测试', '2021-10-27 15:05:58', '2021-10-27 15:05:58');

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br></div></div><p>在UserController中添加</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>	@Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    public User user(@PathVariable("id") Integer id) {
        return userService.getById(id);
    }
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br></div></div><p>在网页中访问</p>
<p><img src="http://note.makerknz.cn/image-20211027154344509.png" alt="image-20211027154344509"></p>
<h2 id="仓库地址" tabindex="-1"><a class="header-anchor" href="#仓库地址" aria-hidden="true">#</a> 仓库地址</h2>
<p>https://gitee.com/eaooglePlatform_he_jiaqi/product-manager-server/tree/%E9%A1%B9%E7%9B%AE%E5%88%9D%E5%A7%8B%E5%8C%96/</p>
<h2 id="总结" tabindex="-1"><a class="header-anchor" href="#总结" aria-hidden="true">#</a> 总结</h2>
<p>本文介绍了使用IDEA创建产品服务的项目，并使用mybatis-plus-generator自动创建Mapper、Entiry、Service和Controller。并保证项目正常运行。</p>
</template>
