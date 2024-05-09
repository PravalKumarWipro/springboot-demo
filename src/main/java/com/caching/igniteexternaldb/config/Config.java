package com.caching.igniteexternaldb.config;

import com.caching.igniteexternaldb.model.Person;
import com.ibm.db2.jcc.DB2DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.DB2Dialect;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Configuration
public class Config {

    @Bean
    public Ignite createClient(){
        IgniteConfiguration igniteCfg = new IgniteConfiguration();
        CacheConfiguration<Integer, Person> personCacheCfg = new CacheConfiguration<>();

        personCacheCfg.setName("PersonCache");
        personCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        personCacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        personCacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        personCacheCfg.setReadThrough(true);
        personCacheCfg.setWriteThrough(true);

        CacheJdbcPojoStoreFactory<Integer, Person> factory = new CacheJdbcPojoStoreFactory<>();
        factory.setDialect(new DB2Dialect());

          factory.setDataSourceFactory((Factory<DataSource>)() -> {
              DB2DataSource dataSource = new DB2DataSource();
              dataSource.setUser("db2inst1");
              dataSource.setPassword("root");
              dataSource.setServerName("172.17.0.2");
              dataSource.setDatabaseName("ignitedb");
              dataSource.setPortNumber(50000); //
              dataSource.setCurrentSchema("ignitedb");
              dataSource.setDriverType(4);
            return dataSource;
        });

        JdbcType personType = new JdbcType();
        personType.setCacheName("PersonCache");
        personType.setKeyType(Integer.class);
        personType.setValueType(Person.class);
        personType.setDatabaseTable("ignitedb.Person");

        personType.setKeyFields(new JdbcTypeField(java.sql.Types.INTEGER, "id", Integer.class, "id"));

        personType.setValueFields(
                new JdbcTypeField(java.sql.Types.INTEGER, "id", Integer.class, "id"),
                new JdbcTypeField(java.sql.Types.VARCHAR, "name", String.class, "name"));

        factory.setTypes(personType);

        personCacheCfg.setCacheStoreFactory(factory);

        QueryEntity qryEntity = new QueryEntity();

        qryEntity.setKeyType(Integer.class.getName());
        qryEntity.setValueType(Person.class.getName());
        qryEntity.setKeyFieldName("id");

        Set<String> keyFields = new HashSet<>();
        keyFields.add("id");
        qryEntity.setKeyFields(keyFields);

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "java.lang.Integer");
        fields.put("name", "java.lang.String");

        qryEntity.setFields(fields);

        personCacheCfg.setQueryEntities(Collections.singletonList(qryEntity));

        igniteCfg.setCacheConfiguration(personCacheCfg);
        Ignite ignite = Ignition.start(igniteCfg);
        System.out.println("ignite >>>>>>>>>>>>>>>>>>>>>>>>"+ignite);
        return ignite;
    }
}
