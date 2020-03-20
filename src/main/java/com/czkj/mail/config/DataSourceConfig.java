package com.czkj.mail.config;

import org.jfaster.mango.datasource.SimpleDataSourceFactory;
import org.jfaster.mango.operator.Mango;
import org.jfaster.mango.plugin.page.MySQLPageInterceptor;
import org.jfaster.mango.plugin.spring.MangoDaoScanner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 数据库相关配置
 * @Author steven.sheng
 * @Date 2019/3/4/004.
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public MangoDaoScanner mangoDaoScanner(){
        MangoDaoScanner scanner = new MangoDaoScanner();
        scanner.setPackages(Arrays.asList("com.czkj.mail.dao"));
        return scanner;
    }

    @Bean
    public Mango mango(){
        SimpleDataSourceFactory factory = new SimpleDataSourceFactory(dataSource());
        Mango mango = Mango.newInstance(factory);
        mango.addInterceptor(new MySQLPageInterceptor());
        return mango;
    }

    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }
}
