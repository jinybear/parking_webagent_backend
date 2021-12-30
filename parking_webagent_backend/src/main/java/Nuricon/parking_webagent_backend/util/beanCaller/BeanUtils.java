package Nuricon.parking_webagent_backend.util.beanCaller;

import Nuricon.parking_webagent_backend.service.UserService;
import org.springframework.context.ApplicationContext;

/**
 * new로 생성되는 객체에서 Bean 객체를 호출해야 할 경우 사용하는 class
 */
public class BeanUtils {

    public static Object getBean(Class<?> classType) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(classType);
    }
}
