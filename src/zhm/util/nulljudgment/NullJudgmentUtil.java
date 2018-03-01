/**
 * NullJudgmentUtil.java
 * zhm.util.nulljudgment
 * 2018年2月28日下午9:16:27
 *
 */
package zhm.util.nulljudgment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author zhuheming
 * NullJudgmentUtil
 * 2018年2月28日下午9:16:27
 */
@SuppressWarnings("serial")
public class NullJudgmentUtil extends Throwable{

	/**
	 * 判断该对象的方法返回是否会为null
	 * boolean
	 * @param obj 传入对象
	 * @param methods 传入方法，
	 * 如果方法数组为空 那么判断对象是否为空，
	 * 如果方法数组长度>1那么表示obj.method1().method2()这种方式的返回是否为null
	 * @param args 所有方法的参数，双重数组方式传入，每个方法的参数作为一个数组，要与方法数组对应
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * 
	 */
	public static boolean isNotNull(Object obj,Method[] methods,Object[][] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		//如果方法数组为空 那么判断对象是否为空，
		if(methods==null||methods.length==0){
			if(obj==null){
				return false;
			}else{
				return true;				
			}
		}
		
		//如果方法数组不为空 
		if(methods!=null&&methods.length>=1){
			if(judgmentMethods(obj,methods)){
				//先实现对象的深拷贝，使用序列化方式实现
				byte[] ab=FastjsonSerializer.serialize(obj);
				Object ob=FastjsonSerializer.deserialize(ab, obj.getClass());
				
				Object objTmp=ob;
				//Object objResult=ob;
		        //循环调用
				for(int i=0;i<methods.length;i++){
					methods[i].setAccessible(true);
					//调用这个方法，并将返回值再作为下一次方法调用的对象传入。
					objTmp=methods[i].invoke(objTmp, args[i]);
					if(objTmp==null){
						return false;
					}
				}
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * //根据obj和clazz得到该对象类和父类和父类的父类...的方法列表，
	 * 如果传入的方法数组中某个方法不在列表内或某个方法返回void，那么返回false，
	 * boolean
	 * @param obj 传入类
	 * @param methods 传入方法数组
	 * @return
	 * 
	 */
	private static boolean judgmentMethods(Object obj,Method[] methods){
		
		ArrayList<Method> methodList=new ArrayList<Method>();
		//将本类实现的所有方法放入方法列表中
		methodList.addAll(Arrays.asList(obj.getClass().getDeclaredMethods()));
		//找到本对象的类的父类
		Class classTmp=obj.getClass().getSuperclass();
		//如果父类不是Object类，那么循环处理
		while(!classTmp.equals(Object.class)){
			//将父类实现方法放入方法列表中
			methodList.addAll(Arrays.asList(classTmp.getDeclaredMethods()));
			//再找父类
			classTmp=classTmp.getSuperclass();
		}
		//直到Object类
		methodList.addAll(Arrays.asList(Object.class.getDeclaredMethods()));
		
		//循环遍历methods中所有元素，如果有不在方法列表中的，
		//或者方法返回值为void，那么返回false，否则循环结束并返回true
		//方法比较使用Method的equals，Method是否重写过equals？？？
		for(int i=0;i<methods.length;i++){
			if(!methodList.contains(methods[i])){
				return false;
			}
			if(methods[i].getReturnType()==Void.class){
				return false;
			}
		}
			
		return true;
	}
}
