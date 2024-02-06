package com.crio.shorturl;

import java.util.HashMap;
import com.crio.shorturl.XUrl;

/**
 * XUrlImpl
 */
public class XUrlImpl implements XUrl {

    HashMap<String,String> longUrlToShortUrlMap = new HashMap<>();
    HashMap<String,String> shortUrlToLongUrlMap = new HashMap<>();   
    HashMap<String,Integer> longUrlHitCountMap = new HashMap<>();   

    @Override
    public String registerNewUrl(String longUrl) {
        // if long url exists return corresponding short url
        if(this.longUrlToShortUrlMap.get(longUrl)!=null)
            return this.longUrlToShortUrlMap.get(longUrl);
        // if not generate new url
        String urlStub = "http://short.url/";

        String shortUrl = urlStub + RandomString.getAlphaNumericString(9);
        //if short url somehow already exists then randomise again
        while(shortUrlToLongUrlMap.containsKey(shortUrl)){
            shortUrl = urlStub + RandomString.getAlphaNumericString(9);
        }

        //add to both maps
        shortUrlToLongUrlMap.put(shortUrl, longUrl);
        longUrlToShortUrlMap.put(longUrl, shortUrl);

        return shortUrl;
    }
    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        if (shortUrlToLongUrlMap.containsKey(shortUrl))
            return null;
        shortUrlToLongUrlMap.put(shortUrl, longUrl);
        longUrlToShortUrlMap.put(longUrl, shortUrl);

        return shortUrl;
    }
    @Override
    public String getUrl(String shortUrl) {
        String longUrl = shortUrlToLongUrlMap.get(shortUrl);
        if(longUrl!=null){
            longUrlHitCountMap.put(longUrl, longUrlHitCountMap.getOrDefault(longUrl, 0)+1);
        }
        return longUrl;
    }
    @Override
    public Integer getHitCount(String longUrl) {
        return longUrlHitCountMap.getOrDefault(longUrl, 0);
    }

    @Override
    public String delete(String longUrl) {
        
        String shortUrl = longUrlToShortUrlMap.get(longUrl);
        //delete if exists
        if (shortUrl!=null){
            longUrlToShortUrlMap.remove(longUrl);
            shortUrlToLongUrlMap.remove(shortUrl);
        }

        return shortUrl;
        
        
    }

    
}