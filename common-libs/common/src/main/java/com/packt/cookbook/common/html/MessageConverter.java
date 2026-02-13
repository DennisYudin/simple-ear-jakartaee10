package com.packt.cookbook.common.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MessageConverter {

	public static String getFormParams(String html) {
		Document doc = Jsoup.parse(html);
		Element form = doc.selectFirst("form[action]");
		if (form == null) {
			throw new RuntimeException("Form not found");
		}
		return form.attr("action");
	}
}
