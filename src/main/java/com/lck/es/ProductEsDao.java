package com.lck.es;

import com.lck.pojo.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * describe:不能放在dao层下，因为dao层做了链接redis，会彼此影响
 *
 * @author lichangkai
 * @date 2019/04/30
 */
public interface ProductEsDao extends ElasticsearchRepository<Product,Integer> {
    
}
