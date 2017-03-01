package com.example.x.xcard;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

/**
 * 文件缓存操作
 *
 * @author wangtao 11-13
 * 
 */
public class SaveCache {
	private Context context;
	private File fileDir;

	public SaveCache(Context context) {
		this.context = context;
		/**
		 * getCacheDir()方法用于获取/data/data//cache目录:保存有时间较长的数据
           getFilesDir()方法用于获取/data/data//files目录
		* */
		fileDir = context.getApplicationContext().getCacheDir();
		if (!fileDir.exists()) {
			//创建此抽象路径名指定的目录，包括创建必须但不存在的父目录。
			fileDir.mkdirs();
		}
	}

	public String getCache(String key) {
		File file = new File(fileDir.getAbsolutePath() + "/" + key);
		if (!file.exists()) {
			return "";
		}
		try {
			//读取文件。
			FileInputStream in = new FileInputStream(file);
			byte[] b = new byte[1024];
			int len = 0;
			StringBuilder sb = new StringBuilder();
			while ((len = in.read(b)) != -1) {
				sb.append(new String(b, 0, len, Properties.CHAR_SET_NAME));
			}
			in.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
   //保存缓存数据
	public void saveCache(String key, String obj) {
		File file = new File(fileDir.getAbsolutePath() + "/" + key);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (!file.exists()) {
			return;
		}
		if (TextUtils.isEmpty(obj)) {
			file.delete();
			return;
		}

		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(obj.getBytes(Properties.CHAR_SET_NAME));
			//刷新，关闭流。
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据一个key保存一个对象，该对象必须全部实现Serializable接口
	 * （序列化）
	 * @param key
	 * @param obj
	 */
	public void saveCacheObj(Object obj, String key) {
		File file = new File(fileDir.getAbsolutePath() + "/" + key);
		if (null == file) {
			return;
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (!file.exists()) {
			return;
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(obj);
			objOut.flush();
			out.flush();
			objOut.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据一个key得到一个对象
	 * 
	 * @param key
	 * @return 若没有文件就返回 null
	 */
	public Object getCacheObj(String key) {
		//getAbsolutePath()返回抽象路径名的绝对路径名字符串。
		File file = new File(fileDir.getAbsolutePath() + "/" + key);
		Object obj = null;
		if (!file.exists()) {
			return obj;
		}
		try {
			FileInputStream in = new FileInputStream(file);
			if (in.available() == 0) {
				in.close();
				return null;
			}
			ObjectInputStream inObj = new ObjectInputStream(in);
			obj = inObj.readObject();
			inObj.close();
			in.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public void deleteObj(String key) {
		File file = new File(fileDir.getAbsolutePath() + "/" + key);
		Object obj = null;
		if (!file.exists()) {
			return;
		}
		file.delete();
	}
}
