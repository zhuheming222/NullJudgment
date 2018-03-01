/**
 * NullJudgmentUtil.java
 * zhm.util.nulljudgment
 * 2018��2��28������9:16:27
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
 * 2018��2��28������9:16:27
 */
@SuppressWarnings("serial")
public class NullJudgmentUtil extends Throwable{

	/**
	 * �жϸö���ķ��������Ƿ��Ϊnull
	 * boolean
	 * @param obj �������
	 * @param methods ���뷽����
	 * �����������Ϊ�� ��ô�ж϶����Ƿ�Ϊ�գ�
	 * ����������鳤��>1��ô��ʾobj.method1().method2()���ַ�ʽ�ķ����Ƿ�Ϊnull
	 * @param args ���з����Ĳ�����˫�����鷽ʽ���룬ÿ�������Ĳ�����Ϊһ�����飬Ҫ�뷽�������Ӧ
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * 
	 */
	public static boolean isNotNull(Object obj,Method[] methods,Object[][] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		//�����������Ϊ�� ��ô�ж϶����Ƿ�Ϊ�գ�
		if(methods==null||methods.length==0){
			if(obj==null){
				return false;
			}else{
				return true;				
			}
		}
		
		//����������鲻Ϊ�� 
		if(methods!=null&&methods.length>=1){
			if(judgmentMethods(obj,methods)){
				//��ʵ�ֶ���������ʹ�����л���ʽʵ��
				byte[] ab=FastjsonSerializer.serialize(obj);
				Object ob=FastjsonSerializer.deserialize(ab, obj.getClass());
				
				Object objTmp=ob;
				//Object objResult=ob;
		        //ѭ������
				for(int i=0;i<methods.length;i++){
					methods[i].setAccessible(true);
					//���������������������ֵ����Ϊ��һ�η������õĶ����롣
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
	 * //����obj��clazz�õ��ö�����͸���͸���ĸ���...�ķ����б�
	 * �������ķ���������ĳ�����������б��ڻ�ĳ����������void����ô����false��
	 * boolean
	 * @param obj ������
	 * @param methods ���뷽������
	 * @return
	 * 
	 */
	private static boolean judgmentMethods(Object obj,Method[] methods){
		
		ArrayList<Method> methodList=new ArrayList<Method>();
		//������ʵ�ֵ����з������뷽���б���
		methodList.addAll(Arrays.asList(obj.getClass().getDeclaredMethods()));
		//�ҵ����������ĸ���
		Class classTmp=obj.getClass().getSuperclass();
		//������಻��Object�࣬��ôѭ������
		while(!classTmp.equals(Object.class)){
			//������ʵ�ַ������뷽���б���
			methodList.addAll(Arrays.asList(classTmp.getDeclaredMethods()));
			//���Ҹ���
			classTmp=classTmp.getSuperclass();
		}
		//ֱ��Object��
		methodList.addAll(Arrays.asList(Object.class.getDeclaredMethods()));
		
		//ѭ������methods������Ԫ�أ�����в��ڷ����б��еģ�
		//���߷�������ֵΪvoid����ô����false������ѭ������������true
		//�����Ƚ�ʹ��Method��equals��Method�Ƿ���д��equals������
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
