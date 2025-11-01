package com.dinhhuan.commons.uid;

import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.baidu.fsg.uid.worker.dao.WorkerNodeDAO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MybatisUid.class)
public class CacheUid {
    @Bean
    @ConditionalOnMissingBean(name = "disposableWorkerIdAssigner")
    public WorkerIdAssigner disposableWorkerIdAssigner(WorkerNodeDAO workerNodeDAO) {
        return new DisposableWorkerIdAssigner(workerNodeDAO);
    }
    @Bean
    public CachedUidGenerator cachedUidGenerator(WorkerIdAssigner assigner) {
        CachedUidGenerator generator = new CachedUidGenerator();
        generator.setWorkerIdAssigner(assigner);
        generator.setTimeBits(29);
        generator.setWorkerBits(21);
        generator.setSeqBits(13);
        generator.setEpochStr("2025-08-01");
        return generator;
    }
}
