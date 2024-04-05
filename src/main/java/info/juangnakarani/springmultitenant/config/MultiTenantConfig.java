package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.pojo.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "info.juangnakarani.springmultitenant", transactionManagerRef = "transcationManager", entityManagerFactoryRef = "entityManager")
@RequiredArgsConstructor
public class MultiTenantConfig {

//    @Value("${defaultTenant}")
//    private String defaultTenant;


    @Autowired
    public TenantConnection tenantConnection;

    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource()).packages("info.juangnakarani.springmultitenant").build();
    }

    @Bean(name = "transcationManager")
    public JpaTransactionManager transactionManager(
            @Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }

    @Bean
    @Primary
    public DataSource dataSource(){

        tenantConnection.createDatabase("tenant_master");
        DataSource tenantMasterDataSource = tenantConnection.tenantDataSource("tenant_master");
        tenantConnection.initMasterDb(tenantMasterDataSource);

        tenantConnection.createDatabase("tenant_default");
//        tenantConnection.createCustomerTable("tenant_default");

        Properties prop = null;
        try {
            prop = tenantConnection.tenantProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        try {
            List<Tenant> tenantList = tenantConnection.listTenant("tenant_master");
            for(Tenant tenant : tenantList) {
                DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
                dataSourceBuilder.url("jdbc:postgresql://localhost:5432/"+ tenant.getName());
                dataSourceBuilder.driverClassName("org.postgresql.Driver");
                dataSourceBuilder.username(prop.getProperty("db.username"));
                dataSourceBuilder.password(prop.getProperty("db.password"));
                resolvedDataSources.put(tenant.getName(), dataSourceBuilder.build());
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        String firstKey = (String) resolvedDataSources.keySet().iterator().next();


        AbstractRoutingDataSource dataSource = new MultiTenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(firstKey));
        dataSource.setTargetDataSources(resolvedDataSources);

        dataSource.afterPropertiesSet();
        return dataSource;
    }

}