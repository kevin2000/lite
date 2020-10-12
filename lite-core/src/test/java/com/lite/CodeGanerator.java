package com.lite;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGanerator {
	public static void main(String[] args) {
		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		System.out.println("projectPath:" + projectPath);
		gc.setOutputDir(projectPath + "/src/main/java");
		gc.setAuthor("joe");
		gc.setOpen(false);
		// gc.setSwagger2(true); 实体属性 Swagger2 注解
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl("jdbc:mysql://localhost:3306/lite?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8");
		dsc.setSchemaName("public");
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("123456");
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent("com.lite.system");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};

		// 如果模板引擎是 freemarker
		// String templatePath = "/templates/mapper.xml.ftl";
		// 如果模板引擎是 velocity
		String templatePath = "/templates/mapper.xml.vm";
		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(templatePath) {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
				return projectPath + "/src/main/resources/mapper/sys/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
			}
		});
		
		// 调整 domain 生成目录演示
        focList.add(new FileOutConfig("/templates/generator/Myservice.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                //输出的位置
                return projectPath + "/src/main/java/com/lite/sys/myService/" + tableInfo.getEntityName() + "Service.java";
            }
        });
		
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);
		
		
		TemplateConfig tc = new TemplateConfig();
		tc.setXml(null);
		//tc.setServiceImpl("/templates/generator/service.java.vm");
		tc.setService(null);
		mpg.setTemplate(tc);
		
		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);

		// 是否启动lombok
		strategy.setEntityLombokModel(true);
		strategy.setRestControllerStyle(true);

		// 策略配置
		//strategy.setInclude("sys_user");// 需要自动生成的表名
		strategy.setControllerMappingHyphenStyle(true);
		strategy.setTablePrefix(pc.getModuleName() + "_");
		// strategy.setSuperEntityColumns("id");
		// strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");//
		// Entity公共父类
		// strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");//
		// Controller公共父类
		mpg.setStrategy(strategy);
		// mpg.setTemplateEngine(new
		// FreemarkerTemplateEngine());//如果有此代码，即不使用默认的velocity模板，使用Freemarker模板
		mpg.execute();
	}
}
