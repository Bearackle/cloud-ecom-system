package com.dinhhuan.commons.uid;


import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@Getter
@Configuration
@ComponentScan(basePackages = "com.baidu.fsg.uid")
@MapperScan(
        basePackages = "com.baidu.fsg.uid.worker.dao",
        sqlSessionFactoryRef = "uidSqlSessionFactory"
)
@EnableTransactionManagement
public class MybatisUid {
    @Value("${postgres.driver}")
    private String driverClassName;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${datasource.filters}")
    private String filters;

    @Value("${datasource.initialSize}")
    private int initialSize;

    @Value("${datasource.minIdle}")
    private int minIdle;

    @Value("${jdbc.maxActive}")
    private int maxActive;

    @Value("${datasource.maxWait}")
    private int maxWait;

    @Value("${datasource.defaultAutoCommit}")
    private boolean defaultAutoCommit;

    @Value("${datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${datasource.validationQuery}")
    private String validationQuery;

    @Value("${datasource.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${datasource.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;

    @Value("${datasource.logAbandoned}")
    private boolean logAbandoned;

    @Value("${datasource.removeAbandoned}")
    private boolean removeAbandoned;

    @Value("${datasource.removeAbandonedTimeout}")
    private int removeAbandonedTimeout;

    @Bean(name = "uidDataSource")
    public DataSource dataSource() throws SQLException {
        System.out.println("=== UID DataSource Config ===");
        System.out.println("URL: " + url);
        System.out.println("Driver: " + driverClassName);
        System.out.println("Username: " + username);
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setFilters(filters);
        ds.setInitialSize(initialSize);
        ds.setMinIdle(minIdle);
        ds.setMaxActive(maxActive);
        ds.setMaxWait(maxWait);
        ds.setDefaultAutoCommit(defaultAutoCommit);
        ds.setTestWhileIdle(testWhileIdle);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setTestOnReturn(testOnReturn);
        ds.setValidationQuery(validationQuery);
        ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        ds.setLogAbandoned(logAbandoned);
        ds.setRemoveAbandoned(removeAbandoned);
        ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        return ds;
    }
    @Bean(name = "uidSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("uidDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/META-INF/mybatis/mapper/WORKER*.xml")
        );
        return factory.getObject();
    }
    @Bean(name = "uidTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("uidDataSource") DataSource  dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "uidBatchSqlSession")
    public SqlSessionTemplate batchSqlSession(@Qualifier("uidSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    }
}
