package com.github.yjcpaj4.play_with_us;

import com.google.gson.Gson;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.ImageResource;
import com.github.yjcpaj4.play_with_us.resource.Resource;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.github.yjcpaj4.play_with_us.util.FileUtil;

/**
 * 리소스 객체.
 * 
 * @author 차명도.
 * @param <T> 
 */
public class ResourceManager {
    
    private Map<String, Resource> mResource = new HashMap<>(); 
    
    protected ResourceManager() {
    }
    
    public void load(File f) throws IOException {
        load(f, FileUtil.getNameWithoutExtension(f));
    }
    
    public void load(String f, String s) throws IOException {
        load(new File(f), s);
    } 
    
    public void load(File f, String k) throws IOException {
        Resource r = null;
        
        if (SpriteResource.canLoad(f, k)) {
            r = new SpriteResource();
        }
        
        switch(FileUtil.getExtension(f)) {
            case "jpg":
            case "jpeg":
            case "bmp": 
            case "gif":
            case "png": 
            case "tiff":
            case "tif":
                r = new ImageResource();
                break;
            case "mp3":
            case "ogg":
            case "wma":
            case "wav":
                r = new SoundResource();
                break;
        }
        
        r.load(f, k);

        mResource.put(k, r);
    }
    
    public ImageResource getImage(String k) {
        return (ImageResource) mResource.get(k);
    }
    
    public SpriteResource getSprite(String k) {
        return (SpriteResource) mResource.get(k);
    }
    
    public SoundResource getSound(String k) {
        return (SoundResource) mResource.get(k);
    }
    
    public void release(String k) {
        mResource.get(k).release();
    }
}
