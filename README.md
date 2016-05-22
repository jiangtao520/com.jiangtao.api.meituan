# com.jiangtao.api.meituan
美团券核销接口

＃Example

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
		
