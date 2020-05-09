package com.awo.sample.common.spi;

import com.awo.sample.common.util.Holder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final ConcurrentHashMap<Class<?>, Object> EXTENSION_INSTANCE = new ConcurrentHashMap<>();

    /**
     * 对象缓存
     */
    private static final Holder<Map<String, Class<?>>> cacheClasses = new Holder<>();

    /**
     * 加载地址
     */
    private static final String SAMPLE_FILE = "META-INF/sample";

    private final Class<?> type;

    private ExtensionLoader(Class<T> type) {
        this.type = type;
        // TODO 实例化ExtensionLoader
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
        return null;
    }

    /**
     * 实例化扩展对象
     *
     * @param name
     * @return
     */
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new NullPointerException("extension type" + name + " 不存在");
        }
        // 先从缓存中拿实例
        T instance = (T) EXTENSION_INSTANCE.get(clazz);
        try {
            if (instance == null) {
                EXTENSION_INSTANCE.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCE.get(clazz);
            }
            injectExtension(instance);
            return (T) clazz;
        } catch (Throwable t) {
            throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    type + ")  could not be instantiated: " + t.getMessage(), t);
        }
    }

    /**
     * 注入扩展的对象
     *
     * @param instance
     */
    private void injectExtension(T instance) {

    }

    /**
     * 线程安全的get
     *
     * @return
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> cacheClass = cacheClasses.get();
        if (cacheClass == null) {
            synchronized (cacheClass) {
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
            String[] values = value.split(",");
            if (values.length > 0) {
                throw new IllegalStateException("extension type " + type + "不合法");
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
                        loadClass(extensionClasses, classLoader, Class.forName(className, true, classLoader), name);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadClass(Map<String, Class<?>> extensionClasses, ClassLoader classLoader,Class<?> clazz,String name){

    }
    public T getAdaptiveExtension() {
        return null;
    }
}
