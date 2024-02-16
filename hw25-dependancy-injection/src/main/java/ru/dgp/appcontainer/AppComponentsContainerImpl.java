package ru.dgp.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.dgp.appcontainer.api.AppComponent;
import ru.dgp.appcontainer.api.AppComponentsContainer;
import ru.dgp.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Object config;

        config = createConfig(configClass);

        var beanMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .toList();

        List<ConfigBeanPresentation> beanPresentations = beanMethods.stream()
                .map(b -> getConfigBeanPresentation(b)).toList();

        beanPresentations.stream().sorted(new BeanConfigurationComparator()).forEach(bean -> {
            if (appComponentsByName.get(bean.name) != null) {
                throw new AppComponentContainerException(
                        "Component with name [%s] is already exists.".formatted(bean.name));
            }

            Object obj = createBean(bean.method, config);

            appComponentsByName.put(bean.name, obj);
            appComponents.add(obj);
        });
    }

    private static ConfigBeanPresentation getConfigBeanPresentation(Method b) {
        var appComponentAnnotation = b.getAnnotation(AppComponent.class);
        return new ConfigBeanPresentation(appComponentAnnotation.order(),
                appComponentAnnotation.name(), b);
    }

    private static Object createConfig(Class<?> configClass) {
        Object config;
        try {
            var constr = configClass.getConstructor();
            config = constr.newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new AppComponentContainerException(e);
        }
        return config;
    }

    private Object createBean(Method method, Object config) {
        var paramTypes = method.getParameterTypes();

        Object[] args = Arrays.stream(paramTypes).map(this::getBeanFor).toArray();

        try {
            return method.invoke(config, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AppComponentContainerException(e);
        }
    }

    private Object getBeanFor(Class<?> clazz) {
        var beans = appComponents.stream()
                .filter(c -> c.getClass().equals(clazz) || hasInterfaceOf(c, clazz))
                .toList();

        if (beans.size() > 1) {
            throw new AppComponentContainerException("There are more than one bean of type [%s]".formatted(clazz));
        }

        return beans.get(0);
    }

    private boolean hasInterfaceOf(Object c, Class<?> clazz) {
        return Arrays.asList(c.getClass().getInterfaces()).contains(clazz);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        var component = getBeanFor(componentClass);
        return (C) component;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var component = (C) appComponentsByName.get(componentName);

        if (component == null) {
            throw new AppComponentContainerException("No component with name [%s]".formatted(componentName));
        }

        return component;
    }

    private static class ConfigBeanPresentation {
        private final int order;

        private final String name;

        private final Method method;

        public ConfigBeanPresentation(int order, String name, Method method) {
            this.order = order;
            this.name = name;
            this.method = method;
        }
    }

    private static class BeanConfigurationComparator implements Comparator<ConfigBeanPresentation> {
        @Override
        public int compare(ConfigBeanPresentation p1, ConfigBeanPresentation p2) {
            return Integer.compare(p1.order, p2.order);
        }
    }
}
