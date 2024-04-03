package info.juangnakarani.springmultitenant.repository;

import info.juangnakarani.springmultitenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSourceConfigRepository extends JpaRepository<Tenant, Long> {
}
