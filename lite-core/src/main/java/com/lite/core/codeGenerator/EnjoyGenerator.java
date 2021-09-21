package com.lite.core.codeGenerator;

import java.io.File;

import com.jfinal.kit.Kv;

public class EnjoyGenerator {
	public static final EnjoyGenerator me = new EnjoyGenerator();
	protected final EnjoyEngine enjoyEngine = new EnjoyEngine();

	protected Kv tablemetaMap = null;
	protected String packageBase = "com.momathink";
	protected String srcFolder = "src";
	protected String viewFolder = "_view";
	protected String basePath = "";
	protected String outputPath = "D:" + File.separator + "autoCodes"; 

	public EnjoyGenerator setPackageBase(String packageBase) {
		this.packageBase = packageBase;
		return this;
	}

	public EnjoyGenerator setBasePath(String basePath) {
		this.basePath = basePath;
		return this;
	}

	public EnjoyGenerator setSrcFolder(String srcFolder) {
		this.srcFolder = srcFolder;
		return this;
	}

	public EnjoyGenerator setViewFolder(String viewFolder) {
		this.viewFolder = viewFolder;
		return this;
	}

	protected class DataGenerator extends DataDictionaryGenerator {
		public DataGenerator(DataSource dataSource, String dataDictionaryOutputDir) {
			super(dataSource, dataDictionaryOutputDir);
		}

		public void rebuildColumnMetas(List<TableMeta> tableMetas) {
			super.rebuildColumnMetas(tableMetas);
		}
	};

	public TableMeta getTableMeta(String tableName) {
		if (tablemetaMap == null) {

			DataSource dataSource = _JFinalDemoGenerator.getDataSource();

			MetaBuilder metaBuilder = new MetaBuilder(dataSource);
			metaBuilder.setDialect(new MysqlDialect());
			// metaBuilder.addExcludedTable(_JFinalDemoGenerator.excludedTable);
			List<TableMeta> tableMetas = metaBuilder.build();
			new DataGenerator(dataSource, null).rebuildColumnMetas(tableMetas);

			if (tableMetas.size() == 0) {
				System.out.println("TableMeta 数量为 0，不生成任何文件");
				return null;
			}
			Kv kv = Kv.create();
			for (TableMeta tableMeta : tableMetas) {
				kv.set(tableMeta.name, tableMeta);
			}
			tablemetaMap = kv;
		}
		return (TableMeta) tablemetaMap.get(tableName);
	}

	/**
	 * 生成手脚架代码
	 */
	public EnjoyGenerator allRender(String className, String tableName) {
		return javaRender(className, tableName).htmlRender(className, tableName);
	}

	/**
	 * java 代码生成
	 */
	public EnjoyGenerator javaRender(String className, String tableName) {
		// 刷新 映射对象
		_JFinalDemoGenerator.main(null);

		controller(className);
		validator(className);
		service(className, tableName);
		return this;
	}

	private String toClassNameSmall(String className) {
		return new StringBuffer(className.substring(0, 1).toLowerCase()).append(className.substring(1)).toString();
	}

	private String toPackages(String className) {
		return new StringBuffer(packageBase).append(".").append(basePath).append(".").append(className.toLowerCase())
				.toString();
	}

	/**
	 * 生成Controller
	 * 
	 * @param className 类名称
	 */
	public void controller(String className) {
		String packages = toPackages(className);

		String classNameSmall = toClassNameSmall(className);

		enjoyEngine.render("/java/controller.html",
				Kv.by("package", packages).set("className", className).set("classNameSmall", classNameSmall)
						.set("basePath", basePath),
				new StringBuilder().append(System.getProperty("user.dir")).append("/").append(srcFolder).append("/")
						.append(packages.replace(".", "/")).append("/").append(className).append("Controller.java"));
	}

	/**
	 * 生成validator
	 * 
	 * @param className 类名称
	 */
	public void validator(String className) {
		String packages = toPackages(className);

		String classNameSmall = toClassNameSmall(className);

		enjoyEngine.render("/java/validator.html",
				Kv.by("package", packages).set("className", className).set("classNameSmall", classNameSmall),
				new StringBuilder().append(System.getProperty("user.dir")).append("/").append(srcFolder).append("/")
						.append(packages.replace(".", "/")).append("/").append(className).append("Validator.java"));
	}

	/**
	 * 生成Service
	 * 
	 * @param className 类名称
	 * @param tableName 表名
	 */
	public void service(String className, String tableName) {
		String packages = toPackages(className);

		String classNameSmall = toClassNameSmall(className);

		enjoyEngine.render("/java/service.html",
				Kv.by("package", packages).set("className", className).set("classNameSmall", classNameSmall)
						.set("tableName", tableName),
				new StringBuilder().append(System.getProperty("user.dir")).append("/").append(srcFolder).append("/")
						.append(packages.replace(".", "/")).append("/").append(className).append("Service.java"));
	}

	/**
	 * @param className
	 * @param tableName
	 */
	public EnjoyGenerator htmlRender(String className, String tableName) {
		TableMeta tablemeta = getTableMeta(tableName);

		htmlList(className, tablemeta);

		return this;
	}

	// 页面的生成一般定制比较多..就来个简单的吧

	public void htmlList(String className, Class<?> clz) {
		String packages = toPackages(className);
		String classNameSmall = toClassNameSmall(className);
		String basePathUrl = basePath.replace('.', '/');

		enjoyEngine.render("/html/list.html",
				Kv.by("entityMeta", clz).set("package", packages).set("className", className)
						.set("classNameSmall", classNameSmall).set("basePath", basePathUrl),
				new StringBuilder().append(outputPath).append("/").append(viewFolder).append("/")
						.append(basePathUrl).append("/").append(classNameSmall).append("/").append(classNameSmall)
						.append("List.html"));
	}

	// ... 继续扩展吧~
}
