package org.spo.fw.service.proxy;

import net.lightbody.bmp.core.har.HarContent;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;

public class JavaScriptLoggerInjector implements ResponseInterceptor {

	
	public String generateReplaceString(){
		StringBuffer buf = new StringBuffer();
		
		buf.append("<script>"+'\n');

		buf.append("	$(document).ready(function(){"+'\n');
		buf.append("	  $('input').change(function(){"+'\n');
//		buf.append("	   var changedId = $(this).attr('id');"+'\n');
//		buf.append("	   var changedVal = $(this).val();"+'\n');
//		buf.append("	    console.log('Enter Text  ' +changedId+'   '+changedVal);"+'\n');
		buf.append("	  });"+'\n');
		buf.append("	});"+'\n');
		buf.append("	</script>"+'\n');
		buf.append(""+'\n');
//		buf.append("	<script>"+'\n');
//		buf.append("	$(document).ready(function(){"+'\n');
//		buf.append("	  $('select').change(function(){"+'\n');
//		buf.append("	   var changedId = $(this).attr('id');"+'\n');
//		buf.append("	   var changedVal = $(this).val();"+'\n');
//		buf.append("	    console.log('Select  ' +changedId+'   '+changedVal);"+'\n');
//		buf.append("	  });"+'\n');
//		buf.append("	});"+'\n');
//		buf.append("	</script>"+'\n');
return buf.toString()
		;
		
	}
	
	public void process(BrowserMobHttpResponse response) {
		String responseBody = response.getBody();
		HarResponse responseHar = response.getEntry().getResponse();
		HarContent content = response.getEntry().getResponse().getContent();
		String contentText = content.getText();
		contentText= contentText.replace("jquery-1.6.2.min.js\"></script>","jquery-1.6.2.min.js\"></script>"+ generateReplaceString());

		String regex = "?";

		if(contentText.contains(regex)){

			content.setText(contentText);
			responseHar.setContent(content);
			response.getEntry().setResponse(responseHar);
			System.out.println(response.getEntry().getResponse().getContent().getText());
			System.err.println("HIHIHIH");

		}

	}

}
