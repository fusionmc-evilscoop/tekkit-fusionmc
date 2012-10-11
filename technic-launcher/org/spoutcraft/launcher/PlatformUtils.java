/*     */ package org.spoutcraft.launcher;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.Certificate;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.swing.JProgressBar;
/*     */ 
/*     */ public class PlatformUtils
/*     */ {
/*     */   public static final String LAUNCHER_DIR = "fusioncraft";
/*  35 */   private static File workDir = null;
/*     */ 
/*     */   public static File getWorkingDirectory() {
/*  38 */     if (workDir == null) {
/*  39 */       workDir = getWorkingDirectory("fusioncraft");
/*     */     }
/*  41 */     return workDir;
/*     */   }
/*     */ 
/*     */   public static File getWorkingDirectory(String applicationName) {
/*  45 */     boolean isPortable = MinecraftUtils.getOptions().isPortable();
/*  46 */     if (isPortable) return new File(".fusioncraft");
/*  47 */     String userHome = System.getProperty("user.home", ".");
/*     */     File workingDirectory;
/*     */     File workingDirectory;
/*  49 */     switch (1.$SwitchMap$org$spoutcraft$launcher$PlatformUtils$OS[getPlatform().ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/*  52 */       workingDirectory = new File(userHome, '.' + applicationName + '/');
/*  53 */       break;
/*     */     case 3:
/*  55 */       String applicationData = System.getenv("APPDATA");
/*  56 */       if (applicationData != null)
/*  57 */         workingDirectory = new File(applicationData, "." + applicationName + '/');
/*     */       else {
/*  59 */         workingDirectory = new File(userHome, '.' + applicationName + '/');
/*     */       }
/*  61 */       break;
/*     */     case 4:
/*  63 */       workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
/*  64 */       break;
/*     */     default:
/*  66 */       workingDirectory = new File(userHome, applicationName + '/');
/*     */     }
/*  68 */     if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) throw new RuntimeException("The working directory could not be created: " + workingDirectory);
/*  69 */     return workingDirectory;
/*     */   }
/*     */ 
/*     */   public static OS getPlatform() {
/*  73 */     String osName = System.getProperty("os.name").toLowerCase();
/*  74 */     if (osName.contains("win")) return OS.windows;
/*  75 */     if (osName.contains("mac")) return OS.macos;
/*  76 */     if (osName.contains("solaris")) return OS.solaris;
/*  77 */     if (osName.contains("sunos")) return OS.solaris;
/*  78 */     if (osName.contains("linux")) return OS.linux;
/*  79 */     if (osName.contains("unix")) return OS.linux;
/*  80 */     return OS.unknown;
/*     */   }
/*     */ 
/*     */   public static String excutePost(String targetURL, String urlParameters, JProgressBar progress)
/*     */   {
/*  89 */     HttpsURLConnection connection = null;
/*     */     try {
/*  91 */       URL url = new URL(targetURL);
/*  92 */       connection = (HttpsURLConnection)url.openConnection();
/*  93 */       connection.setRequestMethod("POST");
/*  94 */       connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*     */ 
/*  96 */       connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
/*  97 */       connection.setRequestProperty("Content-Language", "en-US");
/*     */ 
/*  99 */       connection.setUseCaches(false);
/* 100 */       connection.setDoInput(true);
/* 101 */       connection.setDoOutput(true);
/*     */ 
/* 103 */       connection.setConnectTimeout(10000);
/*     */ 
/* 105 */       connection.connect();
/* 106 */       Certificate[] certs = connection.getServerCertificates();
/*     */ 
/* 108 */       byte[] bytes = new byte[294];
/* 109 */       DataInputStream dis = new DataInputStream(PlatformUtils.class.getResourceAsStream("minecraft.key"));
/* 110 */       dis.readFully(bytes);
/* 111 */       dis.close();
/*     */ 
/* 113 */       Certificate c = certs[0];
/* 114 */       PublicKey pk = c.getPublicKey();
/* 115 */       byte[] data = pk.getEncoded();
/*     */ 
/* 117 */       for (int j = 0; j < data.length; j++) {
/* 118 */         if (data[j] == bytes[j]) {
/*     */           continue;
/*     */         }
/* 121 */         throw new RuntimeException("Public key mismatch");
/*     */       }
/*     */ 
/* 124 */       DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
/* 125 */       wr.writeBytes(urlParameters);
/* 126 */       wr.flush();
/* 127 */       wr.close();
/*     */ 
/* 129 */       InputStream is = connection.getInputStream();
/* 130 */       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
/*     */ 
/* 132 */       StringBuilder response = new StringBuilder();
/*     */       String line;
/* 134 */       while ((line = rd.readLine()) != null) {
/* 135 */         response.append(line);
/* 136 */         response.append('\r');
/*     */       }
/* 138 */       rd.close();
/*     */ 
/* 140 */       String str1 = response.toString();
/*     */       return str1;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 142 */       String message = "Login failed...";
/* 143 */       progress.setString(message);
/*     */     } finally {
/* 145 */       if (connection != null) {
/* 146 */         connection.disconnect();
/*     */       }
/*     */     }
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */   public static enum OS
/*     */   {
/*  85 */     linux, solaris, windows, macos, unknown;
/*     */   }
/*     */ }

/* Location:           C:\Users\freak\Documents\GitHub\Technic\technic-launcher\
 * Qualified Name:     org.spoutcraft.launcher.PlatformUtils
 * JD-Core Version:    0.6.0
 */