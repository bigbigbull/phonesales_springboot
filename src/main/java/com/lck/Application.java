package com.lck;

import com.lck.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
@SpringBootApplication
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.lck.es")
@EnableJpaRepositories(basePackages = {"com.lck.dao", "com.lck.pojo"})
public class Application {
    static {
        PortUtil.checkPort(6379, "Redis 服务端", true);
        PortUtil.checkPort(9300, "ElasticSearch 服务端", true);
        PortUtil.checkPort(5601, "Kibana 工具", true);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
