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
		// ��¼���濪ʼʱ��
		long beginTime = System.currentTimeMillis();

		ArrayList<String> booksList = new ArrayList<>();

		// ץȡ��ҳ����
		booksList = getUrl("����");

		// ������ҳ����
		analyzeUrl(booksList);

		// ��¼�������ʱ��
		long endTime = System.currentTimeMillis();
		// ���㻨��ʱ��
		long time = beginTime - endTime;
		System.out.println("�����ܼƻ���ʱ��:" + time / 1000f + "s");
	}

	/**
	 * ����ָ������ץȡ��ҳ����
	 * 
	 * @param type
	 * @return
	 */
	private static ArrayList<String> getUrl(String type) {
		ArrayList<String> bookList = new ArrayList<>();
		
		//	����cookies
		Map<String, String> cookies = new HashMap<>();
		// ץȡ������ʼ��
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
			
			// ƴ��URL����ʵ������ץȡ��ͬҳ��������
			Connection con = Jsoup.connect("https://book.douban.com/tag/" + type + "?start=" + cum + "&type=T").cookies(cookies);

			//	αװ����ͷ
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
				// ����ץȡ�����ݵ���
				cum += newUrl.size();
				
				//δץȡ�����ݣ�����ѭ��
				System.out.println("��ץȡurl������" + cum);
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
	 * ����ץȡ����վ����
	 * 
	 * @param booksList
	 */
	private static void analyzeUrl(ArrayList<String> booksList) {
		SaveBookInfo savebook = new SaveBookInfo();
		//	����cookies
		Map<String, String> cookies = new HashMap<>();
		
		//	������ʼ��Ϊ0
		int cum = 0;
		 
		//	ѭ������URL��Դ
		for (String url : booksList) {
			//	����getUrl��ȡ����ÿ��������ӽ�����Ӧ���ݵĽ���
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
				
				
				// ƴ��URL����ʵ������ץȡ��ͬҳ��������
				Connection con = Jsoup.connect(url).cookies(cookies);

				//	αװ����ͷ
				con.header("Accept", "text/html, application/xhtml+xml, */*");
				con.header("Content-Type", "application/x-www-form-urlencoded");
				con.header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))");
				
				Document doc = con.get();
				
				//	���ձ�ǩ�ֱ�ץȡ�鼮��Ӧ������
				//	��ȡ�鼮����
				Elements titleElement = doc.getElementsByClass("subject clearfix").select("a");
				String title = titleElement.attr("title");
				//	��ȡ�鼮����
				Elements scoreElement = doc.select("strong");
				String score = scoreElement.html();
				//	��ȡ�鼮��������
				Elements ratingSum = doc.getElementsByClass("rating_sum").select("a").select("span");
				String rating_sum = ratingSum.html();
				//	��ȡ�鼮����
				Elements authorElement = doc.getElementById("info").select("span").first().select("a");
				String author = authorElement.html();
				//	��ȡ�鼮������
				Element pressElement = doc.getElementById("info");
				String press = pressElement.html();

				 //	
				if (press.indexOf("������:") > -1) {
					press = pressElement.text().split("������:")[1].split(" ")[1];
				} else {
					press = "";
				}
				// ��ȡ�鼮��������
				String date = pressElement.text();
				if (date.indexOf("������:") > -1) {
					date = pressElement.text().split("������:")[1].split(" ")[1];
				} else {
					date = "";
				}
				// ��ȡ�鼮�۸�
				String price = pressElement.text();
				if (price.indexOf("����:") > -1) {
					price = pressElement.text().split("����:")[1].split(" ")[1];
					if (price.equals("CNY")) {
						price = pressElement.text().split("����:")[1].split(" ")[2];
					}
				} else {
					price = "";
				}
				
				//	�ж� ֻ�������������� 1000�Ĳ��ܴ洢�����ݿ�
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
