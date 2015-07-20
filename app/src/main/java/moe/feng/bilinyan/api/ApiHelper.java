package moe.feng.bilinyan.api;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import moe.feng.bilinyan.model.BasicMessage;
import moe.feng.bilinyan.util.UrlBuilder;

public class ApiHelper {

	private static Gson gson = new Gson();
	private static OkHttpClient client = new OkHttpClient();

	public static final String API_HOST = "http://api.bilibili.cn";
	public static final String BILIBILI_SITE = "http://www.bilibili.com";

	public static final String TAG = ApiHelper.class.getSimpleName();

	public static String getHTML5Url(String aid) {
		return new UrlBuilder(API_HOST + "/" + ApiUrl.HTML5).addParams("aid", aid).toString();
	}

	public static String getRecommendUrl(String tid, String pagenum, String pagesize, String order) {
		UrlBuilder builder = new UrlBuilder(API_HOST +ApiUrl.RECOMMEND);

		if (tid != null) builder.addParams("tid", tid);
		if (pagenum != null) builder.addParams("page", pagenum);
		if (pagesize != null) builder.addParams("pagesize", pagesize);
		if (order != null) builder.addParams("order", order);

		return builder.toString();
	}

	public static String getVideoInfoUrl(int av, int page, boolean needFav) {
		UrlBuilder builder = new UrlBuilder(API_HOST + ApiUrl.VIEW);

		builder.addParams("id", av);
		builder.addParams("page", page);
		builder.addParams("fav", needFav ? "1" : "0");
		addAPIParmasAndComplete(builder);

		return builder.toString();
	}

	private static void addAPIParmasAndComplete(UrlBuilder builder) {
		builder.addParams("appkey", Secret.APP_KEY);
		builder.addParams("ts", Long.toString(System.currentTimeMillis() / 1000));
		builder.addParams("sign", SecretHelper.produceMD5(builder, Secret.APP_SECRET));
	}

	private static class ApiUrl {

		static final String HTML5 = "/m/html5";
		static final String RECOMMEND = "/recommend";
		static final String VIEW = "/view";

	}

	static class RecommendOrder {
		public static final String DEFAULT = "default", NEW = "new", REVIEW = "review",
				HOT = "hot", DAMKU = "damku", COMMENT = "comment", PROMOTE = "promote";
	}

	public static <OBJ> BasicMessage<OBJ> getSimpleUrlResult(String url, Class<OBJ> obj) {
		Log.i(TAG, url);

		Request request = new Request.Builder().url(url).build();
		Log.i(TAG, "Set up the request" + request.toString());

		BasicMessage<OBJ> msg = new BasicMessage<>();
		try {
			Response response = client.newCall(request).execute();
			Log.i(TAG, "Get response:" + response.code());
			String result = response.body().string();
			Log.i(TAG, result);
			msg.setObject(gson.fromJson(result, obj));
			msg.setCode(BasicMessage.CODE_SUCCEED);
		} catch (IOException e) {
			e.printStackTrace();
			msg.setCode(BasicMessage.CODE_ERROR);
		}

		return msg;
	}

}
