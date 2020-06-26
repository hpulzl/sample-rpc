package com.awo.sample.common.spi;

import com.awo.sample.common.util.Holder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * @author: Create by awo
 * @date: 2020/4/19
 * @Discription: 扩展类
 * @see: http://dubbo.apache.org/zh-cn/blog/introduction-to-dubbo-spi.html
 * @see: http://dubbo.apache.org/zh-cn/blog/introduction-to-dubbo-spi-2.html
 **/
public class ExtensionLoader<T> {

    /**
     * 每个type持有一个ExtensionLoader
     */
    private static final ConcurrentHashMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER = new ConcurrentHashMap<>();

    /**
     * 实例缓存
     */
    private static final ConcurrentHashMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    /**
     * 对象缓存
     */
    private static final Holder<Map<String, Class<?>>> cacheClasses = new Holder<>();

    private static final Holder<Object> cacheAdaptiveInstance = new Holder<>();

    /**
     * 实例缓存
     */
    private static final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 装饰类缓存
     */
    private Set<Class<?>> wrapperCaches;

    private volatile Class<?> cachedAdaptiveClass = null;

    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");

    private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();

    /**
     * 加载地址
     */
    private static final String SAMPLE_FILE = "META-INF/sample/";

    private final Class<?> type;

    private ExtensionFactory objectFactory;

    private ExtensionLoader(Class<T> type) {
        this.type = type;
        // 获取 ExtensionFactory
//        objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
    }

    /**
     * 通过接口，获取ExtensionLoader
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type 不能为空");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("extension type" + type + "必须是接口");
        }
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("extension type 没有" + SPI.class.getSimpleName() + "修饰");
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADER.get(type);
        if (extensionLoader == null) {
            extensionLoader = (ExtensionLoader<T>) new ExtensionLoader<>(type);
            EXTENSION_LOADER.putIfAbsent(type, extensionLoader);
            return extensionLoader;
        }
        return extensionLoader;
    }

    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    public T getExtension(String name) {
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<Object>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        // 如果没有创建实例，则创建(double check)
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) holder.get();
    }

    /**
     * 实例化扩展对象
     *
     * @param name
     * @return
     */
    private T createExtension(String name) {
        // 实例化对象
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new NullPointerException("extension type " + name + " 不存在");
        }
        // 先从缓存中拿实例
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        try {
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            // 对扩展类进行依赖注入(注入set等操作)（支持IOC的做法）
            injectExtension(instance);
            // 如果有wrapper，添加wrapper （支持AOP）
            Set<Class<?>> wrapperClasses = wrapperCaches;
            if (wrapperClasses != null && !wrapperCaches.isEmpty()) {
                for (Class wrapperClass : wrapperClasses) {
                    instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
                }
            }
            return (T) instance;
        } catch (Throwable t) {
            throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    type + ")  could not be instantiated: " + t.getMessage(), t);
        }
    }

    /**
     * 通过 ExtensionFactory装配对象
     * 通过java标准的setter进行装配，实现类包括
     * SPIExtensionFactory AdaptiveExtensionFactory
     *
     * @param instance
     * @return
     */
    private T injectExtension(T instance) {
        if (objectFactory == null) {
            return instance;
        }
        for (Method method : instance.getClass().getMethods()) {
            if (method.getName().startsWith("set")
                    && method.getParameterTypes().length == 1
                    && Modifier.isPublic(method.getModifiers())) {
                Class<?> pt = method.getParameterTypes()[0];
                //  截取set的参数
                String property = method.getName().length() > 3 ?
                        method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : "";
                // TODO 没有实现
                Object obj = objectFactory.getExtension(pt, property);
                if (obj != null) {
                    try {
                        method.invoke(instance, obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 线程安全的get
     *
     * @return
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> cacheClass = cacheClasses.get();
        if (cacheClass == null) {
            synchronized (cacheClasses) {
                cacheClass = cacheClasses.get();
                if (cacheClass == null) {
                    cacheClass = loadExtensionClasses();
                    cacheClasses.set(cacheClass);
                }
            }
        }
        return cacheClass;
    }

    /**
     * 从配置文件中加载扩展类
     *
     * @return
     */
    private Map<String, Class<?>> loadExtensionClasses() {
        // 校验SPI的合法性
        SPI annotation = type.getAnnotation(SPI.class);
        if (annotation != null) {
            String value = annotation.value();
            if (!"".equals(value)) {
                String[] values = value.split(",");
                if (values.length > 0) {
                    throw new IllegalStateException("extension type " + type + "不合法");
                }
            }
        }
        Map<String, Class<?>> extensionClasses = new HashMap<String, Class<?>>();
        loadDirectory(extensionClasses, SAMPLE_FILE);
        return extensionClasses;
    }

    /**
     * 获取到文件路径
     *
     * @param extensionClasses
     * @param sampleFile
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String sampleFile) {
        String fileName = sampleFile + type.getName();
        try {
            Enumeration<URL> resources;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            if (classLoader != null) {
                resources = classLoader.getResources(fileName);
            } else {
                resources = ClassLoader.getSystemResources(fileName);
            }
            if (resources != null) {
                while (resources.hasMoreElements()) {
                    java.net.URL resourceURL = resources.nextElement();
                    loadResource(extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException("extension classes" + fileName + " 文件读取失败");
        }
    }

    /**
     * 按照格式读取文件
     * eg: name = com.awo.sample.spi.JavaServiceImpl #设置name-value
     *
     * @param extensionClasses
     * @param classLoader
     * @param resourceURL
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceURL) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                int len = line.indexOf("#");
                if (len >= 0) {
                    line = line.substring(0, len);
                }
                line = line.trim();
                if (line.length() > 0) {
                    int nameLen = line.indexOf("=");
                    String name = line.substring(0, nameLen).trim();
                    String className = line.substring(nameLen + 1).trim();

                    if (className.length() > 0) {
                        try {
                            loadClass(extensionClasses, classLoader, Class.forName(className, true, classLoader), name);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadClass(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, Class<?> clazz, String name) throws NoSuchMethodException {
        // 是否接口子类型
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("error (接口:" + type + "class line:" + clazz.getName() + "),class" + clazz.getName() + " 不是接口的子类");
        }
        // 如果是adaptive注解
        // 如果是wrapper类（判断构造函数入参是不是自身的类）
        clazz.getConstructor();
        String[] names = NAME_SEPARATOR.split(name);
        if (names != null && names.length > 0) {
            for (String n : names) {
                cacheName(clazz, name);
                saveInExtensionClass(extensionClasses, clazz, n);
            }
        }
    }

    private void saveInExtensionClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name) {
        Class<?> c = extensionClasses.get(name);
        if (c == null) {
            extensionClasses.put(name, clazz);
        } else if (c != clazz) {
            throw new IllegalStateException("错误的注册类，请检查");
        }
    }

    private void cacheName(Class<?> clazz, String name) {
        if (!cachedNames.containsKey(clazz)) {
            cachedNames.put(clazz, name);
        }
    }

    /**
     * 扩展点自定义实现类（查找扩展类所有的实现类-->如果没有会使用javassist实现一个）
     *
     * @return
     */
    public T getAdaptiveExtension() {
        Object instance = cacheAdaptiveInstance.get();
        if (instance == null) {
            synchronized (cacheAdaptiveInstance) {
                instance = cacheAdaptiveInstance.get();
                if (instance == null) {
                    instance = createAdaptiveExtension();
                    cacheAdaptiveInstance.set(instance);
                }
            }
        }
        return (T) instance;
    }

    private T createAdaptiveExtension() {
        try {
            return injectExtension((T) getAdaptiveExtensionClass().newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<?> getAdaptiveExtensionClass() {
        getExtensionClasses();
        if (cachedAdaptiveClass != null) {
            return cachedAdaptiveClass;
        }
        return cachedAdaptiveClass = createAdaptiveExtensionClass();
    }

    private Class<?> createAdaptiveExtensionClass() {
        // TODO 创建扩展类自适应实现，默认实现javassist(有些复杂，暂时不实现了)
        return null;
    }

    public Set<String> getSupportedExtensions() {
        Map<String, Class<?>> clazzes = getExtensionClasses();
        return Collections.unmodifiableSet(new TreeSet<>(clazzes.keySet()));
    }
}
