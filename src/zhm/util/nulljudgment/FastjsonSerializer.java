/**
 * FastjsonSerializer.java
 * zhm.rpc.serializer
 * 2017年12月12日下午11:17:11
 *
 */
package zhm.util.nulljudgment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


/**
 * @author zhuheming
 * FastjsonSerializer
 * 2018年3月1日下午8:08:23
 */
public class FastjsonSerializer {
	
	private static final String charsetName="UTF-8";

	/* (non-Javadoc)
	 * @see zhm.rpc.serializer.ISerializer#serialize(java.lang.Object)
	 */
	public static <T> byte[] serialize(T obj) {
		// TODO Auto-generated method stub
		return JSON.toJSONString(obj).getBytes();
	}

	/* (non-Javadoc)
	 * @see zhm.rpc.serializer.ISerializer#deserialize(byte[], java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] data, Class<T> clazz) {
		// TODO Auto-generated method stub		
		return (T) JSON.parse(data.toString());
	}

}
