/*     */ package org.spoutcraft.launcher;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import org.bukkit.util.config.Configuration;
/*     */ import org.spoutcraft.launcher.async.DownloadListener;
/*     */ 
/*     */ public class MirrorUtils
/*     */ {
/*  20 */   public static final String[] MIRRORS_URL = { "http://localhost/fusioncraft/Technic/mirrors.yml", "https://raw.github.com/fusionmc-evilscoop/Technic/master/mirrors.yml" };
/*  21 */   public static File mirrorsYML = new File(GameUpdater.workDir, "mirrors.yml");
/*  22 */   private static boolean updated = false;
/*  23 */   private static final Random rand = new Random();
/*     */ 
/*     */   public static String getMirrorUrl(String mirrorURI, String fallbackUrl, DownloadListener listener) {
/*     */     try {
/*  27 */       if (Main.isOffline) return null;
/*     */ 
/*  29 */       Map mirrors = getMirrors();
/*  30 */       Set set = mirrors.entrySet();
/*     */ 
/*  32 */       int total = 0;
/*  33 */       Iterator iterator = set.iterator();
/*  34 */       while (iterator.hasNext()) {
/*  35 */         total += ((Integer)((Map.Entry)iterator.next()).getValue()).intValue();
/*     */       }
/*     */ 
/*  38 */       int random = rand.nextInt(total);
/*     */ 
/*  40 */       int count = 0;
/*  41 */       boolean isFinished = false;
/*  42 */       iterator = set.iterator();
/*  43 */       Map.Entry current = null;
/*  44 */       while (!isFinished) {
/*  45 */         while (iterator.hasNext()) {
/*  46 */           current = (Map.Entry)iterator.next();
/*  47 */           count += ((Integer)current.getValue()).intValue();
/*  48 */           String url = (String)current.getKey();
/*  49 */           if (count > random) {
/*  50 */             String mirror = "https://" + url + "/" + mirrorURI;
/*  51 */             Util.log("Testing URL: %s, count: %d Mirror: %s", new Object[] { url, Integer.valueOf(count), mirror });
/*  52 */             if (isAddressReachable(mirror)) {
/*  53 */               Util.log("Testing of: %s successful", new Object[] { mirror });
/*  54 */               return mirror;
/*     */             }
/*  56 */             Util.log("Testing of: %s FAILED", new Object[] { mirror });
/*  57 */             break;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*  62 */         if (set.size() == 1) {
/*  63 */           return null;
/*     */         }
/*  65 */         total -= ((Integer)current.getValue()).intValue();
/*  66 */         random = rand.nextInt(total);
/*  67 */         set.remove(current);
/*  68 */         iterator = set.iterator();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  72 */       e.printStackTrace();
/*     */     }
/*  74 */     System.err.println("All mirrors failed, reverting to default");
/*  75 */     return fallbackUrl;
/*     */   }
/*     */ 
/*     */   public static String getMirrorUrl(String mirrorURI, String fallbackUrl) {
/*  79 */     return getMirrorUrl(mirrorURI, fallbackUrl, null);
/*     */   }
/*     */ 
/*     */   public static Map<String, Integer> getMirrors()
/*     */   {
/*  84 */     Configuration config = getMirrorsYML();
/*  85 */     return (Map)config.getProperty("mirrors");
/*     */   }
/*     */ 
/*     */   public static boolean isAddressReachable(String url) {
/*  89 */     URLConnection urlConnection = null;
/*     */     try {
/*  91 */       urlConnection = new URL(url).openConnection();
/*  92 */       if (url.contains("https")) {
/*  93 */         HttpsURLConnection urlConnect = (HttpsURLConnection)urlConnection;
/*  94 */         urlConnect.setConnectTimeout(5000);
/*  95 */         urlConnect.setReadTimeout(30000);
/*  96 */         urlConnect.setInstanceFollowRedirects(false);
/*  97 */         urlConnect.setRequestMethod("HEAD");
/*  98 */         int responseCode = urlConnect.getResponseCode();
/*  99 */         urlConnect.disconnect();
/* 100 */         urlConnect = null;
/* 101 */         i = responseCode == 200 ? 1 : 0;
/*     */         return i;
/*     */       }
/* 103 */       HttpURLConnection urlConnect = (HttpURLConnection)urlConnection;
/* 104 */       urlConnect.setConnectTimeout(5000);
/* 105 */       urlConnect.setReadTimeout(30000);
/* 106 */       urlConnect.setInstanceFollowRedirects(false);
/* 107 */       urlConnect.setRequestMethod("HEAD");
/* 108 */       int responseCode = urlConnect.getResponseCode();
/* 109 */       urlConnect.disconnect();
/* 110 */       urlConnect = null;
/* 111 */       int i = responseCode == 200 ? 1 : 0;
/*     */       return i;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */     finally
/*     */     {
/* 115 */       if (urlConnection != null) {
/* 116 */         urlConnection = null;
/*     */       }
/*     */     }
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   public static Configuration getMirrorsYML() {
/* 123 */     updateMirrorsYMLCache();
/* 124 */     Configuration config = new Configuration(mirrorsYML);
/* 125 */     config.load();
/* 126 */     return config;
/*     */   }
/*     */ 
/*     */   public static void updateMirrorsYMLCache() {
/* 130 */     if (updated) return;
/* 131 */     updated = true;
/* 132 */     for (String urlentry : MIRRORS_URL)
/* 133 */       if (YmlUtils.downloadMirrorsYmlFile(urlentry)) return;
/*     */   }
/*     */ }

/* Location:           C:\Users\freak\Documents\GitHub\Technic\technic-launcher\
 * Qualified Name:     org.spoutcraft.launcher.MirrorUtils
 * JD-Core Version:    0.6.0
 */