package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.pojo.Tenant;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Component
public class MultitenantMaster {

    @Bean
    public DataSource multitenantMasterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenant_master");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");

        return dataSource;
    }

    @Bean
    public List<Tenant> listTenant() throws SQLException {
        List<Tenant> tenantList = new ArrayList<>();
        PreparedStatement pstmt = MultitenantMaster.this.multitenantMasterDataSource().getConnection().prepareStatement("select * from tenants");
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            String name = rs.getString("name");
            Tenant tenant = new Tenant();
            tenant.setName(name);
            tenantList.add(tenant);
        }
        return tenantList;
    }

}
