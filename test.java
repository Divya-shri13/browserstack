package testpages;

//import java.awt.image.BufferedImage;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class test {
	//private static EdgeDriver driver;
	private static final String TRANSLATE_API_URL = "https://api.example.com/translate";
	public static void main(String[] args) {
WebDriver  driver = new ChromeDriver();
System.setProperty("`webdriver.chrome.driver", "driver\\chromedriver.exe");
driver.get("https://elpais.com/");
driver.manage().window().maximize();
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//Alert ale = driver.switchTo().alert();
//ale.getText();
//ale.accept();
//driver.findElement(By.xpath("//div[@class='sm _df']/a[2]")).click();
driver.findElement(By.xpath("//button[@id='didomi-notice-agree-button']")).click();
// default langugae of  the page 
driver.findElement(By.xpath("(//ul[@class=\"_ls _df\"])[1]"));
//String actual1 = driver.findElement(By.xpath("(//ul[@class= "_ls _df"])[1]")).getText();
//
//String expected1 = "España";
// Assert.assertEquals(actual1, expected1);
 System.out.println("the default language is spanish");
 //validate the language with the attribute 
 driver.findElement(By.xpath("//li[@class='ed_a']/a/span"));
 String actual2 =  driver.findElement(By.xpath("//li[@class='ed_a']/a/span")).getText();
 System.out.println(actual2);
 String expected2 =  "ESPAÑA";
 Assert.assertEquals(actual2, expected2);
 System.out.println("the default language is spanish");
 //vaLidATE the language through url 
 driver.getCurrentUrl();
 String actualurl =  driver.getCurrentUrl();
 String expectedurl = "https://elpais.com/";
 Assert.assertEquals(actualurl, expectedurl);
 System.out.println("the validated url is of the spanish");
 //Navigate to the Opinion section of the website.  --->click on the opinion 
 driver.findElement(By.xpath("//div[@class='sm _df']/a[2]")).click();
 //Fetch the first five articles in this section.
// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//article/header/h2/a")));
List<WebElement> articles = driver.findElements(By.xpath("//article/header/h2/a"));

List<String> articlesTitles = new ArrayList<>();
driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);

int count = 0;

for (WebElement ele : articles) {
	if (count>=5) break ;
	String title = ele.getText();
	System.out.println("the titles in spanish :"+ title);
	articlesTitles.add(title);
//	count++;



	 WebElement imagee = ele.findElement(By.tagName("figure"));
     if (imagee != null) {
         String imageUrl = imagee.getAttribute("src");
         downloadImage(imageUrl, "article" + count + ".jpg");
         }count++;
}	


	
	 // Translate titles to English
    List<String> translatedTitles = new ArrayList<>();
    for (String title : articlesTitles) {
        String translatedTitle = translateText(title, "es", "en");
        System.out.println("Translated Title (English): " + translatedTitle);
        translatedTitles.add(translatedTitle);
    }

    // Analyze repeated words in translated titles
    analyzeRepeatedWords(translatedTitles);

} 

// Method to translate text using an API
public static String translateText(String text, String sourceLang, String targetLang) {
try {
    OkHttpClient client = new OkHttpClient();
    HttpUrl.Builder urlBuilder = HttpUrl.parse(TRANSLATE_API_URL).newBuilder();
    urlBuilder.addQueryParameter("q", text);
    urlBuilder.addQueryParameter("langpair", sourceLang + "|" + targetLang);

    Request request = new Request.Builder().url(urlBuilder.build()).build();
    Response response = client.newCall(request).execute();

    if (response.isSuccessful()) {
        String responseBody = response.body().string();
        // Extract translated text (update based on API's response structure)
        return responseBody.split("\"translatedText\":\"")[1].split("\"")[0];
    }
} catch (Exception e) {
    e.printStackTrace();
}
return text; // Return original text if translation fails
}

// Method to download an image
public static void downloadImage(String imageUrl, String fileName) {
try {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(imageUrl).build();
    Response response = client.newCall(request).execute();

    if (response.isSuccessful()) {
        InputStream inputStream = response.body().byteStream();
        File file = new File(fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[2048];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        System.out.println("Image downloaded: " + fileName);
    }
} catch (Exception e) {
    e.printStackTrace();
}
}

// Method to analyze repeated words in a list of strings
public static void analyzeRepeatedWords(List<String> titles) {
Map<String, Integer> wordCount = new HashMap<>();

for (String title : titles) {
    String[] words = title.split("\\W+"); // Split by non-word characters
    for (String word : words) {
        word = word.toLowerCase();
        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
    }
}

// Filter and print words repeated more than twice
Map<String, Integer> repeatedWords = wordCount.entrySet()
        .stream()
        .filter(entry -> entry.getValue() > 2)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

System.out.println("Repeated Words: " + repeatedWords);
}
 
}
	

