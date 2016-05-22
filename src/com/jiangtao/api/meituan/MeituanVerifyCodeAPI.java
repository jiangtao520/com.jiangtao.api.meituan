package com.jiangtao.api.meituan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;

public class MeituanVerifyCodeAPI {
	
	public static void main(String[] args) {
		MeituanVerifyCodeAPI api = new MeituanVerifyCodeAPI("ca2bf41f1910a9c359370ebf87caeafd", "21be83530509abc81aa945a02bec37601cf3cc21", "12345");
		System.out.println(api.m1EcouponInfo("123412341234"));
		System.out.println(api.m1EcouponInfo("112233441122"));
		System.out.println(api.m1EcouponInfo("121234341212"));
		System.out.println(api.m1EcouponInfo("111133332222"));
		System.out.println(api.m1EcouponInfo("222244441111"));
		System.out.println(api.m1EcouponInfo("111111111111"));
		
		System.out.println("--------------------");
		System.out.println(api.m2EcouponVerify("123412341234", "1"));
		System.out.println(api.m2EcouponVerify("112233441122", "2"));
		System.out.println(api.m2EcouponVerify("121234341212", "3"));
		System.out.println(api.m2EcouponVerify("111133332222", "4"));
		System.out.println(api.m2EcouponVerify("222244441111", "5"));
		System.out.println(api.m2EcouponVerify("111111111111", "6"));
		
		System.out.println("--------------------");
		System.out.println(api.m3EcouponCancel("123412341234", "1"));
		System.out.println(api.m3EcouponCancel("112233441122", "2"));
		System.out.println(api.m3EcouponCancel("121234341212", "3"));
		System.out.println(api.m3EcouponCancel("111133332222", "4"));
		System.out.println(api.m3EcouponCancel("222244441111", "5"));
		System.out.println(api.m3EcouponCancel("111111111111", "6"));
		
	}

	private static String M1_URL = "http://e.meituan.com/api/ecouponinfo";
	private static String M2_URL = "http://e.meituan.com/api/ecouponverify";
	private static String M3_URL = "http://e.meituan.com/api/ecouponverifycancel";

	public MeituanVerifyCodeAPI(String appKey, String singKey, String poiid) {
		this.appKey = appKey;
		this.singKey = singKey;
		this.poiid = poiid;
	}

	public Map<String, String> m1EcouponInfo(String verifycode) {
		TreeMap<String, String> map = new TreeMap<String, String>();

		map.put("verifycode", verifycode);
		map.put("app_key", appKey);
		map.put("poiid", poiid);
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("v", "1.0");

		return post(M1_URL, map);
	}

	public Map<String, String> m2EcouponVerify(String verifycode, String sqnum) {
		TreeMap<String, String> map = new TreeMap<String, String>();

		map.put("verifycode", verifycode);
		map.put("app_key", appKey);
		map.put("poiid", poiid);
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("sqnum", sqnum);
		map.put("v", "1.0");
		

		return post(M2_URL, map);
	}

	public Map<String, String> m3EcouponCancel(String verifycode, String sqnum) {
		TreeMap<String, String> map = new TreeMap<String, String>();

		map.put("verifycode", verifycode);
		map.put("app_key", appKey);
		map.put("poiid", poiid);
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("v", "1.0");
		map.put("consume_sqnum", sqnum);

		return post(M3_URL, map);
	}

	private String sign(TreeMap<String, String> param) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> e : param.entrySet()) {
			String k = e.getKey();
			String v = e.getValue();

			if (v == null || v.equals("") || k.equals("sign"))
				continue;

			sb.append(k).append(v);
		}

		sb.insert(0, singKey);
		String source = sb.toString();

		return Tool.sha1(source);
	}

	private Map<String, String> post(String url, TreeMap<String, String> map) {
		String sign = sign(map);
		map.put("sign", sign);

		String result = Tool.get(url, map);

		result = Tool.decodeUnicode(result);
		Map<String, String> resultMap = Tool.parse(result);

		return resultMap;
	}

	private String appKey;
	private String singKey;
	private String poiid;

}

class Tool {
	public static String get(String url, Map<String, String> paramMap) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;

		StringBuilder paramBuilder = new StringBuilder(url);
		paramBuilder.append("?");

		for (Entry<String, String> e : paramMap.entrySet()) {
			paramBuilder.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}

		String urlNameString = paramBuilder.deleteCharAt(paramBuilder.length() - 1).toString();

		try {
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
//			connection.setRequestProperty("accept", "*/*");
//			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String sha1(String source) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(source.getBytes());
			byte messageDigest[] = digest.digest();
		
			StringBuffer hexString = new StringBuffer();
			
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String decodeUnicode(String encodeStr) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = encodeStr.indexOf("\\u", pos)) != -1) {
			sb.append(encodeStr.substring(pos, i));
			if (i + 5 < encodeStr.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(encodeStr.substring(i + 2, i + 6), 16));
			}
		}

		return sb.toString();

	}

	private static String SIMPLE_REGEX = "\".*?\":(\".*?\"|\\d+)";
	private static Pattern pattern = Pattern.compile(SIMPLE_REGEX);

	public static Map<String, String> parse(String json) {
		Matcher matcher = pattern.matcher(json);

		Map<String, String> map = new HashMap<String, String>();
		while (matcher.find()) {
			String entry = matcher.group();
			String[] arr = entry.split(":");
			String k = arr[0];
			String v = arr[1];

			if (k.startsWith("\""))
				k = k.substring(1, k.length() - 1);
			if (v.startsWith("\""))
				v = v.substring(1, v.length() - 1);
			map.put(k, v);
		}

		return map;
	}

}
