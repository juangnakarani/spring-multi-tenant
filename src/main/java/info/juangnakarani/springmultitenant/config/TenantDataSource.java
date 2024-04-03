package info.juangnakarani.springmultitenant.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import info.juangnakarani.springmultitenant.entity.Tenant;
import info.juangnakarani.springmultitenant.repository.DataSourceConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;


@Component
public class TenantDataSource implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2418234625461365801L;
    private Map<Long, DataSource> dataSources = new HashMap<>();

    @Autowired
    private DataSourceConfigRepository configRepo;

    @PostConstruct
    public Map<Long, DataSource> getAllTenantDS() {
        List<Tenant> dbList = configRepo.findAll();

        Map<Long, DataSource> result = dbList.stream()
                .collect(Collectors.toMap(Tenant::getId, db -> getDataSource(db.getId())));

        return result;
    }

    public DataSource getDataSource(Long dbId) {
        if (dataSources.get(dbId) != null) {
            return dataSources.get(dbId);
        }
        DataSource dataSource = createDataSource(dbId);
        if (dataSource != null) {
            dataSources.put(dbId, dataSource);
        }
        return dataSource;
    }

    private DataSource createDataSource(Long dbId) {
        Optional<Tenant> db = configRepo.findById(dbId);
        if (db != null) {
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName("oracle.jdbc.OracleDriver")
                    .username(db.get().getName()).password(db.get().getPassword())
                    .url("jdbc:postgresql:" + db.get().getUrl());
            DataSource ds = (DataSource) factory.build();
            return ds;
        }
        return null;
    }
}
