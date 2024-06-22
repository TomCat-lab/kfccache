package io.github.tomcatlab.kfccache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KfcApplicationListener implements ApplicationListener<ApplicationEvent> {
@Autowired
List<KfcPlugin> plugins;
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEvent){
            for (KfcPlugin plugin : plugins) {
                plugin.init();
                plugin.startUp();
            }
        }else if (event instanceof ContextClosedEvent){
            for (KfcPlugin plugin : plugins) {
                plugin.shutdown();
            }
        }
    }
}
