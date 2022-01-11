package cn.iocoder.yudao.adminserver.modules.bpm.framework.activiti.config;

import cn.iocoder.yudao.adminserver.modules.bpm.framework.activiti.core.behavior.BpmActivityBehaviorFactory;
import cn.iocoder.yudao.adminserver.modules.bpm.framework.activiti.core.listener.BpmTackActivitiEventListener;
import cn.iocoder.yudao.adminserver.modules.bpm.service.definition.BpmTaskRuleService;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import static org.activiti.spring.boot.ProcessEngineAutoConfiguration.BEHAVIOR_FACTORY_MAPPING_CONFIGURER;

/**
 * BPM 模块的 Activiti 配置类
 */
@Configuration
public class BpmActivitiConfiguration {

    /**
     * BPM 模块的 ProcessEngineConfigurationConfigurer 实现类，主要设置各种监听器
     */
    @Bean
    public ProcessEngineConfigurationConfigurer bpmProcessEngineConfigurationConfigurer(
            BpmTackActivitiEventListener taskActivitiEventListener) {
        return configuration -> {
            // 注册监听器，例如说 BpmActivitiEventListener
            configuration.setEventListeners(Collections.singletonList(taskActivitiEventListener));
        };
    }

    /**
     * 用于设置自定义的 ActivityBehaviorFactory 实现的 ProcessEngineConfigurationConfigurer 实现类
     *
     * 目的：覆盖 {@link org.activiti.spring.boot.ProcessEngineAutoConfiguration} 的
     *      defaultActivityBehaviorFactoryMappingConfigurer 方法创建的 Bean
     */
    @Bean(name = BEHAVIOR_FACTORY_MAPPING_CONFIGURER)
    public ProcessEngineConfigurationConfigurer defaultActivityBehaviorFactoryMappingConfigurer(
            BpmActivityBehaviorFactory bpmActivityBehaviorFactory) {
        return configuration -> {
            // 设置 ActivityBehaviorFactory 实现类，用于流程任务的审核人的自定义
            configuration.setActivityBehaviorFactory(bpmActivityBehaviorFactory);
        };
    }

    @Bean
    public BpmActivityBehaviorFactory bpmActivityBehaviorFactory(BpmTaskRuleService taskRuleService) {
        BpmActivityBehaviorFactory bpmActivityBehaviorFactory = new BpmActivityBehaviorFactory();
        bpmActivityBehaviorFactory.setBpmTaskRuleService(taskRuleService);
        return bpmActivityBehaviorFactory;
    }

}
