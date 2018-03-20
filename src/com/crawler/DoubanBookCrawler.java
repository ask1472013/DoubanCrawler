package com.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DoubanBookCrawler {

	public static void main(String[] args) {
		// 记录爬虫开始时间
		long beginTime = System.currentTimeMillis();

		ArrayList<String> booksList = new ArrayList<>();

		// 抓取网页数据
		booksList = getUrl("程序");

		// 解析网页数据
		analyzeUrl(booksList);

		// 记录爬虫结束时间
		long endTime = System.currentTimeMillis();
		// 计算花费时间
		long time = beginTime - endTime;
		System.out.println("爬虫总计花费时间:" + time / 1000f + "s");
	}

	/**
	 * 根据指定类型抓取网页数据
	 * 
	 * @param type
	 * @return
	 */
	private static ArrayList<String> getUrl(String type) {
		ArrayList<String> bookList = new ArrayList<>();
		
		//	设置cookies
		Map<String, String> cookies = new HashMap<>();
		// 抓取计数初始化
		Integer cum = 0;

		try {
			//book.douban.com
			cookies.put("__utma", "30149280.231617203.1521451884.1521557520.1521562773.3");
			cookies.put("__utma", "81379588.2056572000.1521451884.1521557520.1521562773.3");
			cookies.put("__utmb", "81379588.62.9.1521563527210");
			cookies.put("__utmb", "30149280.31.10.1521562773");
			cookies.put("__utmc", "81379588");
			cookies.put("__utmc", "30149280");
			cookies.put("__utmt", "1");
			cookies.put("__utmt_douban", "1");
			cookies.put("__utmz", "30149280.1521451884.1.1.utmcsr=blog.csdn.net|utmccn=(referral)|utmcmd=referral|utmcct=/qq_23849183/article/details/50654807");
			cookies.put("__utmz", "81379588.1521451884.1.1.utmcsr=blog.csdn.net|utmccn=(referral)|utmcmd=referral|utmcct=/qq_23849183/article/details/50654807");
			
			// 拼接URL链接实现连续抓取不同页数的数据
			Connection con = Jsoup.connect("https://book.douban.com/tag/" + type + "?start=" + cum + "&type=T").cookies(cookies);

			//	伪装请求头
			con.header("Accept", "text/html, application/xhtml+xml, */*");
			con.header("Content-Type", "application/x-www-form-urlencoded");
			con.header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))");

			while (true) {
				Document document = con.get();

				Elements newUrl = document.select("ul").select("h2").select("a");

				for (Element e : newUrl) {
					System.out.println(e.attr("href"));
					bookList.add(e.attr("href"));
				}
				// 计算抓取到数据的数
				cum += newUrl.size();
				
				//未抓取到数据，结束循环
				System.out.println("共抓取url个数：" + cum);
				if (newUrl.size() == 0) {
					System.out.println("end");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bookList;
	}

	/**
	 * 解析抓取到网站数据
	 * 
	 * @param booksList
	 */
	private static void analyzeUrl(ArrayList<String> booksList) {
		SaveBookInfo savebook = new SaveBookInfo();
		//	设置cookies
		Map<String, String> cookies = new HashMap<>();
		
		//	计数初始化为0
		int cum = 0;
		 
		//	循环解析URL资源
		for (String url : booksList) {
			//	根据getUrl获取到的每本书的链接进行相应数据的解析
			try {
				//book.douban.com
				cookies.put("__utma", "30149280.231617203.1521451884.1521557520.1521562773.3");
				cookies.put("__utma", "81379588.2056572000.1521451884.1521557520.1521562773.3");
				cookies.put("__utmb", "81379588.62.9.1521563527210");
				cookies.put("__utmb", "30149280.31.10.1521562773");
				cookies.put("__utmc", "81379588");
				cookies.put("__utmc", "30149280");
				cookies.put("__utmt", "1");
				cookies.put("__utmt_douban", "1");
				cookies.put("__utmz", "30149280.1521451884.1.1.utmcsr=blog.csdn.net|utmccn=(referral)|utmcmd=referral|utmcct=/qq_23849183/article/details/50654807");
				cookies.put("__utmz", "81379588.1521451884.1.1.utmcsr=blog.csdn.net|utmccn=(referral)|utmcmd=referral|utmcct=/qq_23849183/article/details/50654807");
				
				
				// 拼接URL链接实现连续抓取不同页数的数据
				Connection con = Jsoup.connect(url).cookies(cookies);

				//	伪装请求头
				con.header("Accept", "text/html, application/xhtml+xml, */*");
				con.header("Content-Type", "application/x-www-form-urlencoded");
				con.header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))");
				
				Document doc = con.get();
				
				//	按照标签分别抓取书籍相应的数据
				//	获取书籍名称
				Elements titleElement = doc.getElementsByClass("subject clearfix").select("a");
				String title = titleElement.attr("title");
				//	获取书籍评分
				Elements scoreElement = doc.select("strong");
				String score = scoreElement.html();
				//	获取书籍评价总数
				Elements ratingSum = doc.getElementsByClass("rating_sum").select("a").select("span");
				String rating_sum = ratingSum.html();
				//	获取书籍作者
				Elements authorElement = doc.getElementById("info").select("span").first().select("a");
				String author = authorElement.html();
				//	获取书籍出版社
				Element pressElement = doc.getElementById("info");
				String press = pressElement.html();

				 //	
				if (press.indexOf("出版社:") > -1) {
					press = pressElement.text().split("出版社:")[1].split(" ")[1];
				} else {
					press = "";
				}
				// 获取书籍出版日期
				String date = pressElement.text();
				if (date.indexOf("出版年:") > -1) {
					date = pressElement.text().split("出版年:")[1].split(" ")[1];
				} else {
					date = "";
				}
				// 获取书籍价格
				String price = pressElement.text();
				if (price.indexOf("定价:") > -1) {
					price = pressElement.text().split("定价:")[1].split(" ")[1];
					if (price.equals("CNY")) {
						price = pressElement.text().split("定价:")[1].split(" ")[2];
					}
				} else {
					price = "";
				}
				
				//	判断 只有评价人数大于 1000的才能存储到数据库
				if (!rating_sum.equals("") && Integer.parseInt(rating_sum) >= 1000) {
					String sql = "insert into books values (DEFAULT,'" + title + "', '" + score + "', '"
							+ rating_sum + "', '" + author + "', '" + press + "', '" + date + "', '" + price + "')";
					savebook.saveBookInfo(sql);
					System.out.println(++cum);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
