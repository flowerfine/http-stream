package cn.sliew.http.stream.akka.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public enum BeanUtil {
    ;

    private static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<>();

    /**
     * object copy
     *
     * @param srcObj  :  原始值
     * @param destObj : 目标值
     * @return: T
     */
    public static <T> T copy(Object srcObj, T destObj) {
        if (srcObj == null) {
            return destObj;
        }
        String key = genKey(srcObj.getClass(), destObj.getClass());
        BeanCopier copier;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, destObj, null);
        return destObj;
    }

    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + destClazz.getName();
    }
}