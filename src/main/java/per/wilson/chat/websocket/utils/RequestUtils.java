package per.wilson.chat.websocket.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wilson
 */
public class RequestUtils {
    private static final String AND_SIGN = "&";
    private static final String EQUAL_SIGN = "=";
    private static final String QUESTION_SIGN = "?";

    /**
     * 将路径参数转换成Map对象，如果路径参数出现重复参数名，将以最后的参数值为准
     *
     * @param uri 传入的携带参数的路径
     * @return
     */
    public static Map<String, String> getParams(String uri) {
        Map<String, String> params = new HashMap<>(10);
        int idx = uri.indexOf(QUESTION_SIGN);
        if (idx > -1) {
            String[] paramsArr = uri.substring(idx + 1).split(AND_SIGN);
            for (String param : paramsArr) {
                idx = param.indexOf(EQUAL_SIGN);
                params.put(param.substring(0, idx), param.substring(idx + 1));
            }
        }
        return params;
    }

    /**
     * 将路径参数转换成Map对象，如果路径参数出现重复参数名，将以最后的参数值为准
     *
     * @param uri 传入的携带参数的路径
     * @return
     */
    public static JSONObject urlParamsToJson(String uri) {
        JSONObject jsonObject = new JSONObject();
        int idx = uri.indexOf(QUESTION_SIGN);
        if (idx != -1) {
            String[] paramsArr = uri.substring(idx + 1).split(AND_SIGN);
            for (String param : paramsArr) {
                idx = param.indexOf(EQUAL_SIGN);
                jsonObject.put(param.substring(0, idx), param.substring(idx + 1));
            }
        }
        return jsonObject;
    }

    /**
     * 获取URI中参数前的基础路径
     *
     * @param uri
     * @return
     */
    public static String getBasePath(String uri) {
        return Optional.ofNullable(uri)
                .filter(str -> !str.isEmpty())
                .map(str -> str.contains(QUESTION_SIGN) ? str.substring(0, uri.indexOf(QUESTION_SIGN)) : str)
                .orElse(null);
    }
}
